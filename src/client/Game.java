package client;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Game extends JFrame implements ActionListener
{
	JButton but, but2;
	Test t;
	String[] name;
	int[] index;
	int a, b;
	
	PrintWriter writerForMenu;
	menu parent;
	
	
	public Game(String[] name, int[]index, PrintWriter writer, menu m)
	{
		parent = m;
		writerForMenu = writer;
		this.name = name;
		this.index = index;
		setSize(800, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		for(int a = 0; a < 5; a++){
			int b = name[a].indexOf('-');
			if(b > 0){
				StringBuffer str = new StringBuffer(name[a]);
				str.insert(b+1, "\n");
				name[a] = str.toString();
				System.out.println(b+"---"+name[a]);
			}
		}
		
		
		t = new Test(name);
		t.setLocation(0, 0);
		t.init();
		t.start();

	
		but = new JButton(new ImageIcon(this.getClass().getResource("ok.png")));
		but.addActionListener(this);
		but.setRolloverIcon(new ImageIcon(this.getClass().getResource("ok2.png")));
		but.setBounds(64, 290, 50, 50);
		
		but2 = new JButton(new ImageIcon(this.getClass().getResource("no.png")));
		but2.addActionListener(this);
		but2.setRolloverIcon(new ImageIcon(this.getClass().getResource("no2.png")));
		but2.setBounds(64, 360, 50, 50);
		
		a = t.rate;
		b = t.o;
		
		
		add(but);
		add(but2);
		add(t);
	}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		int j = 0;
		
		if(e.getSource() == but)
		{
			//t.here
			for(int i = 0; i<name.length; i++){
				if(t.here.equals(name[i])){
					j = i;
					break;
				}
			}

			writerForMenu.println("adFileData "+index[j]+" "+parent.login+" ");
			writerForMenu.flush();
			
			this.dispose();
		}
		else if(e.getSource() == but2)
		{
			this.dispose();
		}
		
	}
}
