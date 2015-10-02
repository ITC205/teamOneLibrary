package test.integration;

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
 * Test Confirm Loans operation.
 * Completes the borrowing process
 *
 * Pre-conditions:
 *  - BorrowingBookCTL class exists
 *  - Pending loan list exists
 *  - BorrowingBookCTL == CONFIRMING_LOANS
 *
 * Post-conditions:
 *  - Main Menu is displayed
 *  - All pending loans_ are committed and recorded
 *  - Loan slip of committed loans_ printed
 *  - cardReader is disabled
 *  - scanner_ is disabled
 *  - BorrowBookCTL state == COMPLETED
 *
 * @author nicholasbaldwin
 */
public class TestConfirmLoansOperation
{
  //===========================================================================
  // Fixtures
  //===========================================================================

  library.BorrowUC_CTL controller_;
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
    controller_ = spy(new BorrowUC_CTL(reader_, scanner_, printer_, display_,
                                       books_, loans_, members_));

    setPrivateLoanList(controller_, new ArrayList<>());
    setPrivateBookList(controller_, new ArrayList<>());

    ui_ = spy(new library.BorrowUC_UI(controller_));
    setPrivateUI(controller_, ui_);
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
    setPrivateState(controller_, library.interfaces.EBorrowState.CONFIRMING_LOANS);
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
  public void canInitializeControllerWithFixtures()
  {
    initializeController();
    assertThat(books_.listBooks()).isNotEmpty();
    assertThat(members_.listMembers()).isNotEmpty();

    assertThat(loans_.listLoans()).isEmpty();
  }



