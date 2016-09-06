package voxspell.tools;

import javax.swing.SwingWorker;

import voxspell.SpellingQuiz;

/**
 * Uses "festival --tts" calls to bash to read Strings using text to speech.
 * 
 * @author Will Molloy
 *
 */
public class TextToSpeech {

	private ProcessWorker _processWorker;
	private boolean _continueSpellingQuiz = false;
	private SpellingQuiz _spellingQuiz;
	
	// Singleton
	private static final TextToSpeech instance = new TextToSpeech();
	private TextToSpeech() {}
	public static TextToSpeech getInstance(){
		return instance;
	}

	/**
	 * Reads a sentence and continues the spelling quiz once done.
	 * 						Redesign - too much coupling?
	 */
	public void readSentenceAndContinueSpellingQuiz(String sentence, SpellingQuiz spellingQuiz) {
		_continueSpellingQuiz = true;
		_spellingQuiz = spellingQuiz;
		readSentence(sentence);
	}
	
	/**
	 * Reads out a sentence using text to speech.
	 */
	public void readSentence(String sentence){
		String command = "echo " + sentence + " | festival --tts";
		
		// Use the process worker class to process the command on a background thread.
		_processWorker = new ProcessWorker(command);
		_processWorker.execute();
	}
	
	/**
	 * Reads out the letters of a word or sentence using text to speech.
	 */
	public void readLetters(String word) {
		String sentence = "";
		char c;
		for (int i = 0; i < word.length(); i++){
			c = word.charAt(i);
			sentence += c + " .. "; // .. causes the speech synthesis in festival --tts to pause for a sec
		}
		readSentence(sentence);
	}
	
	/**
	 * SwingWorker class to process bash commands on a background thread.
	 * 
	 * @author Will Molloy
	 */
	private class ProcessWorker extends SwingWorker<Void, Void>{
		
		private ProcessBuilder _processBuilder;
		private Process _process;
		
		private String _command;

		
		public ProcessWorker(String command){
			_command = command;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			_processBuilder = new ProcessBuilder("bash" , "-c" , _command);
			_process = _processBuilder.start();
			_process.waitFor();
			return null;
		}
		
		/**
		 * Continues the spelling quiz if specified.
		 * (not very OO)
		 */
		@Override
		public void done(){
			if (_continueSpellingQuiz){
				_spellingQuiz.continueSpellingQuiz();
				_continueSpellingQuiz = false;
			}
		}
	}
	
}
