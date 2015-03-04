package restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 650;
    private final int WINDOWY = 280;
    private final int TABLE1X = 200;
    private final int TABLE1Y = 174;
    private final int TABLE2X = 200;
    private final int TABLE2Y = 74;
    private final int TABLE3X = 300;
    private final int TABLE3Y = 174;
    private final int TABLE4X = 300;
    private final int TABLE4Y = 74;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private ImageIcon imageIconFloor = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/floor3.png");
    private Image imageFloor = imageIconFloor.getImage();
    private ImageIcon imageIconCashRegister = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/cashRegister3.png");
    private Image imageCashRegister = imageIconCashRegister.getImage();
    private ImageIcon imageIconFridge = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/fridge3.png");
    private Image imageFridge = imageIconFridge.getImage();
    private ImageIcon imageIconTable = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/table3.png");
    private Image imageTable = imageIconTable.getImage();
    private ImageIcon imageIconLogo = new ImageIcon("/Users/MitchWorsey/restaurant_worsey/src/restaurant/gui/images/McKaysLogo.png");
    private Image imageLogo = imageIconLogo.getImage();
    
    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        bufferSize = this.getSize();
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        if (imageFloor != null)
            g.drawImage(imageFloor, -20, -20, 575, 450, null);
        
        if (imageLogo != null)
            g.drawImage(imageLogo, 150, 250, 250, 40, null);
        
        
        //Clear the screen by painting a rectangle the size of the frame
        //g2.setColor(getBackground());
        //g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.GRAY);
        g2.fillRect(424, 75, 25, 150);
        
        if (imageFridge != null)
            g.drawImage(imageFridge, 510, 0, 40, 70, null);
        //g2.setColor(Color.BLUE);
        //g2.fillRect(540, 135, 10, 30);
        
        if (imageCashRegister != null)
            g.drawImage(imageCashRegister, 60, 250, 30, 30, null);
        //g2.setColor(Color.GREEN);
        //g2.fillRect(70, 250, 40, 20);
        
        
        //Here is the table
        if (imageTable != null)
            g.drawImage(imageTable,TABLE1X, TABLE1Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE1X, TABLE1Y, 50, 50);//200 and 250 need to be table params
        
        if (imageTable != null)
            g.drawImage(imageTable,TABLE2X, TABLE2Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE2X, TABLE2Y, 50, 50);//200 and 250 need to be table params
        
        if (imageTable != null)
            g.drawImage(imageTable,TABLE3X, TABLE3Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE3X, TABLE3Y, 50, 50);//200 and 250 need to be table params
        
        if (imageTable != null)
            g.drawImage(imageTable,TABLE4X, TABLE4Y, 50, 50, null);
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLE4X, TABLE4Y, 50, 50);//200 and 250 need to be table params


        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        synchronized(guis){
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    public void addGui(WaiterGui gui){
    	guis.add(gui);
    }
    public void addGui(FoodGui gui){
    	guis.add(gui);
    }
    public void addGui(CookGui gui){
    	guis.add(gui);
    }
}
