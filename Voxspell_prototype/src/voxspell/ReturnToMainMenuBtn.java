package voxspell;

import javax.swing.JButton;
import javax.swing.JPanel;

import voxspell.tools.CustomOptionPane;



/**
 * Represents a JButton allowing the user to return to the main menu.
 * 
 * @author will
 */
@SuppressWarnings("serial")
public class ReturnToMainMenuBtn extends JButton {

	public ReturnToMainMenuBtn(JPanel panelToShowPaneOn){
		super("Return to Main Menu"); // name of button
		
		this.addActionListener( (ActionListener) -> {
			
			CustomOptionPane customOptionPane = new CustomOptionPane(panelToShowPaneOn);
			if (customOptionPane.yesNoDialog("Return to the main menu, are you sure?", "Return to main menu?")){
				Voxspell.showMainMenu();
			}
		});
	}

}
