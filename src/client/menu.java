package client;



import internetFinal.videoClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.*;

public class menu extends JFrame implements ActionListener{
		Game game;
		Container ct;

		bgPanel bgp;
		subPanel sbp;
		regPanel rgp;
		userPanel usp;
		loPanel lop;
		topPhoto top3;
		videoPanel vdp;
		
		int login = 0;
		int state = 0;
		int[] thisOne = new int[5];

		JButton searchBut;
		JButton topBut;
		JButton userBut;
		JButton videoBut;
		JButton loginBut;
		JButton logoutBut;
		JButton regBut;
		JButton go1;
		JButton go2;
		JButton go3;
		JScrollPane scroller;
		JTextField search = new JTextField(10);
		JTextField userIn = new JTextField(10);
		JTextField mailIn = new JTextField(10);
		JPasswordField passwordIn = new JPasswordField(10);
		JLabel top;
		String[] video;
		int[] index;
		String[] good = new String[5];
		
		PrintWriter writerInMenu;
		public menu(PrintWriter writer){
			writerInMenu = writer;
		}
		
//		public static void main(String[] args)
//		{
//			
//			
//			menu m = new menu();
//			m.go();
//		}
		
		public void go()
		{
//			videoClient vc = new videoClient(this);
//			vc.go();
//			
//			//
//			System.out.println("vc writer: "+vc.writer);
//			Thread writerThread = new Thread(new outgoingWriter(vc.writer,"fileList"));
//			writerThread.start();
			
			
			JFrame window = new JFrame();
			ct = window.getContentPane();
			window.setLayout(null);

			//menu button
			userBut = new JButton(new ImageIcon("icons/userBut.png"));
			userBut.setRolloverIcon(new ImageIcon("icons/userBut2.png"));
			userBut.setBounds(794,200,150,100);
			userBut.addActionListener(this);
			userBut.setBorderPainted(false);
			ct.add(userBut);
			
			videoBut = new JButton(new ImageIcon("icons/videoBut.png"));
			videoBut.setRolloverIcon(new ImageIcon("icons/videoBut2.png"));
			videoBut.setBounds(794,330,150,100);
			videoBut.addActionListener(this);
			videoBut.setBorderPainted(false);
			ct.add(videoBut);
			
			topBut = new JButton(new ImageIcon("icons/topBut.png"));
			topBut.setRolloverIcon(new ImageIcon("icons/topBut2.png"));
			topBut.setBounds(794,460,150,100);
			topBut.addActionListener(this);
			topBut.setBorderPainted(false);
			ct.add(topBut);

			
			//首頁--最hot影片
			sbp = new subPanel(index, video);
			sbp.setBounds(0,0,1000,700);
			ct.add(sbp);
			
			////觀看按鈕
			go1 = new JButton(new ImageIcon("icons/tv_icon_v2.png"));
			go1.setBounds(660,270,50,50);
			go1.addActionListener(this);
			go1.setBorderPainted(false);
			ct.add(go1);
			
			go2 = new JButton(new ImageIcon("icons/tv_icon_v2.png"));
			go2.setBounds(660,395,50,50);
			go2.addActionListener(this);
			go2.setBorderPainted(false);
			ct.add(go2);
			
			go3 = new JButton(new ImageIcon("icons/tv_icon_v2.png"));
			go3.setBounds(660,515,50,50);
			go3.addActionListener(this);
			go3.setBorderPainted(false);
			ct.add(go3);
			
			
			//會員專區
			usp = new userPanel(login);
			usp.setBounds(0,0,1000,700);
			usp.setVisible(false);
			usp.setLayout(null);
			ct.add(usp);
			
			//註冊專區
			rgp = new regPanel();
			rgp.setBounds(0,0,1000,700);
			rgp.setVisible(false);
			rgp.setLayout(null);
			ct.add(rgp);
			
			//登出專區
			lop = new loPanel();
			lop.setBounds(0,0,1000,700);
			lop.setVisible(false);
			lop.setLayout(null);
			ct.add(lop);
			
			
			////登入註冊按鈕
			userIn.setBounds(390,340,150,32);
			passwordIn.setBounds(390,390,150,32);
			mailIn.setBounds(390,440,150,32);

			loginBut = new JButton(new ImageIcon("icons/loginBut.png"));
			loginBut.setRolloverIcon(new ImageIcon("icons/loginBut2.png"));
			loginBut.setBorderPainted(false);
			loginBut.addActionListener(this);
			
			logoutBut = new JButton(new ImageIcon("icons/logoutBut.png"));
			logoutBut.setRolloverIcon(new ImageIcon("icons/logoutBut2.png"));
			logoutBut.setBounds(570,480,50,50);
			logoutBut.setBorderPainted(false);
			logoutBut.addActionListener(this);
			
			regBut = new JButton(new ImageIcon("icons/regBut.png"));
			regBut.setRolloverIcon(new ImageIcon("icons/regBut2.png"));
			regBut.setBorderPainted(false);
			regBut.addActionListener(this);

			
			//影片頁面
			ct.setLayout(null);
			vdp = new videoPanel(video, writerInMenu,this);
			vdp.setBounds(100, 100, 620, 500);
			vdp.setVisible(false);
			vdp.setLayout(null);
//			int numberOfVideo=10;
//			vdp.setPreferredSize(new Dimension(620, 40+numberOfVideo*130));
			vdp.setOpaque(false);
			vdp.setAutoscrolls(true);
			
			
			scroller = new JScrollPane(vdp);
			scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.setBounds(100, 100, 620, 500);
			scroller.setWheelScrollingEnabled(true);
			scroller.setVisible(false);
			scroller.setOpaque(false);
			scroller.getViewport().setOpaque(false);
			ct.add(scroller);
			
			////搜尋鈕
			search = new JTextField(10);
			searchBut= new JButton(new ImageIcon("icons/searchBut.png"));
			searchBut.setBorderPainted(false);
			searchBut.addActionListener(this);
			searchBut.setBounds(556, 63, 30, 30);
			search.setBounds(430,60,120,32);
//			vdp.add(search);
//			vdp.add(searchBut);
			
			
			
			//背景
			bgp = new bgPanel((new ImageIcon("pic/back33.jpg")).getImage());
			bgp.setBounds(0,0,1000,700);
			ct.add(bgp);
			
			window.setSize(1000,700);
			window.setLocation(200,50);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//進入會員專區
			if(e.getSource() == userBut){
				state = 1;
				go1.setVisible(false);
				go2.setVisible(false);
				go3.setVisible(false);
				sbp.setVisible(false);
				rgp.setVisible(false);
				vdp.setVisible(false);
				scroller.setVisible(false);
				userIn.setText("");
				passwordIn.setText("");
				
				if(login == 0){
					usp.add(userIn);
					usp.add(passwordIn);
					usp.add(loginBut);
					usp.add(regBut);
					usp.setVisible(true);
				}
				else{
					lop.add(logoutBut);
					lop.setVisible(true);
				}
				
				loginBut.setBounds(550,480,50,50);
				regBut.setBounds(620,480,50,50);
			}
			//登入按鈕
			else if(e.getSource() == loginBut){
				//登入畫面->登入
				if(state == 1){
					System.out.println("**Login**");
					System.out.println(userIn.getText());
					System.out.println(passwordIn.getPassword());
					String pwd = new String(passwordIn.getPassword());
					writerInMenu.println("login "+userIn.getText()+" "+pwd+" ");
					writerInMenu.flush();
				}
				//註冊畫面->轉入登入畫面
				if(state == 2){
					state = 1;
					usp.setVisible(true);
					rgp.setVisible(false);
					userIn.setText("");
					passwordIn.setText("");
					
					if(login == 0){
						usp.add(userIn);
						usp.add(passwordIn);
						usp.add(loginBut);
						usp.add(regBut);
					}
					
					loginBut.setBounds(550,480,50,50);
					regBut.setBounds(620,480,50,50);
				}
			}
			//註冊按鈕
			else if(e.getSource() == regBut){
				
				//登入畫面->轉入註冊畫面
				if(state == 1){
					state = 2;
					usp.setVisible(false);
					rgp.setVisible(true);
					userIn.setText("");
					mailIn.setText("");
					passwordIn.setText("");
					rgp.add(userIn);
					rgp.add(mailIn);
					rgp.add(passwordIn);
					rgp.add(loginBut);
					rgp.add(regBut);
					loginBut.setBounds(620,480,50,50);
					regBut.setBounds(550,480,50,50);
				}
				//註冊畫面->註冊
				else if(state == 2){
					System.out.println("**Register**");
					System.out.println(userIn.getText());
					System.out.println(mailIn.getText());
					System.out.println(passwordIn.getPassword());
					String pwd = new String(passwordIn.getPassword());
					
					writerInMenu.println("register "+userIn.getText()+" "+pwd+" "+mailIn.getText()+" ");
					writerInMenu.flush();
					
				}
			}
			//登出按鈕
			else if(e.getSource() == logoutBut){
				login = 0;
				usp.setVisible(true);
				lop.setVisible(false);
				usp.add(userIn);
				usp.add(passwordIn);
				usp.add(loginBut);
				usp.add(regBut);
			}
			//進入影片頁面
			else if(e.getSource() == videoBut){
				go1.setVisible(false);
				go2.setVisible(false);
				go3.setVisible(false);
				sbp.setVisible(false);
				usp.setVisible(false);
				lop.setVisible(false);
				rgp.setVisible(false);
				vdp.setVisible(true);
				scroller.setVisible(true);
				
				userIn.setText("");
				mailIn.setText("");
				passwordIn.setText("");
				
//				int[] main = null;
				int[] main = new int[video.length-1];
				for(int i = 0; i<video.length-1; i++){
					main[i] = i+1;
				}
				vdp.removeAll();
				vdp.setItems(main);
				vdp.repaint();
				vdp.setPreferredSize(new Dimension(620, 40+(main.length)*130));
				vdp.add(search);
				vdp.add(searchBut);
			}
			//精選影片
			else if(e.getSource() == topBut){
				//亂數產生推薦
				random(10);
				
				game = new Game(good, thisOne, writerInMenu, this);
			}
			//top3
			else if(e.getSource() == go1){
				System.out.println(video[index[0]]);
				writerInMenu.println("adFileData "+index[0]+" "+login+" ");
				writerInMenu.flush();
				System.out.print("writer: "+writerInMenu);
				System.out.println("adFileData "+index[0]+" "+login+" ");
			}
			else if(e.getSource() == go2){
				System.out.println(video[index[1]]);
				writerInMenu.println("adFileData "+index[1]+" "+login+" ");
				writerInMenu.flush();
				System.out.print("writer: "+writerInMenu);
				System.out.println("adFileData "+index[1]+" "+login+" ");
			}
			else if(e.getSource() == go3){
				System.out.println(video[index[2]]);
				writerInMenu.println("adFileData "+index[2]+" "+login+" ");
				writerInMenu.flush();
				System.out.print("writer: "+writerInMenu);
				System.out.println("adFileData "+index[2]+" "+login+" ");
			}
			//搜尋鈕
			else if(e.getSource() == searchBut){
				String key = search.getText();
				System.out.println(key);
				search.setText("");
				writerInMenu.println("search "+key+" ");
				writerInMenu.flush();
			}
		}	
		
