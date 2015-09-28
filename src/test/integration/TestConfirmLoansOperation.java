package test.integration;

import java.util.List;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IDisplay;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.EBookState;

import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;

import library.entities.Book;
import library.entities.Loan;
import library.entities.Member;

import library.BorrowUC_CTL;
import library.interfaces.EBorrowState;

import org.junit.Test;
import org.junit.Ignore;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;
import static test.integration.ControllerReflection.*;


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
 *  - All pending loans are committed and recorded
 *  - Loan slip of committed loans printed
 *  - cardReader is disabled
 *  - scanner is disabled
 *  - BorrowBookCTL state == COMPLETED
 */
public class TestConfirmLoansOperation
{
  //===========================================================================
  // Fixtures
  //===========================================================================

  BorrowUC_CTL controller;
  List<ILoan> pendingLoans;

  ICardReader reader = mock(ICardReader.class);
  IScanner scanner = mock(IScanner.class);
  IPrinter printer = mock(IPrinter.class);
  IDisplay display = mock(IDisplay.class);

  IBookHelper bookHelper = new BookHelper();
  IBookDAO books = spy(new BookDAO(bookHelper));

  ILoanHelper loanHelper = new LoanHelper();
  ILoanDAO loans;

  IMemberHelper memberHelper = new MemberHelper();
  IMemberDAO members = spy(new MemberDAO(memberHelper));

  IBook catch22 = spy(books.addBook("Joseph Heller", "CATCH-22", "101.1 [2]"));
  IBook emma = spy(books.addBook("Jane Austen", "Emma", "102.5"));
  IBook scoop = spy(books.addBook("Evelyn Waugh", "Scoop", "103.21"));
  IBook dune = books.addBook("Frank Herbert", "Dune", "104 [21]");
  IBook janeEyre = books.addBook("Charlotte Brontë", "Jane Eyre", "105.2");
  IBook animalFarm = books.addBook("George Orwell", "Animal Farm", "106 [1]");
  IBook ulysses = books.addBook("James Joyce", "Ulysses", "107.345");
  IBook onTheRoad = books.addBook("Jack Kerouac", "On the Road", "108.1");
  IBook dracula = books.addBook("Bram Stoker", "Dracula", "109.1 [21]");
  IBook middlemarch = books.addBook("George Eliot", "Middlemarch", "110");
  IBook hobbit = books.addBook("J.R.R. Tolkien", "The Hobbit", "111.23");
  IBook atonement = spy(books.addBook("Ian McEwan", "Atonement", "112.2 [22]"));
  IBook iClaudius = books.addBook("Robert Graves", "I, Claudius", "113 [2]");

  IMember jim = spy(members.addMember("Jim", "Johnson", "999123", "jim@gmail" +
                                                                      ".com"));
  IMember sam = members.addMember("Sam", "Malone", "888124", "sam@yahoo.com");
  IMember jill = members.addMember("Jill", "Hill", "777125", "jill@gmail.com");
  IMember bob = members.addMember("Bob", "Dylan", "666126", "bob@dylan.com");
  IMember eric = members.addMember("Eric", "Idle", "555127", "eric@life.com");

  public void initializeController()
  {
    loans = spy(new LoanDAO(loanHelper));
    controller = new library.BorrowUC_CTL(reader, scanner, printer, display,
                                          books, loans, members);

  }

  public void setState_ConfirmingLoans()
  {
    setPrivateState(controller, EBorrowState.CONFIRMING_LOANS);
    // other stuff to set up displays etc
  }

public void setPendingLoansEmpty()
{
  List<ILoan> noPendingLoans = new ArrayList<>();
  setPrivateLoanList(controller, noPendingLoans);
}


  //===========================================================================
  // Test setup of library
  //===========================================================================

  @Test
  public void initialStateOfLibrary()
  {
    initializeController();
    assertThat(books.listBooks()).hasSize(13);
    assertThat(members.listMembers()).hasSize(5);
    assertThat(loans.listLoans()).isEmpty();
  }


  @Test
  public void canAddLoans()
  {
    initializeController();
    ILoan firstLoan = loans.createLoan(eric, iClaudius);
    ILoan secondLoan = loans.createLoan(bob, atonement);
    assertThat(loans.listLoans()).isEmpty();
    loans.commitLoan(firstLoan);
    loans.commitLoan(secondLoan);

    assertThat(loans.listLoans()).hasSize(2);
  }


  @Test
  public void preConditionsCanBeMet()
  {
    initializeController();
    setPendingLoansEmpty();
    setState_ConfirmingLoans();

    ILoan firstLoan = loans.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    setPrivateLoanList(controller, pendingLoans);

    // assert pre-conditions met
    assertThat(controller.getClass()).isEqualTo(BorrowUC_CTL.class);
    assertThat(getPrivateState(controller))
        .isEqualTo(EBorrowState.CONFIRMING_LOANS);
    assertThat(getPrivateLoanList(controller)).isNotEmpty();
  }

