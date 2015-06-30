package client;

import java.awt.*;
import javax.swing.*;

public class panel extends JPanel{
	public void paintComponent(Graphics g){
		g.setColor(Color.orange);
		g.fillRect(20, 40, 100, 100);
	}

}
