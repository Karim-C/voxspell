package voxspell.tools;

import java.io.IOException;

import javax.swing.SwingWorker;

import voxspell.SpellingQuiz;

/**
 * Uses "festival --tts" calls to bash to read Strings using text to speech.
 * 
 * @author Will Molloy
 *
 */
public class TextToSpeech {

	private FestivalWorker festivalWorker;
	private boolean _continueSpellingQuiz = false;
	private SpellingQuiz _spellingQuiz;
	
	// Singleton
	private static final TextToSpeech instance = new TextToSpeech();
	private TextToSpeech() {}
	public static TextToSpeech getInstance(){
		return instance;
	}
	
	// voice
	private static String voice;
	
	/**
	 * Sets the voice to be used in TextToSpeech.
	 * Must be in the form "(voice_)"
	 * E.g. "(voice_kal_diphone)" or "(voice_rab_diphone)"
	 */
	public static void setVoice(String newVoice){
		voice = newVoice;
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
		// Use the festival worker class to read the sentence on a background thread
		festivalWorker = new FestivalWorker(sentence);
		festivalWorker.execute();
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
	private class FestivalWorker extends SwingWorker<Void, Void>{
		
		private ProcessBuilder _processBuilder;
		private Process _process;
		
		private String _sentence;

		// hidden scm file to run festival - needed to change voice
		private static final String hiddenScmFile = ".ttsScript.scm";
		
		public FestivalWorker(String sentence){
			_sentence = sentence;
		}
		
		@Override
		protected Void doInBackground() throws Exception {	
			
			String appendVoiceToScmFile = "echo \"" + voice + "\" >> " + hiddenScmFile;
			runBashCommand(appendVoiceToScmFile);
			
			String sayText = "echo \"(SayText \\\"" + _sentence + "\\\")\" >> " + hiddenScmFile;
			runBashCommand(sayText);
			
			String runScmFile = "festival -b " + hiddenScmFile;
			runBashCommand(runScmFile);
			
			String deleteScmFile = "rm -f " + hiddenScmFile;
			runBashCommand(deleteScmFile);
			
			return null;
		}
		
		private void runBashCommand(String command) throws IOException, InterruptedException{
			_processBuilder = new ProcessBuilder("bash" , "-c" , command);
			_process = _processBuilder.start();
			_process.waitFor();
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
