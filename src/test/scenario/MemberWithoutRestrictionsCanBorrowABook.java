package test.scenario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import static test.helper.ControllerReflection.*;
import static test.helper.DateBuilder.*;
import static test.helper.LoanBuilder.*;
import static test.helper.LoanReflection.*;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

import library.BorrowUC_CTL;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.entities.Loan;

/**
 * A member (without restrictions) should be able to authenticate, then scan
 * and borrow a single book.
 * (also see TestScenarioOneJosh - which tests the full scenario where a member
 * without restrictions can scan and loan multiple books until the loan limit is
 * reached).
 * The system sets state to 'borrowing books' which displays member details,
 * existing loan details and scanned book information - and the GUI buttons to
 * complete, confirm, reject or cancel any pending loans.
 *
 * See UAT: Member can borrow a book, when she has no restrictions.
 *
 * Member:
 *  - has no existing loans
 *  - has no fines payable
 *
 * Prior to scenario:
 *  - initialize system
 *
 * Scenario starts:
 *  - member swipes card
 *  - member scans book
 *  - member completes loan
 *  - member confirms loan
 *
 * System should:
 *  - display the member's details
 *  - allow the member to scan books
 *  - display pending loan
 *  - allow the member to complete, confirm, reject or cancel the pending loan
 *
 * Scenario ends:
 *  - member clicks 'complete'
 *  - printer prints out loan slip with loan details
 *  - Main Menu is displayed
 *  - a new loan is added to the system (and member and book records are
 *    updated).
 *
 * @author nicholasbaldwin
 */
public class MemberWithoutRestrictionsCanBorrowABook
{
  //===========================================================================
  // Fixtures
  //===========================================================================

  BorrowUC_CTL controller_;
  IBorrowUI ui_;

  ICardReader reader_ = mock(ICardReader.class);
  IScanner scanner_ = mock(IScanner.class);
  IPrinter printer_ = mock(IPrinter.class);
  IDisplay display_ = mock(IDisplay.class);

  IBookHelper bookHelper_ = new BookHelper();
  IBookDAO books_ = spy(new BookDAO(bookHelper_));

  ILoanHelper loanHelper_ = new LoanHelper();
  ILoanDAO loans_;

  IMemberHelper memberHelper_ = new MemberHelper();
  IMemberDAO members_ = spy(new MemberDAO(memberHelper_));

  // used to check private fields of controller
  List<ILoan> loanList;
  List<IBook> bookList;
  IMember borrower;
  int scanCount;
  EBorrowState state;

  // used to check member's existing loans
  List<ILoan> existingLoans;

  // our friendly member
  IMember jim = spy(members_.addMember("Jim", "Johns", "9123", "j@gmail.com"));

  // the book jim wants to borrow
  IBook animalFarm = spy(books_.addBook("George Orwell", "Animal Farm", "106"));

  // other members
  IMember sam = spy(members_.addMember("Sam", "Malone", "8124", "sam@hoo.com"));
  IMember jill = spy(members_.addMember("Jill", "Hill", "7125", "j@mail.com"));

  // the 5 books that sam and jill have borrowed
  IBook catch22 = spy(books_.addBook("Joseph Heller", "CATCH-22", "101.1 [2]"));
  IBook emma = spy(books_.addBook("Jane Austen", "Emma", "102.5"));
  IBook scoop = spy(books_.addBook("Evelyn Waugh", "Scoop", "103.21"));
  IBook dune = spy(books_.addBook("Frank Herbert", "Dune", "104 [21]"));
  IBook janeEyre = spy(books_.addBook("Charlotte Bronte", "Jane Eyre", "105"));

  // the 5 existing loans (from other 2 members)
  ILoan firstLoan;
  ILoan secondLoan;
  ILoan thirdLoan;
  ILoan fourthLoan;
  ILoan fifthLoan;


  Date today = ignoreTime(new Date());
  Date yesterday = dateBuilder(today, -1);
  Date tomorrow = dateBuilder(today, 1);
  Date borrowed = dateBuilder(yesterday, -(ILoan.LOAN_PERIOD));


