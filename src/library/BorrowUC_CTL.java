package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import library.daos.MemberDAO;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

public class BorrowUC_CTL implements ICardReaderListener, 
									 IScannerListener, 
									 IBorrowUIListener {
	
	private ICardReader reader;
	private IScanner scanner; 
	private IPrinter printer; 
	private IDisplay display;
	//private String state;
	private int scanCount = 0;
	private IBorrowUI ui;
	private EBorrowState state; 
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
	
	private List<IBook> bookList;
	private List<ILoan> loanList;
	private IMember borrower;
	
	private JPanel previous;


	
  //===========================================================================
  // Constructor - modified
  //===========================================================================

  public BorrowUC_CTL(ICardReader reader, IScanner scanner, IPrinter printer,
                      IDisplay display, IBookDAO bookDAO, ILoanDAO loanDAO,
                      IMemberDAO memberDAO) {
    
    this.display = display;
    this.reader = reader;
    this.scanner = scanner;
    this.printer = printer;

    this.bookDAO = bookDAO;
    this.loanDAO = loanDAO;
    this.memberDAO = memberDAO;

    this.ui = new BorrowUC_UI(this);

    this.reader.addListener(this);
    this.scanner.addListener(this);

    state = EBorrowState.CREATED;
  }



	public void initialise() {
		previous = display.getDisplay();
		display.setDisplay((JPanel) ui, "Borrow UI");		
	}
	
	public void close() {
		display.setDisplay(previous, "Main Menu");
	}
	


	@Override
	public void cardSwiped(int borrowerId) 
	{
	  if (state != EBorrowState.INITIALIZED)
	  {
	    throw new RuntimeException("BorrowUC_CTL: cardSwiped: cannot call " +
	        "method when state is: " + state);
	  }
	  // memberDAO must exist
	  if (memberDAO == null)
	  {
	    throw new RuntimeException("BorrowUC_CTL: cardSwiped: cannot call " +
	                               "method when memberDAO is null");
	  }
	  
	  // Check whether borrowerId exists in the list of members
    if (memberDAO.getMemberByID(borrowerId) == null)
    {
      throw new RuntimeException("BorrowUC_CTL: cardSwiped: member does not exist");
    }
	   String loanDetails = "";
	  IMember borrower = memberDAO.getMemberByID(borrowerId);
	  
	  // Retrieve the list of current loans for the current borrower
	  loanList = borrower.getLoans();
	  
	  // Initialize scanCount to the number of loans already existing
	  scanCount = loanList.size();

	  if (borrower.getState() == EMemberState.BORROWING_ALLOWED)
	  {
	    // Prevent swiping of another member card
	    reader.setEnabled(false);
	    // Allow scanning of books
	    scanner.setEnabled(true);
	    
	    ui.displayMemberDetails(borrowerId, 
	                            borrower.getFirstName() + " " + borrower.getLastName(), 
                              borrower.getContactPhone());
	    
	    // Display the details of any outstanding loans
	    if (loanList.size() > 0)
	    {
	      String listOfLoans = buildLoanListDisplay(loanList);
	      ui.displayExistingLoan(listOfLoans);
	    }

	    // Display any outstanding fines
	    if (borrower.getTotalFines() > 0)
	    {
	      ui.displayOutstandingFineMessage(borrower.getTotalFines());
	    }
	    setState(EBorrowState.SCANNING_BOOKS);
	  }
	  else
	  {
	    // Prevent scanning of member card
	    reader.setEnabled(false);
	    // Prevent scanning of books
	    scanner.setEnabled(false);

	    ui.displayMemberDetails(borrower.getId(), borrower.getFirstName(), borrower.getContactPhone());
	    
	    // Display any outstanding loans
	    if (loanList.size() > 0)
	    {
	      for (int n = 0; n < loanList.size(); n++)
	      {
	        loanDetails.concat(loanList.get(n).toString() + "\n");
	      }
	      ui.displayExistingLoan(loanDetails);
	    }
	    
	    // Display any outstanding fines
	    if (borrower.getTotalFines() > 0)
	    {
	      ui.displayOutstandingFineMessage(borrower.getTotalFines());
	    }
	    
	    if (borrower.hasOverDueLoans())
	    {
	      ui.displayOverDueMessage();
	    }
	    ui.displayErrorMessage("Borrowing Restricted");
	    setState(EBorrowState.BORROWING_RESTRICTED);
	  }
	}

	
	
	// bookScanned by Josh Kent
	@Override
	public void bookScanned(int barcode) {
	  // Check for valid state
	  if(state != EBorrowState.SCANNING_BOOKS) {
	    throw new RuntimeException("BorrowUC_CTL: bookScanned: method call not "
	                               + "allowed from " + state);
	  }
	  
		// Clear any error message
		ui.displayErrorMessage("");
		
		// Get book
		IBook book = bookDAO.getBookByID(barcode);
		
		// If the book is not found
		if(book == null) {
			ui.displayErrorMessage("Book not found");
			return;
		}
		
		// Check book state
		EBookState bookState = book.getState();
		
		// If the book is not 'Available'
		if(bookState != EBookState.AVAILABLE) {
			ui.displayErrorMessage("Book not available");
			return;
		}
		
		// If the book has already been scanned
		if(bookList.contains(book)) {
			ui.displayErrorMessage("Book already scanned");
			return;
		}
		
		// Add book to bookList
		bookList.add(book);
		// Increment scanCount
		scanCount++;
		
		// Create loan and add to loanList
		ILoan loan = loanDAO.createLoan(borrower, book);
		loanList.add(loan);
		
		// Get bookDetails directly from book
		String bookDetails = book.toString(); 
		// Get loanDetails using buildLoanListDisplay helper
		String loanDetails = buildLoanListDisplay(loanList);
		
		// Display book and loan details
		ui.displayScannedBookDetails(bookDetails);
		ui.displayPendingLoan(loanDetails);
		
		// Check if scan count is at or exceeds LOAN_LIMIT (5)
		if(scanCount >= IMember.LOAN_LIMIT) {
		  // Switch to CONFIRMING_LOANS state
			ui.setState(EBorrowState.CONFIRMING_LOANS);
			setState(EBorrowState.CONFIRMING_LOANS); 
//			state = EBorrowState.CONFIRMING_LOANS; // Substitute for above
			
			// Display confirming loan details
			ui.displayConfirmingLoan(loanDetails);
			
			// Ensure input is disabled
			reader.setEnabled(false);
			scanner.setEnabled(false);
		}
	}

	
	private void setState(EBorrowState state) {
		this.state = state;
	}

	@Override
	public void cancelled() {
		close();
	}
<<<<<<< HEAD

  @Override
  public void scansCompleted() {
    throw new RuntimeException("Not implemented yet");
  }
=======
	
	// scansCompleted by Josh Kent
	@Override
	public void scansCompleted() {
	  // Check for valid state
	  if(state != EBorrowState.SCANNING_BOOKS) {
	     throw new RuntimeException("BorrowUC_CTL: scansCompleted: method call not"
	                               + " allowed from " + state);
	  }
	  
	  // Check loan list contains some loans
	  if(loanList.isEmpty()) {
	    throw new RuntimeException("BorrowUC_CTL: scansCompleted: loan list is "
	                               + "empty");
	  }
	  
	  // Change state
		setState(EBorrowState.CONFIRMING_LOANS);
		ui.setState(EBorrowState.CONFIRMING_LOANS);
		
		// Disable hardware
		reader.setEnabled(false);
		scanner.setEnabled(false);
		
    // Get loanDetails using buildLoanListDisplay helper
    String loanDetails = buildLoanListDisplay(loanList);
    
    // Display confirming loan details
    ui.displayConfirmingLoan(loanDetails);
	}
>>>>>>> origin/development

	@Override
	public void loansConfirmed() {

    if(state != EBorrowState.CONFIRMING_LOANS) {
      throw new RuntimeException("BorrowUC_CTL: loansConfirmed: cannot call " +
                                 "method when state is: " + state);
    }

    if(loanList.isEmpty()) {
      throw new RuntimeException("BorrowUC_CTL: loansConfirmed: cannot call " +
                                 "method when there are no pending loans");
    }

    setState(EBorrowState.COMPLETED);

    for (ILoan loan : loanList) {
      loanDAO.commitLoan(loan);
    }

    String loanDetails = buildLoanListDisplay(loanList);
    printer.print(loanDetails);
    scanner.setEnabled(false);
    reader.setEnabled(false);
    display.setDisplay(previous, "Main Menu");
	}

	@Override
	public void loansRejected() {

    if(state != EBorrowState.CONFIRMING_LOANS) {
      throw new RuntimeException("BorrowUC_CTL: loansConfirmed: cannot call " +
                                 "method when state is: " + state);
    }

    if(loanList.isEmpty()) {
      throw new RuntimeException("BorrowUC_CTL: loansConfirmed: cannot call " +
                                 "method when there are no pending loans");
    }

    // TODO: check this stuff!
    display.setDisplay((JPanel)ui, "Borrow UI");
    ui.setState(EBorrowState.SCANNING_BOOKS);
    setState(EBorrowState.SCANNING_BOOKS);

    // TODO: check this displays member details properly
    int id = borrower.getId();
    String name = borrower.getFirstName() + " " + borrower.getLastName();
    String phone = borrower.getContactPhone();
    ui.displayMemberDetails(id, name, phone);

    List<ILoan> existingLoans = borrower.getLoans();
    String loanDetails = buildLoanListDisplay(existingLoans);
    ui.displayExistingLoan(loanDetails);

    loanList.clear();
    scanCount = existingLoans.size();
    // TODO: cancel button enabled
    // ?
    reader.setEnabled(false);
    scanner.setEnabled(true);
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();
		for (ILoan ln : loans) {
			if (bld.length() > 0) bld.append("\n\n");
			bld.append(ln.toString());
		}
		return bld.toString();		
	}

}
