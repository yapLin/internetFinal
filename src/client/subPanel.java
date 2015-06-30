package client;

import java.awt.*;
import javax.swing.*;

public class subPanel extends JPanel{
	JLabel top ;
	topPhoto top3;
	String[] name;
	int[] index;
	
	public subPanel(int[] index, String[] name)
	{
		this.name = name;
		this.index = index;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(new Color(10, 35, 69, 130));
		g.fillRect(100, 100, 620, 500);
		
		top = new JLabel("本週最Hot影片Top3！");
		top.setBounds(210,90, 800, 150);
		top.setFont(new java.awt.Font("Dialog", 1, 40));  
		top.setForeground(new Color(163,251,239));
		add(top);
		
		top3 = new topPhoto(index, name);
		top3.setBounds(0,0,1000,700);
		add(top3);
	}
}
