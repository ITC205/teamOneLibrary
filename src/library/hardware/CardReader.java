package library.hardware;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CardReader extends JFrame implements ICardReader {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JButton btnReadCard;
	private ICardReaderListener listener;

	public CardReader() {
		setTitle("Card Reader");
        setBounds(50, 50, 400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Card Reader", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 400, 200);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblErrorMesg = new JLabel("");
		lblErrorMesg.setForeground(Color.RED);
		lblErrorMesg.setBounds(12, 21, 358, 16);
		panel.add(lblErrorMesg);
		
		JLabel lblNewLabel = new JLabel("Enter Member Id:");
		lblNewLabel.setBounds(30, 50, 150, 25);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				lblErrorMesg.setText("");
			}
		});
		textField.setBounds(190, 50, 150, 25);
		panel.add(textField);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setColumns(10);
		textField.setEditable(false);
		
		btnReadCard = new JButton("Swipe Card");
		btnReadCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listener == null) {
					throw new RuntimeException("CardReader: listener is null");
				}
				String text = textField.getText();
				try {
					int memberId = new Integer(text).intValue();
					if (memberId <= 0) {
						throw new NumberFormatException();
					}
					listener.cardSwiped(memberId);
				}
				catch (NumberFormatException e) {
					//e.printStackTrace(System.err);
					lblErrorMesg.setText("Member Id must be a positive intger");
				}
				textField.setText("");
			}
		});
		btnReadCard.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnReadCard.setBounds(121, 88, 150, 40);
		btnReadCard.setEnabled(false);
		panel.add(btnReadCard);
	}


	@Override
	public void setEnabled(boolean enabled) {
		btnReadCard.setEnabled(enabled);
		textField.setEditable(enabled);
	}


	@Override
	public void addListener(ICardReaderListener listener) {
		this.listener = listener;		
	}
}
