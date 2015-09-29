package test.helper;

import static org.mockito.Mockito.mock;

import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

/**
 * Provides static helpers that simply provide an explicitly named interface to
 * create stubs and mocks using Mockito.
 */
public class DoubleBuilder
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
   * Create stub LoanHelper with Mockito. Simply provides an explicitly named
   * method to show that stubs are being used (not mocks or other fakes/doubles)
   * @return stub LoanHelper.
   */
  public static ILoanHelper stubHelper()
  {
    return mock(ILoanHelper.class);
  }



  /**
   * Create stub Loan with Mockito. Simply provides an explicitly named
   * method to show that stubs are being used (not mocks or other fakes/doubles)
   * @return stub Loan.
   */
  public static ILoan stubLoan()
  {
    return mock(ILoan.class);
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
  // Mock helpers
  // ==========================================================================

  /**
   * Create stub Book with Mockito. Simply provides an explicitly named method
   * to show that a mock is being used (not a stub or other fakes/doubles) :-)
   * @return mock Book.
   */
  public static IBook mockBook()
  {
    return mock(IBook.class);
  }



  /**
   * Create LoanHelper mock with Mockito. Simply provides an explicitly named
   * method to show that a mock is being (not a stub or other fakes/doubles).
   * @return mock LoanHelper.
   */
  public static ILoanHelper mockHelper()
  {
    return mock(ILoanHelper.class);
  }



  /**
   * Create Loan mock with Mockito. Simply provides an explicitly named
   * method to show that a mock is being (not a stub or other fakes/doubles).
   * @return mock Loan.
   */
  public static ILoan mockLoan()
  {
    return mock(ILoan.class);
  }



  /**
   * Create stub Member with Mockito. Simply provides an explicitly named method
   * to show that a mock is being used (not a stub or other fakes/doubles) :-)
   * @return mock Member.
   */
  public static IMember mockMember()
  {
    return mock(IMember.class);
  }

}
