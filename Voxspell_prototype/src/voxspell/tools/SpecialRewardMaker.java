/**
 * 
 */
package voxspell.tools;

import java.io.File;
import javax.swing.SwingWorker;

/**
 * This class contains the method which will create the reward video in the background
 * @author Karim Cisse
 *
 */
public class SpecialRewardMaker extends SwingWorker <Void, Void> {
	private String command =  "ffmpeg -i big_buck_bunny_1_minute.avi -ss 00:00:20 -t 10 -vf negate -af volume=2,aecho,atempo";
	private String videoPath = "speacialReward.avi";
	
	// This method creates the final reward video if it does not already exist.
	@Override
	protected Void doInBackground() throws Exception {
		File f = new File(videoPath);
		if(!f.exists()) { 
			ProcessBuilder processBuilder = new ProcessBuilder("bash" , "-c" , command + " " + videoPath);
			Process process = processBuilder.start();
			process.waitFor();
		}
		return null;
	}
	
	
	
}
