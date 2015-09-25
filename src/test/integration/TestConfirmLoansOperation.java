package test.integration;

import java.util.List;

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

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;


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

  ICardReader reader = mock(ICardReader.class);
  IScanner scanner = mock(IScanner.class);
  IPrinter printer = mock(IPrinter.class);
  IDisplay display = mock(IDisplay.class);

  IBookHelper bookHelper = new BookHelper();
  IBookDAO books = new BookDAO(bookHelper);

  ILoanHelper loanHelper = new LoanHelper();
  ILoanDAO loans;

  IMemberHelper memberHelper = new MemberHelper();
  IMemberDAO members = new MemberDAO(memberHelper);

  IBook catch22 = books.addBook("CATCH-22", "Joseph Heller", "101.1 [2]");
  IBook emma = books.addBook("Emma", "Jane Austen", "102.5");
  IBook scoop = books.addBook("Scoop", "Evelyn Waugh", "103.21");
  IBook dune = books.addBook("Dune", "Frank Herbert", "104 [21]");
  IBook janeEyre = books.addBook("Jane Eyre", "Charlotte Brontë", "105.2");
  IBook animalFarm = books.addBook("Animal Farm", "George Orwell", "106 [1]");
  IBook ulysses = books.addBook("Ulysses", "James Joyce", "107.345");
  IBook onTheRoad = books.addBook("On the Road", "Jack Kerouac", "108.1");
  IBook dracula = books.addBook("Dracula", "Bram Stoker", "109.1 [21]");
  IBook middlemarch = books.addBook("Middlemarch", "George Eliot", "110");
  IBook hobbit = books.addBook("The Hobbit", "J.R.R. Tolkien", "111.23");
  IBook atonement = books.addBook("Atonement", "Ian McEwan", "112.2 [22]");
  IBook iClaudius = books.addBook("I, Claudius", "Robert Graves", "113 [2]");

  IMember jim = members.addMember("Jim", "Johnson", "", "");
  IMember sam = members.addMember("Sam", "Malone", "", "");
  IMember jill = members.addMember("Jill", "Underhill", "", "");
  IMember bob = members.addMember("Bob", "Dylan", "", "");
  IMember eric = members.addMember("Eric", "Idle", "", "");

  //===========================================================================
  // Test setup of library
  //===========================================================================

  @Test
  public void initialStateOfLibrary()
  {
    loans = new LoanDAO(loanHelper);
    assertThat(books.listBooks()).hasSize(13);
    assertThat(members.listMembers()).hasSize(6);
    assertThat(loans.listLoans()).isEmpty();
  }


  @Test
  public void canAddLoans()
  {
    loans = new LoanDAO(loanHelper);
    ILoan firstLoan = loans.createLoan(eric, iClaudius);
    ILoan secondLoan = loans.createLoan(bob, atonement);
    assertThat(loans.listLoans()).isEmpty();
    loans.commitLoan(firstLoan);
    loans.commitLoan(secondLoan);

    assertThat(loans.listLoans()).hasSize(2);
  }

  //===========================================================================
  // ?
  //===========================================================================


  //===========================================================================
  // ?
  //===========================================================================


  //===========================================================================
  // ?
  //===========================================================================


}
