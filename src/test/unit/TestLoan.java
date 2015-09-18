package test.unit;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static test.unit.TestLoanBuilder.*;

/**
 * Unit tests for Loan entity.
 */
public class TestLoan
{
  // ==========================================================================
  // Stub helpers
  // ==========================================================================

  /**
   * Create stub Book with Mockito. Simply provides an explicitly named method
   * to show that stubs are being used (not mocks or other fakes/doubles) :-)
   * @return stub Book.
   */
  public static IBook stubBook()
  {
    return mock(IBook.class);
  }



  /**
   * Create stub Member with Mockito. Simply provides an explicitly named method
   * to show that stubs are being used (not mocks or other fakes/doubles) :-)
   * @return stub Member.
   */
  public static IMember stubMember()
  {
    return mock(IMember.class);
  }

  // ==========================================================================
  // Test rules
  // ==========================================================================

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  // ==========================================================================
  // Constructor Testing - with stubs
  // ==========================================================================

  @Test
  public void constructNewLoan()
  {
      // Given stubs for book and member
      IBook book = stubBook();
      IMember borrower = stubMember();
      // With valid, but very simple dates in millis
      Date borrowDate = new Date(1);
      Date dueDate = new Date(2);
      // and valid ID
      int id = 1;

      // When create a loan
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

      // Then can (possibly naively) check that loan was created
      assertTrue(loan instanceof ILoan);
  }



  @Test
  public void constructNewLoanWithNullBookThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given null book and stub for member
    IBook book = null;
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    // and valid ID
    int id = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructNewLoanWithNullBorrowerThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stub for book and null member
    IBook book = stubBook();
    IMember borrower = null;
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    // and valid ID
    int id = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructNewLoanWithNullBorrowDateThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With null borrowDate
    Date borrowDate = null;
    Date dueDate = new Date(2);
    // and valid ID
    int id = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructNewLoanWithNullDueDateThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With null due date
    Date borrowDate = new Date(1);
    Date dueDate = null;
    // and valid ID
    int id = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void checkExceptionMessageWhenBookNull()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Cannot create a new Loan with a null Book.");

