
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * For grabbing sound files
 * @author elmar
 *
 */
public class GameSound {
	private File sound;
	Clip clip;
	public GameSound(String path) {
		sound = new File(path);
	}
	
	public void play() {
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	public void stop() {
		clip.stop();
	}
}
