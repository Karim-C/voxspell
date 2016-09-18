package voxspell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.tools.FileReader;
import voxspell.tools.TextToSpeech;
import voxspell.tools.VideoPlayer;

@SuppressWarnings("serial")
public class SpellingQuiz extends JPanel {

	// Swing components
	private JTextField _wordEntryField;
	private JButton _enterWordBtn;
	private JButton _repeatWordBtn;
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

	/**
	 * Build GUI and configure
	 */
	public SpellingQuiz() {
		initLevels();
		createGUI();
	}

	private void initLevels() {
		if (levels == null) {
			levels = new String[NUM_OF_LEVELS];
			for (int i = 1; i <= NUM_OF_LEVELS; i++) {
				levels[i - 1] = i + "";
			}
		}
	}

	private void createGUI() {

		this.setPreferredSize(new Dimension(300, 450)); 

		// Area displayed by the program to the user
		_programOutputArea = new JTextArea();
		_programOutputArea.setEditable(false);
		_programOutputArea.setPreferredSize(new Dimension(285, 320));
		this.add(_programOutputArea, BorderLayout.NORTH);

		// Where user enters the word
		_wordEntryField = new JTextField();
		_wordEntryField.setPreferredSize(new Dimension(210, 25));
		this.add(_wordEntryField, BorderLayout.EAST);

		// Btn pressed by user after entering word
		_enterWordBtn = new JButton("Enter");
		this.add(_enterWordBtn, BorderLayout.WEST);

		// Button pressed by user to repeat word
		_repeatWordBtn = new JButton("Repeat Word");
		this.add(_repeatWordBtn, BorderLayout.EAST);

		_returnToMainMenuBtn = new ReturnToMainMenuBtn(this);
		_returnToMainMenuBtn.setPreferredSize(new Dimension(210, 25));
		this.add(_returnToMainMenuBtn, BorderLayout.SOUTH);

		createEventHandlers();
	}

	private void createEventHandlers() {
		_enterWordBtn.addActionListener(new WordEntryListener());
		
		_wordEntryField.addActionListener(new WordEntryListener());
		
		_repeatWordBtn.addActionListener((ActionListener) -> {
			// Repeats the word when the user presses the repeat button
			textToSpeech.readSentenceSlowly(wordList.get(0));
		});
	}
	
	private class WordEntryListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			checkInputWord();
		}
	}

	/**
	 * New quiz: Ask user for level, ask for first word in quiz.
	 */
	public void newQuiz() {
		this.setBorder(BorderFactory.createTitledBorder("Level 1")); // default level 1 - if they press cancel will go to level
		promptUserForInitialLevel();
		resetFieldsReadWordsFromFileAndBeginQuiz();
	}

	private void promptUserForInitialLevel() {
		String whatLevel = (String) JOptionPane.showInputDialog(this, "What level to start at?", "What level?",
				JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);

		try {
			level = Integer.parseInt(whatLevel);
		} catch (NumberFormatException e) {
			level = 1; // if user pressed cancel
		}
	}

	private void resetFieldsReadWordsFromFileAndBeginQuiz() {
		_wordsCorrectFirstAttempt = 0;
		_wordsAttempt = 0;
		firstAttempt = true;
		_programOutputArea.setText(""); // any others?
		this.setBorder(BorderFactory.createTitledBorder("Level " + level));

		// displays the words correct and attempted as 0
		Statistics stats = Statistics.getInstance();
		stats.displayWordCount(_wordsCorrectFirstAttempt, _wordsAttempt);

		readWordsFromFile();
		continueSpellingQuiz();
	}

	private void readWordsFromFile() {
		wordList = fileReader.getWordList(level);
	}

	/**
	 * Continue quiz: Next word in quiz (until 10 have been asked).
	 */
	public void continueSpellingQuiz() {

		// when the wordList is empty the quiz is finished
		if (wordList.size() > 0) {

			if (firstAttempt) {
				String line = "Please spell ... " + wordList.get(0);
				_programOutputArea.append("Spell word " + (11 - wordList.size()) + " of 10: ");
				textToSpeech.readSentence(line);
			} else {
				String line = "try once more. " + wordList.get(0) + " ... " + wordList.get(0);
				_programOutputArea.append("Incorrect, try once more: ");
				textToSpeech.readSentence(line);
				firstAttempt = false; // resets so the next attempts can be tracked

			}
		} else {
			/* Quiz has completed */
			FinishedQuizOptionPane finishedQuizOptionPane = new FinishedQuizOptionPane(_wordsCorrectFirstAttempt, this);
			
			if (_wordsCorrectFirstAttempt < 9) {
				/* User has failed the quiz */
				finishedQuizOptionPane.failedLevelOptionPane();
			} else {
				if (level == 10) {
					/* User has passed level 10 */
					finishedQuizOptionPane.passedGameOptionPane();
				} else {
					/* User has passed a level other than level 10 */
					finishedQuizOptionPane.passedLevelOptionPane();
				}
			}
		}
	}
	
	private void checkInputWord() {
		if (wordList.size() > 0) {
			Statistics stats = Statistics.getInstance();

			if (_wordEntryField.getText().equals(wordList.get(0))) {
				textToSpeech.readSentenceAndContinueSpellingQuiz("Correct", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");

				stats.addToStats(wordList.get(0), true); // the word is recorded in the statistics

				wordList.remove(0);// the word is removed from the list when it is correctly spelled

				if (!firstAttempt) {
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

				if (firstAttempt) {
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

	public void nextLevel() {
		level++;
		restartLevel();
	}

	public void restartLevel(){
		enableQuizBtns();
		resetFieldsReadWordsFromFileAndBeginQuiz();
	}

	public void playVideoThenNextQuizLevel(){
		disableQuizBtns();
		playVideoAndGoToNextSpellingQuizLevel();
	}
	
	public void playFinalRewardVideo() {
		// use FFMPEG to make a different reward video for the final level.
		// TODO
	}

	private void playVideoAndGoToNextSpellingQuizLevel() {
		VideoPlayer videoPlayer = new VideoPlayer(this);
		videoPlayer.playVideoThenGoToNextSpellingQuizLevel();
	}

	private void disableQuizBtns() {
		_returnToMainMenuBtn.setEnabled(false);
		_enterWordBtn.setEnabled(false);
	}

	private void enableQuizBtns() {
		_returnToMainMenuBtn.setEnabled(true);
		_enterWordBtn.setEnabled(true);
	}

	

}