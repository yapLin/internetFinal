package client;

import java.awt.*;
import javax.swing.*;

public class bgPanel extends JPanel{
	Image im;
	public bgPanel(Image im)
	{
		this.im=im;
		this.setOpaque(true);
	}
	
	//Draw the back ground.
	public void paintComponent(Graphics g)
	{
		//Image im1 = new ImageIcon("pic/star.ico").getImage();
		super.paintComponents(g);
		g.setColor(new Color(10, 35, 69, 130));
		g.drawImage(im,0,0,this.getWidth(),this.getHeight(),this);
		//g.drawImage(im1,0,0,70,47,this);
		g.fillRect(780, 100, 180, 500);
	}

}
