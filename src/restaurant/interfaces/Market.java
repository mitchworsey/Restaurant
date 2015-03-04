package restaurant.interfaces;

import java.util.List;

import restaurant.MarketAgent.Order;
import restaurant.MarketBill;

public interface Market {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getOrders();

	public abstract void msgHereIsOrder(Cook c, String choice, int numberOrdered);
	
	public abstract void msgHereIsPayment(MarketBill mb, double cash);

	public abstract void msgOrderProcessed(Order o);

	public abstract void deliverOrder(Order o);
	
	public abstract void deliverMarketBill(MarketBill mb);
	
	public abstract void giveChangeToCashier(MarketBill mb);

	public abstract void processOrder(Order o);

	public abstract void outOfFood(Order o);

}