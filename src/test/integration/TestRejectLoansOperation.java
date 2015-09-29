package test.integration;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import static test.helper.ControllerReflection.*;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;

import library.BorrowUC_CTL;
import library.interfaces.EBorrowState;

/**
 * Test Reject Loans operation.
 * Rejects the current list of pending loans restarts the scanning process.
 *
 * PreConditions:
 *  - BorrowBookCTL class exists
 *  - Pending loan list exists
 *  - BorrowBookCTL state == CONFIRMING_LOANS
 *
 * PostConditions:
 *  - Scanning panel of BorrowBookUI displayed
 *  - Borrower details displayed
 *  - Existing loan details displayed
 *  - pending loan list is empty
 *  - scan count == number of existing loans
 *  - Cancel button enabled
 *  - cardReader is disabled
 *  - scanner is enabled Borrow
 *  - BookCTL state == SCANNING_BOOKS
 */
public class TestRejectLoansOperation
{
  //===========================================================================
  // Fixtures
  //===========================================================================

  BorrowUC_CTL controller_;

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

  IBook catch22 = spy(books_.addBook("Joseph Heller", "CATCH-22", "101.1 [2]"));
  IBook emma = spy(books_.addBook("Jane Austen", "Emma", "102.5"));
  IBook scoop = spy(books_.addBook("Evelyn Waugh", "Scoop", "103.21"));
  IBook dune = spy(books_.addBook("Frank Herbert", "Dune", "104 [21]"));
  IBook janeEyre = spy(books_.addBook("Charlotte Brontë", "Jane Eyre", "105"));
  IBook animalFarm = spy(books_.addBook("George Orwell", "Animal Farm", "106"));
  IBook ulysses = spy(books_.addBook("James Joyce", "Ulysses", "107.345"));
  IBook onTheRoad = spy(books_.addBook("Jack Kerouac", "On the Road", "108.1"));
  IBook dracula = spy(books_.addBook("Bram Stoker", "Dracula", "109.1 [21]"));
  IBook middlemarch = spy(books_.addBook("George Eliot", "Middlemarch", "110"));
  IBook hobbit = spy(books_.addBook("J.R.R. Tolkien", "The Hobbit", "111.23"));
  IBook atonement = spy(books_.addBook("Ian McEwan", "Atonement", "112 [22]"));
  IBook iClaudius = spy(books_.addBook("Robert Graves", "I, Claudius", "113"));

  IMember jim = spy(members_.addMember("Jim", "Johns", "9123", "j@gmail.com"));
  IMember sam = spy(members_.addMember("Sam", "Malone", "8124", "sam@hoo.com"));
  IMember jill = spy(members_.addMember("Jill", "Hill", "7125", "j@mail.com"));
  IMember bob = spy(members_.addMember("Bob", "Dylan", "6126", "b@dylan.com"));
  IMember eric = spy(members_.addMember("Eric", "Idle", "5127", "ei@life.com"));

  public void initializeController()
  {
    loans_ = spy(new LoanDAO(loanHelper_));
    controller_ = new BorrowUC_CTL(reader_, scanner_, printer_, display_,
                                   books_, loans_, members_);
    setPrivateLoanList(controller_, new ArrayList<>());
  }

  public void setBorrower(IMember borrower)
  {
    setPrivateBorrower(controller_, borrower);
  }

  public void setCount()
  {
    IMember borrower = getPrivateBorrower(controller_);
    List<ILoan> loans = borrower.getLoans();
    int numberLoans = loans.size();
    setPrivateCount(controller_, numberLoans);
  }

  public void setState_ConfirmingLoans()
  {
    setPrivateState(controller_, EBorrowState.CONFIRMING_LOANS);
  }

  public ILoan addToPendingLoans(IBook book)
  {
    IMember borrower = getPrivateBorrower(controller_);
    List<ILoan> pendingLoans = getPrivateLoanList(controller_);
    ILoan  loan = loans_.createLoan(borrower, book);
    pendingLoans.add(loan);
    return loan;
  }

  //===========================================================================
  // Test setup of library
  //===========================================================================

  @Test
  public void preConditionsCanBeMet()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    // assert pre-conditions met
    assertThat(controller_.getClass()).isEqualTo(BorrowUC_CTL.class);
    assertThat(getPrivateBorrower(controller_)).isSameAs(jim);
    assertThat(getPrivateLoanList(controller_)).isNotEmpty();
    assertThat(getPrivateState(controller_)).isEqualTo(EBorrowState
                                            .CONFIRMING_LOANS);
  }



  @Test
  public void loansRejected_throws_whenStateNotConfirmingLoans()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    // no call to setState_ConfirmingLoans()

    try {
      controller_.loansConfirmed();
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
      assertThat(exception).hasMessageContaining("cannot call method when " +
                                                 "state is: ");
    }
  }



  @Test
  public void loansRejected_throws_whenNoPendingLoans()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    // no pending loans added to list
    setState_ConfirmingLoans();

    try {
      controller_.loansConfirmed();
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
      assertThat(exception).hasMessageContaining("cannot call method when " +
                                                 "there are no pending loans");
    }
  }

  //===========================================================================
  // ?
  //===========================================================================

  @Test
  public void rejectLoan_OnePending_NoExistingLoans_PendingListIsEmpty()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();
    List<ILoan> pendingLoans = getPrivateLoanList(controller_);
    assertThat(pendingLoans).containsExactly(firstPendingLoan);
    assertThat(loans_.listLoans().isEmpty());

    controller_.loansRejected();

    assertThat(pendingLoans).isEmpty();
    assertThat(loans_.listLoans().isEmpty());
  }



  @Test
  public void rejectLoan_OnePending_NoExistingLoans_BorrowerRetained()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    controller_.loansRejected();

    IMember borrower = getPrivateBorrower(controller_);
    assertThat(borrower).isSameAs(jim);
  }



  @Test
  public void rejectLoan_OnePending_NoExistingLoans_ScanCountAccurate()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    controller_.loansRejected();

    int scanCount = getPrivateCount(controller_);
    IMember borrower = getPrivateBorrower(controller_);
    List<ILoan> loans = borrower.getLoans();
    int numberOfLoans = loans.size();
    assertThat(scanCount).isEqualTo(numberOfLoans);
  }



  @Test
  public void rejectLoan_OnePending_NoExistingLoans_StateScanningBooks()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    controller_.loansRejected();

    EBorrowState state = getPrivateState(controller_);
    assertThat(state).isEqualTo(EBorrowState.SCANNING_BOOKS);
  }



  @Test
  public void rejectLoan_OnePending_readerCalledCorrectly()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    controller_.loansRejected();

    verify(reader_).setEnabled(false);
  }



  @Test
  public void rejectLoan_OnePending_scannerCalledCorrectly()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    controller_.loansRejected();

    verify(scanner_).setEnabled(true);
  }



  @Test
  public void rejectLoan_OnePending_setDisplayBorrowingBooksCalled()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    controller_.loansRejected();

    verify(display_).setDisplay(anyObject(), anyString());
  }



  // TODO
  @Test
  public void rejectLoan_OnePending_displayBorrowerDetailsCalled()
  {

  }



  // TODO
  @Test
  public void rejectLoan_OnePending_displayLoanDetailsCalled()
  {

  }



  // TODO
  @Test
  public void rejectLoan_OnePending_cancelButtonCalled()
  {

  }

}