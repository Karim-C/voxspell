package voxspell;

import javax.swing.JButton;
import javax.swing.JPanel;

import voxspell.tools.CustomOptionPane;

/**
 * Represents a JButton allowing the user to return to the main menu of Voxspell.
 * 
 * @author Will Molloy
 */
@SuppressWarnings("serial")
public class ReturnToMainMenuBtn extends JButton {

	public ReturnToMainMenuBtn(JPanel panelToShowPaneOn){
		super("Return to the Main Menu"); // name of button
		
		this.addActionListener( (ActionListener) -> {
			
			CustomOptionPane customOptionPane = new CustomOptionPane(panelToShowPaneOn);
			if (customOptionPane.yesNoDialog("Return to the main menu, are you sure?", "Return to main menu?")){
				Voxspell.showMainMenu();
			}
		});
	}

}
