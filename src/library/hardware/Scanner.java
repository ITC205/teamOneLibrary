package library.hardware;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.JButton;

public class Scanner extends JFrame implements IScanner {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JButton btnScan;
	private IScannerListener listener;

	public Scanner() {
		setTitle("Scanner");
        setBounds(50, 250, 400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Scanner", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 400, 200);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblErrorMesg = new JLabel("");
		lblErrorMesg.setForeground(Color.RED);
		lblErrorMesg.setBounds(12, 21, 358, 16);
		panel.add(lblErrorMesg);
		
		JLabel lblNewLabel = new JLabel("Enter Book Barcode: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(30, 50, 150, 25);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				lblErrorMesg.setText("");
			}
		});
		textField.setBounds(190, 50, 150, 25);
		panel.add(textField);
		textField.setColumns(10);
		textField.setEditable(false);
		
		btnScan = new JButton("Scan Book Barcode");
		btnScan.setEnabled(false);
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listener == null) {
					throw new RuntimeException("Scanner: listener is null");
				}
				String text = textField.getText();
				try {
					int barcode = new Integer(text).intValue();
					if (barcode <= 0) {
						throw new NumberFormatException();
					}
					listener.bookScanned(barcode);
				}
				catch (NumberFormatException e) {
					//e.printStackTrace(System.err);
					lblErrorMesg.setText("Book barcode must be a positive intger");
				}
				textField.setText("");
			}
		});
		btnScan.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnScan.setBounds(85, 88, 215, 39);
		panel.add(btnScan);

		//pack();
	}

	@Override
	public void setEnabled(boolean enabled) {
		btnScan.setEnabled(enabled);
		textField.setEditable(enabled);

	}

	@Override
	public void addListener(IScannerListener listener) {
		this.listener = listener;
		
	}

}
