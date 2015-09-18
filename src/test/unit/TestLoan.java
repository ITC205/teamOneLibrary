package test.unit;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

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
  // Primary methods testing - with stubs & TestLoanBuilder
  // ==========================================================================

  @Test
  public void testOverDueLoanReturnsTrue()
  {
  // Given a manually set overdue loan
    ILoan loan = newLoan().isOverDue().build();

    // check if overdue
    boolean isOverdue = loan.isOverDue();
    assertTrue(isOverdue);
  }


  @Test
  public void testNewLoanIsNotOverDue()
  {
    // Given a manually set overdue loan
    ILoan loan = newLoan().build();

    // check if overdue
    boolean isOverdue = loan.isOverDue();
    assertFalse(isOverdue);
  }

  @Test
  public void testCurrentLoanIsNotOverDue()
  {
    // Given a manually set overdue loan
    ILoan loan = newLoan().isCurrent().build();

    // check if overdue
    boolean isOverdue = loan.isOverDue();
    assertFalse(isOverdue);
  }

}
