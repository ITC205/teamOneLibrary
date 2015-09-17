package test.unit;

import java.util.List;
import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IMember;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import org.junit.Rule;
import org.junit.Ignore;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

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
   * Create stub items with Mockito, but explicitly name method to show that
   * stubs are being used (not mocks or other fakes/doubles) :-)
   * @param classToMock Class to be stubbed
   * @return stub of class
   */
  public static <T> T stub(Class<T> classToMock)
  {
    return mock(classToMock);
  }



  /**
   * Create stub IMember with Mockito.
   * @return stub IMember.
   */
  public static IMember stubMember()
  {
    return mock(IMember.class);
  }



  /**
   * Create stub IBook with Mockito.
   * @return stub IBook.
   */
  public static IBook stubBook()
  {
    return mock(IBook.class);
  }

  // ==========================================================================
  // Test rules
  // ==========================================================================

  @Rule
  public ExpectedException thrown= ExpectedException.none();

  // ==========================================================================
  // Constructor Testing
  // ==========================================================================

  @Test
  public void constructNewLoan()
  {
    // given stubs for member and book
    IMember fakeBorrower = stubMember();
    IBook fakeBook = stubBook();
    // With dates (valid dates to check later)
    Date borrowDate = new Date();
    Date returnDate = new Date();

    // Then can create a loan
    ILoan loan = new Loan(fakeBook, fakeBorrower, borrowDate, returnDate);

    // Naive, initial check that loan was created
    assertTrue(loan instanceof ILoan);
  }


  // TODO: check constructor throws IllegalArgumentException if:
  // borrowDate is null
  // dueDate is null
  // dueDate is less than borrowDate
  // loanID is less than or equal to zero


  @Test
  public void constructNewLoanWithNullBookThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given null member and stub for book
    IMember fakeBorrower = stubMember();
    IBook fakeBook = null;
    // With dates (valid dates to check later)
    Date borrowDate = new Date();
    Date returnDate = new Date();

    // When create a loan, exception is thrown
    ILoan loan = new Loan(fakeBook, fakeBorrower, borrowDate, returnDate);
  }



  @Test
  public void constructNewLoanWithNullBorrowerThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given null member and stub for book
    IMember fakeBorrower = null;
    IBook fakeBook = stubBook();
    // With dates (valid dates to check later)
    Date borrowDate = new Date();
    Date returnDate = new Date();

    // When create a loan, exception is thrown
    ILoan loan = new Loan(fakeBook, fakeBorrower, borrowDate, returnDate);
  }


  @Test
  public void constructNewLoanWithNullBorrowDateThrows()
  {
    thrown.expect(IllegalArgumentException.class);

    // given null member and stub for book
    IMember fakeBorrower = stubMember();
    IBook fakeBook = stubBook();
    // With dates (valid dates to check later)
    Date borrowDate = null;
    Date returnDate = new Date();

    // When create a loan, exception is thrown
    ILoan loan = new Loan(fakeBook, fakeBorrower, borrowDate, returnDate);
  }

  // TODO: check exceptions message from thrown.expectMessage("");

}
