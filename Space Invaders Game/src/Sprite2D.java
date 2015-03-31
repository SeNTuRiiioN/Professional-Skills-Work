import java.awt.*;
public class Sprite2D {
	// member data
	protected double x,y;
	protected double xSpeed=0;
	protected Image myImage, myImage2, myImage3, myImage4;
	int framesDrawn=0;
	int winWidth;
	static boolean isHit = false;
	// constructor
	public Sprite2D(Image i, Image i2, Image i3, Image i4, int windowWidth) {
		myImage = i;
		myImage2 = i2;
		myImage3 = i3;
		myImage4 = i4;
		winWidth = windowWidth;
	}
		
	public void setPosition(double xx, double yy) {
		x=xx;
		y=yy;
	}
		
	public void setXSpeed(double dx) {
		xSpeed=dx;
	}
	
	public void paint(Graphics g) {
		framesDrawn++;
		if(isHit == false)
		{
			if ( framesDrawn%100<50 )
			{
				g.drawImage(myImage, (int)x, (int)y, null);
			}
			else
			{
				g.drawImage(myImage2, (int)x, (int)y, null);
			}
		}
		if(isHit == true)
		{
			if ( framesDrawn%100<50 )
			{
				g.drawImage(myImage3, (int)x, (int)y, null);
			}
			else
			{
				g.drawImage(myImage4, (int)x, (int)y, null);
			}
		}
	}
}
