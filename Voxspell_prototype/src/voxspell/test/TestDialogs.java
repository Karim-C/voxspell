package voxspell.test;

import voxspell.Voxspell;
import voxspell.tools.CustomOptionPane;

public class TestDialogs {

	public static void main(String[] args){
		String[] options = { "Restart current level" , "Return to main menu" , "3" , "4" , "5"};
		
		CustomOptionPane customOptionPane = new CustomOptionPane(null);
		int selection = customOptionPane.optionDialog("You got " + 8 + " words correct out of 10.\n"
				+ "You need 9 or more words correct in order to progress to the next level.\n"
				, "Failure", options, options[0]);
		System.out.println(selection);
	}
	
}
