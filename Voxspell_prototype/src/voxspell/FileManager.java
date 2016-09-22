package voxspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class to manage the hidden files used for storing statistics.
 * 
 * @author Will Molloy
 *
 */
public class FileManager {

	// Files -- can be accessed by classes in the same package
	protected static final File FAILED_WORDS = new File(".failed_words");
	protected static final File STATS_MASTERED = new File(".stats_mastered");
	protected static final File STATS_FAULTED = new File(".stats_faulted");
	protected static final File STATS_FAILED = new File(".stats_failed");
	
	private static final File[] _hiddenFiles = { FAILED_WORDS, STATS_MASTERED, STATS_FAILED, STATS_FAULTED };
	
	/**
	 * Creates the hidden files for stats and failed words used in review mistakes
	 * 
	 * WILL NOT overwrite them if they already exist.
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

	/**
	 * Clears the hidden files for stats and failed words by writing an empty string to them.
	 * 
	 * DOES NOT delete the files.
	 */
	public static void clearStatisticFiles() {
		try {
			for (File f : _hiddenFiles){
				PrintWriter pw = new PrintWriter(f);
				pw.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
