package voxspell;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SpellingQuiz extends JPanel {

	/**
	 * Gets the file name from the sub type, then creates the GUI.
	 */
	public SpellingQuiz(){
		createGUI();
	}
	
	protected final void createGUI() {
		this.setBorder(BorderFactory.createTitledBorder("Level ?"));
		this.setPreferredSize(new Dimension(305,270));

	}
	
	public void newQuiz()  {
		// TODO Auto-generated method stub
		
	}

	
	
}
