package restaurant;

import restaurant.Check.CheckState;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;


public class MarketBill {
	public enum MarketBillState
	{produced, distributed, paymentFinished};
	
	public Market m;
	public Cashier c;
	public double bill;
	public double change = 0;
	public MarketBillState mbs;
	
	public MarketBill(Market m, Cashier c, double bill, MarketBillState mbs){
		this.m = m;
		this.c = c;
		this.bill = bill;
		this.mbs = mbs;
	}
}
