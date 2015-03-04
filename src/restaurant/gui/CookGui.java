
package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Waiter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class CookGui implements Gui {

    private Cook agent = null;
    
    private boolean atDestination = false;

    private int xPos = 0, yPos = 0;//default Cook position
    private int xDestination = -20, yDestination = -20;//default start position
    int counter = 0;
    private int xPlatingPos = 450;
    private int yPlatingPos;
    private int xCookingPos = 450;
    private int yCookingPos;
    

    RestaurantGui gui;
       
    private ArrayList<FoodGui> foodGuis = new ArrayList<FoodGui>();
    
    private ImageIcon imageIcon = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/cook3.png");
    private Image image = imageIcon.getImage();
    
    private int xWatchPos = 450;
    private int yWatchPos = 140;
    

    public CookGui(Cook agent) {
        this.agent = agent;
    }
    
    public CookGui(Cook c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 450;
		yPos = 140;
		xDestination = 450;
		yDestination = 140;
		//maitreD = m;
		this.gui = gui;
	}
    
    public int getXWatchPos(){
    	return xWatchPos;
    }
    
    public void setXWatchPos(int x){
    	xWatchPos += x;
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
        
        if (xPos == xDestination && yPos == yDestination & (xPos == xCookingPos && yPos == yCookingPos)){
        	//if(!atDestination)
        		agent.msgAtCookingArea();
        }
        else if (xPos == xDestination && yPos == yDestination & (xPos == xPlatingPos && yPos == yPlatingPos)){
        	//if(!atDestination)
        		agent.msgAtPlatingArea();
        }
        else if (xPos == xDestination && yPos == yDestination & (xPos == 520 && yPos == 70)){
        	//if(!atDestination)
        		agent.msgAtFridge();
        }
    }
    
    public void createFoodGui(String type, int table){
    	FoodGui fg = new FoodGui(type, table, gui, xPos, yPos, counter);
    	foodGuis.add(fg);
    	gui.animationPanel.addGui(fg);
    }
    
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
    public void foodCooked(String choice, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodCooked();
    		}
    	}
    }
    public void FoodGuiGoToFridge(String type, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(type) && fg.table == table){
    			fg.DoGoToFridge();
    		}
    	}
    }
    public void foodAtFridge(String choice, int table){
    	for(FoodGui fg: foodGuis){
    		if(fg.type.equals(choice) && fg.table == table){
    			fg.foodAtFridge();
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
            g.drawImage(image, xPos, yPos-10, 20, 50, null);
    	//g.setColor(Color.DARK_GRAY);
        //g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToCookingArea(){
		if((counter % 4) == 0){
		    yCookingPos = 90;
		}
		else if((counter % 4) == 1){
			yCookingPos = 110;	
		}
		else if((counter % 4) == 2){
			yCookingPos = 130;
		}
		else if((counter % 4) == 3){
			yCookingPos = 150;
		}
		counter++;
    	xDestination = xCookingPos;
    	yDestination = yCookingPos;
    }
    
    public void DoGoToPlatingArea(){
    	if((counter % 4) == 0){
    		yPlatingPos = 170;
		}
		else if((counter % 4) == 1){
			yPlatingPos = 190;	
		}
		else if((counter % 4) == 2){
			yPlatingPos = 210;
		}
		else if((counter % 4) == 3){
			yPlatingPos = 220;
		}
    	counter++;
    	xDestination = xPlatingPos;
    	yDestination = yPlatingPos;
    }    
    public void DoWatch(){
    	xDestination = xWatchPos;
    	yDestination = yWatchPos;
    }
    public void DoGoToFridge(){
    	xDestination = 520;
    	yDestination = 70;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}

