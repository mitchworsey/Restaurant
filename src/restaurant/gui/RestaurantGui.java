package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    private JCheckBox stateCBWaiter;
    
    private JPanel bioPanel;
    private JLabel bioLabel;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 550;
        int WINDOWY = 850;

        //animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	
    	
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

        Dimension animationDim = new Dimension(WINDOWX, (int) (WINDOWY * .35));
        animationPanel.setPreferredSize(animationDim);
        animationPanel.setMinimumSize(animationDim);
        animationPanel.setMaximumSize(animationDim);
        add(animationPanel); 
        
        
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .35));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        stateCBWaiter = new JCheckBox();
        stateCBWaiter.setVisible(false);
        stateCBWaiter.addActionListener(this);

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(stateCBWaiter);
        add(infoPanel);
        
        
        
        Dimension bioDim = new Dimension(WINDOWX, (int) (WINDOWY*.2));
        bioPanel = new JPanel(new BorderLayout());
        bioPanel.setPreferredSize(bioDim);
        bioPanel.setMinimumSize(bioDim);
        bioPanel.setMaximumSize(bioDim);
        bioPanel.setBorder(BorderFactory.createTitledBorder("BIO"));

        

        bioPanel.setLayout(new GridLayout(1, 1, 30, 0));
        
        ImageIcon image = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/tumblr_m7uesh87a41rw7y79o1_500.jpg");
        
        bioLabel = new JLabel("", image, JLabel.CENTER); 
        bioPanel.add(bioLabel);
        //bioPanel.add(stateCB);
        add(bioPanel);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        
        currentPerson = person;
        
        if (person instanceof CustomerAgent) {
        	stateCB.setVisible(true);
        	stateCBWaiter.setVisible(false);
            Customer customer = (Customer) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
            //stateCB.setSelected(true);
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
            //stateCB.setEnabled(true);
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        else if (person instanceof WaiterAgent) {
        	stateCBWaiter.setVisible(true);
        	stateCB.setVisible(false);
            Waiter waiter = (Waiter) person;
           // if(!waiter.getGui().wantToGoOnBreak()){
            	stateCBWaiter.setText("Go on break?");
            	stateCBWaiter.setSelected(false);
            	stateCBWaiter.setEnabled(true);
            //}
            if(waiter.getGui().wantToGoOnBreak() || waiter.getGui().isOKToGoOnBreak()){
            	stateCBWaiter.setText("Go on break?");
            	stateCBWaiter.setSelected(true);
            	stateCBWaiter.setEnabled(false);
            }     
            if(waiter.getGui().isOnBreak()){
            	stateCBWaiter.setText("Back to work?");
            	stateCBWaiter.setSelected(false);
            	stateCBWaiter.setEnabled(true);
            }
            	
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                Customer c = (Customer) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == stateCBWaiter) {
            if (currentPerson instanceof WaiterAgent) {
                Waiter w = (Waiter) currentPerson;
                if(w.getGui().isOnBreak()){
                	w.getGui().setBackToWork();
                	stateCBWaiter.setEnabled(false);
                }
                else if(!w.getGui().wantToGoOnBreak()){
                	w.getGui().setWantsToGoOnBreak();
                	stateCBWaiter.setEnabled(false);
                }
                
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Customer c) {
        if (currentPerson instanceof CustomerAgent) {
            Customer cust = (Customer) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    public void setWaiterEnabled(Waiter w) {
        if (currentPerson instanceof WaiterAgent) {
            Waiter waiter = (Waiter) currentPerson;
            if (w.equals(waiter)) {
                stateCBWaiter.setEnabled(true);
                stateCBWaiter.setSelected(false);
            }
        }
    }
    
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
