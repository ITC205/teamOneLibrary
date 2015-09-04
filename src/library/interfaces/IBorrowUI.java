package library.interfaces;

public interface IBorrowUI {

	public void setState(EBorrowState state);
	
	public void displayMemberDetails(int memberID, String memberName, String memberPhone);

	public void displayExistingLoan(String loanDetails);

	public void displayOverDueMessage();
	
	public void displayAtLoanLimitMessage();
	
	public void displayOutstandingFineMessage(float amountOwing);

	public void displayOverFineLimitMessage(float amountOwing);

	public void displayScannedBookDetails(String bookDetails);

	public void displayPendingLoan(String loanDetails);
	
	public void displayConfirmingLoan(String loanDetails);
	
	public void displayErrorMessage(String errorMesg);
	
}
