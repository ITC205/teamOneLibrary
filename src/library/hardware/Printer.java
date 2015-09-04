package library.hardware;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import library.interfaces.hardware.IPrinter;

import javax.swing.JTextArea;

public class Printer extends JFrame implements IPrinter {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;

	public Printer() {
        setBounds(50, 450, 400, 350);
		setResizable(false);
		setTitle("Printer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Printer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 20, 400, 280);
		getContentPane().add(panel);
		panel.setLayout(null);
		

		textArea = new JTextArea();
		textArea.setEditable(false);
		//textArea.setBounds(10, 20, 375, 280);
		//textArea.setColumns(40);
		//textArea.setRows(80);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 20, 375, 280);
		panel.add(scrollPane);
	}

	@Override
	public void print(String printData) {
		textArea.setText(printData);
		textArea.setCaretPosition(textArea.getLineCount());		
	}
}
