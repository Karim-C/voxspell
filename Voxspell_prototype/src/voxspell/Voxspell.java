package voxspell;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import voxspell.tools.CustomOptionPane;

/**
 * Voxspell main.
 * 
 * @author Will Molloy
 *
 */
@SuppressWarnings("serial")
public class Voxspell extends JPanel {
			
	// Cardlayout used to switch between JPanels in the cardlayout panel
	private static final CardLayout cardLayout = new CardLayout();

	// Main menu panel and the overall (Spelling Aid) panel
	private JPanel _mainMenuPanel;
	private static JPanel _cardLayoutPanel;
	private SpellingQuiz _spellingQuiz;
	
	// Other Panels next to the card layout - Statistics and Settings
	private Statistics _statistics;
	private Settings _settings;
	
	// Panel to hold these other panels
	private JPanel _sidePanel;
	
	// Names for main menu buttons
	private static final String MAIN_MENU = "Return to main menu";
	private static final String NEW_QUIZ = "New Spelling Quiz";
	private static final String VIEW_STATS = "View Statistics";
	private static final String CLEAR_STATS = "Clear Statistics";

	// Main menu buttons
	private JButton _newSpellingQuizBtn;
	private JButton _viewStatsBtn;
	private JButton _clearStatsBtn;

	private Voxspell() {  
		this.setLayout(new BorderLayout());
		
		/* Create base panels for the program */
		createMainMenuPanel();
		_spellingQuiz = new SpellingQuiz();

		/* Adding MainMenu/Stats/SpellingQuiz to a cardlayout */
		_cardLayoutPanel = new JPanel();
		_cardLayoutPanel.setPreferredSize(new Dimension(300,450));
		_cardLayoutPanel.setLayout(cardLayout);
		_cardLayoutPanel.add(_mainMenuPanel, MAIN_MENU);
		_cardLayoutPanel.add(_spellingQuiz, NEW_QUIZ);
		
		/* Side panel to hold session statistics and settings */
		_sidePanel = new JPanel();
		_sidePanel.setPreferredSize(new Dimension(300,450));
		_sidePanel.setLayout(new BorderLayout());
		
		_statistics = Statistics.getInstance();
		_statistics.setPreferredSize(new Dimension(300,300));
		
		_settings = Settings.getInstance(this);
		_settings.setPreferredSize(new Dimension(300,150));
		
		_sidePanel.add(_statistics, BorderLayout.NORTH);
		_sidePanel.add(_settings, BorderLayout.SOUTH);
		
		/* Adding the cardlayout and sidepanel to the overall panel */
		this.add(_cardLayoutPanel, BorderLayout.WEST);
		this.add(_sidePanel, BorderLayout.EAST);
	}

	private void createMainMenuPanel() {
		_mainMenuPanel = new JPanel();
		_mainMenuPanel.setBorder(BorderFactory.createTitledBorder("Welcome to Voxspell!"));

		_newSpellingQuizBtn = new JButton(NEW_QUIZ);
		_viewStatsBtn = new JButton(VIEW_STATS);
		_clearStatsBtn = new JButton(CLEAR_STATS);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.add(_newSpellingQuizBtn);
		buttonPanel.add(_viewStatsBtn);
		buttonPanel.add(_clearStatsBtn);
		_mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);

		createMainMenuEventHandlers();
	}

	private void createMainMenuEventHandlers() {
		_newSpellingQuizBtn.addActionListener( (ActionListener) -> {
			cardLayout.show(_cardLayoutPanel, NEW_QUIZ);
			_spellingQuiz.newQuiz();
		});

		_viewStatsBtn.addActionListener( (ActionListener) -> {
			cardLayout.show(_cardLayoutPanel, VIEW_STATS);
		});

		_clearStatsBtn.addActionListener( (ActionListener) -> {
			CustomOptionPane customOptionPane = new CustomOptionPane(this);
			if (customOptionPane.yesNoDialog("Clear statistics, are you sure?", "Clearing statistics")){
				_statistics.clearStats();
			}
		});
	}
	
	public static void showMainMenu(){
		cardLayout.show(_cardLayoutPanel, MAIN_MENU);
	}

	private static void createAndShowGUI() {
		Voxspell mainPanel = new Voxspell();

		JFrame frame = new JFrame("Voxspell");
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
}
