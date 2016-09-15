package voxspell.tools;

import java.awt.BorderLayout;
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

public class VideoPlayer extends EmbeddedMediaPlayerComponent {
	
	private static final String videoFileName = "big_buck_bunny_1_minute.avi";
	
	private JFrame videoFrame;
	
	private SpellingQuiz _spellingQuiz;
	
	public VideoPlayer(SpellingQuiz spellingQuiz){
		_spellingQuiz = spellingQuiz;
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
		videoFrame = new JFrame("Reward Video");

		final EmbeddedMediaPlayer video = this.getMediaPlayer();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this, BorderLayout.CENTER);

		videoFrame.setContentPane(panel);

		JButton btnMute = new JButton("Shh....");
		panel.add(btnMute, BorderLayout.NORTH);
		btnMute.addActionListener((ActionEvent) ->  {
				video.mute();
		});

		videoFrame.setLocation(100, 100);
		videoFrame.setSize(1050, 600);
		videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // SO entire program doesn't close!
		videoFrame.setVisible(true);		
		video.playMedia(videoFileName);
		
		videoFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				video.stop();
				_spellingQuiz.nextLevel();
			}
		});
	}
	
	@Override
	public void finished(MediaPlayer mediaPlayer){
		videoFrame.dispose();
		_spellingQuiz.nextLevel();
		super.finished(mediaPlayer);
	}



}
