package restaurant;

import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;



public class Check {
	
	public enum CheckState
	{produced, distributed, paymentFinished};
	
	public Waiter w;
	public Customer c;
	public String choice;
	public double bill;
	public double change = 0;
	public CheckState cs;
	
	public Check(Waiter w, Customer c, String choice, double bill, CheckState cs){
		this.w = w;
		this.c = c;
		this.choice = choice;
		this.bill = bill;
		this.cs = cs;
	}
}
