package voxspell.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
	/*
	 * Used for testing
	 * 
	 * public static void main(String[] args){ FileReader fr = new FileReader();
	 * ArrayList<String> words = fr.getWordList(1); System.out.println("hello");
	 * }
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
}
