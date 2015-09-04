package library.hardware;

import library.interfaces.hardware.IDisplay;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;


public class Display extends JFrame implements IDisplay {

	private static final long serialVersionUID = 1L;
	
	private JPanel current; 

	public Display() {
		setTitle("Display");
		setBounds(500, 50, 470, 680);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		getContentPane().setLayout(new CardLayout(0, 0));
	}


	@Override
	public JPanel getDisplay() {
		return current;		
	}
	
	
	@Override
	public void setDisplay(JPanel panel, String id) {
		getContentPane().add(panel, id);
		CardLayout cardLayout = (CardLayout) (getContentPane().getLayout());
		cardLayout.show(getContentPane(), id);
		if (current != null) {
			System.out.println("Display.setDisplay: removing current");
			cardLayout.removeLayoutComponent(current);
		}
		current = panel;
	}

	
}