		public void changeVideo(int[] main){
			if(main[0] == 0){
				JOptionPane.showMessageDialog(null, "查無影片");
			}
			else{
				vdp.removeAll();
				vdp.setItems(main);
				vdp.repaint();
				vdp.setPreferredSize(new Dimension(620, 40+(main.length)*130));
				vdp.add(search);
				vdp.add(searchBut);
			}
			
		}
		
		//亂數推薦數字
		public void random(int len){
			int i = 0;
			int tem = 0;
			int err = 0;
			
			while(i < 5){
				err = 0;
				if(i == 0){
					thisOne[i] = (int)(Math.random()*len+1);
					i++;
				}
				else{
					tem = (int)(Math.random()*len+1);
					
					for(int j = i-1; j >= 0; j--){
						if(tem == thisOne[j]){
							err = 1;
							break;
						}
					}
					
					if(err == 0){
						thisOne[i] = tem;
						i++;
					}
				}
			}
			
			for(int j = 0; j < 5; j++){
				good[j] = video[thisOne[j]];
				System.out.println(good[j]);
			}
			
		}
		
		public void setFile(String[] fileList){
			this.video = fileList;
		}
		
		public void setTop3(int[] aa){
			this.index = aa;
		}
		
		public void changeView(int i){
			//註冊成功
			if(i == 0){
				state = 1;
				usp.setVisible(true);
				rgp.setVisible(false);
				userIn.setText("");
				passwordIn.setText("");
				
				if(login == 0){
					usp.add(userIn);
					usp.add(passwordIn);
					usp.add(loginBut);
					usp.add(regBut);
				}
				
				loginBut.setBounds(550,480,50,50);
				regBut.setBounds(620,480,50,50);
				JOptionPane.showMessageDialog(null, "註冊成功");
			}
			//註冊失敗
			else if(i == 1){
				userIn.setText("");
				mailIn.setText("");
				passwordIn.setText("");
				JOptionPane.showMessageDialog(null, "帳號重複");
			}
			//登入成功
			else if(i == 2){
				login = 1;
				usp.setVisible(false);
				lop.setVisible(true);
				lop.add(logoutBut);
				
				userIn.setText("");
				passwordIn.setText("");
			}
			//登入失敗
			else if(i == 3){
				JOptionPane.showMessageDialog(null, "帳密錯誤");
			}
		}
}
