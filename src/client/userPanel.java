package client;

import java.awt.*;
import javax.swing.*;

public class userPanel extends JPanel{
	JLabel title ;
	JLabel subtitle ;
	JLabel user;
	JLabel password;

	
	int login;
	
	public userPanel(int a)
	{
		this.login = a;
	}
	
	public void setLogin(int a)
	{
		this.login = a;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(new Color(10, 35, 69, 130));
		g.fillRect(100, 100, 620, 500);
		g.drawImage(new ImageIcon("pic/VODDD.png").getImage(),130, 450, 300, 124,this);
		
		if(login == 0){
			title = new JLabel("尚未登入！");
			title.setBounds(340,100, 800, 150);		
			title.setFont(new java.awt.Font("Dialog", 1, 30));	
			title.setForeground(new Color(163,251,239));
			
			subtitle = new JLabel("請登入開啟會員服務或是註冊新帳號");
			subtitle.setBounds(180,150, 800, 150);
			subtitle.setFont(new java.awt.Font("Dialog", 1, 30));
			subtitle.setForeground(new Color(163,251,239));
			
			user = new JLabel("Account：");
			user.setBounds(250,280, 150, 150);		
			user.setFont(new java.awt.Font("Dialog", 1, 20));	
			user.setForeground(new Color(255,240,210));
			
			password = new JLabel("Password：");
			password.setBounds(250,330, 800, 150);		
			password.setFont(new java.awt.Font("Dialog", 1, 20));	
			password.setForeground(new Color(255,240,210));
			

			add(title);
			add(subtitle);
			add(user);
			add(password);
			

		}
		else{
			title = new JLabel("歡迎登入！");
			title.setBounds(340,250, 800, 150);		
			title.setFont(new java.awt.Font("Dialog", 1, 30));	
			title.setForeground(new Color(255,155,155));
			
			subtitle = new JLabel("快快享受免廣告觀看影片吧～～～");
			subtitle.setBounds(190,300, 800, 150);
			subtitle.setFont(new java.awt.Font("Dialog", 1, 30));
			subtitle.setForeground(new Color(255,155,155));
			
			add(title);
			add(subtitle);
		}
		
		
		
	}
}
