package library.panels;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import library.interfaces.IMainListener;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainPanel(IMainListener listener) {
		setBorder(new TitledBorder(null, "Main Menu", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setBounds(500, 50, 470, 680);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Backwoods Regional Library");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblNewLabel.setBounds(12, 27, 446, 32);
		add(lblNewLabel);
		
		JLabel lblSelfServiceSystem = new JLabel("Self Service System");
		lblSelfServiceSystem.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelfServiceSystem.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblSelfServiceSystem.setBounds(12, 61, 446, 32);
		add(lblSelfServiceSystem);
		
		JButton btnBorrowBooks = new JButton("Borrow Books");
		btnBorrowBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listener.borrowBooks();
			}
		});
		btnBorrowBooks.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBorrowBooks.setBounds(141, 138, 155, 37);
		add(btnBorrowBooks);
        //setBounds(500, 100, 750, 615);
	}
}
