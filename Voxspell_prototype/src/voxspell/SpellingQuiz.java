package voxspell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.tools.CustomOptionPane;
import voxspell.tools.FileReader;
import voxspell.tools.TextToSpeech;
import voxspell.tools.VideoPlayer;

@SuppressWarnings("serial")
public class SpellingQuiz extends JPanel {

	// Swing components
	private JTextField _wordEntryField;
	private JButton _enterWordBtn;
	private JTextArea _programOutputArea;
	private ReturnToMainMenuBtn _returnToMainMenuBtn;

	// game logic
	private static final int NUM_OF_LEVELS = 10;
	private int level;
	private static String[] levels;
	private ArrayList<String> wordList;
	private boolean firstAttempt = true;
	private int _wordsCorrectFirstAttempt;
	private int _wordsAttempt;

	// tools
	private FileReader fileReader = new FileReader();
	private TextToSpeech textToSpeech = TextToSpeech.getInstance();
	private CustomOptionPane customOptionPane = new CustomOptionPane(this);

	/**
	 * Build GUI and configure
	 */
	public SpellingQuiz(){
		initLevels();
		createGUI();
	}

	private void initLevels() {
		if (levels == null){
			levels = new String[NUM_OF_LEVELS];
			for (int i = 1; i <= NUM_OF_LEVELS; i++){
				levels[i-1] = i+"";
			}
		}
	}

	protected final void createGUI() {

		this.setPreferredSize(new Dimension(300,450)); // ? sized for assignment 2, make bigger?

		// Area displayed by the program to the user
		_programOutputArea = new JTextArea();
		_programOutputArea.setEditable(false);
		_programOutputArea.setPreferredSize(new Dimension(285, 340));
		this.add(_programOutputArea, BorderLayout.NORTH);

		// Where user enters the word
		_wordEntryField = new JTextField();
		_wordEntryField.setPreferredSize(new Dimension(210,25));
		this.add(_wordEntryField, BorderLayout.EAST);

		// Btn pressed by user after entering word
		_enterWordBtn = new JButton("Enter");
		this.add(_enterWordBtn, BorderLayout.WEST);
		
		_returnToMainMenuBtn =  new ReturnToMainMenuBtn(this);
		this.add(_returnToMainMenuBtn, BorderLayout.SOUTH);

		createEventHandlers();	
	}

	private void createEventHandlers() {
		_enterWordBtn.addActionListener( (ActionListener) ->{
			// user presss this after entering a word.
			// process word -- compare to actual spelling etc
			checkInputWord();
		});
	}

	/**
	 * New quiz:
	 * Ask user for level, ask for first word in quiz.
	 */
	public void newQuiz()  {
		this.setBorder(BorderFactory.createTitledBorder("Level ?"));
		promptUserForInitialLevel();
		resetFieldsReadWordsFromFileAndBeginQuiz();
	}

	private void promptUserForInitialLevel() {
		String whatLevel = (String) JOptionPane.showInputDialog(this, 
				"What level to start at?",
				"What level?",
				JOptionPane.QUESTION_MESSAGE,
				null,
				levels,
				levels[0]);

		level = Integer.parseInt(whatLevel);
	}

	private void resetFieldsReadWordsFromFileAndBeginQuiz() {
		_wordsCorrectFirstAttempt = 0;
		_wordsAttempt = 0;
		firstAttempt = true; 
		_programOutputArea.setText(""); // any others?
		this.setBorder(BorderFactory.createTitledBorder("Level " + level));
		
		//displays the words correct and attempted as 0
		Statistics stats = Statistics.getInstance();
		stats.displayWordCount(_wordsCorrectFirstAttempt, _wordsAttempt);
		

		readWordsFromFile();
		continueSpellingQuiz();
	}

	private void readWordsFromFile() {
		wordList = fileReader.getWordList(level);
	}

