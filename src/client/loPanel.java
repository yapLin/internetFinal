package client;

import java.awt.*;
import javax.swing.*;

public class loPanel extends JPanel{
	JLabel title ;
	JLabel subtitle ;
	JLabel user;
	JLabel password;


	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(new Color(10, 35, 69, 130));
		g.fillRect(100, 100, 620, 500);
		g.drawImage(new ImageIcon("pic/VODDD.png").getImage(),130, 450, 300, 124,this);
		
		title = new JLabel("歡迎登入！");
		title.setBounds(340,250, 800, 150);		
		title.setFont(new java.awt.Font("Dialog", 1, 30));	
		title.setForeground(new Color(163,251,239));
		
		subtitle = new JLabel("快快享受免廣告觀看影片吧～～～");
		subtitle.setBounds(190,300, 800, 150);
		subtitle.setFont(new java.awt.Font("Dialog", 1, 30));
		subtitle.setForeground(new Color(163,251,239));
		
		add(title);
		add(subtitle);
		
		
		
		
	}
}