  @Test
  public void loansConfirmed_throws_whenStateNotConfirmingLoans()
  {
    initializeController();
    setPendingLoansEmpty();
    // no call to setState_ConfirmingLoans()

    ILoan firstLoan = loans.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    setPrivateLoanList(controller, pendingLoans);

    try {
      controller.loansConfirmed();
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
    setPendingLoansEmpty();
    setState_ConfirmingLoans();
    List<ILoan> pendingLoans = getPrivateLoanList(controller);
    assertThat(pendingLoans).isEmpty();

    try {
      controller.loansConfirmed();
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
    setPendingLoansEmpty();
    setState_ConfirmingLoans();
    ILoan firstPendingLoan = loans.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstPendingLoan);
    setPrivateLoanList(controller, pendingLoans);

    assertThat(loans.listLoans().isEmpty());
    assertThat(firstPendingLoan.getID()).isEqualTo(0);

    controller.loansConfirmed();

    assertThat(loans.listLoans()).hasSize(1);
    ILoan firstCommittedLoan = loans.getLoanByID(1);
    assertThat(firstPendingLoan).isSameAs(firstCommittedLoan);
    assertThat(firstPendingLoan.getID()).isEqualTo(1); // not 0
  }



  @Test
  public void confirmLoan_OnePending_NoLoans_DetailsCorrect()
  {
    initializeController();
    setPendingLoansEmpty();
    setState_ConfirmingLoans();
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    Date borrowDate;
    Date dueDate;
    ILoan firstLoan = loans.createLoan(jim, catch22);
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    setPrivateLoanList(controller, pendingLoans);
    assertThat(loans.listLoans()).isEmpty();
    assertThat(firstLoan.getID()).isEqualTo(0);
    assertThat(firstLoan.isCurrent()).isFalse();
    assertThat(catch22.getState()).isEqualTo(EBookState.AVAILABLE);

    controller.loansConfirmed();

    assertThat(loans.listLoans()).hasSize(1);
    ILoan firstCommittedLoan = loans.getLoanByID(1);

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

    assertThat(loans.findLoansByBorrower(jim))
        .containsExactly(firstCommittedLoan);

    assertThat(loans.listLoans())
        .containsExactly(firstCommittedLoan);
  }



  @Test
  public void confirmLoan_OneBook_EmptyLibrary_CorrectCallsMade()
  {
    initializeController();
    setPendingLoansEmpty();
    setState_ConfirmingLoans();
    ILoan firstLoan = spy(loans.createLoan(jim, catch22));
    List<ILoan> pendingLoans = new ArrayList<>();
    pendingLoans.add(firstLoan);
    assertThat(loans.listLoans()).isEmpty();
    assertThat(firstLoan.getID()).isEqualTo(0);
    setPrivateLoanList(controller, pendingLoans);

    controller.loansConfirmed();

    verify(loans).commitLoan(anyObject());
    verify(firstLoan).commit(anyInt());
    verify(jim).addLoan(firstLoan);
    verify(catch22).borrow(firstLoan);
    verify(printer).print(anyString());
    verify(scanner).setEnabled(false);
    verify(reader).setEnabled(false);
    verify(display).setDisplay(any(), anyString());
  }


  @Test
  public void confirmLoan_ThreeBooks_EmptyLibrary_ResultsCorrect()
  {
    initializeController();
    setPendingLoansEmpty();
    setState_ConfirmingLoans();
    IMember borrower = jim;
    List<IBook> bookList = setUpBookList(catch22, emma, atonement);
    List<ILoan> pendingLoans = setUpPendingLoans(borrower, bookList);
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    setPrivateLoanList(controller, pendingLoans);
    Date borrowDate;
    Date dueDate;

    controller.loansConfirmed();

    assertThat(loans.listLoans()).hasSize(3);

    // first loan
    ILoan firstLoan = loans.getLoanByID(1);
    assertThat(firstLoan.getBorrower()).isSameAs(jim);
    assertThat(firstLoan.getBook()).isSameAs(catch22);
    borrowDate = getPrivateBorrowDate((Loan)firstLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)firstLoan);
    assertThat(dueDate).isEqualTo(calculateDueDate(today));
    assertThat(firstLoan.isCurrent()).isTrue();
    assertThat(catch22.getState()).isEqualTo(EBookState.ON_LOAN);

    // second loan
    ILoan secondLoan = loans.getLoanByID(2);
    assertThat(secondLoan.getBorrower()).isSameAs(jim);
    assertThat(secondLoan.getBook()).isSameAs(emma);
    borrowDate = getPrivateBorrowDate((Loan)secondLoan);
    assertThat(borrowDate).isEqualTo(today);
    dueDate = getPrivateDueDate((Loan)secondLoan);
    assertThat(dueDate).isEqualTo(due);
    assertThat(secondLoan.isCurrent()).isTrue();
    assertThat(emma.getState()).isEqualTo(EBookState.ON_LOAN);

    //third loan
    ILoan thirdLoan = loans.getLoanByID(3);
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

    assertThat(loans.findLoansByBorrower(jim))
                    .containsExactly(firstLoan, secondLoan, thirdLoan);

    assertThat(loans.listLoans())
                    .containsExactly(firstLoan, secondLoan, thirdLoan);
  }

  //===========================================================================
  // ?
  //===========================================================================


  //===========================================================================
  // Loan helper
  //===========================================================================

  private List<ILoan> setUpPendingLoans(IMember borrower,
                                        List<IBook> bookList)
  {
    List<ILoan> pendingLoans = new ArrayList<>();
    for (IBook book : bookList) {
      ILoan loan = loans.createLoan(borrower, book);
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
