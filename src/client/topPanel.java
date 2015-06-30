package client;

import java.awt.*;
import javax.swing.*;

public class topPanel extends JPanel{
	JLabel top ;
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(new Color(98,98,102, 150));
		g.fillRect(100, 100, 620, 500);
		
		top = new JLabel("本月最Hot影片 Top 3");
		top.setBounds(230,100, 800, 150);
		top.setFont(new java.awt.Font("Dialog", 1, 35));  
		top.setForeground(new Color(255,159,154));
		add(top);
	}
}
