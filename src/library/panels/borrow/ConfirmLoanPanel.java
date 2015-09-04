package library.panels.borrow;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import library.interfaces.IBorrowUIListener;

import java.awt.Font;

public class ConfirmLoanPanel extends ABorrowPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea loanListTA;

	/**
	 * Create the panel.
	 */
	public ConfirmLoanPanel(IBorrowUIListener listener) {
		setLayout(null);
		setBorder(new TitledBorder(null, "Confirm Loans", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setBounds(12, 23, 460, 640);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Current Loan List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 24, 415, 496);
		this.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 20, 395, 463);
		panel.add(scrollPane);
		
		loanListTA = new JTextArea();
		loanListTA.setEditable(false);
		scrollPane.setViewportView(loanListTA);

		JButton btnReject = new JButton("Reject");
		btnReject.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnReject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listener.loansRejected();
			}
		});
		btnReject.setBounds(173, 533, 115, 35);
		this.add(btnReject);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.loansConfirmed();
			}
		});
		btnConfirm.setBounds(30, 533, 115, 35);
		this.add(btnConfirm);
		
		JButton button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listener.cancelled();
			}
		});
		button.setFont(new Font("Tahoma", Font.PLAIN, 14));
		button.setBounds(312, 533, 115, 35);
		add(button);
	}


	@Override
	public void displayConfirmingLoan(String loanDetails) {
		loanListTA.setText(loanDetails);
		loanListTA.setCaretPosition(0);
	}


}