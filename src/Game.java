import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRoundRect;
import acm.program.GraphicsProgram;

/**
 * Pacman v0.1 My first amateur game written in java
 * 
 * @author elmar
 */
public class Game extends GraphicsProgram {

	private static final long serialVersionUID = 1L;

	static int BLOCK_SIZE = 20;// Wall block size
	static int COIN_SIZE = 10;// Coin size

	public int pacmanX;
	public int pacmanY;

	/* Ghost locations */
	public int[] ghostX;
	public int[] ghostY;
	public int Gindex = 0;// Ghost index

	public static int score;// Current score
	public static int highScore;// Highest score
	public int numberOfCoins;// Number of coins on the map

	Pacman p;

	/* Ghosts */
	Ghost ghost1;
	Ghost ghost2;
	Ghost ghost3;
	Ghost ghost4;

	/* Checking if key is pressed */
	boolean isUpPressed;
	boolean isDownPressed;
	boolean isRightPressed;
	boolean isLeftPressed;

	GLabel gl;// Current score label
	GLabel Hgl;// High score label

	public int myNums[][]; // Array that holds the map

	Random r;

	/**
	 * Reads the map.txt file and loads it to a 2 dimensional array - myNums
	 */
	public void readTheMap() {
//=============================================================
		// This part counts the number of rows and columns in the map
		Scanner input = null;
		try {
			input = new Scanner(new File("map.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		int rows = 0;
		int columns = 0;
		while (input.hasNextLine()) {
			rows++;
			columns = input.nextLine().trim().length();
		}

		myNums = new int[rows][columns];
//===============================================================
		BufferedReader br = null;
		FileReader fr = null;
		rows = 0;
		try {
			fr = new FileReader("map.txt");
			br = new BufferedReader(fr);

			while (true) {
				String line = br.readLine();

				if (line == null)
					break;
				line = line.trim();
				for (columns = 0; columns < line.length(); columns++)
					myNums[rows][columns] = line.charAt(columns) - '0';
				rows++;
			}

		} catch (FileNotFoundException e) {
			System.err.println("The file you specified does not exist.");
		} catch (IOException e) {
			System.err.println("Some other IO exception occured. Message: " + e.getMessage());
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			input.close();
			try {
				if (br != null) {
					br.close();
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Generates the map 0=Nothing, 1=Wall, 2=Coin, 3=Power, 4=Gate, 5=Pacman,
	 * 6=Ghosts
	 */
	public void generateMap() {

		for (int i = 0; i < myNums.length; i++) {
			for (int j = 0; j < myNums[i].length; j++) {

				if (myNums[i][j] == 1) {// WALL

					GRoundRect g = new GRoundRect(BLOCK_SIZE, BLOCK_SIZE);
					g.setFilled(true);
					g.setFillColor(Color.BLUE);
					add(g, j * BLOCK_SIZE, i * BLOCK_SIZE);

				} else if (myNums[i][j] == 2) {// COIN

					GOval go = new GOval(COIN_SIZE, COIN_SIZE);
					go.setFilled(true);
					go.setFillColor(Color.YELLOW);
					add(go, j * BLOCK_SIZE + COIN_SIZE / 2, i * BLOCK_SIZE + COIN_SIZE / 2);
					numberOfCoins++;

				} else if (myNums[i][j] == 3) {// POWERUP

					GOval sup = new GOval(COIN_SIZE, COIN_SIZE);
					sup.setFilled(true);
					sup.setFillColor(Color.RED);
					add(sup, j * BLOCK_SIZE + 5, i * BLOCK_SIZE + 5);

				}
//			else if (myNums[i][j] == 4) {//GATE
//				
//				GRoundRect gate = new GRoundRect(BLOCK_SIZE, BLOCK_SIZE);
//				gate.setFilled(true);
//				gate.setFillColor(Color.RED);
//				add(gate, j * BLOCK_SIZE, i * BLOCK_SIZE);
//				
//			}
				else if (myNums[i][j] == 5) {// PACMAN
					pacmanX = j;
					pacmanY = i;
				} else if (myNums[i][j] == 6) {
					ghostX[Gindex] = j;
					ghostY[Gindex++] = i;
				}

			}
		}
	}

	/**
	 * Initialize
	 */
	public void init() {
		ghostX = new int[4];
		ghostY = new int[4];

		/* load the high score */
		ScoreHandler.loadHighScore();

		/* Initialize score and high score labels, set font and color */
		gl = new GLabel("");
		Hgl = new GLabel("High: " + highScore);

		gl.setFont("ARIAL-BOLD-18");
		Hgl.setFont("ARIAL-BOLD-18");

		gl.setColor(Color.GREEN);
		Hgl.setColor(Color.GREEN);

		/* save the high score when closed */
		addKeyListeners();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (score > highScore)// If the current score is higher than the Highest
					ScoreHandler.saveHighScore();
			}
		});
		/* Initializing the random generator */
		r = new Random();
	}

	/**
	 * When a key is pressed sets the corresponding boolean to true
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			isUpPressed = true;
			break;
		case KeyEvent.VK_DOWN:
			isDownPressed = true;
			break;
		case KeyEvent.VK_RIGHT:
			isRightPressed = true;
			break;
		case KeyEvent.VK_LEFT:
			isLeftPressed = true;
			break;
		}

	}

	/**
	 * Checks if an object can move up
	 * 
	 * @param p
	 * @return true if can move up
	 */
	public boolean canMoveUp(GameObject o) {
		GObject el = getElementAt(o.getX(), o.getY() - 5);
		GObject el2 = getElementAt(o.getX() + o.getWidth() - 1, o.getY() - 5);
		if (el != null && el instanceof GRoundRect || (el2 != null & el2 instanceof GRoundRect))
			return false;

		return true;
	}

	/**
	 * Checks if an object can move down
	 * 
	 * @param p
	 * @return
	 */
	public boolean canMoveDown(GameObject o) {
		GObject el = getElementAt(o.getX(), o.getHeight() + o.getY() + 3);
		GObject el2 = getElementAt(o.getX() + o.getWidth() - 1, o.getHeight() + o.getY() + 3);

		if ((el != null && el instanceof GRoundRect) || (el2 != null & el2 instanceof GRoundRect))
			return false;

		return true;
	}

	/**
	 * Checks if an object can move left
	 * 
	 * @param p
	 * @return
	 */
	public boolean canMoveLeft(GameObject o) {
		GObject el = getElementAt(o.getX() - 5, o.getY());
		GObject el2 = getElementAt(o.getX() - 5, o.getY() + o.getHeight() - 1);

		if ((el != null && el instanceof GRoundRect) || (el2 != null & el2 instanceof GRoundRect))
			return false;

		return true;
	}

	/**
	 * Checks if an object can move right
	 */
	public boolean canMoveRight(GameObject o) {
		GObject el = getElementAt(o.getWidth() + o.getX() + 3, o.getY());
		GObject el2 = getElementAt(o.getWidth() + o.getX() + 3, o.getY() + o.getWidth() - 1);

		if ((el != null && el instanceof GRoundRect) || (el2 != null & el2 instanceof GRoundRect))
			return false;

		return true;
	}

	/**
	 * When a key is released sets the corresponding boolean to false
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			isUpPressed = false;
			break;
		case KeyEvent.VK_DOWN:
			isDownPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			isRightPressed = false;
			break;
		case KeyEvent.VK_LEFT:
			isLeftPressed = false;
			break;
		}

	}

	/**
	 * Checks if the approaching element is an instance of GOval (a coin) and
	 * removes it + increments the score
	 */
	public void checkForCoins() {
		GObject elR = getElementAt(p.getWidth() + p.getX(), p.getY() + p.getHeight() / 2);
		GObject elL = getElementAt(p.getX() - 1, p.getY() + p.getHeight() / 2);
		GObject elD = getElementAt(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight());
		GObject elU = getElementAt(p.getX() + p.getWidth() / 2, p.getY() - 1);

		if (elR instanceof GOval) {
			score++;
			if (((GOval) elR).getFillColor() == Color.RED)
				powerUp();
			remove(elR);
		}
		if (elL instanceof GOval) {
			score++;
			if (((GOval) elL).getFillColor() == Color.RED)
				powerUp();
			remove(elL);
		}
		if (elD instanceof GOval) {
			score++;
			if (((GOval) elD).getFillColor() == Color.RED)
				powerUp();
			remove(elD);
		}
		if (elU instanceof GOval) {
			score++;
			if (((GOval) elU).getFillColor() == Color.RED)
				powerUp();
			remove(elU);
		}
	}

	/*
	 * Adds a glabel if the user won or lost
	 */
	public void youWinOrLose(String str) {
		GLabel glb = new GLabel(str);
		glb.setFont("ARIAL-BOLD-18");
		glb.setColor(Color.RED);
		add(glb, 5, 60);
	}

	/**
	 * if pacman eats a red coin, score increments by 10
	 */
	public void powerUp() {
		score += 10;

	}

	/**
	 * Checks if pacman collides with the ghosts
	 * 
	 * @return true if there's a collision
	 */
	public boolean checkForCollision() {
		if (p.getBounds().intersects(ghost1.getBounds()) || p.getBounds().intersects(ghost2.getBounds())
				|| p.getBounds().intersects(ghost3.getBounds()) || p.getBounds().intersects(ghost4.getBounds()))
			return true;
		return false;
	}

	public void run() {
		Frame.getFrames()[0].setMenuBar(null);// Removes the menu bar
		/* Reads the map from the file and loads it to array */
		readTheMap();

		/* Generates the map in the array */
		generateMap();

		/* Set windows size according to the map */
		setSize(myNums[0].length * BLOCK_SIZE + BLOCK_SIZE, myNums.length * BLOCK_SIZE + 48);// 48 is for the top menu
																								// size (minimize,exit
																								// etc.)

		/* Initialize Pacman and scale */
		p = new Pacman("pacRight.gif", pacmanX * BLOCK_SIZE + 2, pacmanY * BLOCK_SIZE + 2);
		p.scale(1.1);

		/* Initialize ghosts */
		ghost1 = new Ghost("blueUp.gif", ghostX[0] * BLOCK_SIZE + 2, ghostY[0] * BLOCK_SIZE + 2);
		ghost2 = new Ghost("orangeUp.gif", ghostX[1] * BLOCK_SIZE + 2, ghostY[1] * BLOCK_SIZE + 2);
		ghost3 = new Ghost("pinkUp.gif", ghostX[2] * BLOCK_SIZE + 2, ghostY[2] * BLOCK_SIZE + 2);
		ghost4 = new Ghost("redUp.gif", ghostX[3] * BLOCK_SIZE + 2, ghostY[3] * BLOCK_SIZE + 2);

		/* Start ghost threads */
		ghost1.start();
		ghost2.start();
		ghost3.start();
		ghost4.start();

		/* Add the ghosts */
		add(ghost1);
		add(ghost2);
		add(ghost3);
		add(ghost4);

		add(p);// add Pacman
		add(gl, 5, 20);// add the score
		add(Hgl, 5, 40);// add the highest score
		/* Starting sound */
		GameSound start = new GameSound("sound\\begin.wav");
		start.play();
		pause(4000);
		int dir = 1;
		int dir2=1;
		int dir3=1;
		int dir4=1;
		
		while (true) {

			gl.setLabel("Score: " + score);// renew the score
			
			//==========================
			if (dir == 0)
				if (canMoveLeft(ghost1)) {
					ghost1.setMoveL(true);
				} else {
					ghost1.setMoveL(false);
					dir = r.nextInt(4);
				}
			if (dir == 1)
				if (canMoveUp(ghost1)) {
					ghost1.setMoveU(true);

				} else {
					ghost1.setMoveU(false);
					dir = r.nextInt(4);
				}
			if (dir == 2)
				if (canMoveRight(ghost1)) {
					ghost1.setMoveR(true);
				} else {
					ghost1.setMoveR(false);
					dir = r.nextInt(4);
				}
			if (dir == 3)
				if (canMoveDown(ghost1)) {
					ghost1.setMoveD(true);
				} else {
					ghost1.setMoveD(false);
					dir = r.nextInt(4);
				}
			
			//===============================
			if (dir2 == 0)
				if (canMoveLeft(ghost2)) {
					ghost2.setMoveL(true);
				} else {
					ghost2.setMoveL(false);
					dir2 = r.nextInt(4);
				}
			if (dir2 == 1)
				if (canMoveUp(ghost2)) {
					ghost2.setMoveU(true);

				} else {
					ghost2.setMoveU(false);
					dir2 = r.nextInt(4);
				}
			if (dir2 == 2)
				if (canMoveRight(ghost2)) {
					ghost2.setMoveR(true);
				} else {
					ghost2.setMoveR(false);
					dir2 = r.nextInt(4);
				}
			if (dir2 == 3)
				if (canMoveDown(ghost2)) {
					ghost2.setMoveD(true);
				} else {
					ghost2.setMoveD(false);
					dir2 = r.nextInt(4);
				}
			
			//==============================
			if (dir3 == 0)
				if (canMoveLeft(ghost3)) {
					ghost3.setMoveL(true);
				} else {
					ghost3.setMoveL(false);
					dir3 = r.nextInt(4);
				}
			if (dir3 == 1)
				if (canMoveUp(ghost3)) {
					ghost3.setMoveU(true);

				} else {
					ghost3.setMoveU(false);
					dir3 = r.nextInt(4);
				}
			if (dir3 == 2)
				if (canMoveRight(ghost3)) {
					ghost3.setMoveR(true);
				} else {
					ghost3.setMoveR(false);
					dir3 = r.nextInt(4);
				}
			if (dir3 == 3)
				if (canMoveDown(ghost3)) {
					ghost3.setMoveD(true);
				} else {
					ghost3.setMoveD(false);
					dir3 = r.nextInt(4);
				}
			
			//============================
			if (dir4 == 0)
				if (canMoveLeft(ghost4)) {
					ghost4.setMoveL(true);
				} else {
					ghost4.setMoveL(false);
					dir4 = r.nextInt(4);
				}
			if (dir4 == 1)
				if (canMoveUp(ghost4)) {
					ghost4.setMoveU(true);

				} else {
					ghost4.setMoveU(false);
					dir4 = r.nextInt(4);
				}
			if (dir4 == 2)
				if (canMoveRight(ghost4)) {
					ghost4.setMoveR(true);
				} else {
					ghost4.setMoveR(false);
					dir4 = r.nextInt(4);
				}
			if (dir4 == 3)
				if (canMoveDown(ghost4)) {
					ghost4.setMoveD(true);
				} else {
					ghost4.setMoveD(false);
					dir4 = r.nextInt(4);
				}
			
			/* If pacman collides with a ghost stop the game */
			if (checkForCollision()) {
				Ghost.hasDied = true;
				pause(500);
				p.setImage("pacDead.gif");
				GameSound death = new GameSound("sound\\death.wav");
				youWinOrLose("YOU LOST");
				death.play();
				pause(3000);
				death.stop();
				break;
			}

			/* Checking the key presses to handle images and movement */
			if (isUpPressed)
				if (canMoveUp(p)) {
					p.move(0, -5);
					p.setImage("pacUp.gif");
				}
			if (isDownPressed)
				if (canMoveDown(p)) {
					p.move(0, 5);
					p.setImage("pacDown.gif");
				}
			if (isRightPressed)
				if (canMoveRight(p)) {
					p.move(5, 0);
					p.setImage("pacRight.gif");
				}
			if (isLeftPressed)
				if (canMoveLeft(p)) {
					p.move(-5, 0);
					p.setImage("pacLeft.gif");
				}
			checkForCoins();
			/* Once has collected all coins the user wins */
			if (score == numberOfCoins)
				youWinOrLose("YOU WON");

			pause(50);
		}
	}
}
