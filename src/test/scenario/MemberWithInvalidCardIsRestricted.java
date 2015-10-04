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
 * When a member swipes an invalid card, the system displays an error message.
 *
 * See UAT: Member cannot authenticate with an invalid card.
 *
 * Member:
 *  - has 2 existing loans
 *
 * Prior to scenario:
 *  - initialize system
 *  - setup existing loans
 *
 * Scenario starts:
 *  - member swipes invalid card
 *
 * System should:
 *  - display an appropriate error message
 *  - only allow the member to swipe the card again or click cancel
 *
 * Scenario ends (on cancel):
 *  - member clicks cancel
 *  - Main Menu is displayed
 *  - no changes/additions to system state
 *
 * @author nicholasbaldwin
 */
public class MemberWithInvalidCardIsRestricted
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

  // the 2 books he borrowed a while back
  IBook catch22 = spy(books_.addBook("Joseph Heller", "CATCH-22", "101.1 [2]"));
  IBook emma = spy(books_.addBook("Jane Austen", "Emma", "102.5"));

  // the 2 existing loans
  ILoan firstLoan;
  ILoan secondLoan;

  public void setUpExistingLoans(IMember member) throws Exception
  {
    loans_ = spy(new LoanDAO(loanHelper_));

    firstLoan = loans_.createLoan(member, catch22);
    secondLoan = loans_.createLoan(member, emma);
    loans_.commitLoan(firstLoan);
    loans_.commitLoan(secondLoan);


    if (loans_.getLoanByID(1) != firstLoan ||
            loans_.getLoanByID(2) != secondLoan)
    {
      throw new Exception("Loans required for scenario not setup");
    }

    if(member.getLoans().size() != 2) {
      throw new Exception("Member should have 2 loans");
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
  public void memberWithInvalidCardIsRestricted_CheckResults()
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
    assertThat(existingLoans).hasSize(2);

    //=========================================================================
    // Initialization
    //=========================================================================

    initializeController();

    assertThat(loans_.listLoans()).hasSize(2);
    assertThat(jim.getLoans()).hasSize(2);

    //=========================================================================
    // User interaction in scenario starts here
    //=========================================================================

    //-------------------------------------------------------------------------
    // User clicks Borrow
    controller_.initialise();

    //-------------------------------------------------------------------------
    // User swipes card
    controller_.cardSwiped(101);

    assertThat(members_.getMemberByID(101)).isNull();
    assertThat(jim.getId()).isNotEqualTo(101);

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

    assertThat(loans_.listLoans()).hasSize(2);
    assertThat(jim.getLoans()).hasSize(2);
  }



  @Test
  public void memberWithInvalidCardIsRestricted_CheckState()
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
    controller_.cardSwiped(101);

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
  public void memberWithInvalidCardIsRestricted_CheckCalls()
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
    controller_.cardSwiped(101);

    verify(members_, times(1)).getMemberByID(101); // checks null first
    verify(ui_).displayErrorMessage(any());

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

    verify(reader_, times(1)).setEnabled(false);
    verify(scanner_, times(2)).setEnabled(false);
    verify(ui_).setState(EBorrowState.CANCELLED);
    verify(display_, times(2)).setDisplay(any(), anyString());
  }


  @Test
  public void memberWithInvalidCardIsRestricted_CorrectMessages()
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
    controller_.cardSwiped(101);

    verify(ui_).displayErrorMessage("Member: 101 not found");

    //-------------------------------------------------------------------------
    // User clicks cancel
    controller_.cancelled();

  }

}
