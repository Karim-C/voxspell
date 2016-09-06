package voxspell;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SpellingQuiz extends JPanel {

	// Swing componenets
	private JTextField _wordEntryField;
	private JButton _enterWordBtn;
	private JButton _newQuizBtn;
	protected JTextArea _programOutputArea;
	
	private static int[] levels;
	{
		levels = { 1 };
	}
	
	/**
	 * Gets the file name from the sub type, then creates the GUI.
	 */
	public SpellingQuiz(){
		createGUI();
	}
	
	protected final void createGUI() {
		
		
	//	this.setBorder(BorderFactory.createTitledBorder("Level x"));
		this.setPreferredSize(new Dimension(305,270));

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

		// New quiz button, only available after a quiz has completed
		_newQuizBtn = new JButton("New Quiz");
		this.add(_newQuizBtn, BorderLayout.EAST);

	}
	
	public void newQuiz()  {
		// popup asking for level
		
		
		// 
		int level = 10;
		this.setBorder(BorderFactory.createTitledBorder("Level " + level));
	}

	
	
}
