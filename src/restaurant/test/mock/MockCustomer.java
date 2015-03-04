package restaurant.test.mock;


import restaurant.Check;
import restaurant.WaiterAgent.RestaurantMenu;
import restaurant.gui.CustomerGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}


	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImpatient() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(RestaurantMenu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseReOrder(RestaurantMenu rm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		//log.add(new LoggedEvent("Received HereIsYourFood from waiter"));		
	}

	@Override
	public void msgHereIsYourCheck(Check c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsYourCheck from waiter. Total = "+ c.bill));
		//cashier.msgHereIsPayment(c, getCash());
	}

	@Override
	public void msgHereIsYourChange(double change) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public double getCash() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String getChoice() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void msgAtWaitingArea() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setCash(double cash) {
		// TODO Auto-generated method stub
		
	}

}
