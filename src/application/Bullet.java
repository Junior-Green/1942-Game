package application;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Bullet {
	
	/**
	 * Declare all the variables that shall be used in the class
	 */
	private Image bullet;
	private ImageView ivBullet;
	private double xPos, yPos, width, height;
	private boolean fired;
		
	/**
	 * Initializes all the class variables. Gives the user a default bullet object.
	 */
	public Bullet() {
		
		bullet = new Image("file:images/userBullet.png");				
		width = bullet.getWidth();
		height = bullet.getHeight();
		fired = false;
		xPos = 0;
		yPos = 0;
		
		ivBullet = new ImageView(bullet);
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
	}
	
	/**
	 * Initializes all the class variables. Gives the user a default bullet at the position they want it to be.
	 *
	 * @param x - the x value of the bullet.
	 * @param y - the y value of the bullet.
	 */
	public Bullet(double x, double y) {
		
		bullet = new Image("file:images/userBullet.png");				
		width = bullet.getWidth();
		height = bullet.getHeight();
		fired = false;
		xPos = x;
		yPos = y;
		
		ivBullet = new ImageView(bullet);
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
	}
	
	/**
	 * Returns the x value of the bullet.
	 * 
	 * @return the x value of the bullet
	 */
	public double getX() {
		
		return xPos;
	}
		
	/**
	 * Returns the y value of the bullet.
	 * 
	 * @return the y value of the bullet
	 */
	public double getY() {
		
		return yPos;
	}
	
	/**
	 *Changes the x value of the bullet and sets the x value of the ivBullet.
	 * 
	 * @param x - the x value of the bullet.
	 */
	public void setX(double x) {
		
		xPos = x;
		ivBullet.setX(xPos);	
	}
	
	/**
	 * Changes the y value of the bullet and sets the y value of the ivBullet.
	 * 
	 * @param y - the y value of the bullet.
	 */
	public void setY(double y) {
		
		yPos = y;
		ivBullet.setY(yPos);
	}
	
	/**
	 * Changes the the x and y values of the bullet all at once.
	 * 
	 * @param x - the x value of the bullet.
	 * @param y - the y value of the bullet.
	 */
	public void setPosition(double x, double y) {
		
		xPos = x;
		yPos = y;
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
	}
	
	/**
	 * Makes the bullet object move vertically 10 pixels.
	 */
	public void move() {	
		yPos -= 10;
		ivBullet.setY(yPos);
	}
	
	/**
	 * Makes the bullet object move vertically the amount of pixels specified.
	 * 
	 * @param y - the amount of pixels the object moves vertically.
	 */
	public void move(double y) {
		yPos += y;
		ivBullet.setY(yPos);
	}
	
	/**
	 * Makes the bullet object move vertically and horizontally the amount of pixels specified.
	 * 
	 * @param x - the amount of pixels the object moves horizontally.
	 * @param y - the amount of pixels the object moves vertically.
	 */
	public void move(double x, double y) {
		xPos += x;
		yPos += y;
		ivBullet.setX(xPos);
		ivBullet.setY(yPos);
	}
	
	/**
	 * Returns the width of the bullet.
	 * 
	 * @return the width of the bullet.
	 */
	public double getWidth() {		
		return width;
	}
	
	/**
	 * Returns the height of the bullet.
	 * 
	 * @return the height of the bullet.
	 */
	public double getHeight() {
		
		return height;
	}
	
	/**
	 * Returns a boolean to specify if the bullet has been fired. If the bullet has been fired it shall return true, and vice versa.
	 * 
	 * @return a boolean value to specify if the bullet has been fired.
	 */
	public Boolean getFired()
	{
		return fired;
	}
	
	/**
	 * Changes the value of the fired variable to true or false, to indicate whether the bullet has been fired or not.
	 * 
	 * @param fire - specifies if the bullet has been fired.
	 */
	public void setFired(Boolean fire)
	{
		fired = fire;
	}
	
	/**
	 * Returns the ImageView of the bullet.
	 * 
	 * @return the ImageView of the bullet.
	 */
	public ImageView getNode() {
		return ivBullet;
	}
	
	/**
	 * Creates a rectangle around the bullet which will then be used to collision detection.
	 * 
	 * @return the rectangle around the bullet
	 */
	public Bounds getBounds()
	{
		return new Rectangle(xPos,yPos,width,height).getBoundsInParent();	
	}
}