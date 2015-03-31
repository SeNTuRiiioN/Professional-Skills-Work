import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InvadersApplication extends JFrame implements Runnable, KeyListener 
{
	// member data
	private static final Dimension WindowSize = new Dimension(800,600);
	private BufferStrategy strategy;
	private static final int NUMALIENS = 30;
	private Alien[] AliensArray = new Alien[NUMALIENS];
	private Spaceship PlayerShip;
	private Image bulletImage, alienImage, alienImage2, alienImage3, alienImage4;
	private ArrayList bulletsList = new ArrayList();
	boolean shoot = true;
	boolean isGameInProgress = false;
	private int count2 = 1;
	private int score = 0;
	private int highScore = 0;
	
	// constructor
	public InvadersApplication()
	{
        //Display the window, centered on the screen
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);
    	this.setTitle("Space Invaders! .. (getting there)");
        
        // load images from disk. Make sure you have the path right!
        ImageIcon icon = new ImageIcon("alien_ship_1.png");
        alienImage = icon.getImage();
        icon = new ImageIcon("alien_ship_2.png");
        alienImage2 = icon.getImage();
       
        icon = new ImageIcon("alien_ship_1_yellow.png");
        alienImage3 = icon.getImage();
        icon = new ImageIcon("alien_ship_2_yellow.png");
        alienImage4 = icon.getImage();
        
        icon = new ImageIcon("bullet.png");
        bulletImage = icon.getImage();
        
        // create and initialize some aliens, passing them each the image we have loaded
        for (int i=0; i<NUMALIENS; i++) 
        {
        	AliensArray[i] = new Alien(alienImage,alienImage2,alienImage3, alienImage4, WindowSize.width);
        }
        
        // create and initialize the player's spaceship
        icon = new ImageIcon("player_ship.png");
        Image shipImage = icon.getImage();
        PlayerShip = new Spaceship(shipImage,WindowSize.width);
        PlayerShip.setPosition(300,530);
        
        // create and start our animation thread
        Thread t = new Thread(this);
        t.start();
        
        // send keyboard events arriving into this JFrame back to its own event handlers
        addKeyListener(this);
        
        // Initialize double-buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();

	}
	
	public void startNewWave(int speed)
	{
		 // create and initialize some aliens, passing them each the image we have loaded
        for (int i=0; i<NUMALIENS; i++) 
        {
        	AliensArray[i].isAlive = true;
        	double xx = (i%5)*80 + 70;
        	double yy = (i/5)*40 + 50;
        	AliensArray[i].setPosition(xx, yy);
        	AliensArray[i].setXSpeed(speed);
        }
	}
	
	public void startNewGame()
	{
		 // create and initialize some aliens, passing them each the image we have loaded
        for (int i=0; i<NUMALIENS; i++) 
        {
        	score = 0;
        	count2 = 1;
        	AliensArray[i].isAlive = true;
        	double xx = (i%5)*80 + 70;
        	double yy = (i/5)*40 + 50;
        	AliensArray[i].setPosition(xx, yy);
        	AliensArray[i].setXSpeed(count2);
        }
	}

	// thread's entry point
	public void run() 
	{
		while ( true ) 
		{
			// 1: sleep for 1/50 second
			try 
			{
				Thread.sleep(20);
			} 
			catch (InterruptedException e) { }
			
			// 2: animate game objects
			if(highScore<score)
			{
				highScore = score;
			}
			if(isGameInProgress == true)
			{
				int count = 0;
				for (int i=0;i<NUMALIENS; i++) 
				{
					if ( AliensArray[i].isAlive == false) 
					{
						count++;
					}
				}
				
				if (count == NUMALIENS)
				{
					count2 += 2;
					startNewWave(count2);
				}
				
				for (int i=0;i<NUMALIENS; i++) 
				{
					if ( AliensArray[i].isAlive ) 
					{
						if( AliensArray[i].y>500 )
						{
							isGameInProgress = false;
						}
					}
				}
				boolean alienDirectionReversalNeeded = false;
				for (int i=0;i<NUMALIENS; i++) 
				{
					if ( AliensArray[i].isAlive )
					{
						if (AliensArray[i].move())
						{
							alienDirectionReversalNeeded=true;
						}
					}
				}
				if (alienDirectionReversalNeeded) 
				{
					for (int i=0;i<NUMALIENS; i++) 
					{
						if ( AliensArray[i].isAlive ) 
						{
							AliensArray[i].reverseDirection();
						}
					}
				}
				
				PlayerShip.move();
				
				Iterator iterator = bulletsList.iterator();
				while(iterator.hasNext())
				{
					PlayerBullet b = (PlayerBullet) iterator.next();
					if (b.move()) 
					{
						// true was returned by move method if bullet needs destroying due to going offscreen
						// iterator.remove is a safe way to remove from the ArrayList while iterating thru it
						iterator.remove();
					}
					else
					{
						// check for collision between this bullet and any alien
						double x2 = b.x, y2 = b.y;
						double w1 = 50, h1 = 32;
						double w2 = 6, h2 = 16;
						for (int i=0;i<NUMALIENS; i++) 
						{
							if ( AliensArray[i].isAlive ) 
							{
								double x1 = AliensArray[i].x;
								double y1 = AliensArray[i].y;
								if ( ((x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)) && ((y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1)) )
								{
									// destroy alien and bullet
									AliensArray[i].isAlive = false;
									Sprite2D.isHit = true;
									score++;
									// iterator.remove is a safe way to remove from the ArrayList while iterating thru it
									iterator.remove();
									break; // no need to keep checking aliens so break out of for loop
								}
							}
						}
					}	
				}
			}
			// 3: force an application repaint
			this.repaint();
		}
	}
	
	// Three Keyboard Event-Handler functions
    public void keyPressed(KeyEvent e) 
    {
    	if(isGameInProgress == true)
    	{
	    	if (e.getKeyCode()==KeyEvent.VK_LEFT)
	    		PlayerShip.setXSpeed(-4);
	    	else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
	    		PlayerShip.setXSpeed(4);
	    	else if (e.getKeyCode()==KeyEvent.VK_SPACE)
	    	{
				if(shoot == true)
				{
					shootBullet();
					shoot = false;
				}
			}
    	}
    }
    
    public void keyReleased(KeyEvent e) 
    {	
    	if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT) 
    	{
    		PlayerShip.setXSpeed(0);
    	}
    	if (e.getKeyCode()==KeyEvent.VK_SPACE)
    	{
    		shoot = true;
    	}
    }
    
    public void keyTyped(KeyEvent e) 
    { 
    	if(isGameInProgress == false)
    	{
    		startNewGame();
    		isGameInProgress = true;
    	}
    }
    
    // method to handle shooting
    public void shootBullet() 
    {
    	// add a new bullet to our list
    	PlayerBullet b = new PlayerBullet(bulletImage,WindowSize.width);
    	b.setPosition(PlayerShip.x+54/2, PlayerShip.y);
    	bulletsList.add(b);
    }

	// application's paint method
	public void paint(Graphics g) 
	{
		g = strategy.getDrawGraphics(); // draw to offscreen buffer
		
		// clear the canvas with a big black rectangle
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WindowSize.width, WindowSize.height);
		
		if(isGameInProgress == false)
		{
			Font f = new Font( "Times", Font.PLAIN, 50 );
			g.setFont(f);
			Color c = Color.WHITE;
			g.setColor(c);
			g.drawString("GAME OVER",250,150);
			Font f2 = new Font( "Times", Font.PLAIN, 25 );
			g.setFont(f2);
			g.drawString("Press any key to play",280,250);
			g.drawString("[Arrow keys to move, space to play]",210,300);
		}
		
		if(isGameInProgress == true)
		{
			Font f = new Font( "Times", Font.PLAIN, 20 );
			g.setFont(f);
			Color c = Color.WHITE;
			g.setColor(c);
			g.drawString("Score: "+score+" High Score: "+highScore,30,50);
			// redraw all game objects
			for (int i=0;i<NUMALIENS; i++)
			{
				AliensArray[i].paint(g);
			}
			
			PlayerShip.paint(g);
			
			Iterator iterator = bulletsList.iterator();
			while(iterator.hasNext())
			{
				PlayerBullet b = (PlayerBullet) iterator.next();
				b.paint(g);
			}
		}
		// flip the buffers
		g.dispose();
		strategy.show();
	}
	
	// application entry point
	public static void main(String[] args)
	{
		InvadersApplication w = new InvadersApplication();
	}
}
