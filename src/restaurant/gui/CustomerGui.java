package restaurant.gui;

import restaurant.HostAgent;
import restaurant.interfaces.Customer;

import java.awt.*;

import javax.swing.ImageIcon;

public class CustomerGui implements Gui{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private boolean atDestination = false;

	private ImageIcon imageIcon = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/customer3.png");
    private Image image = imageIcon.getImage();
	
	//private HostAgent host;
	RestaurantGui gui;
	private int xWaitPos = 10;
	private int yWaitPos = 0;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	//public static final int xTable = 200;
	//public static final int yTable = 250;
	
	public static final int xTable1 = 180;
	public static final int yTable1 = 174;
	public static final int xTable2 = 180;
	public static final int yTable2 = 74;
    public static final int xTable3 = 280;
    public static final int yTable3 = 174;
    public static final int xTable4 = 280;
    public static final int yTable4 = 74;

	public CustomerGui(Customer c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = 40;
		yDestination = 40;
		//maitreD = m;
		this.gui = gui;
	}
	
	public void setYWaitPos(int y){
		yWaitPos += y;
	}
	public void setXWaitPos(int x){
		xWaitPos += x;
	}
	
	public int getXWaitPos(){
		return xWaitPos;
	}
	public int getYWaitPos(){
		return yWaitPos;
	}
	public int getYPos(){
		return yPos;
	}

	public void updatePosition() {
		atDestination = false;
		if(xPos == xDestination && yPos == yDestination && !atDestination)
			atDestination = true;
		
		if (xPos < xDestination)
			xPos+=2;
		else if (xPos > xDestination)
			xPos-=2;

		if (yPos < yDestination)
			yPos+=2;
		else if (yPos > yDestination)
			yPos-=2;

		
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
		
		if (xPos == xDestination && yPos == yDestination & (xPos == 24 && yPos == 230)){
        	agent.msgAtCashier();
        }
		if (xPos == xDestination && yPos == yDestination & (xPos == xWaitPos && yPos == yWaitPos)){
			if(!atDestination)
				agent.msgAtWaitingArea();
        }
	}

	public void draw(Graphics2D g) {
		if (image != null)
            g.drawImage(image, xPos, yPos, 20, 50, null);
		//g.setColor(Color.CYAN);
		//g.fillRect(xPos, yPos, 20, 20);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int tableNumber) {//later you will map seatnumber to table coordinates.
		//System.out.println("DoGoToSeat" + tableNumber);
		if(tableNumber==1){
			xDestination = xTable1;
			yDestination = yTable1;
		}
		if(tableNumber==2){
			xDestination = xTable2;
			yDestination = yTable2;
		}
		if(tableNumber==3){
			xDestination = xTable3;
			yDestination = yTable3;
		}
		if(tableNumber==4){
			xDestination = xTable4;
			yDestination = yTable4;
		}
		//xDestination = xTable;
		//yDestination = yTable;
		command = Command.GoToSeat;
	}
	
	public void DoGoToWaitingArea(){
		xDestination = xWaitPos;
		yDestination = yWaitPos;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoToCashier(){
		xDestination = 24;
		yDestination = 230;
	}
}
