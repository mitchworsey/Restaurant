package restaurant.test.mock;

import java.util.Collection;
import java.util.List;

import restaurant.WaiterAgent;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

public class MockHost extends Mock implements Host{

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgIWantFood(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingImpatiently(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTableIsFree(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAskToGoOnBreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readyToBeSeated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWaiter(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoingOnBreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackToWork(Waiter w) {
		// TODO Auto-generated method stub
		
	}

}
