package voxspell;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import voxspell.tools.TextToSpeech;

/**
 * Contains JComboBox components to change a few settings for the Voxspell program.
 * 
 * @author Will Molloy
 */
@SuppressWarnings("serial")
public class Settings extends JPanel {

	// Voxspell instance - needed to repaint GUI components. E.g. if changing font size
	private Voxspell _voxspellInstance;

	// Voice combobox
	private static final String[] _ttsVoices = { "Kal" , "Rab" }; 
	private JComboBox<String> _ttsVoiceComboBox;

	// Fontsize combobox
	private static final String[] _fontSizes = { "10" , "12" , "14" };
	private JComboBox<String> _fontSizeComboBox;

	// this class is a singleton
	private static Settings instance = null;
	public static Settings getInstance(Voxspell voxspell) {
		if (instance == null) {
			instance = new Settings(voxspell);
		}
		return instance;
	}

	/**
	 * Creates the base JPanel for ViewStats
	 * 
	 * @author Will Molloy
	 */
	private Settings(Voxspell voxspell) {
		_voxspellInstance = voxspell;

		/* GUI */
		this.setBorder(BorderFactory.createTitledBorder("Settings"));

		// Voice
		JLabel voiceText = new JLabel("Voice:");
		this.add(voiceText);
		_ttsVoiceComboBox = new JComboBox<String>(_ttsVoices);
		this.add(_ttsVoiceComboBox);

		// Font size
		JLabel fontSizeText = new JLabel("Font Size:" );
		this.add(fontSizeText);
		_fontSizeComboBox = new JComboBox<String>(_fontSizes);
		this.add(_fontSizeComboBox);
		_fontSizeComboBox.setSelectedItem(_fontSizes[1]);

		/* Event handlers for Settings */
		createVoiceDropDownEventHandler();
		createFontSizeEventHandler();
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

	private void createFontSizeEventHandler(){
		_fontSizeComboBox.addActionListener( (ActionListener) -> {
			String item = (String) _fontSizeComboBox.getSelectedItem();
			int fontSize = Integer.parseInt(item);
			setFontSize(fontSize);
		});
	}

	/*
	 * Sets font size of the voxspell instance.
	 * Using code from http://stackoverflow.com/a/2989344
	 */
	private void setFontSize(int fontSize){
		FontUIResource f = new FontUIResource(new Font("Arial", 0, fontSize));
		Enumeration<Object> keys = UIManager.getLookAndFeelDefaults().keys();

		// Placing keys (components?) into the UIManager
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				FontUIResource orig = (FontUIResource) value;
				Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
		// Update component tree recursively
		SwingUtilities.updateComponentTreeUI(_voxspellInstance);
	}

}