  public void setUpExistingLoans(IMember firstMember, IMember secondMember)
    throws Exception
  {
    loans_ = spy(new LoanDAO(loanHelper_));

    firstLoan = loans_.createLoan(firstMember, catch22);
    secondLoan = loans_.createLoan(firstMember, emma);
    thirdLoan = loans_.createLoan(secondMember, scoop);
    fourthLoan = loans_.createLoan(secondMember, dune);
    fifthLoan = loans_.createLoan(secondMember, janeEyre);
    loans_.commitLoan(firstLoan);
    loans_.commitLoan(secondLoan);
    loans_.commitLoan(thirdLoan);
    loans_.commitLoan(fourthLoan);
    loans_.commitLoan(fifthLoan);

    setPrivateBorrowDate((Loan)firstLoan, borrowed);
    setPrivateDueDate((Loan)firstLoan, yesterday);
    setPrivateBorrowDate((Loan)secondLoan, borrowed);
    setPrivateDueDate((Loan)secondLoan, yesterday);
    setPrivateBorrowDate((Loan)thirdLoan, borrowed);
    setPrivateDueDate((Loan)thirdLoan, yesterday);
    setPrivateBorrowDate((Loan)fourthLoan, borrowed);
    setPrivateDueDate((Loan)fourthLoan, yesterday);
    setPrivateBorrowDate((Loan)fifthLoan, borrowed);
    setPrivateDueDate((Loan)fifthLoan, yesterday);

    loans_.updateOverDueStatus(new Date());

    if (loans_.getLoanByID(1) != firstLoan ||
        loans_.getLoanByID(2) != secondLoan ||
        loans_.getLoanByID(3) != thirdLoan ||
        loans_.getLoanByID(4) != fourthLoan ||
        loans_.getLoanByID(5) != fifthLoan)
    {
      throw new Exception("Loans required for scenario not setup");
    }

    if(loans_.listLoans().size() != 5) {
      throw new Exception("Loans required for scenario not setup correctly");
    }
    if(firstMember.getLoans().size() != 2) {
      throw new Exception("Member should have 2 loans");
    }
    if(secondMember.getLoans().size() != 3) {
      throw new Exception("Member should have 2 loans");
    }

  }


  public void initializeController()
  {
    controller_ = spy(new BorrowUC_CTL(reader_, scanner_, printer_, display_,
                                       books_, loans_, members_));

    ui_ = spy(new library.BorrowUC_UI(controller_));
    setPrivateUI(controller_, ui_);
  }


  @Test
  public void noRestrictions_MemberCanBorrowBook_CheckResults()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(sam, jill);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    //=========================================================================
    // Initialization
    //=========================================================================

    initializeController();

    assertThat(loans_.listLoans()).hasSize(5);
    assertThat(loans_.listLoans()).containsExactly(firstLoan, secondLoan,
                                                   thirdLoan, fourthLoan,
                                                   fifthLoan);

    assertThat(sam.getLoans()).containsExactly(firstLoan, secondLoan);
    assertThat(jill.getLoans()).containsExactly(thirdLoan, fourthLoan,
                                                fifthLoan);

    assertThat(jim.getLoans()).isEmpty();
    assertThat(jim.isRestricted()).isFalse();

    //=========================================================================
    // User interaction in scenario starts here
    //=========================================================================

    //-------------------------------------------------------------------------
    // User clicks Borrow
    controller_.initialise();

    assertThat(loans_.listLoans()).hasSize(5); // no changes yet

    //-------------------------------------------------------------------------
    // User swipes card
    controller_.cardSwiped(jim.getId());

    assertThat(loans_.listLoans()).hasSize(5); // no changes yet

    //-------------------------------------------------------------------------
    // User scans book
    controller_.bookScanned(animalFarm.getID());

    assertThat(loans_.listLoans()).hasSize(5); // no changes yet

    //-------------------------------------------------------------------------
    // User completes pending loan
    controller_.scansCompleted();

    assertThat(loans_.listLoans()).hasSize(5); // no changes yet

    //-------------------------------------------------------------------------
    // User confirms pending loan
    controller_.loansConfirmed();

    assertThat(loans_.listLoans()).hasSize(6); // new loan added

    ILoan newLoan = loans_.getLoanByID(6);
    assertThat(loans_.listLoans()).containsExactly(firstLoan, secondLoan,
                                                   thirdLoan, fourthLoan,
                                                   fifthLoan, newLoan);

    assertThat(sam.getLoans()).containsExactly(firstLoan, secondLoan);
    assertThat(jill.getLoans()).containsExactly(thirdLoan, fourthLoan,
                                                fifthLoan);

