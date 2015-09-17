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
    // given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date returnDate = new Date(2);

    // Then can create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);

    // Naive, initial check that loan was created
    assertTrue(loan instanceof ILoan);
  }



  @Test
  public void constructNewLoanWithNullBookThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given null book and stub for member
    IBook book = null;
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date returnDate = new Date(2);

    // When create a loan, exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);
  }



  @Test
  public void constructNewLoanWithNullBorrowerThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given stub for book and null member
    IBook book = stubBook();
    IMember borrower = null;
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date returnDate = new Date(2);

    // When create a loan, exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);
  }



  @Test
  public void constructNewLoanWithNullBorrowDateThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With null borrowDate
    Date borrowDate = null;
    Date returnDate = new Date(2);

    // When create a loan, exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);
  }



  @Test
  public void constructNewLoanWithNullReturnDateThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With null returnDate
    Date borrowDate = new Date(1);
    Date returnDate = null;

    // When create a loan, exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);
  }



  @Test
  public void checkExceptionMessageWhenConstructorThrows()
  {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Cannot create a new Loan with a null Book.");

    // given null book and stub for member
    IBook book = null;
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date returnDate = new Date(2);

    // When create a loan, exception is thrown
    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);
  }


  @Test
  public void constructNewLoanWithValidDates()
  {
    // given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();

    Date borrowDate = null;
    Date returnDate = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      returnDate = dateFormat.parse("18/09/2015");
    }
    catch (ParseException exception) {
      fail();
    }

    ILoan loan = new Loan(book, borrower, borrowDate, returnDate);

    // Naive, initial check that loan was created
    assertTrue(loan instanceof ILoan);
  }


  // TODO: check constructor throws IllegalArgumentException if:
  // loanID is less than or equal to zero




}
