package voxspell.tools;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents an easier way to create JOptionPanes with a few options. 
 * 
 * @author Will Molloy
 */
@SuppressWarnings("serial")
public class CustomOptionPane extends JOptionPane {

	private JPanel _panelToShowOn;
	
	public CustomOptionPane(JPanel panelToShowOn){
		_panelToShowOn = panelToShowOn;
	}
	
	/**
	 * Creates a JOptionPane with a YES_NO dialog.
	 * Returns true if user selects yes. False if user selects no.
	 */
	public boolean yesNoDialog(String msg, String title){
		if(JOptionPane.showConfirmDialog(_panelToShowOn, msg, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	
	/**
	 * Creates an option dialog using a String[] of options.
	 * Returns an int of the index the user selected from that string[] of options.
	 */
	public int optionDialog(String msg, String title, String[] options, String initialOption){
		int selection = JOptionPane.showOptionDialog(
				this, msg, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, initialOption);
		return selection;
	}
	
}
