package restaurant.test;

import restaurant.CashierAgent;
import restaurant.Check;
import restaurant.Check.CheckState;
import restaurant.MarketBill;
import restaurant.MarketBill.MarketBillState;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market1;
	MockMarket market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
	}	
	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact amount on the check.
	 */
	public void testOne(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		

		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.checks.size(), 0);		
				
		Check c = new Check(waiter, customer, "Chicken", 10.99, CheckState.produced);
		cashier.msgProduceCheck(waiter, customer, "Chicken");//send the message from a waiter
		
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.checks.size(), 1);
		
		assertTrue("CashierChecks should contain a check with state == produced. It doesn't.",
				cashier.checks.get(0).cs == CheckState.produced);
		
		assertTrue("CashierChecks should contain a check of price = $10.99. It contains something else instead: $" 
				+ cashier.checks.get(0).bill, cashier.checks.get(0).bill == 10.99);
		
		assertTrue("CashierChecks should contain a check with the right customer in it. It doesn't.", 
					cashier.checks.get(0).c == customer);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to waiter's ProduceCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should not have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("CashierChecks should contain a check with state == distrubuted. It doesn't.",
				cashier.checks.get(0).cs == CheckState.distributed);
		
		customer.msgHereIsYourCheck(c);//Not necessary, but helps with understanding the steps of the test
		
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourCheck\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourCheck from waiter. Total = 10.99"));
	
		cashier.msgHereIsPayment(cashier.checks.get(0), 10.99);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("CashierChecks should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.checks.get(0).change, cashier.checks.get(0).change == 0.0);
		
		
		customer.msgHereIsYourChange(c.change);//Not necessary, but helps with understanding the steps of the test
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		
		assertTrue("CashierChecks should contain a check with state == paymentFinished. It doesn't.",
				cashier.checks.get(0).cs == CheckState.paymentFinished);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario
	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay more than the amount on the check.
	 */
	public void testTwo(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.checks.size(), 0);		
				
		Check c = new Check(waiter, customer, "Chicken", 10.99, CheckState.produced);
		cashier.msgProduceCheck(waiter, customer, "Chicken");//send the message from a waiter
		
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.checks.size(), 1);
		
		assertTrue("CashierChecks should contain a check with state == produced. It doesn't.",
				cashier.checks.get(0).cs == CheckState.produced);
		
		assertTrue("CashierChecks should contain a check of price = $10.99. It contains something else instead: $" 
				+ cashier.checks.get(0).bill, cashier.checks.get(0).bill == 10.99);
		
		assertTrue("CashierChecks should contain a check with the right customer in it. It doesn't.", 
					cashier.checks.get(0).c == customer);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to waiter's ProduceCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
				
		assertEquals(
				"MockWaiter should not have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		

		assertTrue("CashierChecks should contain a check with state == distrubuted. It doesn't.",
				cashier.checks.get(0).cs == CheckState.distributed);
		
		customer.msgHereIsYourCheck(c);//Not necessary, but helps with understanding the steps of the test
		

		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourCheck\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourCheck from waiter. Total = 10.99"));
	
		cashier.msgHereIsPayment(cashier.checks.get(0), 20.00);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("CashierChecks should contain changeDue == 9.01. It contains something else instead: $" 
				+ cashier.checks.get(0).change, cashier.checks.get(0).change == 9.01);
		
		customer.msgHereIsYourChange(c.change);//Not necessary, but helps with understanding the steps of the test
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 9.01"));
		
		assertTrue("CashierChecks should contain a check with state == paymentFinished. It doesn't.",
				cashier.checks.get(0).cs == CheckState.paymentFinished);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}
	
	/**
	 * This tests the cashier under very simple terms: one customer CANNOT pay the amount on the check.
	 */
	public void testThree(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.checks.size(), 0);		
				
		Check c = new Check(waiter, customer, "Chicken", 10.99, CheckState.produced);
		cashier.msgProduceCheck(waiter, customer, "Chicken");//send the message from a waiter
		
				assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.checks.size(), 1);
		
		assertTrue("CashierChecks should contain a check with state == produced. It doesn't.",
				cashier.checks.get(0).cs == CheckState.produced);
		
		assertTrue("CashierChecks should contain a check of price = $10.99. It contains something else instead: $" 
				+ cashier.checks.get(0).bill, cashier.checks.get(0).bill == 10.99);
		
		assertTrue("CashierChecks should contain a check with the right customer in it. It doesn't.", 
					cashier.checks.get(0).c == customer);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to waiter's ProduceCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should not have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("CashierChecks should contain a check with state == distrubuted. It doesn't.",
				cashier.checks.get(0).cs == CheckState.distributed);
		
		
		customer.msgHereIsYourCheck(c);//Not necessary, but helps with understanding the steps of the test
		

		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourCheck\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourCheck from waiter. Total = 10.99"));
	
		double cashiersCashBalance = cashier.getCashInRegister();
		
		cashier.msgHereIsPayment(cashier.checks.get(0), 2.00);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertEquals("CashierUnpaidChecks should have 1 check in it. It doesn't", 1, cashier.unpaidChecks.size());
		
		assertEquals("CashierUnpaidChecks should contain this customer. It contains something else instead: " 
				+ cashier.unpaidChecks.get(0).c, customer, cashier.unpaidChecks.get(0).c);
		
		assertEquals("Cashier should have the same amount of cash in the register before and after the customer tried to pay, but couldn't."
				+ " Instead, they are different", cashiersCashBalance, cashier.getCashInRegister());
		
		
		customer.msgHereIsYourChange(c.change);//Not necessary, but helps with understanding the steps of the test
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with no change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		assertTrue("CashierChecks should contain a check with state == paymentFinished because the cashier lets the customer pay both checks"
				+ " next time the customer comes to the restaurant. It doesn't.",
				cashier.checks.get(0).cs == CheckState.paymentFinished);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}
	
	/**
	 * Tests the cashier with interleaving bills and checks from customers and markets, with customers paying the exact amount on the checks
	 * and cashiers paying the exact amount on the Market bills
	 */
	public void testFour(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		

		assertEquals("Cashier should have 0 checks in it. It doesn't.", cashier.checks.size(), 0);		
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		
		
		Check c = new Check(waiter, customer, "Chicken", 10.99, CheckState.produced);
		cashier.msgProduceCheck(waiter, customer, "Chicken");//send the message from a waiter
		MarketBill mb = new MarketBill(market1, cashier, 11.00, MarketBillState.produced);
		cashier.msgMarketBillDelivered(mb);
		
		
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		
		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.checks.size(), 1);
		assertEquals("CashierBills should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		
		assertTrue("CashierChecks should contain a check with state == produced. It doesn't.",
				cashier.checks.get(0).cs == CheckState.produced);
		assertTrue("CashierBill should contain a bill with state == distributed. It doesn't.",
				cashier.bills.get(0).mbs == MarketBillState.distributed);
		
		
		double cashiersCashBalance = cashier.getCashInRegister();
		
		
		assertTrue("CashierChecks should contain a check of price = $10.99. It contains something else instead: $" 
				+ cashier.checks.get(0).bill, cashier.checks.get(0).bill == 10.99);
		assertTrue("CashierBills should contain a bill of price = $11.00. It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 11.00);
		
		
		assertTrue("CashierChecks should contain a check with the right customer in it. It doesn't.", 
				cashier.checks.get(0).c == customer);
		assertTrue("CashierBills should contain a bill with the right market in it. It doesn't.", 
				cashier.bills.get(0).m == market1);
		
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to waiter's ProduceCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		
		assertTrue("CashierChecks should contain a check with state == distrubuted. It doesn't.",
				cashier.checks.get(0).cs == CheckState.distributed);
		
		
		assertEquals(
				"MockWaiter should not have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's MarketBillDelivered), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		
		customer.msgHereIsYourCheck(c);
		market1.msgHereIsPayment(mb, 11.00);
		
		
		assertNotSame("Cashier should not have the same amount of cash in the register before and after he pays the market. "
				+ "Instead, they are the same", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourCheck\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourCheck from waiter. Total = 10.99"));
		assertTrue("MockMarket should have logged an event for receiving \"HereIsPayment\" with the correct cash, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received HereIsPayment from cashier. Total = 11.0"));
		
		
		cashiersCashBalance = cashier.getCashInRegister();
		
		
		cashier.msgHereIsPayment(c, 10.99);
		
		
		assertTrue("CashierChecks should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.checks.get(0).change, cashier.checks.get(0).change == 0.0);
		
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		
		assertNotSame("Cashier should not have the same amount of cash in the register before and after he receives the customer's payment. "
				+ "Instead, they are the same", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		
		cashiersCashBalance = cashier.getCashInRegister();
		
		
		cashier.msgHereIsChange(mb.change);
		customer.msgHereIsYourChange(cashier.checks.get(0).change);
		
		
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
		
		
		assertEquals("Cashier should have the same amount of cash in the register before and after he gives the customer his change AND"
				+ " receives his change from the market because the customer paid the exact amount on the check"
				+ " and he paid the exact amount on the Market bill. Instead, they are different", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		
		assertTrue("CashierChecks should contain a check with state == paymentFinished. It doesn't.",
				cashier.checks.get(0).cs == CheckState.paymentFinished);
		
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	}
	
	/**
	 * Tests the one order, fulfilled by the market, bill paid in full scenario.
	 */
	public void testFive(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		market1.cashier = cashier;
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);		
		
		//step 1 of the test
		MarketBill mb = new MarketBill(market1, cashier, 11.00, MarketBillState.produced);
		cashier.msgMarketBillDelivered(mb);

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("CashierBills should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertTrue("CashierBill should contain a bill with state == distributed. It doesn't.",
				cashier.bills.get(0).mbs == MarketBillState.distributed);
		
		double cashiersCashBalance = cashier.getCashInRegister();
		
		
		assertTrue("CashierBills should contain a bill of price = $11.00. It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 11.00);
		
		assertTrue("CashierBills should contain a bill with the right market in it. It doesn't.", 
					cashier.bills.get(0).m == market1);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's MarketBillDelivered), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		market1.msgHereIsPayment(mb, 11.00);//Not necessary, but helps with understanding the steps of the test
		
		assertNotSame("Cashier should not have the same amount of cash in the register before and after he pays the market. "
				+ "Instead, they are the same", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		assertTrue("MockMarket should have logged an event for receiving \"HereIsPayment\" with the correct cash, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received HereIsPayment from cashier. Total = 11.0"));
		
		
		cashiersCashBalance = cashier.getCashInRegister();
		
		cashier.msgHereIsChange(mb.change);
		
		assertEquals("Cashier should have the same amount of cash in the register before and after he receives his change "
				+ "because he paid the exact amount on the Market bill. Instead, they are different", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	}
	
	/**
	 * Tests the one order, fulfilled by TWO markets, 2 bills paid in full scenario.
	 */
	public void testSix(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		market1.cashier = cashier;
		market2.cashier = cashier;
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);		
		
		//step 1 of the test
		MarketBill mb1 = new MarketBill(market1, cashier, 11.00, MarketBillState.produced);
		MarketBill mb2 = new MarketBill(market2, cashier, 9.00, MarketBillState.produced);
		
		cashier.msgMarketBillDelivered(mb1);//send the message from a waiter

		assertEquals("CashierBills should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertTrue("CashierBill should contain a bill with state == distributed. It doesn't.",
				cashier.bills.get(0).mbs == MarketBillState.distributed);
		
		cashier.msgMarketBillDelivered(mb2);//send the message from a waiter

		assertEquals("CashierBills should have 2 bills in it. It doesn't.", cashier.bills.size(), 2);
		
		assertTrue("CashierBill should contain 2 bills with state == distributed. It doesn't.",
				cashier.bills.get(0).mbs == MarketBillState.distributed && cashier.bills.get(1).mbs == MarketBillState.distributed);
		
		double cashiersCashBalance = cashier.getCashInRegister();
		
		assertTrue("CashierBills should contain a bill of price = $11.00 and a bill of price + $9.00. It contains something else instead: $" 
				+ cashier.bills.get(0).bill + " & " + cashier.bills.get(1).bill, 
				cashier.bills.get(0).bill == 11.00 && cashier.bills.get(1).bill == 9.00);
		
		assertTrue("CashierBills should contain bills with the right markets in it. It doesn't.", 
					cashier.bills.get(0).m == market1 && cashier.bills.get(1).m == market2);
		
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to market1's MarketBillDelivered), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		market1.msgHereIsPayment(mb1, 11.00);//Not necessary, but helps with understanding the steps of the test
		
		assertTrue("MockMarket1 should have logged an event for receiving \"HereIsPayment\" with the correct cash, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received HereIsPayment from cashier. Total = 11.0"));
		
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to market2's MarketBillDelivered), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		market2.msgHereIsPayment(mb2, 9.00);//Not necessary, but helps with understanding the steps of the test
		
		assertTrue("MockMarket2 should have logged an event for receiving \"HereIsPayment\" with the correct cash, but his last event logged reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received HereIsPayment from cashier. Total = 9.0"));
		
		
		assertNotSame("Cashier should not have the same amount of cash in the register before and after he pays the market. "
				+ "Instead, they are the same", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		
		cashiersCashBalance = cashier.getCashInRegister();
		
		cashier.msgHereIsChange(mb1.change);
		
		cashier.msgHereIsChange(mb2.change);
		
		assertEquals("Cashier should have the same amount of cash in the register before and after he receives his change from both markets "
				+ "because he paid the exact amount on the Market bills. Instead, they are different", 
				cashiersCashBalance, cashier.getCashInRegister());
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
}