    assertThat(newLoan.getBook().toString()).isEqualTo(animalFarm.toString());
    assertThat(newLoan.getBorrower().toString()).isEqualTo(jim.toString());
    assertThat(jim.getLoans()).containsExactly(newLoan);
    assertThat(jim.isRestricted()).isFalse();
  }



  @Test
  public void noRestrictions_MemberCanBorrowBook_CheckState()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(sam, jill);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(2);

    //=========================================================================
    // Initialization
    //=========================================================================

    initializeController();

    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.CREATED);
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower).isNull();
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isNull();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isNull();
    scanCount = getPrivateCount(controller_);
    assertThat(scanCount).isEqualTo(0);

    //=========================================================================
    // User interaction in scenario starts here
    //=========================================================================

    //-------------------------------------------------------------------------
    // User clicks Borrow
    controller_.initialise();

    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.INITIALIZED);
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower).isNull();
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isEmpty();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isEmpty();
    scanCount = getPrivateCount(controller_);
    assertThat(scanCount).isEqualTo(0);

    //-------------------------------------------------------------------------
    // User swipes card
    controller_.cardSwiped(jim.getId());

    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.BORROWING_RESTRICTED);
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower.toString()).isEqualTo(jim.toString());
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isEmpty();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isEmpty();
    scanCount = getPrivateCount(controller_);
    assertThat(scanCount).isEqualTo(2);

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.CANCELLED);
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower).isNull();
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isEmpty();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isEmpty();
    scanCount = getPrivateCount(controller_);
    assertThat(scanCount).isEqualTo(0);
  }


  @Test
  public void noRestrictions_MemberCanBorrowBook_CheckCalls()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(sam, jill);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(2);

    verify(jim, atLeastOnce()).addLoan(any());

    //=========================================================================
    // Initialization
    //=========================================================================

    initializeController();

    //=========================================================================
    // User interaction in scenario starts here
    //=========================================================================

    //-------------------------------------------------------------------------
    // User clicks Borrow
    controller_.initialise();

    verify(reader_).setEnabled(true);
    verify(scanner_).setEnabled(false);
    verify(display_).getDisplay();
    verify(display_).setDisplay(any(), anyString());
    verify(ui_).setState(EBorrowState.INITIALIZED);

    //-------------------------------------------------------------------------
    // User swipes card
    controller_.cardSwiped(jim.getId());

    verify(members_, times(2)).getMemberByID(jim.getId()); // checks null first
    verify(jim, times(3)).getLoans(); // setup invokes this
    verify(jim).isRestricted();
    verify(ui_).setState(EBorrowState.BORROWING_RESTRICTED);
    verify(reader_).setEnabled(false);
    verify(scanner_, times(2)).setEnabled(false);
    verify(jim, times(2)).getId();
    verify(jim, atLeastOnce()).getFirstName();
    verify(jim, atLeastOnce()).getLastName();
    verify(ui_).displayMemberDetails(anyInt(), anyString(), anyString());
    verify(ui_).displayExistingLoan(any());
    verify(jim).hasOverDueLoans();
    verify(ui_).displayOverDueMessage();
    verify(jim).hasReachedFineLimit();
    verify(jim, atLeastOnce()).hasReachedLoanLimit();
    verify(ui_).displayErrorMessage(any());

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

    verify(reader_, times(2)).setEnabled(false);
    verify(scanner_, times(3)).setEnabled(false);
    verify(ui_).setState(EBorrowState.CANCELLED);
    verify(display_, times(2)).setDisplay(any(), anyString());
  }


  @Test
  public void noRestrictions_MemberCanBorrowBook_CorrectMessages()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(sam, jill);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(2);

    verify(jim, atLeastOnce()).addLoan(any());

    //=========================================================================
    // Initialization
    //=========================================================================

    initializeController();

    //=========================================================================
    // User interaction in scenario starts here
    //=========================================================================

    //-------------------------------------------------------------------------
    // User clicks Borrow
    controller_.initialise();

    //-------------------------------------------------------------------------
    // User swipes card
    controller_.cardSwiped(jim.getId());

    verify(ui_).displayMemberDetails(1, "Jim Johns", "9123");
    verify(ui_).displayExistingLoan("Loan ID:  1\n" +
                                        "Author:   Joseph Heller\n" +
                                        "Title:    CATCH-22\n" +
                                        "Borrower: Jim Johns\n" +
                                        "Borrowed: " +
                                        formattedDate(borrowed) + "\n" +
                                        "Due Date: " +
                                        formattedDate(yesterday) + "\n" +
                                        "\n" +
                                        "Loan ID:  2\n" +
                                        "Author:   Jane Austen\n" +
                                        "Title:    Emma\n" +
                                        "Borrower: Jim Johns\n" +
                                        "Borrowed: " +
                                        formattedDate(borrowed) + "\n" +
                                        "Due Date: " +
                                        formattedDate(yesterday));
    verify(ui_).displayErrorMessage("Borrowing Restricted");

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

  }

}
