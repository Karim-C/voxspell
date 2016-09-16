package voxspell;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import voxspell.tools.TextToSpeech;

public class Settings extends JPanel {
	
	// Voice combobox
	private static final String[] _ttsVoices = { "Kal" , "Rab" }; 
	private JComboBox<String> _ttsVoiceComboBox;
	

	// this class is a singleton
	private static Settings instance = null;
	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	/**
	 * Creates the base JPanel for ViewStats
	 * 
	 * @author Will Molloy
	 */
	private Settings() {

		/* GUI */
		this.setBorder(BorderFactory.createTitledBorder("Settings"));
		
		// Voice
		JLabel voiceText = new JLabel("Voice:");
		this.add(voiceText);
		_ttsVoiceComboBox = new JComboBox<String>(_ttsVoices);
		this.add(_ttsVoiceComboBox);
	
		// maybe font size setting later (using MVC)
		
		/* Event handlers for Settings */
		createVoiceDropDownEventHandler();

	}
	
	private void createVoiceDropDownEventHandler() {
		_ttsVoiceComboBox.addActionListener( (ActionListener) -> {
			String voice = (String) _ttsVoiceComboBox.getSelectedItem();
			String voiceCommand = getFestivalVoiceCommand(voice);
			TextToSpeech.setVoice(voiceCommand);
		});
	}

	private String getFestivalVoiceCommand(String voice) {
		switch (voice){
		case "Rab":
			return "(voice_rab_diphone)";
		case "Kal":
			return "(voice_kal_diphone)";
		default:
			return "(voice_kal_diphone)";
		} 
	}
	
}