  @Test
  public void preConditionsCanBeMet()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    // assert pre-conditions met
    assertThat(getPrivateBorrower(controller_)).isSameAs(jim);
    assertThat(getPrivateLoanList(controller_)).isNotEmpty();
    assertThat(getPrivateState(controller_)).isEqualTo(EBorrowState
                                            .CONFIRMING_LOANS);
  }



  @Test
  public void loansConfirmed_throws_whenStateNotConfirmingLoans()
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
  public void loansConfirmed_throws_whenNoPendingLoans()
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
  public void confirmLoan_OnePending_NoLoans_PendingBecomesCommitted()
  {
    initializeController();
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();
    List<ILoan> pendingLoans = getPrivateLoanList(controller_);
    assertThat(pendingLoans).containsExactly(firstPendingLoan);
    assertThat(loans_.listLoans().isEmpty());
    assertThat(firstPendingLoan.getID()).isEqualTo(0);

    controller_.loansConfirmed();

    assertThat(loans_.listLoans()).hasSize(1);
    ILoan firstCommittedLoan = loans_.getLoanByID(1);
    assertThat(firstPendingLoan).isSameAs(firstCommittedLoan);
    assertThat(firstPendingLoan.getID()).isEqualTo(1); // not 0
  }



  @Test
  public void confirmLoan_OnePending_NoLoans_DetailsCorrect()
  {
    initializeController();
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    Date borrowDate;
    Date dueDate;
    setBorrower(jim);
    setCount();
    ILoan firstPendingLoan = addToPendingLoans(catch22);
    setState_ConfirmingLoans();

    assertThat(loans_.listLoans()).isEmpty();
    assertThat(firstPendingLoan.getID()).isEqualTo(0);
    assertThat(firstPendingLoan.isCurrent()).isFalse();
    assertThat(catch22.getState()).isEqualTo(EBookState.AVAILABLE);

    controller_.loansConfirmed();

    assertThat(loans_.listLoans()).hasSize(1);
    ILoan firstCommittedLoan = loans_.getLoanByID(1);

    assertThat(firstCommittedLoan.getBorrower()).isSameAs(jim);
    assertThat(firstCommittedLoan.getBook()).isSameAs(catch22);
    borrowDate = getPrivateBorrowDate((Loan)firstCommittedLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)firstCommittedLoan);
    assertThat(dueDate).isEqualTo(calculateDueDate(today));
    assertThat(firstCommittedLoan.isCurrent()).isTrue();
    assertThat(catch22.getState()).isEqualTo(EBookState.ON_LOAN);

    assertThat(jim.getLoans()).containsExactly(firstCommittedLoan);

    assertThat(loans_.findLoansByBorrower(jim))
                     .containsExactly(firstCommittedLoan);

    assertThat(loans_.listLoans()).containsExactly(firstCommittedLoan);
  }



  @Test
  public void confirmLoan_OneBook_EmptyLibrary_CorrectCallsMade()
  {
    initializeController();
    setState_ConfirmingLoans();
    ILoan firstLoan = spy(loans_.createLoan(jim, catch22));
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    assertThat(loans_.listLoans()).isEmpty();
    assertThat(firstLoan.getID()).isEqualTo(0);
    setPrivateLoanList(controller_, pendingLoans);

    controller_.loansConfirmed();

    verify(loans_).commitLoan(anyObject());
    verify(firstLoan).commit(anyInt());
    verify(jim).addLoan(firstLoan);
    verify(catch22).borrow(firstLoan);
    verify(printer_).print(anyString());
    verify(scanner_).setEnabled(false);
    verify(reader_).setEnabled(false);
    verify(display_).setDisplay(any(), anyString());
  }


  @Test
  public void confirmLoan_ThreeBooks_EmptyLibrary_ResultsCorrect()
  {
    initializeController();
    setState_ConfirmingLoans();
    IMember borrower = jim;
    List<IBook> bookList = setUpBookList(catch22, emma, atonement);
    List<ILoan> pendingLoans = setUpPendingLoans(borrower, bookList);
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    setPrivateLoanList(controller_, pendingLoans);
    Date borrowDate;
    Date dueDate;

    controller_.loansConfirmed();

    assertThat(loans_.listLoans()).hasSize(3);

    // first loan
    ILoan firstLoan = loans_.getLoanByID(1);
    assertThat(firstLoan.getBorrower()).isSameAs(jim);
    assertThat(firstLoan.getBook()).isSameAs(catch22);
    borrowDate = getPrivateBorrowDate((Loan)firstLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)firstLoan);
    assertThat(dueDate).isEqualTo(calculateDueDate(today));
    assertThat(firstLoan.isCurrent()).isTrue();
    assertThat(catch22.getState()).isEqualTo(EBookState.ON_LOAN);

    // second loan
    ILoan secondLoan = loans_.getLoanByID(2);
    assertThat(secondLoan.getBorrower()).isSameAs(jim);
    assertThat(secondLoan.getBook()).isSameAs(emma);
    borrowDate = getPrivateBorrowDate((Loan)secondLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)secondLoan);
    assertThat(dueDate).isEqualTo(due);
    assertThat(secondLoan.isCurrent()).isTrue();
    assertThat(emma.getState()).isEqualTo(EBookState.ON_LOAN);

    //third loan
    ILoan thirdLoan = loans_.getLoanByID(3);
    assertThat(thirdLoan.getBorrower()).isSameAs(jim);
    assertThat(thirdLoan.getBook()).isSameAs(atonement);
    borrowDate = getPrivateBorrowDate((Loan)thirdLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)thirdLoan);
    assertThat(dueDate).isEqualTo(due);
    assertThat(thirdLoan.isCurrent()).isTrue();
    assertThat(atonement.getState()).isEqualTo(EBookState.ON_LOAN);

    assertThat(jim.getLoans())
                  .containsExactly(firstLoan, secondLoan, thirdLoan);

    assertThat(loans_.findLoansByBorrower(jim))
                     .containsExactly(firstLoan, secondLoan, thirdLoan);

    assertThat(loans_.listLoans())
                     .containsExactly(firstLoan, secondLoan, thirdLoan);
  }

  //===========================================================================
  // Loan helpers
  //===========================================================================

  private List<ILoan> setUpPendingLoans(IMember borrower,
                                        List<IBook> bookList)
  {
    List<ILoan> pendingLoans = new ArrayList<>();
    for (IBook book : bookList) {
      ILoan loan = loans_.createLoan(borrower, book);
      pendingLoans.add(loan);
    }
    return pendingLoans;
  }



  private List<IBook> setUpBookList(IBook... booksToBeBorrowed)
  {
    List<IBook> bookList = new ArrayList<>();
    for (IBook book : booksToBeBorrowed) {
      bookList.add(book);
    }
    return bookList;
  }

}