	/**
	 * Continue quiz:
	 * Next word in quiz (until 10 have been asked).
	 */
	public void continueSpellingQuiz() {

		//when the wordList is empty the quiz is finished
		if (wordList.size() > 0){

			if (firstAttempt){
				String line = "Please spell ... " + wordList.get(0);
				_programOutputArea.append("Spell word " + (11 - wordList.size()) + " of 10: ");
				textToSpeech.readSentence(line);
			}else {
				String line = "try once more. " + wordList.get(0) + " ... " + wordList.get(0);
				_programOutputArea.append("Incorrect, try once more: ");
				textToSpeech.readSentence(line);
				firstAttempt = false; // resets so the next attempts can be tracked
			}
		} else {
			/* Quiz has completed */

			if (_wordsCorrectFirstAttempt < 9){
				// Failed - option to restart current level or go back to main menu
				String[] options = { "Restart current level" , "Return to main menu" };

				int selection = customOptionPane.optionDialog("You got " + _wordsCorrectFirstAttempt + " words correct out of 10.\n"
						+ "You need 9 or more words correct in order to progress to the next level.\n"
						, "Failure", options, options[0]);
				switch(selection){
				case 0:
					resetFieldsReadWordsFromFileAndBeginQuiz(); // restart level
					break;
				case 1:
					Voxspell.showMainMenu(); // go back to main menu
					break;
				}
			} else {
				if (level == 10){
					// DIFFERENT reward video is played and dialog if passed final level
					
					// USE FFMPEG to make a different video?
				} else {

					// Passed - option to play video AND start next level, go to next level (no video), or restart current level
					String[] options = { "Play video then go to next level" , "Go to next level" , "Restart current level" , "Return to main menu" };

					int selection = customOptionPane.optionDialog("You got " + _wordsCorrectFirstAttempt + " words correct out of 10.\n"
							+ "You have passed!\n"
							+ "You have unlocked the reward video and can proceed to the next level."
							, "Passed!", options, options[0]);
					switch(selection){
					case 0:
						playVideoAndGoToNextSpellingQuizLevel();
						break;
					case 1:
						nextLevel();
						break;
					case 2:
						resetFieldsReadWordsFromFileAndBeginQuiz(); // restart level
						break;
					case 3:
						Voxspell.showMainMenu(); // go back to main menu
						break;
					}
				}
			}
		}
	}

	public void nextLevel(){
		level++;
		resetFieldsReadWordsFromFileAndBeginQuiz(); 
		enableQuizBtns();
	}

	private void playVideoAndGoToNextSpellingQuizLevel() {
		VideoPlayer videoPlayer = new VideoPlayer(this);
		videoPlayer.playVideoThenGoToNextSpellingQuizLevel();
		disableQuizBtns();
	}
	
	private void disableQuizBtns(){
		_returnToMainMenuBtn.setEnabled(false);
		_enterWordBtn.setEnabled(false);
	}
	
	private void enableQuizBtns(){
		_returnToMainMenuBtn.setEnabled(true);
		_enterWordBtn.setEnabled(true);
	}
	

	private void checkInputWord() {
		if (wordList.size() > 0){
			Statistics stats = Statistics.getInstance();
			
			if (_wordEntryField.getText().equals(wordList.get(0))){
				textToSpeech.readSentenceAndContinueSpellingQuiz("Correct", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");

				stats.addToStats(wordList.get(0), true); // the word is recorded in the statistics
				wordList.remove(0);// the word is removed from the list when it is correctly spelled
				if (!firstAttempt){
					firstAttempt = true;
				} else {
					_wordsCorrectFirstAttempt++;
				}
				_wordsAttempt++;
				stats.generateAndShowTable();

			} else {
				stats.addToStats(wordList.get(0), false);
				textToSpeech.readSentenceAndContinueSpellingQuiz("Incorrect", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");
				if (firstAttempt){
					firstAttempt = false; // the next attempt will no longer be the first
				} else {
					wordList.remove(0); // the word is removed from the list after it is seen twice
					firstAttempt = true;
					stats.generateAndShowTable();// resets so the next attempts can be tracked
					_wordsAttempt++;
				}
				
			}
			stats.displayWordCount(_wordsCorrectFirstAttempt, _wordsAttempt);
			
			_wordEntryField.setText(""); // clears the entry field
		}

	}

}