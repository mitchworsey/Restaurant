package restaurant.test.mock;

import java.util.List;

import restaurant.CashierAgent;
import restaurant.MarketAgent.Order;
import restaurant.MarketBill;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;

public class MockMarket extends Mock implements Market{

	public EventLog log = new EventLog();
	public Cashier cashier;

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgHereIsOrder(Cook c, String choice, int numberOrdered) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderProcessed(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliverOrder(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOrder(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outOfFood(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliverMarketBill(MarketBill mb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(MarketBill mb, double cash) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsPayment from cashier. Total = " + cash));
		
	}

	@Override
	public void giveChangeToCashier(MarketBill mb) {
		// TODO Auto-generated method stub
		
	}
	

}
