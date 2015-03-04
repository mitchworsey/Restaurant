package restaurant.test.mock;

import java.util.List;

import restaurant.CookAgent.Order;
import restaurant.MarketAgent;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

public class MockCook extends Mock implements Cook{

	public MockCook(String name) {
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
	public void msgRestockInventory(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsOrder(Waiter w, String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodDone(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfStock(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderBeingProcessed(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderDelivered(String choice, int numberFulfilled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMenu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillBegInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restockInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void plateIt(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookIt(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderFood(Market m, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outOfFood(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMarket(Market m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Cashier getCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAtPlatingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCookingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtFridge() {
		// TODO Auto-generated method stub
		
	}

	
}
