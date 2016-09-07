package voxspell.tools;

public class TextReader {
	
	public void read(String cmd) {
		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c" , "echo " + cmd +  " | festival --tts");
			Process process= builder.start();
			process.waitFor();
			process.destroy();
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
