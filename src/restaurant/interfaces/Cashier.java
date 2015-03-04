package restaurant.interfaces;

import restaurant.Check;
import restaurant.MarketBill;

public interface Cashier {

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getMaitreDName()
	 */
	public abstract String getMaitreDName();

	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	public abstract String getName();

	//STUB get foods??
	// Messages
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgProduceCheck(restaurant.WaiterAgent, restaurant.CustomerAgent, java.lang.String)
	 */
	public abstract void msgProduceCheck(Waiter w, Customer c,
			String choice);
	
	
	public abstract void msgMarketBillDelivered(MarketBill mb);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgHereIsPayment(restaurant.Check, double)
	 */
	public abstract void msgHereIsPayment(Check c, double cash);

	// Actions
	/* (non-Javadoc)
	 * @see restaurant.Cashier#giveCheckToWaiter(restaurant.Check)
	 */
	public abstract void giveCheckToWaiter(Check c);

	/* (non-Javadoc)
	 * @see restaurant.Cashier#giveChangeToCustomer(restaurant.Check)
	 */
	public abstract void giveChangeToCustomer(Check c);
	
	public abstract void payMarketBill(MarketBill mb);

	public abstract void msgHereIsChange(double change);

}