package restaurant.test.mock;

import restaurant.Check;
import restaurant.MarketBill;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockCashier extends Mock implements Cashier{

	EventLog log = new EventLog();
	
	public MockCashier(String name) {
		super(name);
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgProduceCheck(Waiter w, Customer c, String choice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received ProduceCheck from waiter " + w + " for food " + choice));
	}

	@Override
	public void msgHereIsPayment(Check c, double cash) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received HereIsPayment"));
	}

	@Override
	public void giveCheckToWaiter(Check c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveChangeToCustomer(Check c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketBillDelivered(MarketBill mb) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received MarketBillDelivered"));
	}

	@Override
	public void payMarketBill(MarketBill mb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
