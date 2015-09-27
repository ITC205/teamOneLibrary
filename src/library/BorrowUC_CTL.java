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
	  if (memberDAO == null)
	  {
	    throw new RuntimeException("BorrowUC_CTL: cardSwiped: cannot call " +
	                               "method when memberDAO is null");
	  }
	  
	  String loanDetails = "";
	  IMember borrower = memberDAO.getMemberByID(borrowerId);
	  List<ILoan> loanList = borrower.getLoans();
	  scanCount = loanList.size();

	  if (borrower.getState() == EMemberState.BORROWING_ALLOWED)
	  {
	    reader.setEnabled(false);
	    scanner.setEnabled(true);
	    ui.displayMemberDetails(borrower.getId(), borrower.getFirstName(), borrower.getContactPhone());
	    for (int n = 0; n < loanList.size(); n++)
	    {
	      loanDetails.concat(loanList.get(n).toString() + "\n");
	    }
	    ui.displayExistingLoan(loanDetails);

	    if (borrower.getTotalFines() > 0)
	    {
	      ui.displayOutstandingFineMessage(borrower.getTotalFines());
	    }
	    setState(EBorrowState.SCANNING_BOOKS);
	  }
	  else
	  {
	    reader.setEnabled(false);
	    scanner.setEnabled(false);
	    ui.displayMemberDetails(borrower.getId(), borrower.getFirstName(), borrower.getContactPhone());
	    for (int n = 0; n < loanList.size(); n++)
	    {
	      loanDetails.concat(loanList.get(n).toString() + "\n");
	    }
	    ui.displayExistingLoan(loanDetails);
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


	
	@Override
	public void bookScanned(int barcode) {
		throw new RuntimeException("Not implemented yet");
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
