package restaurant.interfaces;

import restaurant.Check;
import restaurant.WaiterAgent.RestaurantMenu;
import restaurant.gui.CustomerGui;

public interface Customer {

	/**
	 * hack to establish connection to Host agent.
	 */
	public abstract void setHost(Host host);
	
	public abstract double getCash();
	
	public abstract void setCash(double cash);
	
	public abstract String getChoice();

	public abstract void setWaiter(Waiter waiter);

	public abstract void setCashier(Cashier cashier);

	public abstract String getCustomerName();

	public abstract boolean isImpatient();

	// Messages

	public abstract void gotHungry();

	public abstract void msgRestaurantFull();

	public abstract void msgFollowMeToTable(RestaurantMenu m);

	public abstract void msgWhatDoYouWant();

	public abstract void msgPleaseReOrder(RestaurantMenu rm);

	public abstract void msgHereIsYourFood();

	public abstract void msgHereIsYourCheck(Check c);

	public abstract void msgHereIsYourChange(double change);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgAtCashier();

	public abstract String getName();

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

	public abstract void msgAtWaitingArea();

}