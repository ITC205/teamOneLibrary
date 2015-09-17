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


}
