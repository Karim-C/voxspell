package voxspell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import voxspell.tools.CaseInsensitiveComparator;
import voxspell.tools.CustomFileReader;

/**
 * History Statistics panel, shown when the user clicks 'View Statistics' from the main menu.
 * Shows the entire history of the Voxspell program using the hidden files to store
 * mastered,faulted and failed words. These statistics will be cleared when clicking the clear
 * statistics button on the main menu.
 * 
 * Most of this code is copied from assignment 2.
 * 
 * @author Will Molloy
 */
@SuppressWarnings("serial")
public class HistoryStatistics extends JPanel {

	// Datatypes to hold words
	private List<String> _sortedWordsToDisplay;

	// Swing componenets
	private JTable _statTable;
	private JScrollPane _tableScroll;
	private ReturnToMainMenuBtn _returnToMainMenuBtn;

	// tools
	private CustomFileReader _fileReader = new CustomFileReader();
	
	/**
	 * Creates the base JPanel for ViewStats
	 */
	public HistoryStatistics(){
		this.setBorder(BorderFactory.createTitledBorder("History Statistics"));
		
		this.setPreferredSize(new Dimension(300,250));
		getTableAndScrollPaneInstance();
		
		_returnToMainMenuBtn = new ReturnToMainMenuBtn(this);
		_returnToMainMenuBtn.setPreferredSize(new Dimension(260, 25));
		this.add(_returnToMainMenuBtn, BorderLayout.SOUTH);
	}
	
	public static void clearStatistics(){
		FileManager.clearStatisticFiles();
	}

	/**
	 * Adds empty scroll pane to this JPanel which will later hold the statistic table.
	 * Only initialised once so that statTable can be updated at run time.
	 */
	private void getTableAndScrollPaneInstance() {
		if (_tableScroll == null){
			_tableScroll = new JScrollPane();
			this.add(_tableScroll, BorderLayout.NORTH);
		}
	}

	/**
	 * Generates a Table of statistics based on what is in the hidden statistic files.
	 */
	protected void generateAndShowStats(){
		// read hidden files then generate and show the table of statistics.
		readStatisticFiles();
		generateAndShowTable();
	}

	/**
	 * Reads all of the words from the hidden statistic files: mastered, failed and faulted into a set.
	 * Then places the set in an ArrayList so that it can be sorted alphabetically.
	 */
	private void readStatisticFiles() {
		HashSet<String> wordsToDisplay = new HashSet<String>();

			_fileReader.readFileByLineIntoSet(FileManager.STATS_MASTERED, wordsToDisplay);
			_fileReader.readFileByLineIntoSet(FileManager.STATS_FAULTED, wordsToDisplay);
			_fileReader.readFileByLineIntoSet(FileManager.STATS_FAILED, wordsToDisplay);

		_sortedWordsToDisplay = new ArrayList<String>(wordsToDisplay);
		Collections.sort(_sortedWordsToDisplay, new CaseInsensitiveComparator()); // alphabetical order
	}

	/**
	 * Generates the statistics table based on what is in the sorted list 'sortedWordsToDisplay'
	 * and is in the hidden statistic files.
	 * 
	 * Found code here: http://stackoverflow.com/a/11095952/6122976
	 */
	private void generateAndShowTable() {
		String[] columnNames = { "Word" , "Mastered" , "Faulted" , "Failed" };
		List<String[]> stats = new ArrayList<String[]>();

		// Read the hidden files keeping a count of mastered, faulted and failed for each word
		for (String word : _sortedWordsToDisplay){
					int mastered = _fileReader.getWordCountFromFile(word, FileManager.STATS_MASTERED);
					int faulted = _fileReader.getWordCountFromFile(word, FileManager.STATS_FAULTED);
					int failed = _fileReader.getWordCountFromFile(word, FileManager.STATS_FAILED);
			stats.add(new String[] { word , mastered +"", faulted+"" , failed +"" });
		}

		// Create the JTable using the List of stats and String[] of columnNames
		TableModel tableModel = new DefaultTableModel(stats.toArray(new Object[][] {}), columnNames){
			@Override	// Makes all of the cells in the table uneditable
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		_statTable = new JTable(tableModel);		
		showTable();

		// Let the user know if there are no stats -- down here so that it shows the table as empty
		if (_sortedWordsToDisplay.size() == 0){
			JOptionPane.showMessageDialog(this, "No statistics to show!",
					"No Statistics", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Shows the statistic table
	 */
	private void showTable() {
		_statTable.setPreferredScrollableViewportSize(new Dimension(260,370));
		_statTable.setFillsViewportHeight(true);
		_tableScroll.setViewportView(_statTable);
		this.repaint(); // repaints all components so that the scroll pane adjusts to the new table size
	}
}
