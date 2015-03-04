package restaurant;

import agent.Agent;
import restaurant.Check.CheckState;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.HostAgent.MyCustomer;
import restaurant.MarketBill.MarketBillState;
import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

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
public class CashierAgent extends Agent implements Cashier {
	
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Check> checksToRemove = new ArrayList<Check>();
	public List<Check> unpaidChecks = new ArrayList<Check>();
	
	public List<MarketBill> bills = Collections.synchronizedList(new ArrayList<MarketBill>());
	public List<MarketBill> billsToRemove = Collections.synchronizedList(new ArrayList<MarketBill>());
			
	private double cashInRegister = 200.0;
	
	public Map<String, Food> foods = new HashMap<String, Food>();
	
	private String name;
	
	public CashierAgent(String name) {
		super();
		foods.put("Chicken", new Food("Chicken"));
		foods.put("Steak", new Food("Steak"));
		foods.put("Salad", new Food("Salad"));
		foods.put("Pizza", new Food("Pizza"));
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	public double getCashInRegister(){
		return cashInRegister;
	}
	
	public void setLowCash(){
		cashInRegister = 20;
	}

	//STUB get foods??
	// Messages
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgProduceCheck(restaurant.WaiterAgent, restaurant.CustomerAgent, java.lang.String)
	 */
	@Override
	public void msgProduceCheck(Waiter w, Customer c, String choice){
		print("Received msgProduceCheck");
		checks.add(new Check(w, c, choice, foods.get(choice).price, CheckState.produced));
		stateChanged();
	}
	
	public void msgMarketBillDelivered(MarketBill mb){
		print("Received msgMarketBillDelivered");
		mb.mbs = MarketBillState.distributed;
		bills.add(mb);
		stateChanged();
	}
	
	public void msgHereIsChange(double change){
		print("Received msgHereIsChange");
		cashInRegister += change;
		DecimalFormat decim = new DecimalFormat("#.00");
		this.cashInRegister = Double.parseDouble(decim.format(this.cashInRegister));
		print("Current cash in Restaurant register balance = $" + cashInRegister);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgHereIsPayment(restaurant.Check, double)
	 */
	@Override
	public void msgHereIsPayment(Check c, double cash){
		print("Received msgHereIsPayment");		
		if(cash < c.bill){
			print(c.c + " can't pay. Letting him pay for it next time he comes");
			unpaidChecks.add(c);
		}
		else{
			this.cashInRegister += cash;
			c.change = cash - c.bill;
			this.cashInRegister -= c.change;
		}
		
		
		DecimalFormat decim = new DecimalFormat("#.00");
		this.cashInRegister = Double.parseDouble(decim.format(this.cashInRegister));
		c.change = Double.parseDouble(decim.format(c.change));
		print("Bill Total including outstanding checks = $" + c.bill);
		print("Received = $" + cash);
		print("Change for " + c.c + " = $" + c.change);
		print("Current cash in register balance = $" + this.cashInRegister);
		
		c.cs = CheckState.paymentFinished;
		
		checks.add(c);
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	public boolean pickAndExecuteAnAction() {
		if(!checksToRemove.isEmpty()){
			for(Check c: checksToRemove){
				checks.remove(c);
			}
			checksToRemove.clear();
		}
		if(!billsToRemove.isEmpty()){
			for(MarketBill mb: billsToRemove){
				bills.remove(mb);
			}
			billsToRemove.clear();
		}
		synchronized(checks){
			for(Check c: checks){
				if(c.cs == CheckState.paymentFinished){
					giveChangeToCustomer(c);
					return true;
				}
			}
		}
		synchronized(checks){
			for(Check c: checks){
				if(c.cs == CheckState.produced){
					giveCheckToWaiter(c);
					return true;
				}
			}
		}
		synchronized(bills){
			for(MarketBill mb: bills){
				if(mb.mbs == MarketBillState.distributed){
					payMarketBill(mb);
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
	 * @see restaurant.Cashier#giveCheckToWaiter(restaurant.Check)
	 */
	@Override
	public void giveCheckToWaiter(Check c){
		print("Called giveCheckToWaiter");
		Check temp = null;
		if(!unpaidChecks.isEmpty()){
			for(Check unpaid: unpaidChecks){
				if(unpaid.c == c.c){
					c.bill += unpaid.bill;
					temp = unpaid;
				}
			}
		}
		if(temp!=null)
			unpaidChecks.remove(temp);
		c.cs = CheckState.distributed;
		checksToRemove.add(c);
		c.w.msgCheckIsReady(c);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#giveChangeToCustomer(restaurant.Check)
	 */
	@Override
	public void giveChangeToCustomer(Check c){
		print("Called giveChangeToCustomer");
		checksToRemove.add(c);
		c.c.msgHereIsYourChange(c.change);
	}
	
	public void payMarketBill(MarketBill mb){
		print("Called payMarketBill");
		if(cashInRegister < mb.bill){
			mb.m.msgHereIsPayment(mb, cashInRegister);
			cashInRegister += 200.00;
		}
		else{
			mb.m.msgHereIsPayment(mb, mb.bill);
			cashInRegister -= mb.bill;
		}
		billsToRemove.add(mb);
	}
	
	
	private class Food{
		String type;
		double price;
		
		Food(String type){
			this.type = type;
			if(type == "Chicken"){
				price = 10.99;
			}
			else if(type == "Steak"){
				price = 15.99;
			}
			else if(type == "Salad"){
				price = 5.99;
			}
			else if(type == "Pizza"){
				price = 8.99;
			}
		}
	}
}

