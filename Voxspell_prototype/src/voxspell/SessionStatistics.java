package voxspell;

/** 
 * This class contains fields which record Session statistics and methods which record 
 * and display the Statistics.
 * 
 * @author Karim Cisse
 * @author Will Molloy
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class SessionStatistics extends JPanel {
	
	/*
	 * List of stats. Contains an ArrayList of Strings for each levels stats.
	 * In the String[] arrays the elements work as follows: 
	 * element 0: word, element 1: successes, element 2: attempts
	 */
	private List<ArrayList<String[]>> currentStats;   

	private static final Dimension TABLE_DIMENSION = new Dimension(260, 240);
	private static final int NUM_LEVELS = SpellingQuiz.NUM_OF_LEVELS;

	private JTabbedPane _tableTabPane = new JTabbedPane();
	private List<JScrollPane> _tableScrolls = new ArrayList<JScrollPane>();
	private List<JTable> _statTables = new ArrayList<JTable>(Collections.nCopies(NUM_LEVELS, new JTable()));
	private static SessionStatistics instance;
	private JLabel _wordCountOutputArea;

	// this class is a singleton
	public static SessionStatistics getInstance() {
		if (instance == null) {
			instance = new SessionStatistics();
		}
		return instance;
	}
	
	/**
	 * Shows the stats table for the given level.
	 */
	public void setLevelShownForTable(int level){
		_tableTabPane.setSelectedIndex(level-1);
	}

	/**
	 * Creates the base JPanel for SessionStats
	 * 
	 * @author Will Molloy
	 */
	private SessionStatistics() {
		this.setBorder(BorderFactory.createTitledBorder("Session Statistics"));

		_wordCountOutputArea = new JLabel();
		_wordCountOutputArea.setText("Current Quiz) Correct: 0 Attempted: 0");

		this.add(_wordCountOutputArea, BorderLayout.NORTH);
		getTableAndScrollPaneInstance(); // initialises the empty List of JScrollPanes and adds JTables to them
		clearStats(); // initialises the empty List of statistics and List of JTables
	}

	/**
	 * This method takes a word (string) and whether it was spelled correctly
	 * (boolean) and then adds the information to the currentStats ArrayList
	 * 
	 * @author Karim Cisse - implemented method.
	 * @author Will Molloy - added support for specifying the level the word is from.
	 */
	public void addToStats(String word, boolean spelledCorrectly, int level) {

		int index = wordAlreadyAttemptThisSession(word, level);
		
		// the success variable uses a binary representation for true and false,
		// 1 and 0 respectively
		int success = spelledCorrectly? 1 : 0;

		/*
		 * Need to get the ListOfStats for the current level. (A List of Strings)
		 * Must recreate the object so that there is no reference to the old object.
		 * (Simply using .get() causes the stats for every level to be updated).
		 */
		ArrayList<String[]> listOfStats = new ArrayList<String[]>(currentStats.get(level-1));
		
		// -1 Indicates the word has not been attempted this session
		if (index == -1) {
			String[] stats = new String[] { word, success + "", "1" };
			listOfStats.add(stats);
		} else {
			String[] stats = listOfStats.get(index);
			String successes = "" + (Integer.parseInt(stats[1]) + success);
			String Attempts = "" + (Integer.parseInt(stats[2]) + 1);
			
			/*
			 * the old statistics are removed and the 
			 * new are added to update the ArrayList
			 */
			listOfStats.remove(index);  
			listOfStats.add(new String[] { word, successes, Attempts });
		}
		// Finally update the overall stats 
		currentStats.set(level-1, listOfStats);
	}

	/**
	 * This method clears the current statistics
	 * 
	 * @author Karim Cisse - implemented method using list of strings 
	 * @author Will Molloy - changed to clear a list of list of strings 
	 */
	public void clearStats() {
		currentStats = new ArrayList<ArrayList<String[]>>(Collections.nCopies(NUM_LEVELS, new ArrayList<String[]>()));
		generateAndShowTables();
	}

	/**
	 * Creates and shows the table for every level.
	 * 
	 * @author Will Molloy
	 */
	private void generateAndShowTables() {
		for (int i = 1; i <= NUM_LEVELS; i++){
			generateAndShowTableForLevel(i);
		}
	}

	/**
	 * Creates the JScrollPanes that will hold the JTables and adds the ScrollPanes
	 * to a tabbed pane. Then adds the tabbed pane to this JPanel.
	 * 
	 * @author Will Molloy
	 */
	private void getTableAndScrollPaneInstance() {
		for (int i = 0; i < NUM_LEVELS; i++){
			JScrollPane scrollPane = new JScrollPane();
			JLabel label = new JLabel("" +(i+1)); 		// (i+1), the level of stats being shown on the table
			label.setPreferredSize(new Dimension(20,10));
			
			_tableScrolls.add(scrollPane);				// add scrollPane to JScrollPane List
			_tableTabPane.add(scrollPane);				// add scrollPane to tabbed pane
			_tableTabPane.setTabComponentAt(i, label); 	// the label shown on the tabbed pane
		}
		this.add(_tableTabPane, BorderLayout.NORTH);
	}

	/**
	 * Generates the statistics table based on what is in the sorted list
	 * 'sortedWordsToDisplay' and is in the hidden statistic files.
	 * 
	 * Found the code for using DefaultTableModel here: http://stackoverflow.com/a/11095952/6122976
	 * 
	 * @author Karim Cisse - statistics retrieval / placing stats into array.
	 * @author Will Molloy - code for creating a JTable using the array.
	 */
	public void generateAndShowTableForLevel(int level) {
		String[] columnNames = { "Word", "Accuracy", "Attempts" };
		List<String[]> stats = new ArrayList<String[]>();

		/*
		 * Level-1 is the position of the table/stats for the level in the List
		 */
		for (String[] wordStats : currentStats.get(level-1)) { 
			stats.add(new String[] { wordStats[0], calcAccurracy(wordStats), wordStats[2] });
		}

		// Create the JTable using the List of stats and String[] of columnNames
		TableModel tableModel = new DefaultTableModel(stats.toArray(new Object[][] {}), columnNames) {
			@Override // Makes all of the cells in the table uneditable
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		// Update the table in the correct position in the list of tables.
		JTable table = new JTable(tableModel);
		_statTables.set(level-1, table);
		
		showTables();
	}

	/**
	 * Shows the statistic tables
	 * 
	 * @author Will Molloy
	 */
	private void showTables() {
		for (int i = 0; i < NUM_LEVELS; i++){
			_statTables.get(i).setPreferredScrollableViewportSize(TABLE_DIMENSION);
			_statTables.get(i).setFillsViewportHeight(true);
			_tableScrolls.get(i).setViewportView(_statTables.get(i));
			
			/*
			 * repaints all components so that the scroll pane
			 * adjusts to the new table size
			 */
			this.repaint();  
		}
	}

	/**
	 * Calculates the accuracy of the spelling of a word overtime based on the
	 * number of successes and the number of attempts.
	 * 
	 * @author Karim Cisse
	 */
	private String calcAccurracy(String[] wordStats) {
		Double accurracy = (Double.parseDouble(wordStats[1]) / Double.parseDouble(wordStats[2])) * 100;
		return accurracy.toString();
	}

	/**
	 * The method takes string and checks the whether it has been attempted
	 * during the current session. If it has then the method returns the index
	 * of the words statistics stored in the currentStats ArrayList. If it has
	 * not been attempted during this session the method returns -1.
	 * 
	 * @author Karim Cisse
	 */
	private int wordAlreadyAttemptThisSession(String word, int level) {
		int index = 0;
		for (String[] stats : currentStats.get(level-1)) {
			if (stats[0].equals(word)) {
				return index;
			}
			index++;
		}
		return -1;

	}

	/**
	 * This method displays the number of words correct and the number of
	 * words attempted for a given quiz
	 * 
	 * @author Karim Cisse
	 */
	public void displayWordCount(int wordsCorrectFirstAttempt, int wordsAttempt) {
		_wordCountOutputArea.setText("Current Quiz) Correct: " + wordsCorrectFirstAttempt + " Attempted: " + wordsAttempt);
	}
}
