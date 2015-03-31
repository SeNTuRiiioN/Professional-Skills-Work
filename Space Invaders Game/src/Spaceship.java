import java.awt.Image;
public class Spaceship extends Sprite2D {
	public Spaceship(Image i, int windowWidth) {
		super(i,i,i,i,windowWidth); // invoke constructor on superclass Sprite2D
	}
	
	public void move() {
		// apply current movement
		x+=xSpeed;
		
		// stop movement at screen edge?
		if (x<=0) {
			x=0;
			xSpeed=0;
		}
		else if (x>=winWidth-myImage.getWidth(null)) {
			x=winWidth-myImage.getWidth(null);
			xSpeed=0;
		}
	}
}
