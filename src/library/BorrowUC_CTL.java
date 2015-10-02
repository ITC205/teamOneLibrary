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



// initialise by Rebecca Callow
public void initialise()
{
  // State must be EBorrowState.CREATED before this method can be called
  if (state != EBorrowState.CREATED)
  {
     throw new RuntimeException("BorrowUC_CTL: initialise: cannot call " +
          "method when state is: " + state);
  }
  // Enable member card reader
  reader.setEnabled(true);
  // Disable book scanner
  scanner.setEnabled(false);
  previous = display.getDisplay();
  display.setDisplay((JPanel) ui, "Borrow UI");
  setState(EBorrowState.INITIALIZED);
  ui.setState(EBorrowState.INITIALIZED);
  bookList = new ArrayList<IBook>();
  loanList = new ArrayList<ILoan>();
}
	
	
	
	public void close() {
		display.setDisplay(previous, "Main Menu");
	}
	

  
  // cardSwiped by Rebecca Callow
  @Override
  public void cardSwiped(int borrowerId) 
  {
    if (state != EBorrowState.INITIALIZED)
    {
      throw new RuntimeException("BorrowUC_CTL: cardSwiped: cannot call " +
          "method when state is: " + state);
    }

    // Check whether borrowerId exists in the list of members
    if (memberDAO.getMemberByID(borrowerId) == null)
    {
      ui.displayErrorMessage("Member: " + borrowerId +" not found");
      return;
    }
    borrower = memberDAO.getMemberByID(borrowerId);
    
    // Retrieve the list of current loans for the current borrower
    List<ILoan> existingLoans = borrower.getLoans();
    
    // Initialize scanCount to the number of loans already existing
    scanCount = existingLoans.size();

    if (!borrower.isRestricted())
    {
      setState(EBorrowState.SCANNING_BOOKS);
      ui.setState(EBorrowState.SCANNING_BOOKS);
      // Prevent swiping of another member card
      reader.setEnabled(false);
      // Allow scanning of books
      scanner.setEnabled(true);

      ui.displayMemberDetails(borrowerId, 
                              borrower.getFirstName() + " " + borrower.getLastName(), 
                              borrower.getContactPhone());
      
      // Display the details of any outstanding loans
      if (existingLoans.size() > 0)
      {
        String listOfLoans = buildLoanListDisplay(existingLoans);
        ui.displayExistingLoan(listOfLoans);
      }

      // Display any outstanding fines
      if (borrower.hasFinesPayable())
      {
        ui.displayOutstandingFineMessage(borrower.getTotalFines());
      }
      ui.displayScannedBookDetails("");
      ui.displayPendingLoan("");
    }
    else
    {
      setState(EBorrowState.BORROWING_RESTRICTED);
      ui.setState(EBorrowState.BORROWING_RESTRICTED);
      // Prevent scanning of member card
      reader.setEnabled(false);
      // Prevent scanning of books
      scanner.setEnabled(false);

      ui.displayMemberDetails(borrowerId, 
                              borrower.getFirstName() + " " + borrower.getLastName(), 
                              borrower.getContactPhone());
      
      // Display any outstanding loans
      // Display the details of any outstanding loans
      if (existingLoans.size() > 0)
      {
        String listOfLoans = buildLoanListDisplay(existingLoans);
        ui.displayExistingLoan(listOfLoans);
      }
      
      // Display any outstanding fines
      if (borrower.hasFinesPayable())
      {
        ui.displayOutstandingFineMessage(borrower.getTotalFines());
      }
      
      if (borrower.hasOverDueLoans())
      {
        ui.displayOverDueMessage();
      }
      if(borrower.hasReachedFineLimit()) 
      {
        ui.displayOverFineLimitMessage(borrower.getTotalFines());
      }
      if(borrower.hasReachedLoanLimit()) 
      {
        ui.displayAtLoanLimitMessage();
      }
      ui.displayErrorMessage("Borrowing Restricted");
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

	// cancelled by Josh Kent
	@Override
	public void cancelled() {
	  // Clear all borrow details for this session
	  bookList.clear();
	  loanList.clear();
	  borrower = null;
	  scanCount = 0;
	  
	  // Disable hardware
	  reader.setEnabled(false);
	  scanner.setEnabled(false);
	  
	  // Switch to CANCELLED state
	  setState(EBorrowState.CANCELLED);
	  ui.setState(EBorrowState.CANCELLED);
	  
	  // Set display to Main Menu
		display.setDisplay(previous, "Main Menu");
	}



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
	    // This can occur if a user clicks Confirm immediately after they have 
	    // just rejected a loan list. Perhaps a better way to handle it is to stop
	    //  method execution at this point with a return statement, rather than 
	    // throw an exception
	    return;
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

    // set state
    setState(EBorrowState.COMPLETED);

    // commit all pending loans
    for (ILoan loan : loanList) {
      loanDAO.commitLoan(loan);
    }

    // print loans details
    String loanDetails = buildLoanListDisplay(loanList);
    printer.print(loanDetails);

    // clear lists of scanned books & loans and borrower
    bookList.clear();
    loanList.clear();
    borrower = null;

    // set hardware ready for next borrower
    scanner.setEnabled(false);
    reader.setEnabled(false);

    // set ui back to main menu
    display.setDisplay(previous, "Main Menu");
	}

	@Override
	public void loansRejected() {

    if(state != EBorrowState.CONFIRMING_LOANS) {
      throw new RuntimeException("BorrowUC_CTL: loansRejected: cannot call " +
                                 "method when state is: " + state);
    }

    if(loanList.isEmpty()) {
      throw new RuntimeException("BorrowUC_CTL: loansRejected: cannot call " +
                                 "method when there are no pending loans");
    }

    // set state and ui back to scanning books
    setState(EBorrowState.SCANNING_BOOKS);
    ui.setState(EBorrowState.SCANNING_BOOKS);

    // display borrower details
    int id = borrower.getId();
    String name = borrower.getFirstName() + " " + borrower.getLastName();
    String phone = borrower.getContactPhone();
    ui.displayMemberDetails(id, name, phone);

    // clear display
    ui.displayPendingLoan("");
    ui.displayScannedBookDetails("");

    // ensure scanCount is correct
    List<ILoan> existingLoans = borrower.getLoans();
    scanCount = existingLoans.size();

    // clear lists of scanned books and loans (but not borrower)
    bookList.clear();
    loanList.clear();

    // set hardware ready for next borrower
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
