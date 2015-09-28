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
import static test.helper.LoanReflection.*;

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

import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.entities.Loan;

import library.BorrowUC_CTL;
import library.interfaces.EBorrowState;

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
 */
public class TestConfirmLoansOperation
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
  IBook dune = books_.addBook("Frank Herbert", "Dune", "104 [21]");
  IBook janeEyre = books_.addBook("Charlotte Brontë", "Jane Eyre", "105.2");
  IBook animalFarm = books_.addBook("George Orwell", "Animal Farm", "106 [1]");
  IBook ulysses = books_.addBook("James Joyce", "Ulysses", "107.345");
  IBook onTheRoad = books_.addBook("Jack Kerouac", "On the Road", "108.1");
  IBook dracula = books_.addBook("Bram Stoker", "Dracula", "109.1 [21]");
  IBook middlemarch = books_.addBook("George Eliot", "Middlemarch", "110");
  IBook hobbit = books_.addBook("J.R.R. Tolkien", "The Hobbit", "111.23");
  IBook atonement = spy(books_.addBook("Ian McEwan", "Atonement", "112.2 [22]"));
  IBook iClaudius = books_.addBook("Robert Graves", "I, Claudius", "113 [2]");

  IMember jim = spy(members_.addMember("Jim", "Johnson", "999123", "jim@gmail" +
                                                                      ".com"));
  IMember sam = members_.addMember("Sam", "Malone", "888124", "sam@yahoo.com");
  IMember jill = members_.addMember("Jill", "Hill", "777125", "jill@gmail.com");
  IMember bob = members_.addMember("Bob", "Dylan", "666126", "bob@dylan.com");
  IMember eric = members_.addMember("Eric", "Idle", "555127", "eric@life.com");

  public void initializeController()
  {
    loans_ = spy(new LoanDAO(loanHelper_));
    controller_ = new library.BorrowUC_CTL(reader_, scanner_, printer_, display_,
                                           books_, loans_, members_);
    setPrivateLoanList(controller_, new ArrayList<>());
  }

  public void setState_ConfirmingLoans()
  {
    setPrivateState(controller_, EBorrowState.CONFIRMING_LOANS);
    // other stuff to set up displays etc
  }

  public void setPendingLoans(List<ILoan> pendingLoans)
  {
    setPrivateLoanList(controller_, pendingLoans);
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
    List<ILoan> pendingLoans = new ArrayList<>();
    ILoan firstPendingLoan = loans_.createLoan(jim, catch22);
    pendingLoans.add(firstPendingLoan);
    setPendingLoans(pendingLoans);
    setState_ConfirmingLoans();

    // assert pre-conditions met
    assertThat(controller_.getClass()).isEqualTo(BorrowUC_CTL.class);
    assertThat(getPrivateState(controller_)).isEqualTo(EBorrowState
                                            .CONFIRMING_LOANS);
    assertThat(getPrivateLoanList(controller_)).isNotEmpty();
  }

  @Test
  public void loansConfirmed_throws_whenStateNotConfirmingLoans()
  {
    initializeController();
    // no call to setState_ConfirmingLoans()

    ILoan firstLoan = loans_.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    setPrivateLoanList(controller_, pendingLoans);

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
    setState_ConfirmingLoans();
    List<ILoan> pendingLoans = new ArrayList<>();
    setPendingLoans(pendingLoans);

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
    setState_ConfirmingLoans();
    ILoan firstPendingLoan = loans_.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstPendingLoan);
    setPrivateLoanList(controller_, pendingLoans);

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
    setState_ConfirmingLoans();
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    Date borrowDate;
    Date dueDate;
    ILoan firstLoan = loans_.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    setPrivateLoanList(controller_, pendingLoans);
    assertThat(loans_.listLoans()).isEmpty();
    assertThat(firstLoan.getID()).isEqualTo(0);
    assertThat(firstLoan.isCurrent()).isFalse();
    assertThat(catch22.getState()).isEqualTo(EBookState.AVAILABLE);

    controller_.loansConfirmed();

    assertThat(loans_.listLoans()).hasSize(1);
    ILoan firstCommittedLoan = loans_.getLoanByID(1);

    assertThat(firstCommittedLoan.getBorrower()).isSameAs(jim);
    assertThat(firstCommittedLoan.getBook()).isSameAs(catch22);
    borrowDate = getPrivateBorrowDate((Loan)firstLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)firstLoan);
    assertThat(dueDate).isEqualTo(calculateDueDate(today));
    assertThat(firstLoan.isCurrent()).isTrue();
    assertThat(catch22.getState()).isEqualTo(EBookState.ON_LOAN);

    assertThat(jim.getLoans())
        .containsExactly(firstCommittedLoan);

    assertThat(loans_.findLoansByBorrower(jim))
        .containsExactly(firstCommittedLoan);

    assertThat(loans_.listLoans())
        .containsExactly(firstCommittedLoan);
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
  // ?
  //===========================================================================


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



  private Date ignoreTime(Date date)
  {
    Calendar calendar = Calendar.getInstance();

    calendar.setTime(date);
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
    calendar.set(java.util.Calendar.MINUTE, 0);
    calendar.set(java.util.Calendar.SECOND, 0);
    calendar.set(java.util.Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }



  private Date calculateDueDate(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, ILoan.LOAN_PERIOD);
    return calendar.getTime();
  }

}
