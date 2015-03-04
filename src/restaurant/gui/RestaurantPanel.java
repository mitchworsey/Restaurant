package restaurant.gui;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.MarketAgent;
import restaurant.NormalWaiter;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Host");
    private WaiterAgent waiter;
    private CookAgent cook = new CookAgent("Cook");
    private CookGui cookGui;
    private MarketAgent market1 = new MarketAgent("Market 1");
    private MarketAgent market2 = new MarketAgent("Market 2");
    private MarketAgent market3 = new MarketAgent("Market 3");
    private CashierAgent cashier = new CashierAgent("Cashier");

    private WaiterGui waiterGui;
    private int waiterGuiXPos = 80;
    
    private int customerGuiYPos = 10;
    private int customerGuiXPos = 10;
    
    private boolean isPaused = false;

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private JButton pauseButton = new JButton("Pause");

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        /*
        host.setWaiter(waiter);        
        
        waiter.setGui(waiterGui);
        waiter.setCook(cook);
        waiter.setHost(host);

        gui.animationPanel.addGui(waiterGui);
        waiter.startThread();
        host.startThread();
        cook.startThread();*/
        cookGui = new CookGui(cook, gui);
    	cook.setGui(cookGui);
    	gui.animationPanel.addGui(cookGui);
        
        gui.animationPanel.add(pauseButton);

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));
        initRestLabel();
        group.add(restLabel);
        group.add(waiterPanel);
        group.add(customerPanel);
        pauseButton.addActionListener(this);
        //group.add(pauseButton);
        
        add(group);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pauseButton) {
        	// Chapter 2.19 describes showInputDialog()
        	if(!isPaused){
        		isPaused = true;
	            for(WaiterAgent w: waiters){
	            	w.pause();
	            }
	            market1.pause();
	            market2.pause();
	            market3.pause();
	            cashier.pause();
	            cook.pause();
	        	host.pause();
	            for(CustomerAgent c: customers){
	            	c.pause();
	            }
        	}
        	else{
        		isPaused = false;
        		for(WaiterAgent w: waiters){
            		w.resumeRestaurant();
	            }
        		market1.resumeRestaurant();
	            market2.resumeRestaurant();
	            market3.resumeRestaurant();
	            cashier.resumeRestaurant();
	            cook.resumeRestaurant();
	        	host.resumeRestaurant();
	            for(CustomerAgent c: customers){
	            	c.resumeRestaurant();
	            }
        	}
        	
        }
    }
    

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("  "), BorderLayout.EAST);
        restLabel.add(new JLabel("  "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

    	if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                Waiter temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    	
        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                Customer temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {
    	
    	if (type.equals("Waiters")) {
    		waiter = new NormalWaiter(name);	
    		waiterGui = new WaiterGui(waiter, gui);    			
    		waiterGui.setXWatchPos(waiterGuiXPos);
    		waiterGuiXPos += 40;
    		/*
    		gui.animationPanel.addGui(waiterGui);// dw
    		w.setGui(waiterGui);
    		waiters.add(w);
    		w.startThread();
    		*/
    		if(name.equals("one low market"))
    			market1.setLowInventory();
    		if(name.equals("poor cashier"))
    			cashier.setLowCash();
    		
    		host.setWaiter(waiter);    
    		
    		cook.setCashier(cashier);
    		
    		cook.setWaiterName(name);
    		
            
            waiter.setGui(waiterGui);
            waiter.setCook(cook);
            waiter.setHost(host);
            waiter.setCashier(cashier);
            //cashier.setWaiter(waiter);

            gui.animationPanel.addGui(waiterGui);
            waiters.add(waiter);
            
            cashier.startThread();
            
            market1.startThread();
            market2.startThread();
            market3.startThread();

            
            cook.addMarket(market1);
            cook.addMarket(market2);
            cook.addMarket(market3);
            
            
            waiter.startThread();
            host.startThread();
            cook.startThread();
    	}
    	
    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		/*boolean custAtStartPos = false;
    		for(Customer cust: customers){
    			if(cust.getGui().getYPos() == 10)
    				custAtStartPos = true;
    		}
    		if(!custAtStartPos)
    			customerGuiYPos = 10;
    		*/
    		g.setYWaitPos(customerGuiYPos);
    		g.setXWaitPos(customerGuiXPos);
    		customerGuiYPos += 50;
    		if(customerGuiYPos >= 260){
    			customerGuiYPos = 10;
    			customerGuiXPos += 30;
    		}
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		//g.setHungry();
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
	        	
    }

}
