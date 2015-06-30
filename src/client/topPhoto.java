package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class topPhoto extends JPanel{
	JLabel t1, t2, t3;
	JButton go1, go2, go3;
	String[] name;
	String ip = "http://114.36.76.84/npPhoto/";
	int[] index;
	public topPhoto(int[] index, String[] name)
	{
		this.name = name;
		this.index = index;
		this.setOpaque(true);
	}
	
	//Draw the back ground.
	public void paintComponent(Graphics g)
	{
		super.paintComponents(g);
		
		URL url1 = null;
		URL url2 = null;
		URL url3 = null;
		try {
			url1 = new URL(ip+index[0]+".jpg");
			url2 = new URL(ip+index[1]+".jpg");
			url3 = new URL(ip+index[2]+".jpg");
			Image im1 = ImageIO.read(url1);
			Image im2 = ImageIO.read(url2);
			Image im3 = ImageIO.read(url3);
			g.drawImage(im1,150,220,100,100,this);
			g.drawImage(im2,150,345,100,100,this);
			g.drawImage(im3,150,470,100,100,this);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		t1 = new JLabel(name[index[0]]);
		t2 = new JLabel(name[index[1]]);
		t3 = new JLabel(name[index[2]]);
		
		t1.setBounds(270,190,450,150);
		t1.setFont(new java.awt.Font("Dialog", 1, 30));  
		t1.setForeground(new Color(255,240,210));
		add(t1);
		
		t2.setBounds(270,315,450,150);
		t2.setFont(new java.awt.Font("Dialog", 1, 30));  
		t2.setForeground(new Color(255,240,210));
		add(t2);
		
		t3.setBounds(270,440,450,150);
		t3.setFont(new java.awt.Font("Dialog", 1, 30));  
		t3.setForeground(new Color(255,240,210));
		add(t3);
		
		Image no1 = (new ImageIcon("icons/no1.png")).getImage();
		g.drawImage(no1,115,180,60,60,this);
	}
}
