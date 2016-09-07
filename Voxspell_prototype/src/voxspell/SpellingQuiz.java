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

import voxspell.tools.FileReader;
import voxspell.tools.TextToSpeech;

@SuppressWarnings("serial")
public class SpellingQuiz extends JPanel {

	// Swing components
	private JTextField _wordEntryField;
	private JButton _enterWordBtn;
	protected JTextArea _programOutputArea;
	
	// game logic
	private static final int NUM_OF_LEVELS = 10;
	private int level;
	private static String[] levels;
	private ArrayList<String> wordList;
	private boolean firstAttempt = true;
	
	// tools
	private FileReader fileReader = new FileReader();
	private TextToSpeech textToSpeech = TextToSpeech.getInstance();
	
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
		
		this.setPreferredSize(new Dimension(305,270)); // ? sized for assignment 2, make bigger?

		// Area displayed by the program to the user
		_programOutputArea = new JTextArea();
		_programOutputArea.setEditable(false);
		_programOutputArea.setPreferredSize(new Dimension(285,180));
		this.add(_programOutputArea, BorderLayout.NORTH);
		
		// Where user enters the word
		_wordEntryField = new JTextField();
		_wordEntryField.setPreferredSize(new Dimension(210,25));
		this.add(_wordEntryField, BorderLayout.EAST);

		// Btn pressed by user after entering word
		_enterWordBtn = new JButton("Enter");
		this.add(_enterWordBtn, BorderLayout.WEST);
		
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
		firstAttempt = true; 
		_programOutputArea.setText(""); // any others?
		this.setBorder(BorderFactory.createTitledBorder("Level " + level));
		
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
			/*
			 * Quiz has finished, determine if user passed the current level and prompt if they want to
			 * restart current level or continue to next (play video?)
			 */
			level++; // not if level = 10
			resetFieldsReadWordsFromFileAndBeginQuiz();
		}
	}
	
	private void checkInputWord() {
		if (wordList.size() > 0){
			
			if (_wordEntryField.getText().equals(wordList.get(0))){
				textToSpeech.readSentenceAndContinueSpellingQuiz("Correct", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");
				wordList.remove(0);// the word is removed from the list when it is correctly spelled
				
				if (!firstAttempt){
					firstAttempt = true;
				}
				
			}else {
				textToSpeech.readSentenceAndContinueSpellingQuiz("Incorrect", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");
				if (firstAttempt){
					firstAttempt = false; // the next attempt will no longer be the first
				}else {
					wordList.remove(0); // the word is removed from the list after it is seen twice
					firstAttempt = true; // resets so the next attempts can be tracked
				}
			}
			_wordEntryField.setText(""); // clears the entry field
		}
	}
	
}