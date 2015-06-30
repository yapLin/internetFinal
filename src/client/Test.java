package client;
import java.util.Arrays;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;


public class Test extends PApplet
{
	int o, rate = -1;
	private Random r = new Random();
	private Pie p;
	private PImage map;
	private PFont font = createFont("華康細圓體", 50);
	private int all = 0, aa = 0, rr;
	boolean check = false;
	String[] name;
	String here;
	
	public Test(String[]name){
		this.name = name;
	}
	
	public void setup()
	{
		map = loadImage("back9.jpg");
		
		rr = r.nextInt(100);
		
		frameRate(20);
		setSize(800, 600);
	}
	
	public void draw()
	{
		image(map, 0,0);
		
		fill(163, 73 , 164);
		textFont(font);
		
		
		fill(105,146,134);
		stroke(127,148,178);
		
		
		
		
		p = new Pie(this, radians(90), radians(162), o, 1, name);
		p.display();
		p = new Pie(this, radians(162), radians(234), o, 2, name);
		p.display();
		
		p = new Pie(this, radians(306), radians(378), o, 4, name);
		p.display();
		p = new Pie(this, radians(378), radians(450), o, 5, name);
		p.display();
		p = new Pie(this, radians(234), radians(306), o, 3, name);
		p.display();
		
		
		fill(0);
		triangle(385, 250, 420, 250, 400, 180);
		
		fill(250, 166, 109);
		stroke(245, 109, 18);
		ellipse(400, 270, 75, 75);
		
		fill(0);
		textSize(32);
		text("Go", 380, 280);
		
		
		o++;
	
		
		
		if(aa > rr)
		{
			all++;
		}
		
		if(all > 20)
		{
			rate = 19;
			frameRate(19);
		}
		if(all > 30)
		{
			rate = 18;
			frameRate(18);
		}
		if(all > 20)
		{
			rate = 16;
			frameRate(16);
		}
		if(all > 25)
		{
			rate = 13;
			frameRate(13);
		}
		if(all > 30)
		{
			rate = 10;
			frameRate(10);
		}
		if(all > 35)
		{
			rate = 8;
			frameRate(8);
		}
		if(all > 40)
		{
			rate = 6;
			frameRate(6);
		}
		if(all > 43)
		{
			rate = 3;
			frameRate(3);
		}
		if(all > 46)
		{
			rate = 2;
			frameRate(2);
		}
		//frameRate(2);
		if(all > 47)
		{
			rate = 0;
			frameRate(0);
			check = true;
			here = p.getFav();
		}
		
	
		aa++;
	}
}
