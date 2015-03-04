package restaurant.interfaces;

import java.util.Collection;
import java.util.List;

import restaurant.WaiterAgent;
import restaurant.gui.WaiterGui;

public interface Host {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getCustomers();

	public abstract Collection getTables();

	public abstract void setWaiter(Waiter w);

	// Messages

	public abstract void msgIWantFood(Customer cust);

	public abstract void msgLeavingImpatiently(Customer cust);

	public abstract void msgTableIsFree(Customer cust);

	public abstract void msgAskToGoOnBreak(Waiter w);

	public abstract void msgGoingOnBreak(Waiter w);

	public abstract void msgBackToWork(Waiter w);

	public abstract void msgAtTable();

	public abstract void readyToBeSeated();

	public abstract void setGui(WaiterGui gui);

	public abstract WaiterGui getGui();

}