package client;
import processing.core.PApplet;


public class Pie 
{
	private int i, q;
	private float start, end;
	private PApplet parent;
	String name[];
	String here;
	
	public Pie(PApplet a, float x, float y, int u, int s, String[] name)
	{
		this.name = name;
		this.parent = a;
		this.start = x;
		this.end = y;
		this.i = u;
		this.q = s;
	}
	
	public void display()
	{
		parent.textSize(30);
		
		if(q == 1)
		{
			if(i % 5 == 0)
			{	
				parent.fill(133, 252, 116);
			}
			if(i % 5 == 1)
			{
				parent.fill(247, 250, 118);
			}
			if(i % 5 == 2)
			{	
				parent.fill(2, 19, 232);
			}
			if(i % 5 == 3)
			{	
				parent.fill(85 , 238, 251);
			}
			if(i % 5 == 4)
			{	
				parent.fill(222, 39, 108);
			}
		}
		if(q == 2)
		{
			if(i % 5 == 0)
			{
				
				parent.fill(222, 39, 108);
			}
			if(i % 5 == 1)
			{
				
				parent.fill(133, 252, 116);
			}
			if(i % 5 == 2)
			{
				
				parent.fill(247, 250, 118);
			}
			if(i % 5 == 3)
			{
				
				parent.fill(2, 19, 232);
			}
			if(i % 5 == 4)
			{
				
				parent.fill(85 , 238, 251);
			}
		}
		if(q == 3)
		{
			if(i % 5 == 0)
			{
				
				parent.fill(85 , 238, 251);
				
			}
			if(i % 5 == 1)
			{
				
				parent.fill(222, 39, 108);
			}
			if(i % 5 == 2)
			{
				
				parent.fill(133, 252, 116);
			}
			if(i % 5 == 3)
			{
				
				parent.fill(247, 250, 118);
			}
			if(i % 5 == 4)
			{
				
				parent.fill(2, 19, 232);
			}
		}
		if(q == 4)
		{
			if(i % 5 == 0)
			{
				
				parent.fill(2, 19, 232);
			}
			if(i % 5 == 1)
			{
				
				parent.fill(85 , 238, 251);
			}
			if(i % 5 == 2)
			{
			
				parent.fill(222, 39, 108);
			}
			if(i % 5 == 3)
			{
				
				parent.fill(133, 252, 116);
			}
			if(i % 5 == 4)
			{
				
				parent.fill(247, 250, 118);
			}
		}
		if(q == 5)
		{
			if(i % 5 == 0)
			{
				
				parent.fill(247, 250, 118);
			}
			if(i % 5 == 1)
			{
				
				parent.fill(2, 19, 232);
			}
			if(i % 5 == 2)
			{
				
				parent.fill(85 , 238, 251);
			}
			if(i % 5 == 3)
			{
				
				parent.fill(222, 39, 108);
			}
			if(i % 5 == 4)
			{
				
				parent.fill(133, 252, 116);
			}
		}
		
		parent.arc(400, 270, 320, 320, start, end);
		parent.textSize(15);
		
		if(q == 1)
		{
			if(i % 5 == 0)
			{	
				parent.fill(0);
				parent.text(name[0], 300, 370);
			}
			if(i % 5 == 1)
			{
				parent.fill(0);
				parent.text(name[4], 300, 370);
			}
			if(i % 5 == 2)
			{
				parent.fill(0);
				parent.text(name[3], 300, 370);
			}
			if(i % 5 == 3)
			{
				parent.fill(0);
				parent.text(name[2], 300, 370);
			}
			if(i % 5 == 4)
			{
				parent.fill(0);
				parent.text(name[1], 300, 370);
			}
		}
		if(q == 2)
		{
			if(i % 5 == 0)
			{
				parent.fill(0);
				parent.text(name[1], 265, 260);
			}
			if(i % 5 == 1)
			{
				parent.fill(0);
				parent.text(name[0], 265, 260);
			}
			if(i % 5 == 2)
			{
				parent.fill(0);
				parent.text(name[4], 265, 260);
			}
			if(i % 5 == 3)
			{
				parent.fill(0);
				parent.text(name[3], 265, 260);
			}
			if(i % 5 == 4)
			{
				parent.fill(0);
				parent.text(name[2], 265, 260);
			}
		}
		if(q == 3)
		{
			if(i % 5 == 0)
			{
				here = name[2];
				parent.fill(0);
				parent.text(name[2], 370, 160);
			}
			if(i % 5 == 1)
			{
				here = name[1];
				parent.fill(0);
				parent.text(name[1], 370, 160);
			}
			if(i % 5 == 2)
			{
				here = name[0];
				parent.fill(0);
				parent.text(name[0], 370, 160);
			}
			if(i % 5 == 3)
			{
				here = name[4];
				parent.fill(0);
				parent.text(name[4], 370, 160);
			}
			if(i % 5 == 4)
			{
				here = name[3];
				parent.fill(0);
				parent.text(name[3], 370, 160);
			}
		}
		if(q == 4)
		{
			if(i % 5 == 0)
			{
				parent.fill(0);
				parent.text(name[3], 455, 250);
			}
			if(i % 5 == 1)
			{
				parent.fill(0);
				parent.text(name[2], 455, 250);
			}
			if(i % 5 == 2)
			{
				parent.fill(0);
				parent.text(name[1], 455, 250);
			}
			if(i % 5 == 3)
			{
				parent.fill(0);
				parent.text(name[0], 455, 250);
			}
			if(i % 5 == 4)
			{
				parent.fill(0);
				parent.text(name[4], 455, 250);
			}
		}
		if(q == 5)
		{
			if(i % 5 == 0)
			{
				parent.fill(0);
				parent.text(name[4], 430, 370);
			}
			if(i % 5 == 1)
			{
				parent.fill(0);
				parent.text(name[3], 430, 370);
			}
			if(i % 5 == 2)
			{
				parent.fill(0);
				parent.text(name[2], 430, 370);
			}
			if(i % 5 == 3)
			{
				parent.fill(0);
				parent.text(name[1], 430, 370);
			}
			if(i % 5 == 4)
			{
				parent.fill(0);
				parent.text(name[0], 430, 370);
			}
		}
	}
	public String getFav(){
		return here;
	}
	
}
