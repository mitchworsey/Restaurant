package restaurant;

import agent.Agent;
import restaurant.Check.CheckState;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.HostAgent.MyCustomer;
import restaurant.MarketBill.MarketBillState;
import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class MarketAgent extends Agent implements Market {
	
	public enum OrderState
	{pending, processing, processed};
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Order> ordersToRemove = new ArrayList<Order>();
	
	Timer t = new Timer();
	
	private double cash = 0.0;
	
	public Map<String, Food> foods = new HashMap<String, Food>();
	
	private List<MarketBill> bills = Collections.synchronizedList(new ArrayList<MarketBill>());
	private List<MarketBill> unpaidBills = Collections.synchronizedList(new ArrayList<MarketBill>());
	private List<MarketBill> billsToRemove = new ArrayList<MarketBill>();
	
	private String name;
	
	public MarketAgent(String name) {
		super();
		this.name = name;
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Steak", new Food("Steak"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
		
	}
	

	/* (non-Javadoc)
	 * @see restaurant.Market#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}
	
	public void setLowInventory(){
		foods.get("Chicken").stock = 0;
		foods.get("Steak").stock = 0;
		foods.get("Salad").stock = 0;
		foods.get("Pizza").stock = 0;

	}

	/* (non-Javadoc)
	 * @see restaurant.Market#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Market#getOrders()
	 */
	@Override
	public List getOrders() {
		return orders;
	}

	//STUB get foods??
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Market#msgHereIsOrder(restaurant.interfaces.Cook, java.lang.String, int)
	 */
	@Override
	public void msgHereIsOrder(Cook c, String choice, int numberOrdered){
		print("Received msgHereIsOrder from Cook");
		if(numberOrdered==0){
			orders.add(new Order(c, choice, OrderState.processed, numberOrdered));
		}
		else{
			orders.add(new Order(c, choice, OrderState.pending, numberOrdered));
		}
		//foods.put(choice, new Food(choice));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#msgOrderProcessed(restaurant.MarketAgent.Order)
	 */
	@Override
	public void msgOrderProcessed(Order o){
		print("Received msgOrderProcessed");
		print(o.choice + " stock = " + foods.get(o.choice).stock);
		print("Fulfilled " + o.numberFulfilled + " out of " + o.numberOrdered + " ordered");
		
		double total = foods.get(o.choice).price * o.numberFulfilled;
		bills.add(new MarketBill(this, o.c.getCashier(), total, MarketBillState.produced));
		
		o.os = OrderState.processed;
		stateChanged();
	}
	
	public void msgHereIsPayment(MarketBill mb, double cash){
		print("Received msgHereIsPayment");
		if(cash < mb.bill){
			print("Cashier can't pay. Letting him pay for it next time he orders");
			unpaidBills.add(mb);
		}
		else{
			this.cash += cash;
			mb.change = cash - mb.bill;
			this.cash -= mb.change;
		}
		
		
		DecimalFormat decim = new DecimalFormat("#.00");
		this.cash = Double.parseDouble(decim.format(this.cash));
		mb.change = Double.parseDouble(decim.format(mb.change));
		print("Bill Total including outstanding bills = $" + mb.bill);
		print("Received = $" + cash);
		print("Change for Cashier = $" + mb.change);
		print("Current Market cash balance = $" + this.cash);
		
		mb.mbs = MarketBillState.paymentFinished;
		
		bills.add(mb);
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(!ordersToRemove.isEmpty()){
			for(Order o: ordersToRemove){
				orders.remove(o);
			}
			ordersToRemove.clear();
		}
		if(!billsToRemove.isEmpty()){
			for(MarketBill mb: billsToRemove){
				bills.remove(mb);
			}
			billsToRemove.clear();
		}
		synchronized(orders){
			for(Order ord: orders){
				if(ord.os == OrderState.processed){
					deliverOrder(ord);
					for(MarketBill mb: bills){
						if(mb.mbs == MarketBillState.produced){
							deliverMarketBill(mb);
							return true;
						}
					}
				}
			}
		}
		
		synchronized(orders){
			for(Order ord: orders){
				if(ord.os == OrderState.pending){
					if(foods.get(ord.choice).stock == 0){
						outOfFood(ord);
						return true;
					}
				}
			}
		}
		synchronized(orders){
			for(Order ord: orders){
				if(ord.os == OrderState.pending){
					processOrder(ord);
					return true;
				}
			}
		}
		synchronized(bills){
			for(MarketBill mb: bills){
				if(mb.mbs == MarketBillState.paymentFinished){
					giveChangeToCashier(mb);
					return true;
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	/* (non-Javadoc)
	 * @see restaurant.Market#deliverOrder(restaurant.MarketAgent.Order)
	 */
	@Override
	public void deliverOrder(Order o){
		print("Called deliverOrder");
		o.c.msgOrderDelivered(o.choice, o.numberFulfilled);
		ordersToRemove.add(o);
	}
	
	public void deliverMarketBill(MarketBill mb){
		print("Called deliverMarketBill");
		MarketBill temp = null;
		if(!unpaidBills.isEmpty()){
			for(MarketBill unpaid: unpaidBills){
				if(unpaid.c == mb.c){
					mb.bill += unpaid.bill;
					temp = unpaid;
				}
			}
		}
		if(temp!=null)
			unpaidBills.remove(temp);
		mb.mbs = MarketBillState.distributed;
		billsToRemove.add(mb);
		mb.c.msgMarketBillDelivered(mb);
	}
	
	public void giveChangeToCashier(MarketBill mb){
		print("Called marketBillPaid");
		billsToRemove.add(mb);
		mb.c.msgHereIsChange(mb.change);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#processOrder(restaurant.MarketAgent.Order)
	 */
	@Override
	public void processOrder(final Order o){
		print("Called processOrder");
		o.c.msgOrderBeingProcessed(o.choice);
		o.os = OrderState.processing;
		while((foods.get(o.choice).stock > 0) && (o.numberFulfilled < o.numberOrdered)){
			foods.get(o.choice).stock--;
			o.numberFulfilled++;
		}
		t.schedule(new TimerTask() {
			public void run() {
				msgOrderProcessed(o);
				//print("Done processing "+ o.choice);
			}
		},foods.get(o.choice).orderTime);
		//stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Market#outOfFood(restaurant.MarketAgent.Order)
	 */
	@Override
	public void outOfFood(Order o){
		print("called outOfFood");
		o.c.msgOutOfStock(o.choice);
		ordersToRemove.add(o);
	}


	public class Order{
		public Cook c;
		public String choice;
		public OrderState os;
		public int numberOrdered;
		public int numberFulfilled;
		
		Order(Cook c, String choice, OrderState os, int numOrdered){
			this.c = c;
			this.choice = choice;
			this.os = os;
			this.numberOrdered = numOrdered;
			numberFulfilled = 0;
		}
		
	}
	private class Food{
		String type;
		int orderTime;
		int stock = 45;
		double price;
		
		Food(String type){
			this.type = type;
			if(type == "Chicken"){
				orderTime = 8000;
				price = 5.50;
			}
			else if(type == "Steak"){
				orderTime = 10000;
				price = 8.00;
			}
			else if(type == "Salad"){
				orderTime = 5000;
				price = 3.00;
			}
			else if(type == "Pizza"){
				orderTime = 12000;
				price = 4.50;
			}
		}
	}
}

