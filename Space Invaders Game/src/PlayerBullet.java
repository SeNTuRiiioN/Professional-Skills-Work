import java.awt.Image;
public class PlayerBullet extends Sprite2D {
	public PlayerBullet(Image i, int windowWidth) {
		super(i,i,i,i,windowWidth); // invoke constructor on superclass Sprite2D
	}
	
	public boolean move() {
		y-=10;
		return (y<0); // return true if bullet is offscreen and needs destroying
	}
}
