
package application;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Player {

	/**
	 * Declare all the class variables.
	 */
	private double xPos, yPos, width, height;
	private int index;
	private Image[] explosion;
	private Image plane;
	private ImageView ivPlayer;
	private boolean isDead;

	/**
	 * Initializes all the class variables and gives the user a default player object.
	 */
	public Player() {
		xPos = 0;
		yPos = 0;
		index = 0;
		plane = new Image("file:images/plane.png");
		explosion = new Image[8];
		for (int i = 0; i < explosion.length; i++) {
			explosion[i] = new Image("file:images/fireball" + i + ".png");
		}
		width = plane.getWidth();
		height = plane .getHeight();
		ivPlayer = new ImageView(plane);
		ivPlayer.setX(xPos);
		ivPlayer.setY(yPos);
		isDead = false;
	}

	/**
	 * Returns the x value of the player.
	 * 
	 * @return the x value of the player
	 */
	public double getX() {
		return xPos;
	}

	/**
	 * Returns the y value of the player.
	 * 
	 * @return the y value of the player
	 */
	public double getY() {
		return yPos;
	}

	/**
	 * Returns the width of the player.
	 * 
	 * @return the width of the player
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Returns the height of the player.
	 * 
	 * @return the height of the player
	 */
	public double getHeight() {
		return height;
	}


	/**
	 * Returns the ImageView object of the player.
	 * 
	 * @return the ImageView object of the player.
	 */
	public ImageView getNode() {
		return ivPlayer;
	}

	/**
	 * Changes the ImageView of the player.
	 * 
	 * @param ivNode - the new ImageView object of the player
	 */
	public void setNode(ImageView ivNode)
	{
		ivPlayer = ivNode;
	}

	/**
	 * Returns a rectangle around the player which will be used for collision detection.
	 * 
	 * @return - the rectangle around the player.
	 */
	public Bounds getObjectBounds() {
		return new Rectangle(xPos + 10, yPos + 10, width - 10, height - 10).getBoundsInParent();
	}

	/**
	 * Returns the index of the explosion array the game is currently on.
	 * 
	 * @return the index of the image being used currently from the explosion array.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Changes the x value of the player.
	 * 
	 * @param x - the x value of the bullet.
	 */
	public void setX(double x) {
		xPos = x;
		ivPlayer.setX(xPos);
	}

	/**
	 * Changes the y value of the player.
	 * 
	 * @param y - the y value of the bullet.
	 */
	public void setY(double y) {
		yPos = y;
		ivPlayer.setY(yPos);
	}

	/**
	 * Returns the death animation.
	 * 
	 * @return the death animation.
	 */
	public Image[] getDeathAnimation()
	{
		return explosion;
	}

	/**
	 * Changes the value of the isDead variable to true or false to indicate whether the player is dead.
	 * 
	 * @param dead - specifies whether the player is dead.
	 */
	public void setDead(Boolean dead)
	{
		isDead = dead;
	}
	
	/**
	 * Returns a boolean value to indicate if the player is dead. If the value is true, the player is dead and vice versa.
	 * 
	 * @return a boolean value to indicate if the player is dead.
	 */
	public Boolean getDead()
	{
		return isDead;
	}

	/**
	 * Returns the image of the player.
	 * 
	 * @return the image of the player.
	 */
	public Image getPlaneImage()
	{
		return plane;
	}

}