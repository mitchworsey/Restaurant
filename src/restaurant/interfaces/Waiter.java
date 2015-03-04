package restaurant.interfaces;

import java.util.List;

import restaurant.Check;
import restaurant.WaiterAgent;
import restaurant.WaiterAgent.MyCustomer;
import restaurant.gui.WaiterGui;

public interface Waiter {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getCustomers();

	public abstract boolean wantToGoOnBreak();

	public abstract boolean isOnBreak();

	public abstract boolean isOKToGoOnBreak();

	public abstract void setCook(Cook cook);

	public abstract void setHost(Host host);

	public abstract void setCashier(Cashier cashier);

	public abstract void msgBegInventoryFilled();

	public abstract void msgAddToMenu(String food);

	public abstract void msgRestockInventory();

	public abstract void msgCheckIsReady(Check c);

	public abstract void msgWantsToGoOnBreak();

	public abstract void msgBackToWork();

	public abstract void msgOKToGoOnBreak();

	public abstract void msgNOTOKToGoOnBreak();

	public abstract void msgSitAtTable(Customer c, int table);

	public abstract void msgReadyToOrder(Customer c);

	public abstract void msgHereIsMyChoice(Customer c, String choice);

	public abstract void msgOutOfFood(String choice, int table);

	public abstract void msgOrderIsReady(String choice, int table);

	public abstract void msgReadyForCheck(Customer c);

	public abstract void msgDonePayingAndLeaving(Customer c);

	public abstract void msgAtTable();

	public abstract void msgAtCook();

	public abstract void msgAtHost();

	public abstract void msgAtCashier();

	public abstract int getTableNumber(Customer c);

	public abstract MyCustomer find(Customer c);

	public abstract void setGui(WaiterGui gui);

	public abstract WaiterGui getGui();

}