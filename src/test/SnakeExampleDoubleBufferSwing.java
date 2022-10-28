package test;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

/** 
 * Snake example - double buffered using swing
 * The double buffer implementation was inspired by
 * 
 * Simple Java 2D Example, Double Buffering 
 * (http://www.opensource.org/licenses/bsd-license.php)
 * Author: Berlin Brown
 * https://gist.github.com/berlinbrown/4176103
 * 
 * Example at stackoverflow
 * Author: Usman Saleem 
 * https://stackoverflow.com/a/4430551
 * 
 * 
 * Remark: This example was not meant to show a up-to-date java
 *         code example but to demonstrate effects of different 
 *         rendering concepts - such as game loop and double
 *         buffering. 
 *         
 *         For using all rendering features of java consider javaFX
 *         which, e.g., has a better integration of double buffering
 *         and build-in timers for game/animation loops.   
 *         
 * @author Alfred Franz
 *
 */
public class SnakeExampleDoubleBufferSwing extends Canvas implements AWTEventListener, ActionListener   {
	
	private static final long serialVersionUID = 1L;
	
	//Size of the game area. Before rendering it is scaled by factor 2,
	//so the size of the actual window is 400 x 400 pixels + border. 
	static int width = 200;
	static int height = 200;
		
	//Double buffer: image and graphics objects
	Image dbImage;
	Graphics dbg; 
    
	//Timer for the game loop
	Timer timer;
	
	//Properties of the snake object
	ArrayList<Point2D.Float> snake;
	int snakeStartSize = 20;
	enum movingDirection {LEFT,UP,RIGHT,DOWN};
	movingDirection currentSnakeMovingDirection = movingDirection.RIGHT;
	int positionX = 50;
	int positionY = 50;
	int stopSnakeShrinking = 0;
	int score;
	
	//Properties of target
	Random myRandomGenerator = new Random();	
	Point2D.Float target = new Point2D.Float(width/2,height/2);
	
	public static void main(String[] args) {	
		final JFrame frame = new JFrame("Snake game - double buffered") {
            private static final long serialVersionUID = 1L;
            public void processWindowEvent(java.awt.event.WindowEvent e) {
                super.processWindowEvent(e);
                if (e.getID() == java.awt.event.WindowEvent.WINDOW_CLOSING) {
                    System.exit(-1);
                }
              }
        };
        frame.setPreferredSize(new Dimension((int)(width*2.1),(int)(height*2.1)));
        frame.setBackground(Color.white);
        frame.add(new SnakeExampleDoubleBufferSwing());
        frame.pack();
        frame.setVisible(true);
	}
	
	public SnakeExampleDoubleBufferSwing(){
		this.setPreferredSize(new Dimension(width*2, height*2));
        this.setBackground(Color.white);
				
		//Set up timer to drive animation events.
        timer = new Timer(50, this);
        timer.setInitialDelay(0);
        timer.start();
        
		//Add event listener for keyboard interaction		
		this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
		
		reset();
				
	}
	
	/** Resets the snake if the game starts or after the game was lost. */
	public void reset()
	{
		snake = new ArrayList<Point2D.Float>();
		Point2D.Float newPoint = new Point2D.Float(50,50);
		for (int i = 0; i < snakeStartSize; i++)
		  {
		  snake.add(newPoint);
		  newPoint.x++;
		  }
		currentSnakeMovingDirection = movingDirection.RIGHT;
		target = new Point2D.Float(width/2,height/2);
		score = 0;
	}
	
	@Override
	public void update (Graphics g) {
	    // initialize buffer
	    if (dbImage == null) {

	      dbImage = createImage (width*2, height*2);
	      dbg = dbImage.getGraphics ();
	    }

	    // clear screen in background
	    dbg.setColor (Color.white);
	    dbg.fillRect (0, 0, width*2, height*2);

	    // draw elements in background
	    paint (dbg);

	    // draw image on the screen
	    g.drawImage (dbImage, 0, 0, this); 
	  }

	@Override
	public void paint(Graphics g) {
		drawSnakeScene((Graphics2D)g);							
	}
	
	public void drawSnakeScene(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		java.awt.geom.AffineTransform my2DTransform = new java.awt.geom.AffineTransform();
		my2DTransform.scale(2, 2); //transformation to scale everything by factor 2
		g2d.setTransform(my2DTransform);
		g2d.setColor(Color.blue);
		if(snake!=null && !snake.isEmpty())
		{
			int[] x = new int[snake.size()];
			int[] y = new int[snake.size()];
			for (int i=0; i<snake.size(); i++)
			{
				x[i] = (int) snake.get(i).getX();
				y[i] = (int) snake.get(i).getY();
			}
			g2d.drawPolyline(x, y, snake.size()); //draws the snake
			
		}
		
		g2d.setColor(Color.red);
		g2d.drawRect((int)target.x-5,(int)target.y-5,10,10); //draws the target
		g2d.setColor(Color.gray);
		g2d.drawRect(20,40,width-40,height-60); //draws a border: if the snake hits it, the game is lost
		g2d.drawString("Score:     " + score, 20, 20); //draws the score
		g2d.setColor(Color.orange);
		g2d.drawOval(68, 9, 20, 13);
	}


