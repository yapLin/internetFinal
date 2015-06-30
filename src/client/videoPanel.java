package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class videoPanel extends JPanel implements ActionListener{
//	private String[] fileList = {"no", "名偵探柯南-野火的向日葵", "金牌特務", "阿喵奇遇記", "犬夜叉1", "犬夜叉2", "犬夜叉3", "犬夜叉4", "犬夜叉5", "犬夜叉6", "進擊的巨人"};
	private String[] fileList;
	private ArrayList<String>videoName, videoPic;
	private ArrayList<JButton>videoButton;
	private int x=50,w=100,h=100, name_w=150, name_h=150, button_w=60, button_h=60;
	private int dx=50, dy=50, start_y=0;
	private JLabel title;
	int[] main;
	JTextField search;
	JButton searchBut;
	String ip = "http://114.36.76.84/npPhoto/";
	
	PrintWriter writerForMenu;
	menu parent;
	
	public videoPanel(String[] file, PrintWriter writer, menu m){
		writerForMenu = writer;
		parent = m;
		fileList = file;
		videoName=new ArrayList<String>();
		videoPic=new ArrayList<String>();
		for(int i=0;i<fileList.length;i++){
			videoName.add(fileList[i]);
//			videoPic.add("pic/11.jpg");
		}
		
//		setItems();
	}
	public videoPanel(ArrayList<String>_videoName, ArrayList<String>_videoPic){
		videoName=_videoName;
		videoPic=_videoPic;
		//setItems(main);
	}
	public void setItems(int[] main){
		this.main = main;
		videoButton=new ArrayList<JButton>();
		
		//search
//		search = new JTextField(10);
//		searchBut= new JButton(new ImageIcon("icons/searchBut.png"));
//		searchBut.setBorderPainted(false);
//		searchBut.addActionListener(this);
//		searchBut.setBounds(556, 63, 30, 30);
//		search.setBounds(430,60,120,32);
//		add(search);
//		add(searchBut);
		URL url = null;
		Image im1;
		
		for(int i=0;i<main.length;i++){
			int y=(i)*h+dy+start_y+20*i+43;
			JLabel top = new JLabel(fileList[main[i]]);
			top.setBounds(x+w+dx-33, y-4, name_w+320, name_h);
			top.setFont(new java.awt.Font("Dialog", 1, 30));  
			top.setForeground(new Color(255,240,210));
			add(top);
			this.revalidate();
			
			
			JButton button= new JButton(new ImageIcon("icons/tv_icon_v2.png"));
			button.setBounds(x+w+dx+name_w+dx+133, y+70, button_w, button_h);
			button.addActionListener(this);
//			button.setBackground(new Color(255,255,255,20));
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			
//			button.setFont(new java.awt.Font("Dialog", 1, 35)); 
			add(button);
			videoButton.add(button);
			this.revalidate();
			
			
			//picture
			
			
			int yy=(i)*h+dy+start_y+i*20+73;
			
			try {
				url = new URL("http://114.36.76.84/npPhoto/"+main[i]+".jpg");
				im1 = ImageIO.read(url);
				
				JLabel back = new JLabel(new ImageIcon(im1));
				back.setBounds(x,yy-4,w,h);
				add(back);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		title = new JLabel("影音專區！");
		title.setBounds(200,-12, 800, 150);
		title.setFont(new java.awt.Font("Dialog", 1, 38));	
		title.setForeground(new Color(163,251,239));
		add(title);
	}
	public void paintComponent(Graphics g){

		super.paintComponents(g);
		g.setColor(new Color(10, 35, 69, 130));
		g.fillRect(0, 0, 620, 20+main.length*170);
		
	}	
	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == searchBut){
//			int main[] = {1,2,3, 4};
//			removeAll();
//			setItems(main);
//			repaint();
//			setPreferredSize(new Dimension(620, 40+(main.length)*130));
//		}
		for(int i=0;i<videoButton.size();i++){
			if(e.getSource() == videoButton.get(i)){
				System.out.println("PRESS ON TV BUTTON: "+fileList[main[i]]);
				writerForMenu.println("adFileData "+main[i]+" "+parent.login+" ");
				writerForMenu.flush();
			}
		}
	}
}