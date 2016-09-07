package voxspell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
		
		this.setPreferredSize(new Dimension(305,270)); // ? 

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
		});
		
	}
	
	/**
	 * New quiz:
	 * Ask user for level, ask for first word in quiz.
	 */
	public void newQuiz()  {
		// popup asking for level
		String whatLevel = (String) JOptionPane.showInputDialog(this, 
				"What level to start at?",
				"What level?",
				JOptionPane.QUESTION_MESSAGE,
				null,
				levels,
				levels[0]);
		
		// 
		level = Integer.parseInt(whatLevel);
		this.setBorder(BorderFactory.createTitledBorder("Level " + level));
		
		// read words from file based on level
	}
	
	/**
	 * Continue quiz:
	 * Next word in quiz (until 10 have been asked).
	 */
	public void continueSpellingQuiz() {
		// TODO Auto-generated method stub
		
	}

	
	
}
