package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
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
	public void cardSwiped(int memberID) {
		throw new RuntimeException("Not implemented yet");
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
//			setState(EBorrowState.CONFIRMING_LOANS); WAITING FOR IMPLEMENTATION
			state = EBorrowState.CONFIRMING_LOANS; // Substitute for above
			
			// Display confirming loan details
			ui.displayConfirmingLoan(loanDetails);
			
			// Ensure input is disabled
			reader.setEnabled(false);
			scanner.setEnabled(false);
		}
	}

	
	private void setState(EBorrowState state) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void cancelled() {
		close();
	}
	
	@Override
	public void scansCompleted() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansConfirmed() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansRejected() {
		throw new RuntimeException("Not implemented yet");
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
