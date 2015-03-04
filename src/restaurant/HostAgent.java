package restaurant;

import agent.Agent;
import restaurant.WaiterAgent.CustomerState;
import restaurant.WaiterAgent.MyCustomer;
import restaurant.gui.HostGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent implements Host {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public List<Waiter> waiters = Collections.synchronizedList(new ArrayList<Waiter>());
	
	public List<Waiter> waitersToRemove = new ArrayList<Waiter>();
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	public List<MyCustomer> customersToRemove = new ArrayList<MyCustomer>();
	
	int waiterNum = 0;
	
	private boolean justOpened = false;
	
	public enum CustomerState
	{waitingToBeSeated, mayLeave, seated, leftRestaurant};

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public WaiterGui waiterGui = null;
	//public HostGui hostGui = null;
	
	public HostAgent(String name) {
		super();

		//waiter = new WaiterAgent("Waiter 1");
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		justOpened = true;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getCustomers()
	 */
	@Override
	public List getCustomers() {
		return customers;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getTables()
	 */
	@Override
	public Collection getTables() {
		return tables;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#setWaiter(restaurant.WaiterAgent)
	 */
	public void setWaiter(Waiter w){
		waiters.add(w);
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Host#msgIWantFood(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgIWantFood(Customer cust) {
		print("Received msgIWantFood");
		customers.add(new MyCustomer(cust, CustomerState.waitingToBeSeated));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgLeavingImpatiently(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgLeavingImpatiently(Customer cust){
		print("Received msgLeavingImpatiently");
		for(MyCustomer mc: customers){
			if(mc.c == cust)
				customersToRemove.add(mc);
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgTableIsFree(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgTableIsFree(Customer cust){
		print("Received msgTableIsFree");
		for(MyCustomer mc: customers){
			if(mc.c == cust)
				customersToRemove.add(mc);
		}
		for(Table t: tables){
			if(t.getOccupant() == cust){
				t.setUnoccupied();
				//t.occupiedBy.cs = CustomerState.leftRestaurant;	
			}
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgAskToGoOnBreak(restaurant.WaiterAgent)
	 */
	@Override
	public void msgAskToGoOnBreak(Waiter w){
		print("Received msgAskToGoOnBreak");
		//if(waiters.size()>1){
		//for(WaiterAgent waiter: waiters){
			//if(waiter == w)
				//waiter.wantToGoOnBreak = true;
		//}
			stateChanged();
		//}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgGoingOnBreak(restaurant.WaiterAgent)
	 */
	public void msgGoingOnBreak(Waiter w){
		waitersToRemove.add(w);
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgBackToWork(restaurant.WaiterAgent)
	 */
	public void msgBackToWork(Waiter w){
		waiters.add(w);
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#readyToBeSeated()
	 */
	@Override
	public void readyToBeSeated(){
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
		if(justOpened){
			if(waiters.size()>0){
				restockInventory(waiters.get(0));
				return true;
			}
		}
		
		if(!customersToRemove.isEmpty()){
			for(MyCustomer mc: customersToRemove){
				customers.remove(mc);
			}
			customersToRemove.clear();
		}
		
		if(!waitersToRemove.isEmpty()){
			for(Waiter w: waitersToRemove){
				waiters.remove(w);
			}
			waitersToRemove.clear();
		}
		
		if(waiters.size()>1){
			for(Waiter w: waiters){
				if(w.wantToGoOnBreak()){
					tellWaiterOKToGoOnBreak(w);
					return true;
				}
			}
		}
		else{
			for(Waiter w: waiters){
				if(w.wantToGoOnBreak()){
					tellWaiterNOTOKToGoOnBreak(w);
					return true;
				}
			}
		}
		synchronized(customers){
			for(int x = 0; x <= waiters.size(); x++){
				if(x == (waiterNum % waiters.size())){
					for(MyCustomer mc: customers){
						if(mc.cs == CustomerState.waitingToBeSeated){
							for (Table table : tables) {
								if (!table.isOccupied()) {
									tellWaiterToSeatCustomer(waiters.get(x), mc, table);//the action
									waiterNum++;
									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
							if(mc.c.isImpatient()){
								tellCustomerRestaurantFull(mc);
								return true;
							}
						}
					}
				}
			}
		}
		
		
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void restockInventory(Waiter w){
		print("Called restockInventory");
		w.msgRestockInventory();
		justOpened = false;
	}

	private void tellWaiterToSeatCustomer(Waiter w, MyCustomer mc, Table table){
		print("Called tellWaiterToSeatCustomer"); 
		//for(WaiterAgent w: waiters){
			//if(!w.isBusy()){
		mc.c.setWaiter(w);
		w.msgSitAtTable(mc.c, table.tableNumber);
			//}
		//}
		table.setOccupant(mc.c);
		//customers.remove(mc);
		mc.cs = CustomerState.seated;
		//waiterGui.DoLeaveCustomer();
	}
	
	private void tellCustomerRestaurantFull(MyCustomer mc){
		print("Called tellCustomerRestaurantFull");
		mc.c.msgRestaurantFull();
		mc.cs = CustomerState.mayLeave;
	}
	
	private void tellWaiterOKToGoOnBreak(Waiter w){
		print("Called tellWaiterOKToGoOnBreak");
		w.msgOKToGoOnBreak();
	}
	private void tellWaiterNOTOKToGoOnBreak(Waiter w){
		print("Called tellWaiterNOTOKToGoOnBreak");
		w.msgNOTOKToGoOnBreak();
	}
	

	//utilities

	/* (non-Javadoc)
	 * @see restaurant.Host#setGui(restaurant.gui.WaiterGui)
	 */
	@Override
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getGui()
	 */
	@Override
	public WaiterGui getGui() {
		return waiterGui;
	}

	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	public class MyCustomer{
		Customer c;
		CustomerState cs;
		
		MyCustomer(Customer cust, CustomerState custstate){
			c = cust;
			cs = custstate;
		}
	}

}

