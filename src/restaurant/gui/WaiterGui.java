package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.interfaces.Waiter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class WaiterGui implements Gui {

    private Waiter agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    RestaurantGui gui;
       
    private ArrayList<FoodGui> foodGuis = new ArrayList<FoodGui>();
    
    private int xWatchPos = 0;
    private int yWatchPos = 20;
    
    private ImageIcon imageIcon = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/waiter3.png");
    private Image image = imageIcon.getImage();
    
    private int xCustPos = 0;
    private int yCustPos = 0;
    
	public static final int xTable1 = 200;
	public static final int yTable1 = 174;
	public static final int xTable2 = 200;
	public static final int yTable2 = 74;
    public static final int xTable3 = 300;
    public static final int yTable3 = 174;
    public static final int xTable4 = 300;
    public static final int yTable4 = 74;

    public WaiterGui(Waiter agent) {
        this.agent = agent;
    }
    
    public WaiterGui(Waiter w, RestaurantGui gui){ //HostAgent m) {
		agent = w;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
	}
    
    public int getXWatchPos(){
    	return xWatchPos;
    }
    
    public void setXWatchPos(int x){
    	xWatchPos += x;
    }
    
    public void setWantsToGoOnBreak() {
		agent.msgWantsToGoOnBreak();
		//setPresent(true);
	}
    public void setBackToWork(){
    	agent.msgBackToWork();
    }
    
    public boolean wantToGoOnBreak(){
    	return agent.wantToGoOnBreak();
    }
    public boolean isOKToGoOnBreak(){
    	return agent.isOKToGoOnBreak();
    }
    public boolean isOnBreak(){
    	return agent.isOnBreak();
    }
    

    public void updatePosition() {
        if (xPos < xDestination)
            xPos+=2;
        else if (xPos > xDestination)
            xPos-=2;

        if (yPos < yDestination)
            yPos+=2;
        else if (yPos > yDestination)
            yPos-=2;

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
        
        if (xPos == xDestination && yPos == yDestination & (xPos == 388 && yPos == 112)){
        	agent.msgAtCook();
        }
        
        else if (xPos == xDestination && yPos == yDestination & (xPos == 388 && yPos == 174)){
        	agent.msgAtCook();
        }
        
        if (xPos == xDestination && yPos == yDestination & (xPos == xCustPos && yPos == yCustPos)){
        	agent.msgAtHost();
        }
        
        if (xPos == xDestination && yPos == yDestination & (xPos == 24 && yPos == 230)){
        	agent.msgAtCashier();
        }
        
       /* if(xPos == -20 && yPos == -20){
        	agent.readyToBeSeated();
        	
        }*/
    }
    
    /*public void setFoodGui(String type){
    	foodGui = new FoodGui(type, gui);
    }
    */
    public void FoodGuiGoToCookingArea(String type, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(type) && fg.table == table){
    			fg.DoGoToCookingArea();
    		}
    		
    	}
    }
    public void FoodGuiGoToPlatingArea(String type, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(type) && fg.table == table){
    			fg.DoGoToPlatingArea();
    		}
    	}
    }
    
    public void createFoodGui(String type, int table){
    	FoodGui fg = new FoodGui(type, table, gui, xPos, yPos, 0);
    	foodGuis.add(fg);
    	gui.animationPanel.addGui(fg);
    }
    
    public void FoodGuiGoToCustomer(String choice, int tableNumber){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == tableNumber){
    			fg.DoGoToTable(tableNumber);
    		}
    		
    	}
    }
    public void FoodGuiSetCounter(String choice, int tableNumber){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == tableNumber){
    			//fg.setCounter();
    		}
    		
    	}
    }
    public void FoodGuiGoToCook(String choice, int tableNumber){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == tableNumber){
    			fg.DoGoToCook();
    		}
    		
    	}
    }
    
    public void foodOrdered(String choice, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodOrdered();
    		}
    	}
    }
    
    public void foodCooked(String choice, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodCooked();
    		}
    	}
    }
    
    public void foodAte(String choice, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodEaten();
    		}
    		
    	}
    }
    public void removeFoodGui(String choice, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodEaten();
    		}	
    	}
    }

    public void draw(Graphics2D g) {
    	if (image != null)
            g.drawImage(image, xPos, yPos, 35, 50, null);
    	//g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(int tableNumber) {
    	if(tableNumber==1){
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
		}
    	if(tableNumber==2){
			xDestination = xTable2 + 20;
			yDestination = yTable2 - 20;
		}
    	if(tableNumber==3){
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 20;
		}
    	if(tableNumber==4){
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 20;
		}
        //xDestination = xTable + 20;
        //yDestination = yTable - 20;
    }
    
    public void DoGoToCookingArea(){
    	xDestination = 388;
    	yDestination = 112;
    }
    
    public void DoGoToPlatingArea(){
    	xDestination = 388;
    	yDestination = 174;
    }    
    public void DoWatch(){
    	xDestination = xWatchPos;
    	yDestination = yWatchPos;
    }
    
    public void DoGoToHost(){
    	xDestination = 30;
    	yDestination = 30;
    }
    
    public void DoGoToCashier(){
    	xDestination = 24;
    	yDestination = 230;
    }
    
    public void DoGoToCustomer(int x, int y){
    	xCustPos = x+20;
    	yCustPos = y+20;
    	xDestination = x+20;
    	yDestination = y+20;
    }

    public void DoLeaveCustomer() {
        xDestination = -40;
        yDestination = -40;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
