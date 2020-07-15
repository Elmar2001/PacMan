
/**
 * Ghost class for representing the ghosts in the game
 * @author elmar
 *
 */
public class Ghost extends GameObject implements Runnable{
	
	Thread ghostThread;
	private boolean moveU;
	private boolean moveR;
	private boolean moveL;
	private boolean moveD;
	public static boolean hasDied;//If set true thread stops (pacman died)
	
	public boolean isMovingU() {
		return moveU;
	}

	public void setMoveU(boolean moveU) {
		this.moveU = moveU;
	}

	public boolean isMovingR() {
		return moveR;
	}

	public void setMoveR(boolean moveR) {
		this.moveR = moveR;
	}

	public boolean isMovingL() {
		return moveL;
	}

	public void setMoveL(boolean moveL) {
		this.moveL = moveL;
	}

	public boolean isMovingD() {
		return moveD;
	}

	public void setMoveD(boolean moveD) {
		this.moveD = moveD;
	}

	public Ghost(String img, int x, int y) {
		super(img, x, y);
		ghostThread = new Thread(this);
	}
	
	@Override 
	public void start() {
		ghostThread.start();
	}
	
	@Override
	public void run() {

		while(true) {
			if(hasDied) break;//stop if pacman is dead
			if(moveU) move(0,-5);
			if(moveD) move(0,5);
			if(moveR) move(5,0);
			if(moveL) move(-5,0);
			pause(70);
		}
	}
}
