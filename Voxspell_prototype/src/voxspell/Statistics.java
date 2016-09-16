package voxspell;

/** 
 * This class contains fields which record Session statistics and methods which record and display the Statistics.
 */
/*
 * The following class was based on code written by Will Molloy.
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class Statistics extends JPanel {
	private ArrayList<String[]> currentStats; // element 0: word, element 1:
												// successes,
	// element 2: attempts

	private static final Dimension TABLE_DIMENSION = new Dimension(260, 230);

	private JScrollPane _tableScroll;
	private JTable _statTable;
	private static Statistics instance;
	private JTextArea _wordCountOutputArea;

	// this class is a singleton
	public static Statistics getInstance() {
		if (instance == null) {
			instance = new Statistics();
		}
		return instance;
	}

	/**
	 * Creates the base JPanel for ViewStats
	 * 
	 * @author Will Molloy
	 */
	private Statistics() {
		currentStats = new ArrayList<String[]>();

		this.setBorder(BorderFactory.createTitledBorder("Session Statistics"));

		_wordCountOutputArea = new JTextArea();
		_wordCountOutputArea.setEditable(false);
		//_wordCountOutputArea.setVisible(true);
		_wordCountOutputArea.setText("Quiz) Correct: 0 Attempted: 0");
		// _wordCountOutputArea.setPreferredSize(new Dimension(285, 340));
		this.add(_wordCountOutputArea, BorderLayout.NORTH);

		getTableAndScrollPaneInstance();
	}

	/**
	 * This method takes a word (string) and whether it was spelled correctly
	 * (boolean) and then adds the information to the currentStats ArrayList
	 * 
	 * @author Karim Cisse
	 */
	public void addToStats(String word, boolean spelledCorrectly) {

		int index = wordAlreadyAttemptThisSession(word);
		int success = 0;

		// the success variable uses a binary representation for true and false,
		// 1 and 0 respectively
		if (spelledCorrectly) {
			success = 1;
		}

		// -1 Indicates the word has not been attempted this session
		if (index == -1) {
			String[] stats = new String[] { word, success + "", "1" };
			currentStats.add(stats);
		} else {
			String[] stats = currentStats.get(index);
			String successes = "" + (Integer.parseInt(stats[1]) + success);
			String Attempts = "" + (Integer.parseInt(stats[2]) + 1);
			currentStats.remove(index); // the old statistics are removed and
										// the the
										// new are added to update the ArrayList
			currentStats.add(index, new String[] { word, successes, Attempts });
		}
	}

	/**
	 * This method clears the current statistics
	 * 
	 */
	public void clearStats() {
		currentStats = new ArrayList<String[]>();
		generateAndShowTable();
	}

	/**
	 * Adds empty scroll pane to this JPanel which will later hold the statistic
	 * table. Only initialised once so that statTable can be updated at run
	 * time.
	 * 
	 * @author Will Molloy
	 */
	private void getTableAndScrollPaneInstance() {
		if (_tableScroll == null) {
			_tableScroll = new JScrollPane();
			this.add(_tableScroll, BorderLayout.NORTH);
		}
	}

	/**
	 * Generates the statistics table based on what is in the sorted list
	 * 'sortedWordsToDisplay' and is in the hidden statistic files.
	 * 
	 * Found code here: http://stackoverflow.com/a/11095952/6122976
	 */
	public void generateAndShowTable() {
		String[] columnNames = { "Word", "Accuraccy", "Attempts" };
		List<String[]> stats = new ArrayList<String[]>();

		for (String[] wordStats : currentStats) {
			stats.add(new String[] { wordStats[0], calcAccurracy(wordStats), wordStats[2] });
		}

		// Create the JTable using the List of stats and String[] of columnNames
		TableModel tableModel = new DefaultTableModel(stats.toArray(new Object[][] {}), columnNames) {
			@Override // Makes all of the cells in the table uneditable
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		_statTable = new JTable(tableModel);
		showTable();

	}

	/**
	 * Shows the statistic table
	 */
	private void showTable() {
		_statTable.setPreferredScrollableViewportSize(TABLE_DIMENSION);
		_statTable.setFillsViewportHeight(true);
		_tableScroll.setViewportView(_statTable);
		this.repaint(); // repaints all components so that the scroll pane
						// adjusts to the new table size
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
	private int wordAlreadyAttemptThisSession(String word) {
		int index = 0;
		for (String[] stats : currentStats) {
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
		_wordCountOutputArea.setText("Quiz) Correct: " + wordsCorrectFirstAttempt + " Attempted: " + wordsAttempt);
	}
}
