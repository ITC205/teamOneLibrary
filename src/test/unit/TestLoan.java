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
  // Constructor Testing
  // ==========================================================================

  @Test
  public void constructNewLoan()
  {
    try {
      // Given stubs for book and member
      IBook book = stubBook();
      IMember borrower = stubMember();
      // With valid, but very simple dates in millis
      Date borrowDate = new Date(1);
      Date dueDate = new Date(2);
      int iD = 1;

      // When create a loan
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);

      // Then can (naively) check that loan was created
      assertTrue(loan instanceof ILoan);
    }
    catch (Exception exception) {
      fail(exception.getMessage());
    }


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
    int iD = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
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
    int iD = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
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
    int iD = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
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
    int iD = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
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
    int iD = 1;

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
  }



  @Test
  public void constructNewLoanWithValidDates()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = null;
    Date dueDate = null;
    int iD = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("18/09/2015");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);

    // Then can (naively) check that loan was created
    assertTrue(loan instanceof ILoan);
  }



  @Test
  public void constructNewLoanDueDateBeforeBorrowDate()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = null;
    Date dueDate = null;
    int iD = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("16/09/2015");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
  }



  @Test
  public void constructNewLoanDueDateSameAsBorrowDate()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = null;
    Date dueDate = null;
    int iD = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("17/09/2015");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
  }



  @Test
  public void checkExceptionMessageWhenInvalidDueDate()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Cannot create a new Loan when the return Date is " +
                         "before or the same as the Borrowing Date.");

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = null;
    Date dueDate = null;
    int iD = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("16/09/2015");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
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
    Date borrowDate = null;
    Date dueDate = null;
    int iD = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("01/01/2015");
      dueDate = dateFormat.parse("01/01/2025");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
  }



  @Test
  public void constructNewLoanIDNegativeThrows()
  {
    // Expect exception to be thrown
    thrown.expect(IllegalArgumentException.class);

    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = null;
    Date dueDate = null;
    int iD = -1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("01/01/2015");
      dueDate = dateFormat.parse("01/01/2025");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
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
    Date borrowDate = null;
    Date dueDate = null;
    int iD = -1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("01/01/2015");
      dueDate = dateFormat.parse("01/01/2025");
    }
    catch (ParseException exception) {
      fail();
    }

    // When create a loan, then exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);
  }

  // ==========================================================================
  // Getters & setters testing
  // ==========================================================================

  @Test
  public void getBorrowerFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();

    // With valid dates
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);
    int iD = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, iD);

    // Then can return borrower and verify it is same Member as local instance
    IMember loanBorrower = loan.getBorrower();
    assertSame(loanBorrower, borrower);
  }

}
