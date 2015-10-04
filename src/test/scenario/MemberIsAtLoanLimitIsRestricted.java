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
 * When a member has reached the loan limit, and tries to borrow more books, the
 * system sets state to 'borrowing restricted' which displays member and loan
 * information only.
 *
 * See UAT: Member cannot scan or borrow books when status is restricted (due to
 * reaching loan limit).
 *
 * Member:
 *  - has 5 existing loans
 *  - all loans are current
 *  - no fines payable
 *
 * Prior to scenario:
 *  - initialize system
 *  - setup existing loans
 *
 * Scenario starts:
 *  - member swipes card
 *
 * System should:
 *  - display the member's details & current loans
 *  - only allow the member to click cancel
 *
 * Scenario ends:
 *  - member clicks cancel
 *  - Main Menu is displayed
 *  - no changes/additions to system state
 *
 * @author nicholasbaldwin
 */
public class MemberIsAtLoanLimitIsRestricted
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

  // the 5 books he borrowed a while back
  IBook catch22 = spy(books_.addBook("Joseph Heller", "CATCH-22", "101.1 [2]"));
  IBook emma = spy(books_.addBook("Jane Austen", "Emma", "102.5"));
  IBook scoop = spy(books_.addBook("Evelyn Waugh", "Scoop", "103.21"));
  IBook dune = spy(books_.addBook("Frank Herbert", "Dune", "104 [21]"));
  IBook janeEyre = spy(books_.addBook("Charlotte Bronte", "Jane Eyre", "105"));

  // the 5 existing loans
  ILoan firstLoan;
  ILoan secondLoan;
  ILoan thirdLoan;
  ILoan fourthLoan;
  ILoan fifthLoan;

  Date today = ignoreTime(new Date());
  Date yesterday = dateBuilder(today, -1);
  Date tomorrow = dateBuilder(today, 1);
  Date borrowed = dateBuilder(tomorrow, -(ILoan.LOAN_PERIOD));


  public void setUpExistingLoans(IMember member) throws Exception
  {
    loans_ = spy(new LoanDAO(loanHelper_));

    firstLoan = loans_.createLoan(member, catch22);
    secondLoan = loans_.createLoan(member, emma);
    thirdLoan = loans_.createLoan(member, scoop);
    fourthLoan = loans_.createLoan(member, dune);
    fifthLoan = loans_.createLoan(member, janeEyre);
    loans_.commitLoan(firstLoan);
    loans_.commitLoan(secondLoan);
    loans_.commitLoan(thirdLoan);
    loans_.commitLoan(fourthLoan);
    loans_.commitLoan(fifthLoan);

    setPrivateBorrowDate((Loan)firstLoan, borrowed);
    setPrivateDueDate((Loan)firstLoan, tomorrow);
    setPrivateBorrowDate((Loan)secondLoan, borrowed);
    setPrivateDueDate((Loan)secondLoan, tomorrow);
    setPrivateBorrowDate((Loan)thirdLoan, borrowed);
    setPrivateDueDate((Loan)thirdLoan, tomorrow);
    setPrivateBorrowDate((Loan)fourthLoan, borrowed);
    setPrivateDueDate((Loan)fourthLoan, tomorrow);
    setPrivateBorrowDate((Loan)fifthLoan, borrowed);
    setPrivateDueDate((Loan)fifthLoan, tomorrow);

    loans_.updateOverDueStatus(new Date());

    if (loans_.getLoanByID(1) != firstLoan ||
        loans_.getLoanByID(2) != secondLoan ||
        loans_.getLoanByID(3) != thirdLoan ||
        loans_.getLoanByID(4) != fourthLoan ||
        loans_.getLoanByID(5) != fifthLoan)
    {
      throw new Exception("Loans required for scenario not setup");
    }

    if (!firstLoan.isCurrent() || !secondLoan.isCurrent() ||
        !thirdLoan.isCurrent() || !fourthLoan.isCurrent() ||
        !fifthLoan.isCurrent()) {
      throw new Exception("Loans for scenario should be current with due " +
                          "date set to tomorrow");
    }

    if(member.getLoans().size() != 5) {
      throw new Exception("Member should have 5 loans");
    }

    if (!member.isRestricted()) {
      throw new Exception("Member should be restricted");
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
  public void memberAtLoanLimitIsRestricted_CheckResults()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(jim);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(5);

    //=========================================================================
    // Initialization
    //=========================================================================

    initializeController();

    assertThat(loans_.listLoans()).hasSize(5);
    assertThat(loans_.getLoanByID(1)).isSameAs(jim.getLoans().get(0));
    assertThat(loans_.getLoanByID(2)).isSameAs(jim.getLoans().get(1));
    assertThat(loans_.getLoanByID(3)).isSameAs(jim.getLoans().get(2));
    assertThat(loans_.getLoanByID(4)).isSameAs(jim.getLoans().get(3));
    assertThat(loans_.getLoanByID(5)).isSameAs(jim.getLoans().get(4));
    assertThat(jim.getLoans()).hasSize(5);
    assertThat(jim.getLoans().get(0).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(1).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(2).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(3).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(4).isCurrent()).isTrue();
    assertThat(jim.isRestricted()).isTrue();

    //=========================================================================
    // User interaction in scenario starts here
    //=========================================================================

    //-------------------------------------------------------------------------
    // User clicks Borrow
    controller_.initialise();

    //-------------------------------------------------------------------------
    // User swipes card
    controller_.cardSwiped(jim.getId());

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

    assertThat(loans_.listLoans()).hasSize(5);
    assertThat(loans_.getLoanByID(1)).isSameAs(jim.getLoans().get(0));
    assertThat(loans_.getLoanByID(2)).isSameAs(jim.getLoans().get(1));
    assertThat(loans_.getLoanByID(3)).isSameAs(jim.getLoans().get(2));
    assertThat(loans_.getLoanByID(4)).isSameAs(jim.getLoans().get(3));
    assertThat(loans_.getLoanByID(5)).isSameAs(jim.getLoans().get(4));
    assertThat(jim.getLoans()).hasSize(5);
    assertThat(jim.getLoans().get(0).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(1).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(2).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(3).isCurrent()).isTrue();
    assertThat(jim.getLoans().get(4).isCurrent()).isTrue();
    assertThat(jim.isRestricted()).isTrue();
  }



  @Test
  public void memberAtLoanLimitIsRestricted_CheckState()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(jim);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(5);

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
    assertThat(scanCount).isEqualTo(5);

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
  public void memberAtLoanLimitIsRestricted_CheckCalls()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(jim);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(5);

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
    verify(jim, times(3)).getLoans(); // called in setup

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
    verify(jim).hasReachedFineLimit();
    verify(jim, atLeastOnce()).hasReachedLoanLimit(); // setup calls indirectly
    verify(ui_).displayAtLoanLimitMessage();
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
  public void memberAtLoanLimitIsRestricted_CorrectMessages()
  {
    //=========================================================================
    // Set up data
    //=========================================================================

    existingLoans = jim.getLoans();
    assertThat(existingLoans).isEmpty();

    try {
      setUpExistingLoans(jim);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }

    existingLoans = jim.getLoans();
    assertThat(existingLoans).hasSize(5);

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
                                    formattedDate(tomorrow) + "\n" +
                                    "\n" +

                                    "Loan ID:  2\n" +
                                    "Author:   Jane Austen\n" +
                                    "Title:    Emma\n" +
                                    "Borrower: Jim Johns\n" +
                                    "Borrowed: " +
                                    formattedDate(borrowed) + "\n" +
                                    "Due Date: " +
                                    formattedDate(tomorrow)+ "\n" +
                                    "\n" +

                                    "Loan ID:  3" + "\n" +
                                    "Author:   Evelyn Waugh" + "\n" +
                                    "Title:    Scoop" + "\n" +
                                    "Borrower: Jim Johns" + "\n" +
                                    "Borrowed: " +
                                    formattedDate(borrowed) + "\n" +
                                    "Due Date: " +
                                    formattedDate(tomorrow)+ "\n" +
                                    "\n" +

                                    "Loan ID:  4" + "\n" +
                                    "Author:   Frank Herbert" + "\n" +
                                    "Title:    Dune" + "\n" +
                                    "Borrower: Jim Johns" + "\n" +
                                    "Borrowed: " +
                                    formattedDate(borrowed) + "\n" +
                                    "Due Date: " +
                                    formattedDate(tomorrow)+ "\n" +
                                    "\n" +

                                    "Loan ID:  5" + "\n" +
                                    "Author:   Charlotte Bronte" + "\n" +
                                    "Title:    Jane Eyre" + "\n" +
                                    "Borrower: Jim Johns" + "\n" +
                                    "Borrowed: " +
                                    formattedDate(borrowed) + "\n" +
                                    "Due Date: " +
                                    formattedDate(tomorrow));
    verify(ui_).displayErrorMessage("Borrowing Restricted");

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

  }

}
