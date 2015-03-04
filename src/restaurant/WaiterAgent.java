package restaurant;
import agent.Agent;
import restaurant.gui.FoodGui;
//import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class WaiterAgent extends Agent implements Waiter {

	public enum CustomerState
	{waiting, seated, readyToOrder, askedToOrder, ordered, waitingForFoodToCook, waitingForFoodToArrive, needsToReOrder, eating,
		doneEating, waitingForCheckToBeProduced, waitingForCheckToArrive, receivedCheck, leaving};
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Check> checksToRemove = new ArrayList<Check>();
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	protected Semaphore atCook = new Semaphore(0, true);
	private Semaphore atHost = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	
	boolean wantToGoOnBreak = false;
	boolean OKToGoOnBreak = false;
	boolean onBreak = false;
	boolean backToWork = false;
	
	public WaiterGui waiterGui = null;
	public Cook cook = null;
	public Host host = null;
	public Cashier cashier = null;
	
	private boolean justOpened = false;
	private boolean begInventoryFilled = true;
	
	RestaurantMenu rm = new RestaurantMenu();
	
	public WaiterAgent(String name) {
		super();
		this.name = name;
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getCustomers()
	 */
	@Override
	public List getCustomers() {
		return customers;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#wantToGoOnBreak()
	 */
	@Override
	public boolean wantToGoOnBreak(){
		return wantToGoOnBreak;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#isOnBreak()
	 */
	@Override
	public boolean isOnBreak(){
		return onBreak;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#isOKToGoOnBreak()
	 */
	@Override
	public boolean isOKToGoOnBreak(){
		return OKToGoOnBreak;
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCook(restaurant.interfaces.Cook)
	 */
	@Override
	public void setCook(Cook cook){
		this.cook = cook;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setHost(restaurant.interfaces.Host)
	 */
	@Override
	public void setHost(Host host){
		this.host = host;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier cashier){
		this.cashier = cashier;
	}
	
	
	// Messages
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgBegInventoryFilled()
	 */
	@Override
	public void msgBegInventoryFilled(){
		print("Beginning Inventory filled, now can start seeting customers.");
		begInventoryFilled = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAddToMenu(java.lang.String)
	 */
	@Override
	public void msgAddToMenu(String food){
		print("received msgAddToMenu");
		if(!rm.entrees.containsKey(food))
			rm.add(food);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgRestockInventory()
	 */
	@Override
	public void msgRestockInventory(){
		print("Received msgRestockInventory");
		justOpened = true;
		begInventoryFilled = false;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgCheckIsReady(restaurant.Check)
	 */
	@Override
	public void msgCheckIsReady(Check c){
		print("Received msgCheckIsReady");
		checks.add(c);
		MyCustomer mc = find(c.c);
		mc.cs = CustomerState.waitingForCheckToArrive;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgWantsToGoOnBreak()
	 */
	@Override
	public void msgWantsToGoOnBreak(){
		print("Received msgWantsToGoOnBreak");
		wantToGoOnBreak = true;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgBackToWork()
	 */
	@Override
	public void msgBackToWork(){
		backToWork = true;
		OKToGoOnBreak = false;
		onBreak = false;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOKToGoOnBreak()
	 */
	@Override
	public void msgOKToGoOnBreak(){
		print("Received msgOKToGoOnBreak");
		wantToGoOnBreak = false;
		OKToGoOnBreak = true;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgNOTOKToGoOnBreak()
	 */
	@Override
	public void msgNOTOKToGoOnBreak(){
		print("Received msgNOTOKToGoOnBreak");
		wantToGoOnBreak = false;
		OKToGoOnBreak = false;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSitAtTable(restaurant.interfaces.Customer, int)
	 */
	@Override
	public void msgSitAtTable(Customer c, int table){
		print("Received msgSitAtTable()");
		customers.add(new MyCustomer(c, table, CustomerState.waiting));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToOrder(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgReadyToOrder(Customer c){
		print("Received msgReadyToOrder()");
		MyCustomer mc = find(c);
		mc.cs = CustomerState.readyToOrder;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMyChoice(restaurant.interfaces.Customer, java.lang.String)
	 */
	@Override
	public void msgHereIsMyChoice(Customer c, String choice){
		print("Received msgHereIsMyChoice()");
		MyCustomer mc = find(c);
		mc.cs = CustomerState.ordered;
		mc.choice = choice;
		waiterGui.createFoodGui(choice, mc.table);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOutOfFood(java.lang.String, int)
	 */
	@Override
	public void msgOutOfFood(String choice, int table){
		print("Received msgOutOfFood()");
		for(MyCustomer myCust: customers){
			if(myCust.choice == choice && myCust.table == table)
				myCust.cs = CustomerState.needsToReOrder;
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderIsReady(java.lang.String, int)
	 */
	@Override
	public void msgOrderIsReady(String choice, int table){
		print("Received msgOrderIsReady()");
		for(MyCustomer myCust: customers){
			if(myCust.choice == choice && myCust.table == table)
				myCust.cs = CustomerState.waitingForFoodToArrive;
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyForCheck(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgReadyForCheck(Customer c){
		print("Received msgReadyForCheck");
		MyCustomer mc = find(c);
		mc.cs = CustomerState.doneEating;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgDonePayingAndLeaving(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgDonePayingAndLeaving(Customer c){
		print("Received msgDonePayingAndLeaving()");
		//isAvailable = true;
		MyCustomer mc = find(c);
		mc.cs = CustomerState.leaving;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtCook()
	 */
	@Override
	public void msgAtCook(){
		//print("msgAtCook() called");
		atCook.release();
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtHost()
	 */
	@Override
	public void msgAtHost(){
		atHost.release();
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtCashier()
	 */
	@Override
	public void msgAtCashier(){
		atCashier.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(wantToGoOnBreak)
			askToGoOnBreak();
		if(justOpened && cook!=null){
			restockInventory();
			return true;
		}
		
		if(!checksToRemove.isEmpty()){
			for(Check c: checksToRemove){
				checks.remove(c);
			}
			checksToRemove.clear();
		}
		
		if(begInventoryFilled){
			synchronized(customers){
				for(MyCustomer mc: customers){
					if(mc.cs == CustomerState.waiting){
						seatCustomer(mc);
						return true;
					}
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.readyToOrder){	
					takeOrder(mc);
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.ordered){
					deliverOrderToCook(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.needsToReOrder){
					reOrder(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.waitingForFoodToArrive){
					deliverOrderToCustomer(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.doneEating){
					tellCashierToProduceCheck(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.waitingForCheckToArrive){
					synchronized(checks){
						for(Check c: checks){
							if(c.c == mc.c){
								deliverCheckToCustomer(mc, c);//the action
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}
			}
		}
		synchronized(customers){
			for(MyCustomer mc: customers){
				if(mc.cs == CustomerState.leaving){
					alertHostTableIsFree(mc);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		waiterGui.DoWatch();
		if(customers.isEmpty() && OKToGoOnBreak)
			goOnBreak();
		if(onBreak)
			waiterGui.DoLeaveCustomer();
		else if(backToWork){
			tellHostYourBack();
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void restockInventory(){
		cook.msgRestockInventory(this);
		justOpened = false;
	}

	private void askToGoOnBreak(){
		host.msgAskToGoOnBreak(this);
	}
	
	private void seatCustomer(MyCustomer mc){
		print("Called seatCustomer()");
		waiterGui.DoGoToCustomer(mc.c.getGui().getXWaitPos(), mc.c.getGui().getYWaitPos());
		try {
			atHost.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgFollowMeToTable(rm);
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mc.cs = CustomerState.seated;
		//if(name.equals("go on break")){
			//wantToGoOnBreak = true;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void goOnBreak(){
		print("Called goOnBreak");
		wantToGoOnBreak = false;
		OKToGoOnBreak = false;
		onBreak = true;
		host.msgGoingOnBreak(this);
	}
	
	
	private void tellHostYourBack(){
		backToWork = false;
		host.msgBackToWork(this);
	}
	
	private void takeOrder(MyCustomer mc){
		print("Called takeOrder()");
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgWhatDoYouWant();
		mc.cs = CustomerState.askedToOrder;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	protected abstract void deliverOrderToCook(MyCustomer mc);
	
	private void reOrder(MyCustomer mc){
		print("called reOrder()");
		waiterGui.removeFoodGui(mc.choice, mc.table);
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rm.removeEntree(mc.choice);
		System.out.println("Removed " + mc.choice);
		//System.out.println(rm.entrees.containsKey(mc.choice));
		mc.c.msgPleaseReOrder(rm);
		mc.cs = CustomerState.askedToOrder;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void deliverOrderToCustomer(MyCustomer mc){
		print("Called deliverOrderToCustomer()");
		waiterGui.DoGoToPlatingArea();
		waiterGui.foodCooked(mc.choice, mc.table);
		waiterGui.FoodGuiGoToPlatingArea(mc.choice, mc.table);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToTable(mc.table);
		waiterGui.FoodGuiGoToCustomer(mc.choice, mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgHereIsYourFood();
		mc.cs = CustomerState.eating;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void tellCashierToProduceCheck(MyCustomer mc){
		print("called tellCashierToProduceCheck");
		waiterGui.foodAte(mc.choice, mc.table);
		waiterGui.DoGoToCashier();//HACK to Cashier
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashier.msgProduceCheck(this, mc.c, mc.choice);
		mc.cs = CustomerState.waitingForCheckToBeProduced;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void deliverCheckToCustomer(MyCustomer mc, Check check){
		print("Called deliverCheckToCustomer()");
		waiterGui.DoGoToCashier();//HACK to Cashier
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToTable(mc.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mc.c.msgHereIsYourCheck(check);
		checksToRemove.add(check);
		mc.cs = CustomerState.receivedCheck;
		if(wantToGoOnBreak)
			host.msgAskToGoOnBreak(this);
	}
	
	private void alertHostTableIsFree(MyCustomer mc){
		print("Called alertHostTableIsFree()");
		waiterGui.removeFoodGui(mc.choice, mc.table);
		/*waiterGui.DoGoToHost();
		try {
			atHost.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		host.msgTableIsFree(mc.c);
		customers.remove(mc);
	}

	//utilities
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#getTableNumber(restaurant.interfaces.Customer)
	 */
	@Override
	public int getTableNumber(Customer c){
		for(MyCustomer myCust: customers){
			if(myCust.c == c)
				return myCust.table;		
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#find(restaurant.interfaces.Customer)
	 */
	@Override
	public MyCustomer find(Customer c){
		for(MyCustomer myCust: customers){
			if(myCust.c == c)
				return myCust;		
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setGui(restaurant.gui.WaiterGui)
	 */
	@Override
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	@Override
	public WaiterGui getGui() {
		return waiterGui;
	}

	public class MyCustomer{
		Customer c;
		int table;
		CustomerState cs;
		String choice;
		
		MyCustomer(Customer cust, int table, CustomerState custstate){
			c = cust;
			this.table = table;
			cs = custstate;
		}
	}
	public class RestaurantMenu{
		Map<String, Food> entrees = new HashMap<String, Food>();
		
		RestaurantMenu(){
			entrees.put("Chicken", new Food("Chicken"));
			entrees.put("Steak", new Food("Steak"));
			entrees.put("Salad", new Food("Salad"));
			entrees.put("Pizza", new Food("Pizza"));
		}
		
		public void removeEntree(String choice){
			entrees.remove(choice);
		}
		
		public void add(String choice){
			entrees.put(choice, new Food(choice));
		}
		
		
		public String getEntree(double cash){
			Random rand = new Random();
			String choice = null;
			while(choice == null){
				if(cash < 5.99)
					return null;
				int num = rand.nextInt(4);
				if(num==0 && entrees.containsKey("Chicken")){
					if(cash >= entrees.get("Chicken").getPrice())
						choice = "Chicken";
				}
				else if(num==1 && entrees.containsKey("Steak")){
					if(cash >= entrees.get("Steak").getPrice())
						choice = "Steak";
				}
				else if(num==2 && entrees.containsKey("Salad")){
					if(cash >= entrees.get("Salad").getPrice())
						choice = "Salad";
				}
				else if(num==3 && entrees.containsKey("Pizza")){
					if(cash >= entrees.get("Pizza").getPrice())
						choice = "Pizza";
				}
				if((cash >= 5.99 && cash < 8.99) && !entrees.containsKey("Salad"))
					return null;
				if(entrees.isEmpty()){
					System.out.println("empty menu");
					return null;
				}
			}
			return choice;
		}
		
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
		
		public double getPrice(){
			return price;
		}
	}
}

