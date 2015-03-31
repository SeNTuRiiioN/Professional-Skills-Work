import java.awt.Graphics;
import java.awt.Image;
public class Alien extends Sprite2D {
	public boolean isAlive = true;
	public boolean isHit = false;
	
	public Alien(Image i, Image i2, Image i3, Image i4,int windowWidth) {
		super(i,i2,i3,i4,windowWidth); // invoke constructor on superclass Sprite2D
	}
	
	public void paint(Graphics g) {
		if (isAlive)
		{
			super.paint(g);
		}
	}
	
	public boolean move() {
		x+=xSpeed;
		
		// direction reversal needed?
		if (x<=0 || x>=winWidth-myImage.getWidth(null))
			return true;
		else
			return false;
	}
	
	public void reverseDirection() {
		xSpeed=-xSpeed;
		y+=20;
	}
}
