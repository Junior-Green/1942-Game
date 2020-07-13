
/*Kehn Saplagio, Junior Green
 *Mr. Bulhao
 * ICS4U1
 * 30 November 2019
 */

package application;

import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application implements MouseListener{

	//Declare and/or initialize all private variable
	private Random rnd;
	private Image imgWelcome, imgBackground;
	private Timeline backgroundTimer, deathAnimationTL, spawnTimerTL;
	private ImageView[] ivBackground;
	private Image[] imgHealth, imgLives;
	private int[] yPos;
	private Player p1;
	private FileWriter fileWriter;
	private Enemy[] enemyPlanes;
	private AnimationTimer mainTimer;
	private Font gameFont;
	private File highScoreSave;
	private Label lblp1, lblHigh,lblp1score,lblHighScore;
	private int highScore, p1Score, healthRemaining, livesRemaining, planePick = 0, counter = 1000, index = -1;
	private double spawnTimer = 1;
	private ImageView ivLives, ivHealthBar;
	private Alert lifeLost, endGame, thankYou;
	private Boolean control, start = false;
	private KeyFrame spawner, deathAnimationKF;
	private Media bgmEasy, bgmMedium, bgmHard, titlebgm;
	private MediaPlayer[] mP;
	public static int difficulty = 0;

	public void start(Stage primaryStage) {
		try {
			
			//Initialize new file that stores the high score
			highScoreSave = new File("highscore.txt");
			//Scanner which gets text file stream
			Scanner sc = new Scanner(highScoreSave);
		
			//If data is available in file convert it to an int and store as high score
			while(sc.hasNextLine())
			{
				String num = sc.nextLine();
				highScore = Integer.parseInt(num);
			}
			sc.close(); //Closes scanner
 	
			//Initialize all the sound variables that shall be used in the game
			AudioClip[] sfx = new AudioClip[] {new  AudioClip("file:sounds/player_death.wav"), 
					new AudioClip("file:sounds/enemy_destroy.wav"), new AudioClip("file:sounds/titlescreen_transition.wav"), 
					new AudioClip("file:sounds/player_shoot.mp3") };
			sfx[3].setVolume(0.20);
			titlebgm = new Media(new File("titlebackground.mp3").toURI().toString()); 
			bgmEasy = new Media(new File("bgm_easy.wav").toURI().toString()); 
			bgmMedium = new Media(new File("bgm_medium.mp3").toURI().toString()); 
			bgmHard = new Media(new File("bgm_hard.mp3").toURI().toString());

			//Initialize the MediaPlayer
			mP = new MediaPlayer[] {new MediaPlayer(bgmEasy),new MediaPlayer(bgmMedium),new MediaPlayer(bgmHard), new MediaPlayer(titlebgm)};
			mP[3].play();
			mP[3].setCycleCount(Integer.MAX_VALUE);
			//Declare and Initialize the bullet array
			Bullet[] bullet = new Bullet[5];
			for(int i = 0; i < bullet.length; i++)
				bullet[i] = new Bullet();


			//Initialize alert that will show up when the player has lost a life or when player has lost the game
			lifeLost = new Alert(AlertType.INFORMATION);
			endGame = new Alert(AlertType.CONFIRMATION);
			thankYou = new Alert(AlertType.INFORMATION);

			//Variables that represent the player's health and lives
			healthRemaining = 10;
			livesRemaining = 3;

			imgWelcome = new Image("file:images/homescreen.png");
			ImageView ivWelcome = new ImageView(imgWelcome);

			//Import in-game font
			try {
				gameFont = Font.loadFont(new FileInputStream(new File("ArcadeClassic.ttf")), 28);
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			//GUI
			imgBackground = new Image("file:images/background.png");

			ivBackground = new ImageView[2];
			ivBackground[0] = new ImageView(imgBackground);
			ivBackground[1] = new ImageView(imgBackground);

			yPos = new int[] {0, -700};

			ivBackground[0].setLayoutY(yPos[0]);
			ivBackground[1].setLayoutY(yPos[1]);

			imgHealth = new Image[11];
			for (int i = 0; i < imgHealth.length; i++) {
				imgHealth[i] = new Image("file:images/health" + i + ".png");
			}

			ivHealthBar = new ImageView(imgHealth[healthRemaining]);
			ivHealthBar.setLayoutX(380);
			ivHealthBar.setLayoutY(10);

			imgLives = new Image[4];
			for (int i = 0; i < imgLives.length; i++) {
				imgLives[i] = new Image("file:images/lives" + i + ".png");
			}

			ivLives = new ImageView(imgLives[livesRemaining]);
			ivLives.setLayoutX(375);
			ivLives.setLayoutY(30);

			lblp1 = new Label("1UP");
			lblp1.setFont(gameFont);
			lblp1.setPrefSize(100, 30);
			lblp1.setAlignment(Pos.CENTER);
			lblp1.setTextFill(Color.WHITE);
			lblp1.setLayoutX(20);
			lblp1.setLayoutY(5);

			lblHigh = new Label("HIGH SCORE");
			lblHigh.setFont(gameFont);
			lblHigh.setPrefSize(160, 30);
			lblHigh.setAlignment(Pos.CENTER);
			lblHigh.setTextFill(Color.WHITE);
			lblHigh.setLayoutX(500 / 2 - 160 / 2);
			lblHigh.setLayoutY(5);

			lblp1score = new Label();
			lblp1score.setFont(gameFont);
			lblp1score.setText(Integer.toString(p1Score));
			lblp1score.setPrefSize(100, 30);
			lblp1score.setAlignment(Pos.CENTER);
			lblp1score.setTextFill(Color.WHITE);
			lblp1score.setLayoutX(20);
			lblp1score.setLayoutY(35);

			lblHighScore = new Label();
			lblHighScore.setFont(gameFont);
			lblHighScore.setText(Integer.toString(highScore));
			lblHighScore.setPrefSize(160, 30);
			lblHighScore.setAlignment(Pos.CENTER);
			lblHighScore.setTextFill(Color.WHITE);
			lblHighScore.setLayoutX(lblHigh.getLayoutX());
			lblHighScore.setLayoutY(35);

			Pane root = new Pane();
			root.getChildren().addAll(ivWelcome);

			//Initialize a new Player object
			p1 = new Player();
			//Initialize an array of enemy objects
			enemyPlanes = new Enemy[20];

			//Keyframe which spawns a new enemy object every "x" seconds in correspondence to the current difficulty
			spawner = new KeyFrame(Duration.millis(500), e ->{
				spawnTimer -= 0.500;
				if (spawnTimer <= 0)
				{
					if(difficulty == 3)
						spawnTimer = 0.500;
					else
					spawnTimer = 1;
					do
					{
						rnd = new Random();
						planePick = rnd.nextInt(20);

						if(enemyPlanes[planePick].getisDead() == true)
						{
							control = false;
						}
						else 
							control = true;
					}
					while(control == true);

					enemyPlanes[planePick] = new Enemy();
					root.getChildren().add(enemyPlanes[planePick].getNode());
					enemyPlanes[planePick].setX(rnd.nextInt((int) (root.getWidth() - enemyPlanes[planePick].getWidth())));
					enemyPlanes[planePick].setY(0 - enemyPlanes[planePick].getHeight());
					enemyPlanes[planePick].toggleIsDead();
				}

			});

			//Keyframe that triggers death animation of player when the player loses a life
			deathAnimationKF = new KeyFrame(Duration.millis(200), e -> 
			{			
				index++;
				p1.getNode().setImage(p1.getDeathAnimation()[index]);			

				if(index == 7)
				{
					livesRemaining--;
					healthRemaining = 10;
					ivLives.setImage(imgLives[livesRemaining]);	
					ivHealthBar.setImage(imgHealth[healthRemaining]);
					lifeLost = new Alert(AlertType.INFORMATION);
					lifeLost.setHeaderText(null);
					lifeLost.setTitle("1942");
					lifeLost.setGraphic(new ImageView(new Image("file:images/fireball2.png")));
					if(livesRemaining == 1)
					{
						lifeLost.setContentText("You're dead!\nYou have " + livesRemaining + " life remaining.");
						lifeLost.show();
					}
					else if (livesRemaining >= 0)
					{
						lifeLost.setContentText("You're dead!\nYou have " + livesRemaining + " lives remaining.");
						lifeLost.show();
					}
						index = -1;
				}

			});
			
			//Timeline for respective Keyframes
			deathAnimationTL = new Timeline(deathAnimationKF);
			deathAnimationTL.setCycleCount(8);
			spawnTimerTL = new Timeline(spawner);
			spawnTimerTL.setCycleCount(Timeline.INDEFINITE);

			//AnimationTimer that checks through all possibilities 
			mainTimer = new AnimationTimer()
			{

				public void handle(long arg0) 
				{			
					for(int i = 0; i < enemyPlanes.length; i ++)
					{
						if (enemyPlanes[i].getisDead() == true) //if enemy is dead hide ImageView object off of frame
						{
							enemyPlanes[i].setY(root.getHeight());
						}
						else if(enemyPlanes[i].getisDead() == false && enemyPlanes[i].getHitPoints() != 0) //If alive execute move method in enemy class
						{
							enemyPlanes[i].move();
						}
						//If enemy plane manages to bypass player and exit the screen decrement health by 1
						if(enemyPlanes[i].getY() > root.getHeight() && enemyPlanes[i].getisDead() == false)
						{
							healthRemaining--;
							ivHealthBar.setImage(imgHealth[healthRemaining]);
							enemyPlanes[i].toggleIsDead();

							//If player's health is 0 decrement the player's life by 1 and show alert indicating the loss of a life
							if(healthRemaining == 0)
							{		
								p1.setDead(true);
								deathAnimationTL.play();
								sfx[0].play();
							}	
						}		

						//If the player comes in contact with an enemy plane trigger death animation and set isDead within player class to true
						if((enemyPlanes[i].getisDead() == false && enemyPlanes[i].getBounds().intersects(p1.getObjectBounds())) && (p1.getDead() == false && enemyPlanes[i].getHitPoints() != 0))
						{
							p1.setDead(true);
							deathAnimationTL.play();
							sfx[0].play();
						}
						//Hide the enemy planes if the player death animation is currently running
						if(lifeLost.isShowing() ^ deathAnimationTL.getStatus() == Animation.Status.RUNNING)
						{	
							for(int k = 0; k < enemyPlanes.length; k++)
							{
								enemyPlanes[k].toggleIsDead(true);
								enemyPlanes[k].setY(root.getHeight());
								enemyPlanes[k].getNode().setLayoutY(enemyPlanes[k].getY());
							}

							//If the player has lost all health hide the player ImageView object
							if(endGame.isShowing() && deathAnimationTL.getStatus() == Animation.Status.STOPPED)
								p1.setY(root.getHeight());
						}
						//If player is alive and well just set the Player image to default plane image and add 15 points to score when counter reaches 1000
						else
						{	
							p1.setDead(false);
							p1.getNode().setImage(p1.getPlaneImage());
							counter--;
							if(counter <= 0)
							{
								p1Score += 15;
								lblp1score.setText(Integer.toString(p1Score));
								counter = 1000;
							}
						}

					}
					for(int b = 0; b < bullet.length; b++)
					{
						//If a bullet is fired, move it. Otherwise, the bullet should be beneath the player.
						if(bullet[b].getFired() == true)
							bullet[b].move();
						else
							bullet[b].setPosition((p1.getX() + p1.getWidth()/2)-bullet[b].getWidth()/2,(p1.getY() + p1.getHeight()/2)-bullet[b].getHeight()/2);
						
						//If a bullet goes past the top of the screen set the fired variable to false
						if (bullet[b].getY() + bullet[b].getHeight() < 0) {
							bullet[b].setFired(false);
						}
						//If a bullet intersects an enemy, the enemy shall lose a hit point. If the enemy's hit points are depleted it will trigger a death animation.
						for (int j = 0; j < enemyPlanes.length; j++) {
							if (bullet[b].getBounds().intersects(enemyPlanes[j].getBounds()) && enemyPlanes[j].getImage(true).equals(enemyPlanes[j].getImage(false))) 
							{
								bullet[b].setFired(false);
								enemyPlanes[j].damagePlane();
								if(enemyPlanes[j].getHitPoints() == 0)
								{
									p1Score += enemyPlanes[j].getPoints();
									enemyPlanes[j].kill();
									sfx[1].play();
								}
							}
						}
						//If the player is in its death animation, the bullets should be moved off the screen.
						if(deathAnimationTL.getStatus() == Animation.Status.RUNNING)
							bullet[b].setPosition(-20, -20);

					}
					//If player reaches indicated points increase the game difficulty in accordance to the static variable "difficulty"
					//and play the correct background music
					if(p1Score >= 20000)
					{
						difficulty = 3;
						
						mP[2].setVolume(0.7);
						mP[1].stop();
						mP[2].play();
						mP[2].setCycleCount(Integer.MAX_VALUE);
					}
					else if (p1Score >= 9500)
					{
						mP[1].setVolume(0.7);
						mP[0].stop();
						mP[1].play();
						mP[1].setCycleCount(Integer.MAX_VALUE);
						difficulty = 2;
					}
					else if(p1Score >= 4500)
						difficulty = 1;
					else
					{
						mP[3].stop();
						mP[0].setVolume(0.7);
						mP[0].play();
						mP[0].setCycleCount(Integer.MAX_VALUE);
						difficulty = 3;
					}				
				}		
			};

			Scene scene = new Scene(root, imgWelcome.getWidth(), imgWelcome.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					
					if(p1.getDead() == false)
					{					 			
						//If the space key is clicked after the game has started, shoot a bullet.
						if (e.getCode() == KeyCode.SPACE && start == true)
						{
							for(int i = 0; i < bullet.length; i++)
							{
								if(bullet[i].getFired() == false)
								{								
									sfx[3].play();
									bullet[i].setFired(true);
									break;
								}
							}
						}	
						else if (e.getCode() == KeyCode.SPACE && start == false)
							start = true;
					}
				} 
			});
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					
					//Transitions from home screen to gameplay screen and adds all necessary assets to the Pane
					if(start == false)
					{
						if (e.getCode() == KeyCode.SPACE) {

							sfx[2].play();
							root.getChildren().clear();
							root.getChildren().addAll(ivBackground[0], ivBackground[1], lblp1, lblHigh, lblp1score,
									lblHighScore, ivHealthBar, ivLives);

							for (int i = 0; i < enemyPlanes.length; i++)
							{
								enemyPlanes[i] = new Enemy();
								root.getChildren().add(enemyPlanes[i].getNode());
							}
							for(int i = 0; i < bullet.length; i++)
							{
								root.getChildren().add(bullet[i].getNode());
							}
							root.getChildren().add(p1.getNode());

							primaryStage.setWidth(imgBackground.getWidth());
							primaryStage.setHeight(imgBackground.getHeight());
							primaryStage.centerOnScreen();
							backgroundTimer.play();

							spawnTimerTL.play();
							mainTimer.start();
						}
					}
				} 
			});
			
			//If mouse is moved or dragged move the player ImageView object to the mouse's coordinates
			scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

				public void handle(MouseEvent e) 
				{
					if(deathAnimationTL.getStatus() == Animation.Status.STOPPED)
					{
						p1.setX(e.getX() - p1.getWidth() / 2);		
						p1.setY(e.getY() - p1.getHeight() / 2);	
						if(p1.getX() <= 0)
							p1.setX(0);
						else if (p1.getX() + p1.getWidth() > root.getWidth() )
							p1.setX(root.getWidth() - p1.getWidth());
						if(p1.getY() >  root.getHeight() - p1.getHeight())
							p1.setY(root.getHeight() - p1.getHeight());	
						if (p1.getY() < 0)
							p1.setY(0);	
						for(int i = 0; i < bullet.length; i++)
						{
							if(bullet[i].getFired() == false)
								bullet[i].setPosition((p1.getX() + p1.getWidth()/2)-bullet[0].getWidth()/2,(p1.getY() + p1.getHeight()/2)-bullet[0].getHeight()/2);
						}				
					}
					triggerEndGame();
				}		
			});
			scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

				public void handle(MouseEvent e) 
				{
					if(deathAnimationTL.getStatus() == Animation.Status.STOPPED)
					{
						p1.setX(e.getX() - p1.getWidth() / 2);		
						p1.setY(e.getY() - p1.getHeight() / 2);	
						if(p1.getX() <= 0)
							p1.setX(0);
						else if (p1.getX() + p1.getWidth() > root.getWidth() )
							p1.setX(root.getWidth() - p1.getWidth());
						if(p1.getY() >  root.getHeight() - p1.getHeight())
							p1.setY(root.getHeight() - p1.getHeight());				
						if (p1.getY() < 0)
							p1.setY(0);	
						for(int i = 0; i < bullet.length; i++)
						{
							if(bullet[i].getFired() == false)
								bullet[0].setPosition((p1.getX() + p1.getWidth()/2)-bullet[0].getWidth()/2,(p1.getY() + p1.getHeight()/2)-bullet[0].getHeight()/2);
						}

					}
					triggerEndGame();
				}		
			});
			
			//Keyframe which controls background animation
			KeyFrame kfBackground = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {

					for (int i = 0; i < ivBackground.length; i++) {
						yPos[i] += 5;
						ivBackground[i].setLayoutY(yPos[i]);
					}
					if (yPos[0] >= primaryStage.getHeight()) {
						yPos[0] = -700;
					}

					if (yPos[1] >= primaryStage.getHeight()) {
						yPos[1] = -700;
					}
				}
			});

			backgroundTimer = new Timeline(kfBackground);
			backgroundTimer.setCycleCount(Timeline.INDEFINITE);

			//If user attempts to exit window shows alert asking if they want to terminate program 
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent e) 
				{
					endGame = new Alert(AlertType.CONFIRMATION);
					endGame.setTitle("1942");
					endGame.setHeaderText(null);
					endGame.getButtonTypes().clear();
					endGame.setContentText("You currently have " + p1Score + " points. \nAre you sure you want to leave?");
					endGame.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
					spawnTimerTL.stop();
					mainTimer.stop();
					Optional<ButtonType> result = endGame.showAndWait();

					if(result.get() == ButtonType.YES)
					{
						thankYou = new Alert(AlertType.INFORMATION);
						thankYou.setTitle("1942");
						thankYou.setHeaderText(null);
						thankYou.setGraphic(new ImageView(new Image("file:images/plane.png")));
						thankYou.setContentText("Thank you for playing.\nGoodbye!");
						thankYou.showAndWait();
						Platform.exit();
						System.exit(0);
					}
					else
					{
						if(root.getChildren().contains(ivBackground[1]))
						{
							mainTimer.start();
							spawnTimerTL.play();
							e.consume();
						}
						else 
							e.consume();
					}		 

				}			
			});

			primaryStage.getIcons().add(new Image("file:images/plane.png"));
			primaryStage.setResizable(false); //Disallows user to resize window
			primaryStage.setScene(scene);
			primaryStage.setTitle("1942");
			primaryStage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Static method which return the current difficulty stage
	public static int getDifficulty()
	{
		return difficulty;
	}

	public void triggerEndGame()
	{
		//If player loses all lives asks of player would like to play again, if not terminates application
		if(livesRemaining <= 0)
		{
			mP[0].stop();
			mP[1].stop();
			mP[2].stop();
			endGame = new Alert(AlertType.CONFIRMATION);
			endGame.setTitle("1942");
			endGame.setHeaderText(null);
			endGame.getButtonTypes().clear();
			endGame.setContentText("You're dead! \nYou finished with " + p1Score + " points. \nWould you like to play again?");
			endGame.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
			endGame.setGraphic(new ImageView(new Image("file:images/gameOver.png")));
			spawnTimerTL.stop();
			mainTimer.stop();
			p1.getNode().setImage(null); //Removes player from screen
			Optional<ButtonType> result = endGame.showAndWait();

			if(result.get() == ButtonType.YES)
			{
				livesRemaining = 3;
				healthRemaining = 10;
				ivHealthBar.setImage(imgHealth[healthRemaining]);
				ivLives.setImage(imgLives[livesRemaining]);	

				if (highScore < p1Score)
					highScore = p1Score;
				try //Writes new high score and throws IO Exception
				{
				fileWriter = new FileWriter(highScoreSave);
				fileWriter.write(String.valueOf(highScore));
				fileWriter.close();
				}
				catch(IOException e)
				{e.printStackTrace();}
				
				lblHighScore.setText(Integer.toString(highScore));
				p1Score = 0;
				lblp1score.setText(Integer.toString(p1Score));

				p1.setDead(false);
				mainTimer.start();
				spawnTimerTL.play();
				
				for(int k = 0; k < enemyPlanes.length; k++)
				{
					enemyPlanes[k].toggleIsDead(true);
					enemyPlanes[k].setY(2000);
					enemyPlanes[k].getNode().setLayoutY(enemyPlanes[k].getY());
				}
			}
			else
			{
				if (highScore < p1Score)
					highScore = p1Score;
				try //Writes new high score and throws IO Exception
				{
				fileWriter = new FileWriter(highScoreSave);
				fileWriter.write(String.valueOf(highScore));
				fileWriter.close();
				}
				catch(IOException e)
				{e.printStackTrace();}
				thankYou = new Alert(AlertType.INFORMATION);
				thankYou.setTitle("1942");
				thankYou.setHeaderText(null);
				thankYou.setGraphic(new ImageView(new Image("file:images/plane.png")));
				thankYou.setContentText("Thank you for playing.\nGoodbye!");
				thankYou.show();
				Platform.exit();
				System.exit(0);
			}

		}
	}
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent arg0) {}
	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {}
	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {}
	@Override
	public void mousePressed(java.awt.event.MouseEvent arg0) {}
	@Override
	public void mouseReleased(java.awt.event.MouseEvent arg0) {}
}