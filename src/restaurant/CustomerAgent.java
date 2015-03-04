package restaurant;

import restaurant.WaiterAgent.CustomerState;
import restaurant.WaiterAgent.RestaurantMenu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import agent.Agent;

import java.awt.Menu;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer {
	private String name;
	private double cash = 0.00;
	Timer timeEat = new Timer();
	Timer timeOrder = new Timer();
	private CustomerGui customerGui;
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atWaitingArea = new Semaphore(0,true);
	//public int tableNumber;

	// agent correspondents
	private Host host = null;
	private Cashier cashier = null;
	
	private Waiter waiter = null;
	
	private Check check = null;
		
	private String choice;
	
	private RestaurantMenu restMenu = null;
	
	private boolean impatient = false;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, Seated, WaitingForFood, Eating, DoneEating, WaitingForCheck, PayingCheck, Leaving, LeavingImpatiently};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, askedForOrder, receivedFood, doneEating, receivedCheck, donePaying, doneLeaving, leaveImpatiently};
	AgentEvent event = AgentEvent.none;
	

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		if(this.name.equals("impatient"))
			impatient = true;
		
		if(this.name.equals("poor"))
			cash += 4.00;
		else if(this.name.equals("only salad"))
			cash += 6.00;
		else if(this.name.equals("dine n dash"))
			cash += 4.00;
		else
			cash += 30.00;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHost(restaurant.HostAgent)
	 */
	@Override
	public void setHost(Host host) {
		this.host = host;
	}
	public double getCash(){
		return cash;
	}
	public void setCash(double cash){
		this.cash = cash;
	}
	
	public String getChoice(){
		return choice;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#setWaiter(restaurant.WaiterAgent)
	 */
	@Override
	public void setWaiter(Waiter waiter){
		this.waiter = waiter;
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier cashier){
		this.cashier = cashier;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#isImpatient()
	 */
	@Override
	public boolean isImpatient(){
		return impatient;
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Customer#gotHungry()
	 */
	@Override
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgRestaurantFull()
	 */
	@Override
	public void msgRestaurantFull(){
		print("Received msgRestaurantFull");
		if(impatient){
			event = AgentEvent.leaveImpatiently;
			stateChanged();
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMeToTable(restaurant.WaiterAgent.RestaurantMenu)
	 */
	@Override
	public void msgFollowMeToTable(RestaurantMenu m){
		print("Received msgFollowMeToTable");
		event = AgentEvent.followWaiter;
		restMenu = m;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWhatDoYouWant()
	 */
	@Override
	public void msgWhatDoYouWant(){
		print("Received msgWhatDoYouWant");
		event = AgentEvent.askedForOrder;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgPleaseReOrder(restaurant.WaiterAgent.RestaurantMenu)
	 */
	@Override
	public void msgPleaseReOrder(RestaurantMenu rm){
		print("Received msgPleaseReOrder");
		restMenu = rm;
		event = AgentEvent.askedForOrder;
		state = AgentState.Seated;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourFood()
	 */
	@Override
	public void msgHereIsYourFood(){
		print("Received msgHereIsYourFood");
		event = AgentEvent.receivedFood;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourCheck(restaurant.Check)
	 */
	@Override
	public void msgHereIsYourCheck(Check c){
		print("Received msgHereIsYourCheck");
		check = c;
		event = AgentEvent.receivedCheck;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourChange(double)
	 */
	@Override
	public void msgHereIsYourChange(double change){
		print("Received msgHereIsYourChange");
		cash += change;
		DecimalFormat decim = new DecimalFormat("#.00");
		this.cash = Double.parseDouble(decim.format(this.cash));
		print("Current cash balance = $" + cash);
		if(name.equals("dine n dash"))
			cash+=50;
		event = AgentEvent.donePaying;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToSeat()
	 */
	@Override
	public void msgAnimationFinishedGoToSeat() {
		/*print("Received msgAnimationFinishedGoToSeat");
		//from animation
		event = AgentEvent.seated;
		stateChanged();*/
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		print("Received msgAnimationFinishedLeaveRestaurant");
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAtCashier()
	 */
	@Override
	public void msgAtCashier(){
		atCashier.release();
		stateChanged();
	}
	
	public void msgAtWaitingArea(){
		atWaitingArea.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.leaveImpatiently ){
			state = AgentState.LeavingImpatiently;
			leaveImpatiently();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.Seated;
			SitDown();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.askedForOrder){
			state = AgentState.WaitingForFood;
			SayOrder();
			return true;
		}

		if (state == AgentState.WaitingForFood && event == AgentEvent.receivedFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			waitForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.receivedCheck){
			state = AgentState.PayingCheck;
			payCheck(check);
			return true;
		}
		if (state == AgentState.PayingCheck && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		customerGui.DoGoToWaitingArea();
		try {
			atWaitingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(waiter.getTableNumber(this));
		waiter.msgReadyToOrder(this);
	}
	
	private void SayOrder(){
		/*timeOrder.schedule(new TimerTask() {
			public void run() {
				//print("Deciding what to order");
				//HACK LATER
			}
		},4000);
		*/
		choice = null;
		if(name.equals("Chicken")){
			choice = "Chicken";
			if(!restMenu.entrees.containsKey("Chicken")){
				state = AgentState.Leaving;
				leaveRestaurant();
			}
		}
		else if(name.equals("dine n dash")){
			choice = restMenu.getEntree(cash+20.00);
		}
		else{
			print("Called sayOrder");
			choice = restMenu.getEntree(cash);
		}
		if(choice == null){
			state = AgentState.Leaving;
			leaveRestaurant();
		}
		else{
			print("Ordered " + choice);
			waiter.msgHereIsMyChoice(this, choice);
		}
	}

	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timeEat.schedule(new TimerTask() {
			public void run() {
				print("Done eating " + choice);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
		//stateChanged();
	}
	
	private void waitForCheck(){
		print("called waitForCheck");
		waiter.msgReadyForCheck(this);
	}
	
	private void payCheck(Check c){
		print("called payCheck");
		customerGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cash < c.bill){
			cashier.msgHereIsPayment(c, cash);
			//cash += 100.00;
		}
		else{
			cashier.msgHereIsPayment(c, c.bill);
			cash -= c.bill;
		}
	}
	private void leaveRestaurant() {
		Do("Leaving.");
		//host.msgLeavingTable(this);
		waiter.msgDonePayingAndLeaving(this);
		customerGui.DoExitRestaurant();
	}
	
	private void leaveImpatiently() {
		Do("Leaving Impatiently.");
		//host.msgLeavingTable(this);
		host.msgLeavingImpatiently(this);
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override
	public String toString() {
		return "customer " + getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	@Override
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	@Override
	public CustomerGui getGui() {
		return customerGui;
	}
}

