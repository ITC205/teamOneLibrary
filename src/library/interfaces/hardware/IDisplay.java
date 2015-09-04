package library.interfaces.hardware;

import javax.swing.JPanel;

public interface IDisplay {
	
	public JPanel getDisplay();
	public void setDisplay(JPanel panel, String identifier);
	
}
