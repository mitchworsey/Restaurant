package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.interfaces.Host;

import java.awt.*;

public class HostGui implements Gui {

    private Host agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    //public static final int xTable = 200;
    //public static final int yTable = 250;
    
    
	public static final int xTable1 = 100;
	public static final int yTable1 = 150;
	public static final int xTable2 = 100;
	public static final int yTable2 = 50;
    public static final int xTable3 = 200;
    public static final int yTable3 = 150;
    public static final int xTable4 = 200;
    public static final int yTable4 = 50;

    public HostGui(Host agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20)) {
           agent.msgAtTable();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
           agent.msgAtTable();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable3 + 20) & (yDestination == yTable3 - 20)) {
           agent.msgAtTable();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable4 + 20) & (yDestination == yTable4 - 20)) {
           agent.msgAtTable();
        }
        
        if(xPos == -20 && yPos == -20){
        	agent.readyToBeSeated();
        	
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    /*public void DoBringToTable(CustomerAgent customer) {
    	if(customer.tableNumber==1){
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
		}
    	if(customer.tableNumber==2){
			xDestination = xTable2 + 20;
			yDestination = yTable2 - 20;
		}
    	if(customer.tableNumber==3){
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 20;
		}
    	if(customer.tableNumber==4){
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 20;
		}
        //xDestination = xTable + 20;
        //yDestination = yTable - 20;
    }
*/
    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
