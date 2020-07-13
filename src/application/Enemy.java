package application;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Enemy {
	
	/**
	 * Declare all the class variables.
	 */
	private KeyFrame death;
	private Timeline playDeath;
	private Random rnd;
	private Image planeImg;
	private Image[] deathAnimation;
	private ImageView planeNode;
	private double height = 0, width = 0, xPos = 0, yPos = 0;
	private int hitPoints, planeType, points, index = 0;
	private Boolean isDead = true;
	
	/**
	 * Initializes all the class variables and gives the user a default enemy object.
	 */
	public Enemy()
	{
		rnd = new Random();
		if(Main.getDifficulty() == 0)
			planeType = rnd.nextInt(4);
		else if(Main.getDifficulty() == 1) 
			planeType = rnd.nextInt(3) + 1;
		else 
			planeType = rnd.nextInt(2) + 2;

		planeImg = new Image("file:images/enemy" + planeType + ".png");
		planeNode = new ImageView(planeImg);
		height = planeImg.getHeight();
		width = planeImg.getWidth();
		hitPoints = planeType + 1;
		
		switch(planeType)
		{
			case 0: points = 10;
				break;
			case 1: points = 20;
				break;
			case 2: points = 30;
				break;
			case 3: points = 50;
				break;
			default: System.out.println("Error");
				break;
		}	
		setDeathAnimation();
		
	}
	
	/**
	 * Returns the amount of points the enemy is worth when killed.
	 * 
	 * @return the amount of points the enemy is worth.
	 */
	public int getPoints()
	{
		return points;
	}
	
	/**
	 * Changes the amount of hitPoints the enemy has before it is killed.
	 */
	public void damagePlane()
	{
		hitPoints--;
	}
	
	/**
	 * Returns the amount of hits the enemy can take before it is killed.
	 * 
	 * @return the amount of hits the enemy can take before it is killed.
	 */
	public int getHitPoints()
	{
		return hitPoints;
	}
	
	/**
	 * Sets the death animation for the enemy object.
	 */
	private void setDeathAnimation()
	{
		deathAnimation = new Image[7];
		for(int i = 0; i < deathAnimation.length; i++)
		{
			deathAnimation[i] = new Image("file:images/explosion" + i + ".png");
		}
	}
	
	/**
	 * Changes the value of the isDead variable.
	 */
	public void toggleIsDead()
	{
		isDead = !isDead;
	}
	
	/**
	 * Changes the value of the isDead variable.
	 * 
	 * @param killAll - a boolean value to indicate if all enemy planes are dead.
	 */
	public void toggleIsDead(Boolean killAll)
	{
		isDead = !isDead;
		
		if(killAll == true)
			isDead = true;
	}
	
	/**
	 * Returns whether the enemy is dead.
	 * 
	 * @return a boolean value to indicate if the enemy is dead.
	 */
	public Boolean getisDead()
	{
		return isDead;
	}
	
	/**
	 * Triggers the death animation for the enemy.
	 */
	public void kill()
	{
		death = new KeyFrame(Duration.millis(100), e -> 
		{
			planeNode.setImage(deathAnimation[index]);
			index++;
			if(index > 6)
			{
				isDead = true;
			}
		}); 
		playDeath = new Timeline(death);
		playDeath.setCycleCount(7);
		playDeath.play();
		
				
	}
	
	/**
	 * Changes the amount of pixels the enemy player shall move vertically.
	 */
	public void move()
	{
		if(Main.getDifficulty() == 0)
			yPos += 2.5;
		else if(Main.getDifficulty() == 1) 
			yPos += 3.5;
		else if(Main.getDifficulty() == 2)
			yPos += 4.5;
		else
			yPos += 5.5;

		planeNode.setLayoutX(xPos);
		planeNode.setLayoutY(yPos);
		
	}
	
	/**
	 * Returns the x value of the enemy object.
	 * 
	 * @return the x value of the enemy.
	 */
	public double getX()
	{
		return xPos;
	}
	
	/**
	 * Returns the y value of the enemy object.
	 * 
	 * @return the y value of the enemy.
	 */
	public double getY()
	{
		return yPos;
	}
	
	/**
	 * Changes the x value of the enemy plane.
	 * 
	 * @param x - the x value of the enemy plane.
	 */
	public void setX(double x)
	{
		xPos = x;
		planeNode.setLayoutX(xPos);
		
	}
	
	/**
	 * Changes the y value of the enemy plane.
	 * 
	 * @param y - the y value of the enemy plane.
	 */
	public void setY(double y)
	{
		yPos = y;
		planeNode.setLayoutY(yPos);
	}
	
	/**
	 * Returns the height of the enemy plane.
	 * 
	 * @return the height of the enemy plane.
	 */
	public double getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the width of the enemy plane.
	 * 
	 * @return the width of the enemy plane.
	 */
	public double getWidth()
	{
		return width;
	}	
	
	/**
	 * Returns the ImageView object of the enemy plane.
	 * 
	 * @return the ImageView object of the enemy plane.
	 */
	public ImageView getNode()
	{
		return planeNode;
	}
	
	/**
	 * Creates a rectangle around the enemy plane which will be used for collision detection.
	 * 
	 * @return the rectangle around the enemy plane.
	 */
	public Bounds getBounds()
	{
		return new Rectangle(xPos + 15 ,yPos + 15 ,width - 15 ,height - 15).getBoundsInParent();
	}
	
	/**
	 * Returns the image  or ImageView of the enemy plane.
	 * 
	 * @param current - a boolean value to indicate whether to return the ImageView or Image
	 * @return the image or ImageView of the enemy plane.
	 */
	public Image getImage(Boolean current)
	{
		if(current == true)
			return planeNode.getImage();
		else 
			return planeImg;
	}
	

		

}