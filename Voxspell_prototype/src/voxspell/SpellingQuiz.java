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

public class SpellingQuiz extends JPanel {

	// Swing components
	private JTextField _wordEntryField;
	private JButton _enterWordBtn;
	private JButton _newQuizBtn;
	protected JTextArea _programOutputArea;
	
	// game logic
	private int level;
	
	private static final String[] levels = {"1","2","3","4","5","6","7","8","9","10"}; //k better way?
	
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
		
		addEventHandlers();	
	}
	
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
		// read file for level
	}
	
	private void addEventHandlers() {
		/* 
		 *remove this button, user gets option to proceed after completing quiz.
		 */
		_newQuizBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newQuiz();
			}
		});
		
	}

	
	
}
