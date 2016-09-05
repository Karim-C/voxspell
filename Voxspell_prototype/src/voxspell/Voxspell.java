package voxspell;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Voxspell extends JPanel {

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
		frame.setPreferredSize(new Dimension(500,500));
		frame.setLocationByPlatform(true);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run(){
				createAndShowGUI();
				// a change
			}
		});
	}

}