	/**
	 * Processes keyboard events, changes the moving direction
	 * of the snake if right or left was pressed.
	 */
	@Override
	public void eventDispatched(AWTEvent event) {
	    if((event instanceof KeyEvent) && (event.getID()==KeyEvent.KEY_PRESSED)){
	      KeyEvent key = (KeyEvent)event;
	      switch(key.getKeyCode())
	      {
	      case 37: //Key_left
	    	  switch (currentSnakeMovingDirection)
	    	  {
	    	  case LEFT:
	    		  currentSnakeMovingDirection = movingDirection.DOWN;
	    		  break;
	    	  case UP:
	    		  currentSnakeMovingDirection = movingDirection.LEFT;
	    		  break;
	    	  case RIGHT:
	    		  currentSnakeMovingDirection = movingDirection.UP;
	    		  break;
	    	  case DOWN:
	    		  currentSnakeMovingDirection = movingDirection.RIGHT;
	    		  break;
	    	  }
	    	  break;
	      case 39: //Key_right
	    	  switch (currentSnakeMovingDirection)
	    	  {
	    	  case LEFT:
	    		  currentSnakeMovingDirection = movingDirection.UP;
	    		  break;
	    	  case UP:
	    		  currentSnakeMovingDirection = movingDirection.RIGHT;
	    		  break;
	    	  case RIGHT:
	    		  currentSnakeMovingDirection = movingDirection.DOWN;
	    		  break;
	    	  case DOWN:
	    		  currentSnakeMovingDirection = movingDirection.LEFT;
	    		  break;
	    	  }
	    	  break;
	      case 27: //ESC
	    	  System.exit(0);    
	    
	      }
	      key.consume();
	    }
	  }

	/** Processes a timer event for the game loop. */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(snake!=null && !snake.isEmpty())
		{
	      Point2D.Float nextSnakeElement = new Point2D.Float();
		  Point2D.Float lastSnakeElement = snake.get(snake.size()-1);
		  switch (currentSnakeMovingDirection)
	  	  {
	  	  case LEFT:
	  		  nextSnakeElement.setLocation(lastSnakeElement.getX()-1,lastSnakeElement.getY());
	  		  break;
	  	  case UP:
	  		  nextSnakeElement.setLocation(lastSnakeElement.getX(),lastSnakeElement.getY()+1);
	  		  break;
	  	  case RIGHT:
	  		  nextSnakeElement.setLocation(lastSnakeElement.getX()+1,lastSnakeElement.getY());
	  		  break;
	  	  case DOWN:
	  		  nextSnakeElement.setLocation(lastSnakeElement.getX(),lastSnakeElement.getY()-1);
	  		  break;
	  	  }	
		  snake.add(nextSnakeElement);
		  if (checkBorderHit()) reset();
		  if (checkSnakeHit()) reset();
		  if (checkTargetHit()) 
			  {
			  stopSnakeShrinking = 10;
			  score++;
			  newRandomTarget();
			  }
		  //last tail element is removed unless snake grows because a target was "eaten"
		  if (stopSnakeShrinking==0) snake.remove(0);
		  else stopSnakeShrinking--;
		  repaint();
		}
	}
	
	public boolean checkTargetHit(){
		Point2D.Float lastPoint = snake.get(snake.size()-1);
		if (lastPoint.x > (target.x-5) &&
		    lastPoint.x < (target.x+5) &&
		    lastPoint.y > (target.y-5) &&
		    lastPoint.y < (target.y+5))
		    return true;
		else 
			return false;
	}
	
	public boolean checkSnakeHit(){
		Point2D.Float lastPoint = snake.get(snake.size()-1);
		for (int i=0; i<snake.size()-2; i++) 
		{
			if (lastPoint.equals(snake.get(i))) return true;
		}
		return false;
	}
	
	public boolean checkBorderHit(){
		if (snake.get(snake.size()-1).x < 20) return true;
		else if (snake.get(snake.size()-1).y < 40) return true;
		else if (snake.get(snake.size()-1).x > width-20) return true;
		else if (snake.get(snake.size()-1).y > height-20) return true;
		else return false;
	}
	
	public void newRandomTarget()
	{
		int posX = (int)(myRandomGenerator.nextFloat()*(width-50))+25;
		int posY = (int)(myRandomGenerator.nextFloat()*(height-70))+45;
		target = new Point2D.Float(posX,posY);
	}
}

