package voxspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {

	// Files -- can be accessed by classes in the same package
	protected static final File FAILED_WORDS = new File(".failed_words");
	protected static final File STATS_MASTERED = new File(".stats_mastered");
	protected static final File STATS_FAULTED = new File(".stats_faulted");
	protected static final File STATS_FAILED = new File(".stats_failed");
	private static final File[] _hiddenFiles = { FAILED_WORDS, STATS_MASTERED, STATS_FAILED, STATS_FAULTED };
	
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
