##Restaurant Project Repository

###Student Information
  + Name: Mitch Worsey
  + USC Email: worsey@usc.edu
  + Developed: October 2013

###Resources
  + [Restaurant v2.2](http://www-scf.usc.edu/%7Ecsci201/assignments/assignment03.html)
  + [Agent Roadmap](http://www-scf.usc.edu/~csci201/readings/agent-roadmap.html)
  + My Design Documents and Interaction Diagrams have been turned in by hand.

###Instructions
In order for my v2.2 to run, you must:
+ Compile and run the program.
+ Enter a name into either the Waiter or Customer text box.
+ Press the `Add` button.
  + Customers will not be added to v2.2 until their `Hungry?` box is clicked below.
  + Customers will not be seated until all beginning inventory has been filled.

###v2.2 Scenarios
1. One order, fulfilled by the market, bill paid in full.
  + Name the `Waiter` "one order one market".
      + The `Cook` will order Chicken from one Market.
      + The `Cashier` will receive the bill from the `Market` after the order is processed and delivered.
          + The `Cashier` will pay the exact amount on the bill.
2. One order, fulfilled by TWO markets, 2 bills paid in full.
  + Name the `Waiter` "one order two markets".
      + The `Cook` will order Chicken and Steak from two Markets.
      + The `Cashier` will receive the bills from the Markets after the orders are processed and delivered.
          + The `Cashier` will pay the exact amount on the bills.
3. EXTRA CREDIT: One order, fulfilled by the market, bill unable to be paid.
  + Name the `Waiter` "poor cashier".
      + The `Cook` will order all 4 Foods from the three available Markets.
      + `Market 1` will receive two orders, one for salad and one for pizza.
      + After the salad order has been fulfilled and delivered (salad deliveries take the shortest to process), 
        the `Cashier` will receive the bill for the salad from
        `Market 1`.
          + The `Cashier` is unable to pay this bill, so the Market will tell the Cashier he can pay this bill the next
            time he orders something.
      + Now, the `Cashier` will receive more money, allowing him to be able to pay for all future bills.
      + When the pizza order has been fulfilled and delivered (pizza deliveries take the longest to process), the `Cashier`
        will receive one bill that includes the amount due from the salad bill.
          + The `Cashier` will pay the exact amount on the bill.

###v2.2 Cashier Unit Tests
1. Test 1: This tests the cashier under very simple terms: one customer is ready to pay the exact amount on the check.
2. Test 2: This tests the cashier under very simple terms: one customer is ready to pay more than the amount on the check.
3. Test 3: This tests the cashier under very simple terms: one customer CANNOT pay the amount on the check.
4. Test 4: Tests the cashier with interleaving bills and checks from customers and markets, with customers paying the 
   exact amount on the checks and cashiers paying the exact amount on the Market bills.
5. Test 5: Tests the one order, fulfilled by the market, bill paid in full scenario.
6. Test 6: Tests the one order, fulfilled by TWO markets, 2 bills paid in full scenario.

###Possible Errors
All v2.2 requirements have been implemented in my project. However, one small animation problem may occur:
+ ONLY IF there are many customers and many waiters performing tasks in the restaurant simultaneously, a `FoodGui` icon may
  move to a customer's table without waiting for the waiter to bring it with him/her.
    + This only occurs because of how I implemented the `FoodGui` class. The Waiter's scheduler will continue to run 
      (i.e. seating customers, taking orders, delivering orders, etc.) with the other customers while it tells 
      the Food Icons to move. This error doesnt effect my v2.2 in any way other than this.
    + This error only happens occassionally (i.e. usually after waiters have seated more than 8 customers, multiple times each).

###Other Notes
+ I have completed BOTH extra credit requirements in Milestone v2.2A and Milestone v2.2D.
+ My Market, Cashier, and Customer keep track of their own money. When paying the Check, my v2.2 will outprint various values,
  such as: the Check's bill, the amount of cash the Cashier received, the amount of change the Cashier needs to give
  the Customer, the amount of cash in the Cashier's register, the amount of cash the Customer has after the payment,
  the MarketBill's bill, the amount of cash the Market received, the amount of change the Market needs to give the
  Cashier, and the amount in the Market's register.

###v2.1 Scenarios

####Testing Normative Scenarios
1. One of every type of agent, no market interactions, customer orders, pays, and leaves.
  + Run the program.
2. No customers, cook orders low items from market, when food arrives, then customers arrive.
  + Name the `Waiter` "low beg inventory".
  + My v2.2 will automatically order food from the market as soon as the restaurant opens 
    (i.e. when the first `Waiter` is added).
  + Also, NO customers will be seated until all beginning inventory has been fulfilled.
3. Multiple customers, multiple waiters, cashier operating normally, no running out of food.
  + Name the `Waiter` "high beg inventory".
  + Run the program
4. Waiter wants to go on break, he's told it's ok, goes on break when he finished all his current customers; 
   goes on break, then goes off break.
  + Add multiple waiters to the restaurant.
  + Click on any waiter's name and then click on the `Go on break?` checkbox in the information panel.
  + Once that waiter is on break, click on his name again and then click on the `Back to work?` checkbox in the information
    panel.

####Testing Non-Normative Scenarios
1. Customer orders, food has run out, cook has obviously ordered but food hasn't arrived, Customer makes new choice.
  + Name the `Waiter` "no chicken".
  + Run the program until `Chicken` is ordered twice, with other `Food` still available in the Restaurant.
      + `Cook` will tell the `Waiter` that he is out of food that food.
      + `Waiter` will tell the customer to re-order, and the customer will make another selection.
2. Cook orders from a Market, but is told they can't fulfill his order; must order from one of his backup markets.
  + Name the `Waiter` "one low market".
  + Cook will attempt to order from one `Market`
      + If that `Market` is "Market 1", then the Cook will receive a message saying that it is out of stock.
      + It will then order from a different market, the next time that 'Food' is ordered by a customer.
3. Waiter wants to go on break, he's told it's NOT OK and must keep working.
  + Run the program with only one `Waiter`.
  + Click the Waiter's `Go on break?` button.
      + The `Waiter` will receive a message from 'Host' saying that he CAN'T go on break.
      + The Waiter's `Go on break?` button will now be re-enabled.
4. Customer comes to restaurant and restaurant is full, customer is told and waits.
  + Run the program with multiple Waiters and 4 Cutomers.
  + Add another `Customer`, any name.
      + That `Customer` will receive a message from `Host` that the Restaurant is full.
      + He will wait until a table is unoccupied.
5. Customer comes to restaurant and restaurant is full, customer is told and leaves.
  + Run the program with multiple Waiters and 4 Cutomers.
  + Add another `Customer` named "impatient".
      + That `Customer` will receive a message from `Host` that the Restaurant is full.
      + He will NOT wait and will leave the Restaurant immediately, re-enabling his `Hungry?` checkbox
      + If the Restaurant is not full, "impatient" will be seated normally.
6. Customer doesn't have enough money to order anything and leaves.
  + Run the program and add a `Customer` named "poor".
  + After "poor" is seated, he will see everything is too expensive, and leave immediately.
7. Customer has only enough money to order the cheapest item.
  + Run the program and add a `Customer` named "only salad".
  + After "only salad" is seated, he will see everything is too expensive except salad, and only order salad.
8. Customer has only enough money to order the cheapest item; he orders it, then they run out of it and he leaves.
  + Run the program and add a `Customer` named "only salad".
  + Name the `Waiter` "no salad".
  + After "only salad" is seated, he will see everything is too expensive except salad, and only order salad.
      + Once the `Waiter` returns saying that 'Cook' is out of salad, "only salad" will leave immediately.
9. Customer orders, eats, but hasn't enough money to pay.
  + Run the program and add a `Customer` named "dine n dash".
  + "dine n dash" will eat whatever food he decided to order.
  + When trying to pay, `Cashier` will see he doesn't have enough money, and will tell "dine n dash" that he can pay next time.
      + "dine n dash" will leave the Restaurant, without paying for anything.
      + `Cashier` will add "dine n dash"'s original bill to `List<Check> unpaidChecks`.
      + When "dine n dash" returns to the Restaurant for a second time, he now has enough money to pay for both checks.
          + When attempting to pay his bill, 'Cashier' will add the amount on his unpaid bill to the amount on his current bill,
            making "dine n dash" pay for both.
          + "dine n dash" will pay then pay the entire bill.


