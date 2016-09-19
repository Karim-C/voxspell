package voxspell;

import java.io.File;
import java.io.IOException;

public class FileManager {

	// Files -- can be accessed by classes in the same package
	protected static File FAILED_WORDS = new File(".failed_words");
	protected static File STATS_MASTERED = new File(".stats_mastered");
	protected static File STATS_FAULTED = new File(".stats_faulted");
	protected static File STATS_FAILED = new File(".stats_failed");
	private static File[] _hiddenFiles = { FAILED_WORDS, STATS_MASTERED, STATS_FAILED };
	
	/**
	 * Hidden files for stats and failed words used in review mistakes
	 */
	public static void createHiddenFiles(){
		try {
			for (File f : _hiddenFiles){
				f.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
