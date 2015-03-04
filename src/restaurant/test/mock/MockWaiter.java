package restaurant.test.mock;

import java.util.List;

import restaurant.Check;
import restaurant.WaiterAgent.MyCustomer;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{

	public EventLog log = new EventLog();
	public MockWaiter(String name) {
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
	public boolean wantToGoOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOKToGoOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCook(Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBegInventoryFilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddToMenu(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestockInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckIsReady(Check c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received CheckIsReady from cashier"));
		
	}

	@Override
	public void msgWantsToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOKToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNOTOKToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Customer c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Customer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForCheck(Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received ReadyForCheck from customer " + c));
		
	}

	@Override
	public void msgDonePayingAndLeaving(Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received DonePayingAndLeaving from customer " + c));
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtHost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTableNumber(Customer c) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MyCustomer find(Customer c) {
		// TODO Auto-generated method stub
		return null;
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

}