    // Given null book and stub for member
    IBook book = null;
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    // and valid ID
    int id = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructNewLoanWithValidDates()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with formatted dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and valid ID
    int id = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("18/09/2015");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then can (naively) check that loan was created
    assertTrue(loan instanceof ILoan);
  }



  @Test
  public void constructNewLoanDueDateBeforeBorrowDateThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with formatted dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and valid ID
    int id = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      // dueDate is before the borrowDate
      dueDate = dateFormat.parse("16/09/2015");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructNewLoanDueDateSameAsBorrowDateThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with formatted dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and valid ID
    int id = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      // dueDate is same as the borrowDate
      dueDate = dateFormat.parse("17/09/2015");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void checkExceptionMessageWhenInvalidDueDate()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Cannot create a new Loan when the Return Date is " +
                         "before or the same as the Borrowing Date.");

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with formatted dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and valid ID
    int id = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      // dueDate is before the borrowDate
      dueDate = dateFormat.parse("16/09/2015");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }

  // TODO: need to check if borrow date later than return date - but same day?


  @Test
  public void constructNewLoanIDEqualsZeroThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with valid dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and ID equals zero
    int id = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("01/01/2015");
      dueDate = dateFormat.parse("01/01/2025");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructNewLoanIDNegativeThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with valid dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and negative ID
    int id = -1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("01/01/2015");
      dueDate = dateFormat.parse("01/01/2025");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void checkExceptionMessageWhenInvalidID()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Cannot create a new Loan with an ID less than or " +
                             "equal to zero.");

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with valid dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;
    // and negative ID
    int id = -1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("01/01/2015");
      dueDate = dateFormat.parse("01/01/2025");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
  }



  @Test
  public void constructInvalidLoanDoesNotInstantiate()
  {
    ILoan loan = null;
    try {
      // Given stubs for book and member
      IBook book = stubBook();
      IMember borrower = stubMember();
      // with valid dates to be assigned
      Date borrowDate = new Date(1);
      Date dueDate = new Date(2);
      // and negative ID
      int id = -1;

      // When create a loan, exception will be thrown
       loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }
    catch (IllegalArgumentException exception) {
      // verify loan remains null
      assertTrue(loan == null);
      assertFalse(loan instanceof ILoan);
    }
  }

  // ==========================================================================
  // Getters & setters testing - with stubs
  // ==========================================================================

  @Test
  public void getBorrowerFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();

    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    int id = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then can return borrower and verify it is same Member as local instance
    IMember loanBorrower = loan.getBorrower();
    assertSame(loanBorrower, borrower);
  }



  @Test
  public void getBorrowerFirstNameFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    String borrowerFirstName = "Jim";
    when(borrower.getFirstName()).thenReturn(borrowerFirstName);

    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    int id = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then can return borrower and verify it is same Member as local instance
    IMember loanBorrower = loan.getBorrower();
    String loanBorrowerFirstName = loanBorrower.getFirstName();
    assertSame(loanBorrower, borrower);
    assertEquals(loanBorrowerFirstName, borrowerFirstName);
  }



  @Test
  public void getBookFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    String author = "Charles Dickens";
    when(book.getAuthor()).thenReturn(author);
    IMember borrower = stubMember();

    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    int id = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then can return book and verify it is same Member as local instance
    IBook loanBook = loan.getBook();
    String loanBookAuthor = loanBook.getAuthor();
    assertSame(loanBook, book);
    assertEquals(loanBookAuthor, author);
  }



  @Test
  public void getID()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    int id = 125;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then can return ID
    int loanID = loan.getID();
    assertEquals(loanID, id);
  }

  // ==========================================================================
  // Primary methods testing - with stubs, TestLoanBuilder & dateBuilder
  // ==========================================================================

  @Test
  public void testOverDueLoan()
  {
  // Given a manually set overdue loan
    ILoan loan = newLoan().makeOverDue().build();

    // Then loan state should be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertTrue(isStateOverDue);
  }



  @Test
  public void testNewLoanIsNotOverDue()
  {
    // Given a default loan (state is not overdue)
    ILoan loan = newLoan().build();

    // Then loan state should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertFalse(isStateOverDue);
  }



  @Test
  public void testCurrentLoanIsNotOverDue()
  {
    // Given a loan with a manually set status of current
    ILoan loan = newLoan().makeCurrent().build();

    // Then loan state should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertFalse(isStateOverDue);
  }

  // TODO: check status of items after isOverdue() changes status etc

  @Test
  public void testHundredYearLoanIsNotOverDue()
  {
    // Given a new 100 year loan!
    ILoan loan = newLoan().withBorrowDate(1,0,2010)
                          .withDueDate(1,0,2110)
                          .makeCurrent().build();
    Date today = new Date();
    // Then loan should not be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertFalse(isOverDue);
  }



  @Test
  public void testLoanDueNextDayIsNotOverDue()
  {
    // Given a new loan with due date 1st January
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(1,0,2016)
                          .makeCurrent().build();
    // When today's date is 31st December
    Date today = dateBuilder(31,11,2015);
    // Then loan should not be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertFalse(isOverDue);
  }



  @Test
  public void testLoanDueSameDayIsNotOverDue()
  {
    // Given a new loan with due date 1st January
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(1,0,2016)
                          .makeCurrent().build();
    // When today's date is 1st January
    Date today = dateBuilder(31,11,2015);
    // Then loan should not be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertFalse(isOverDue);
  }



  @Test
  public void testLoanDuePreviousDayIsOverDue()
  {
    // Given a new loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
                          .makeCurrent().build();
    // When today's date is 1st January
    Date today = dateBuilder(1,1,2016);
    // Then loan should be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertTrue(isOverDue);
  }


  @Test
  public void testLoanDuePreviousWeekIsOverDue()
  {
    // Given a new loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(25,11,2015)
                          .makeOverDue().build();
    // When today's date is 1st January
    Date today = dateBuilder(1,1,2016);
    // Then loan should be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertTrue(isOverDue);
  }




  @Test
  public void testLoanNotDueWithCurrentStateDoesNotChangeToOverDue()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();
    // When today's date is 31st December
    Date today = dateBuilder(31,11,2015);
    // Then loan status should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertFalse(isStateOverDue);
    // and loan should not be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertFalse(isOverDue);
    // and loan state should not be overdue now (after checkOverDue)
    isStateOverDue = loan.isOverDue();
    assertFalse(isStateOverDue);
  }



  @Test
  public void testLoanDueWithCurrentStateDoesChangeToOverDue()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
                          .makeCurrent().build();
    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);
    // and loan state is not yet overdue (check has not been run)
    boolean isStateOverDue = loan.isOverDue();
    assertFalse(isStateOverDue);
    // but loan should be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertTrue(isOverDue);
    // and loan state should now be overdue (after checkOverDue)
    isStateOverDue = loan.isOverDue();
    assertTrue(isStateOverDue);
  }



  @Test
  public void testLoanDueWithOverDueStateDoesNotChange()
  {
    // Given an overdue loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
                          .makeOverDue().build();
    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);
    // loan state is overdue
    boolean isStateOverDue = loan.isOverDue();
    assertTrue(isStateOverDue);
    // and loan should be overdue
    boolean isOverDue = loan.checkOverDue(today);
    assertTrue(isOverDue);
    // and loan state should still be overdue (after checkOverDue)
    isStateOverDue = loan.isOverDue();
    assertTrue(isStateOverDue);
  }



  @Test
  public void checkOverDueOnCompletedLoanThrows()
  {
    thrown.expect(RuntimeException.class);
    // Given an old loan that was completed
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
                          .makeComplete().build();
    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);

    // Then checkOverDue should throw exception
    boolean isOverDue = loan.checkOverDue(today);
  }



  @Test
  public void checkOverDueOnPendingLoanThrows()
  {
    thrown.expect(RuntimeException.class);
    // Given a pending loan
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
                          .makePending().build();
    // When today's date is 20th December (borrowing date)
    Date today = dateBuilder(20, 11, 2014);

    // Then checkOverDue should throw exception
    boolean isOverDue = loan.checkOverDue(today);
  }



  // TODO: check if loan is always meant to be constructed with default loan
  // period - in which case need to test that behavior

  // TODO: check using dates with hours and seconds set



  // TODO : remove use of setter and use reflection
  // TODO: return ILoan
  @Test
  public void completeCurrentLoanSetsStateToComplete()
  {
    // Given a new current loan with due date 31st December
    Loan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
                          .makeCurrent().build();

    // When complete loan
    loan.complete();

    // Then loan state should change to complete
    boolean isLoanStateComplete =
        loan.getState() == ELoanState.COMPLETE;
    assertTrue(isLoanStateComplete);
  }


  // TODO : remove use of setter and use reflection
  // TODO: return ILoan
  @Test
  public void completeOverDueLoanSetsStateToComplete()
  {
    // Given an overdue loan with due date 31st December
    Loan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
                          .makeOverDue().build();

    // When complete loan
    loan.complete();

    // Then loan state should change to complete
    boolean isLoanStateComplete =
      loan.getState() == ELoanState.COMPLETE;
    assertTrue(isLoanStateComplete);
  }



  @Test
  public void completeCompletedLoanThrows()
  {
    thrown.expect(RuntimeException.class);

    // Given an old loan that was completed
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
                          .makeComplete().build();

    // Attempting to complete loan (again) should throw exception
    loan.complete();
  }



  @Test
  public void completePendingLoanThrows()
  {
    thrown.expect(RuntimeException.class);

    // Given a pending loan
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
                          .makePending().build();

    // Attempting to complete pending loan should throw exception
    loan.complete();
  }


}
