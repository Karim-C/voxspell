package voxspell.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Contains a few methods to read files for statistics/words from the wordlist.
 * 
 * @author Karim Cisse
 * @author Will Molloy
 */
public class CustomFileReader {

	/**
	 * Reads a set of 10 words from the wordlist file based on the level provided.
	 * 
	 * @author Karim Cisse
	 */
	public ArrayList<String> getWordList(int level) {
		ArrayList<String> wordList = readInWords(level);

		ArrayList<String> returnWords = new ArrayList<String>();
		int wordsNum = wordList.size();

		// the condition in the for loop allows for 10 words or less than 10 if
		// there are less than 10 words in the list
		for (int i = 0; i < (wordsNum) && (i < 10); i++) {
			int randomNum = (int) (Math.random() * wordList.size());
			returnWords.add(wordList.get(randomNum));
			wordList.remove(randomNum);
		}
		return returnWords;
	}

	/**
	 * Read words in from wordlist file.
	 * @author Karim Cisse
	 */
	private ArrayList<String> readInWords(int level) {

		ArrayList<String> wordList = new ArrayList<String>();
		String filename = "NZCER-spelling-lists.txt";
		String levelID = "%Level " + level;

		// The file is read into standard in
		try {
			System.setIn(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// scanner is setup to read from standard in
		Scanner sc = new Scanner(System.in);

		while (sc.hasNext()) {
			String line = sc.nextLine();

			// a check is performed to see whether the desired level words have
			// been reached
			if (line.equals(levelID)) {
				levelID = "%Level " + (level + 1);
				while (sc.hasNext()) {
					line = sc.nextLine();
					
					// when the the next level words in the list is reached it
					// stops adding words to the list
					if (line.equals(levelID)) {
						break;
					}
					wordList.add(line);
				}
			}
		}
		sc.close();

		return wordList;
	}
	
	/** 
	 * Appends a word to a file.
	 * 
	 * @author Will Molloy
	 */
	public void appendWordToFile(String word, File file){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write(word);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads words line by line from a file into a HashSet.
	 * 
	 * @author Will Molloy
	 */
	public void readFileByLineIntoSet(File file, HashSet<String> words){
		String word;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((word = reader.readLine()) != null){
				words.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a count of lines containing 'word' in 'file'
	 * 
	 * @author Will Molloy
	 */
	public int getWordCountFromFile(String word, File file) {
		BufferedReader reader;
		String temp;
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null){
				if (temp.equals(word)){
					count++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
	
}
