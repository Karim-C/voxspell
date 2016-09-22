package voxspell.tools;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import voxspell.SpellingQuiz;

/**
 * Plays a video in a JFrame.
 * 
 * Linked to the voxspell.SpellingQuiz class.
 * 
 * Has a method to play the reward level and progress to the next level.
 * And a method to play the final reward video after finishing the final level in the SpellingQuiz class. (todo)
 * 
 * @author will
 */
@SuppressWarnings("serial")
public class VideoPlayer extends EmbeddedMediaPlayerComponent {
	
	private static String _videoFileName = "big_buck_bunny_1_minute.avi";
	private JFrame _videoFrame;
	private SpellingQuiz _spellingQuiz;
	
	// Button press logic
	private boolean _pausePressed = false;
	private boolean _mutePressed = false;
	
	public VideoPlayer(SpellingQuiz spellingQuiz, boolean special){
		_spellingQuiz = spellingQuiz;
		
		if (special) {
			_videoFileName = "speacialReward.avi";
		}
	
		
	}
	
	public void playVideoThenGoToNextSpellingQuizLevel(){
		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/Applications/vlc-2.0.0/VLC.app/Contents/MacOS/lib"
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				execute();
			}
		});
		
	}
	
	private void execute(){
		
		_videoFrame = new JFrame("Reward Video");

		final EmbeddedMediaPlayer video = this.getMediaPlayer();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this, BorderLayout.CENTER);

		_videoFrame.setContentPane(panel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,4));
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnPause = new JButton("Pause");
		buttonPanel.add(btnPause);
		btnPause.addActionListener((ActionEvent) ->  {
				
				// The text on the button changes after the user pauses to play and vice versa
				if (!_pausePressed){
					video.pause();
					btnPause.setText("Play");
				}else {
					video.play();
					btnPause.setText("Pause");
				}
				_pausePressed = !_pausePressed;
		});
		
		
		JButton btnMute = new JButton("Mute");
		buttonPanel.add(btnMute);
		btnMute.addActionListener((ActionEvent) ->  {
				video.mute();
				
				// The text on the button changes after the user mute to unmute and vice versa
				if (!_mutePressed) {
					btnMute.setText("Unmute");
					_mutePressed = true;
				} else {
					btnMute.setText("Mute");
					_mutePressed = false;
				}
		});
		
		// Allows the user to go back 5 seconds
		JButton btnBackSkip = new JButton("Back");
		buttonPanel.add(btnBackSkip);
		btnBackSkip.addActionListener((ActionEvent) ->  {
				video.skip(-5000);
		});
		
		// Allows the user to go forwards 5 seconds
		JButton btnSkip = new JButton("Forward");
		buttonPanel.add(btnSkip);
		btnSkip.addActionListener((ActionEvent) ->  {
				video.skip(5000);
		});

		_videoFrame.setLocation(100, 100);
		_videoFrame.setSize(1050, 600);
		_videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // SO entire program doesn't close!
		_videoFrame.setVisible(true);		
		video.playMedia(_videoFileName);
		
		
		
		_videoFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				video.stop();
				_spellingQuiz.nextLevel();
			}
		});
	}
	
	@Override
	public void finished(MediaPlayer mediaPlayer){
		_videoFrame.dispose(); // closes only the JFrame with the video, not entire voxspell program
		_spellingQuiz.nextLevel();
		super.finished(mediaPlayer);
	}



}
