import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
/**
 * Loads/Saves the high score to highscore.txt file
 * @author elmar
 *
 */
public class ScoreHandler {
	
	/**
	 * Loads the high score from highscore.txt file and assigns it to highScore in Game class
	 */
	public static void loadHighScore() {
		Scanner sc=null;
		try {
			sc = new Scanner(new File("highscore.txt"));
			Game.highScore = sc.nextInt();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println(e);
		}
		finally {
				sc.close();
		}
	}
	
	/**
	 * Saves the high score to highscore.txt file
	 */
	public static void saveHighScore() {
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(new File("highscore.txt"));
			pw.println(Game.score);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
		System.err.println(e);
		}
	}
}
