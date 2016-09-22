package voxspell;

import voxspell.tools.CustomOptionPane;

/**
 * Contains methods for a set of option panes displayed at the end of a Spelling Quiz.
 * 
 * @author Will Molloy
 */
public class FinishedQuizOptionPane {

	private CustomOptionPane _customOptionPane;
	private int _wordsCorrect;
	private SpellingQuiz _quiz;

	public FinishedQuizOptionPane(int wordsCorrect, SpellingQuiz spellingQuiz) {
		_wordsCorrect = wordsCorrect;
		_quiz = spellingQuiz;
		_customOptionPane = new CustomOptionPane(_quiz);
	}

	/**
	 * When the user fails a spelling quiz level (i.e. they got less than 9 words correct).
	 */
	public void failedLevelOptionPane(){
		String[] options = { "Restart current level", "Return to main menu" };

		int selection = _customOptionPane.optionDialog(
				"You got " + _wordsCorrect + " words correct out of 10.\n"
						+ "You need 9 or more words correct in order to progress to the next level.\n",
				"Failure", options, options[0]);
		
		switch (selection) {
		case 0:
			_quiz.restartLevel();
			break;
		case 1:
			Voxspell.showMainMenu(); // go back to main menu
			break;
		}
	}
	
	/**
	 * When the user passes a spelling quiz level (9 or 10 words correct) other than level 10.
	 * 
	 * They will have the option to play the reward video (and go to the next level), go to the next level,
	 * restart the current level OR return to the main menu.
	 * 
	 * For level 10 use the passedQuizOptionPane() method.
	 */
	public void passedLevelOptionPane(){
		String[] options = { "Play video then go to next level", "Go to next level",
				"Restart current level", "Return to main menu" };

		int selection = _customOptionPane.optionDialog(
				"You got " + _wordsCorrect + " words correct out of 10.\n"
						+ "You have passed!\n"
						+ "You have unlocked the reward video and can proceed to the next level.",
						"Passed!", options, options[0]);
		
		switch (selection) {
		case 0:
			_quiz.playVideoThenNextQuizLevel();
			break;
		case 1:
			_quiz.nextLevel();
			break;
		case 2:
			_quiz.restartLevel();
			break;
		case 3:
			Voxspell.showMainMenu(); 
			break;
		}
	}

	/**
	 * When the user completes the overall spelling quiz (i.e. the 11th level). They will have the option
	 * to play the final reward video, restart at a certain level OR return to the main menu.
	 */
	public void passedGameOptionPane() {
		
		String[] options = { "Play final reward video", "Restart from a specific level",
				"Return to main menu" };

		int selection = _customOptionPane.optionDialog(
				"You got " + _wordsCorrect + " words correct out of 10.\n"
						+ "You have passed the final level!\n"
						+ "You have unlocked the final reward video!",
						"Completed Quiz!", options, options[0]);
		
		switch (selection) {
		case 0:
			_quiz.playFinalRewardVideo();
			break;
		case 1:
			_quiz.newQuiz();
			break;
		case 2:
			Voxspell.showMainMenu(); 
			break;
		}
	}

}
