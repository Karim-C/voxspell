package voxspell;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Voxspell main.
 * 
 * @author Will Molloy
 *
 */
@SuppressWarnings("serial")
public class Voxspell extends JPanel {

	// Main menu panel and the overall (Spelling Aid) panel
	private JPanel mainMenuPanel, overallPanel;

	private SpellingQuiz spellingQuiz;

	// Names for main menu buttons
	private static final String MAIN_MENU = "Return to main menu";
	private static final String NEW_QUIZ = "New Spelling Quiz";
	private static final String REVIEW_MISTAKES = "Review Mistakes";
	private static final String VIEW_STATS = "View Statistics";
	private static final String CLEAR_STATS = "Clear Statistics";

	// Main menu buttons
	private JButton _newSpellingQuizBtn = new JButton(NEW_QUIZ);
	private JButton _viewStatsBtn = new JButton(VIEW_STATS);
	private JButton _clearStatsBtn = new JButton(CLEAR_STATS);

	// Cardlayout used to switch between JPanels in the overallPanel 
	private CardLayout cardLayout = new CardLayout();

	public Voxspell() {  

		/* Create base panels for the program */
		createMainMenuPanel();
		createNewQuizPanel();

		/* Add the above JPanels to this JPanel using a CardLayout */
		overallPanel = new JPanel();
		overallPanel.setLayout(cardLayout);
		overallPanel.add(mainMenuPanel, MAIN_MENU);
		overallPanel.add(spellingQuiz, NEW_QUIZ);

		this.add(overallPanel);
		// A CHANGE
	}

	private void createMainMenuPanel() {
		mainMenuPanel = new JPanel();
		mainMenuPanel.setBorder(BorderFactory.createTitledBorder("Welcome to the Spelling Aid"));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.add(_newSpellingQuizBtn);
		buttonPanel.add(_viewStatsBtn);
		buttonPanel.add(_clearStatsBtn);
		mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

		createMainMenuEventHandlers();
	}

	private void createNewQuizPanel() {
		spellingQuiz = new SpellingQuiz();
		spellingQuiz.add(new ReturnToMainMenuButton(), BorderLayout.SOUTH);

	}

	private void createMainMenuEventHandlers() {
		_newSpellingQuizBtn.addActionListener( (ActionListener) -> {
			cardLayout.show(overallPanel, NEW_QUIZ);
			spellingQuiz.newQuiz();
		});

		_viewStatsBtn.addActionListener( (ActionListener) -> {
			cardLayout.show(overallPanel, VIEW_STATS);
			//		viewStatsPanel.generateAndShowStats(); // will show entire history of stats 
			// session stats will always be showing
		});

		_clearStatsBtn.addActionListener( (ActionListener) -> {
			if (!areYouSure("Clear statistics")){
				return;
				//		clearStats(); // seperate ones for clearing session stats vs. entire history?
			}
		});
	}

	private static void createAndShowGUI() {
		Voxspell mainPanel = new Voxspell();

		JFrame frame = new JFrame("Spelling Aid");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});

		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run(){
				createAndShowGUI();
			}
		});
	}

	/**
	 * Prompts the user with a YES_NO JOptionPane if they want to proceed with a menu selection or not.
	 * 
	 * Got the code here: http://stackoverflow.com/a/15853127/6122976
	 */
	private boolean areYouSure(String selection) {
		if(JOptionPane.showConfirmDialog(this, selection + ", are you sure?", "Proceed?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}

	/**
	 * Represents a JButton allowing the user to return to the main menu.
	 * Nested because it needs access to private fields.
	 */
	private class ReturnToMainMenuButton extends JButton{

		public ReturnToMainMenuButton(){
			super("Return to Main Menu"); // name of button

			this.addActionListener( (ActionListener) -> {
				if (!areYouSure("Return to the main menu")){ // dialog popup
					return;
				}
				cardLayout.show(overallPanel, MAIN_MENU);

			});
		}
	}
}
