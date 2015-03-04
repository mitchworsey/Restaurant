package restaurant.interfaces;

import java.util.List;

import restaurant.MarketAgent;
import restaurant.CookAgent.Order;

public interface Cook {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getOrders();
	
	public abstract Cashier getCashier();

	public abstract void msgRestockInventory(Waiter w);

	public abstract void msgHereIsOrder(Waiter w, String choice, int table);

	public abstract void msgFoodDone(Order o);

	public abstract void msgOutOfStock(String choice);

	public abstract void msgOrderBeingProcessed(String choice);

	public abstract void msgOrderDelivered(String choice, int numberFulfilled);

	public abstract void updateMenu();

	public abstract void fillBegInventory();

	public abstract void restockInventory();

	public abstract void plateIt(Order o);

	public abstract void cookIt(Order o);

	public abstract void orderFood(Market m, String choice);

	public abstract void outOfFood(Order o);

	public abstract void addMarket(Market m);

	public abstract void msgAtPlatingArea();
	
	public abstract void msgAtCookingArea();

	public abstract void msgAtFridge();

}