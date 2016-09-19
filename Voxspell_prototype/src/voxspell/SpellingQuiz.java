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

import voxspell.tools.CustomFileReader;
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
	private int _level;
	private static String[] _levels;
	private ArrayList<String> _wordList;
	private boolean _firstAttempt = true;
	private int _wordsCorrectFirstAttempt;
	private int _wordsAttempt;

	// tools
	private CustomFileReader _fileReader = new CustomFileReader();
	private TextToSpeech _textToSpeech = TextToSpeech.getInstance();

	/**
	 * Build GUI and configure
	 */
	public SpellingQuiz() {
		initLevels();
		createGUI();
	}

	private void initLevels() {
		if (_levels == null) {
			_levels = new String[NUM_OF_LEVELS];
			for (int i = 1; i <= NUM_OF_LEVELS; i++) {
				_levels[i - 1] = i + "";
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
		_enterWordBtn.setPreferredSize(new Dimension(75,25));	/* default 5px between buttons */
		this.add(_enterWordBtn, BorderLayout.WEST);
				
		// Button pressed by user to repeat word		
		_repeatWordBtn = new JButton("Repeat Word");
		_repeatWordBtn.setPreferredSize(new Dimension(130,25));
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
			_textToSpeech.readSentenceSlowly(_wordList.get(0));
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
				JOptionPane.QUESTION_MESSAGE, null, _levels, _levels[0]);

		try {
			_level = Integer.parseInt(whatLevel);
		} catch (NumberFormatException e) {
			_level = 1; // if user pressed cancel
		}
	}

	private void resetFieldsReadWordsFromFileAndBeginQuiz() {
		_wordsCorrectFirstAttempt = 0;
		_wordsAttempt = 0;
		_firstAttempt = true;
		_programOutputArea.setText(""); // any others?
		this.setBorder(BorderFactory.createTitledBorder("Level " + _level));

		// displays the words correct and attempted as 0
		SessionStatistics stats = SessionStatistics.getInstance();
		stats.displayWordCount(_wordsCorrectFirstAttempt, _wordsAttempt);

		readWordsFromFile();
		continueSpellingQuiz();
	}

	private void readWordsFromFile() {
		_wordList = _fileReader.getWordList(_level);
	}

	/**
	 * Continue quiz: Next word in quiz (until 10 have been asked).
	 */
	public void continueSpellingQuiz() {

		// when the wordList is empty the quiz is finished
		if (_wordList.size() > 0) {

			if (_firstAttempt) {
				String line = "Please spell ... " + _wordList.get(0);
				_programOutputArea.append("Spell word " + (11 - _wordList.size()) + " of 10: ");
				_textToSpeech.readSentence(line);
			} else {
				String line = "try once more. " + _wordList.get(0) + " ... " + _wordList.get(0);
				_programOutputArea.append("Incorrect, try once more: ");
				_textToSpeech.readSentence(line);
				_firstAttempt = false; // resets so the next attempts can be tracked

			}
		} else {
			/* Quiz has completed */
			FinishedQuizOptionPane finishedQuizOptionPane = new FinishedQuizOptionPane(_wordsCorrectFirstAttempt, this);
			
			if (_wordsCorrectFirstAttempt < 9) {
				/* User has failed the quiz */
				finishedQuizOptionPane.failedLevelOptionPane();
			} else {
				if (_level == 10) {
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
		if (_wordList.size() > 0) {
			String word = _wordList.get(0);
			SessionStatistics stats = SessionStatistics.getInstance();

			if (_wordEntryField.getText().equals(_wordList.get(0))) {
				_textToSpeech.readSentenceAndContinueSpellingQuiz("Correct", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");

				stats.addToStats(_wordList.get(0), true); // the word is recorded in the statistics

				_wordList.remove(0);// the word is removed from the list when it is correctly spelled

				if (!_firstAttempt) {
					_fileReader.appendWordToFile(word, FileManager.STATS_FAULTED);
					_firstAttempt = true;
				} else {
					_fileReader.appendWordToFile(word, FileManager.STATS_MASTERED);
					_wordsCorrectFirstAttempt++;
				}
				_wordsAttempt++;
				stats.generateAndShowTable();

			} else {

				stats.addToStats(_wordList.get(0), false);
				_textToSpeech.readSentenceAndContinueSpellingQuiz("Incorrect", this);
				_programOutputArea.append(_wordEntryField.getText() + "\n");

				if (_firstAttempt) {
					_firstAttempt = false; // the next attempt will no longer be the first
				} else {
					_fileReader.appendWordToFile(word, FileManager.FAILED_WORDS);
					_fileReader.appendWordToFile(word, FileManager.STATS_FAILED);
					_wordList.remove(0); // the word is removed from the list after it is seen twice
					_firstAttempt = true;
					stats.generateAndShowTable();// resets so the next attempts can be tracked
					_wordsAttempt++;
				}

			}
			stats.displayWordCount(_wordsCorrectFirstAttempt, _wordsAttempt);

			_wordEntryField.setText(""); // clears the entry field
		}

	}

	public void nextLevel() {
		_level++;
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