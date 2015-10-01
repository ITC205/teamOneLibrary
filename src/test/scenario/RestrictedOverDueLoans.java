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
 * Test a simple Borrowing Restricted scenario
 *
 * Member:
 *  - has 2 existing loans
 *  - both loans are overdue
 *  - has fines payable (due to overdue loans)
 *  - has not reached the fine limit
 *
 * Prior to scenario:
 *  - initialize system
 *  - setup existing loans
 *
 * Scenario starts
 *  - member swipes card
 *  - display shows overdue loans
 *  - member clicks cancel
 *
 * Scenario ends:
 *  - Main Menu is displayed
 *  - no changes/additions to system state
 *
 * @author nicholasbaldwin
 */
public class RestrictedOverDueLoans
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
  ILoanDAO loans_ = spy(new LoanDAO(loanHelper_));

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

  // the 2 exisiting loans
  ILoan firstLoan;
  ILoan secondLoan;


  public void setUpExistingLoans(IMember member) throws Exception
  {
    Date today = ignoreTime(new Date());
    Date yesterday = dateBuilder(today, -1);
    Date borrowed = dateBuilder(yesterday, -(ILoan.LOAN_PERIOD));

    firstLoan = loans_.createLoan(member, catch22);
    secondLoan = loans_.createLoan(member, emma);
    loans_.commitLoan(firstLoan);
    loans_.commitLoan(secondLoan);

    setPrivateBorrowDate((Loan)firstLoan, borrowed);
    setPrivateDueDate((Loan)firstLoan, yesterday);
    setPrivateBorrowDate((Loan)secondLoan, borrowed);
    setPrivateDueDate((Loan)secondLoan, yesterday);

    loans_.updateOverDueStatus(new Date());

    if (loans_.getLoanByID(1) != firstLoan || loans_.getLoanByID(2) != secondLoan)
    {
      throw new Exception("Loans required for scenario not setup");
    }

    if (!firstLoan.isOverDue() || !firstLoan.isOverDue()) {
      throw new Exception("Loans for scenario should have due date set to yesterday");
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
  public void borrowingRestricted_MemberHasOverDueLoans_CheckState()
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

    //=========================================================================
    // Scenario
    //=========================================================================

    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.CREATED);
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower).isNull();
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isNull();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isNull();

    controller_.initialise();

    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.INITIALIZED);
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower).isNull();
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isEmpty();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isEmpty();

    controller_.cardSwiped(jim.getId());

    // BROKEN TEST ============================================================
    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.BORROWING_RESTRICTED);
    // ========================================================================
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower.toString()).isEqualTo(jim.toString());
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isEmpty();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isEmpty();

    controller_.cancelled();

    // BROKEN TEST ============================================================
    state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.CANCELLED);
    // ========================================================================
    borrower = getPrivateBorrower(controller_);
    assertThat(borrower.toString()).isEqualTo(jim.toString());
    loanList = getPrivateLoanList(controller_);
    assertThat(loanList).isEmpty();
    bookList = getPrivateBookList(controller_);
    assertThat(bookList).isEmpty();
  }

}
