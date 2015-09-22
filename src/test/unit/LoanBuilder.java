package test.unit;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.interfaces.daos.ILoanHelper;

import library.entities.Loan;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import static test.unit.LoanReflection.*;

/**
 * Helper class for building customized Loans for test cases.
 * Provides a fluent API, and allows all parameters of Loan constructor - as
 * well as private state to be passed in.
 *  - e.g.: IBook book = newLoan().withBook(book).withBorrower(borrower).build()
 */
public class LoanBuilder
{
  //===========================================================================
  // Variables
  //===========================================================================

  public static final IBook DEFAULT_BOOK = stubBook();
  public static final IMember DEFAULT_BORROWER = stubMember();
  public static final Date DEFAULT_BORROW_DATE = dateBuilder(1, 0, 2015);
  public static final Date DEFAULT_DUE_DATE = dateBuilder(2, 0, 2015);
  public static final int DEFAULT_ID = 0;

  public static final ELoanState DEFAULT_STATE = ELoanState.PENDING;

  private IBook book_ = DEFAULT_BOOK;
  private IMember borrower_ = DEFAULT_BORROWER;
  private Date borrowDate_ = DEFAULT_BORROW_DATE;
  private Date dueDate_ = DEFAULT_DUE_DATE;
  private int id_ = DEFAULT_ID;

  private ELoanState state_ = DEFAULT_STATE;

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

  // ==========================================================================
  // Mock helpers
  // ==========================================================================

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

  // ==========================================================================
  // Date helpers
  // ==========================================================================

  /**
   * Create Date using simple interface (via Calendar) where time portion is
   * set to zero.
   * @param day   int The day of the month (1 or 2 digits).
   * @param month int The month of the year (1 or 2 digits, where 0 = January,
   *              11 = December).
   * @param year  int The year (4 digits).
   * @return Date Date with time portion set to 0).
   */
  public static Date dateBuilder(int day, int month, int year)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.set(year, month, day, 0, 0, 0);
    Date date = calendar.getTime();
    return date;
  }



  /**
   * Create Date using simple interface (via Calendar) including time portion.
   * @param day   int The day of the month (1 or 2 digits).
   * @param month int The month of the year (1 or 2 digits, where 0 = January,
   *              11 = December).
   * @param year  int The year (4 digits).
   * @param hour  int The hour of the day (1 or 2 digits, using 24 hour clock).
   * @param min   int The minutes of the hour (1 or 2 digits).
   * @param sec   int The seconds of the minute (1 or 2 digits).
   * @return Date Date with time portion set to 0).
   */
  public static Date dateBuilder(int day, int month, int year,
                                 int hour, int min, int sec)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.set(year, month, day, hour, min, sec);
    Date date = calendar.getTime();
    return date;
  }



  public static String formattedDate(Date date)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateString = dateFormat.format(date);
    return dateString;
  }

  //===========================================================================
  // Constructors
  //===========================================================================

  private LoanBuilder()
  {
    //
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /*
   * Provides a new TestLoanBuilder instance that can then be customized
   * through the use of the helper methods to set desired attributes of the
   * Loan to be built. Usage example:
   *   IBook book = newLoan().withBook(book).withBorrower(borrower).build()
   */
  public static LoanBuilder newLoan()
  {
    return new LoanBuilder();
  }



  /*
   * Generates the Loan to be built. Usage example:
   *   IBook book = newLoan().withBook(book).withBorrower(borrower).build()
   */
  public ILoan build()
  {
    ILoan loan = new Loan(book_, borrower_, borrowDate_, dueDate_, id_);
    // use helper to set state of new Loan
    setPrivateState(loan, state_);
    return loan;
  }

  //===========================================================================
  // Helper methods
  //===========================================================================

  public LoanBuilder withID(int id)
  {
    id_ = id;
    return this;
  }



  public LoanBuilder withBorrowDate(int day, int month, int year)
  {
    borrowDate_ = dateBuilder(day, month, year);
    return this;
  }



  public LoanBuilder withBorrowDate(int day, int month, int year,
                                       int hour, int min, int sec)
  {
    borrowDate_ = dateBuilder(day, month, year, hour, min, sec);
    return this;
  }



  public LoanBuilder withDueDate(int day, int month, int year)
  {
    dueDate_ = dateBuilder(day, month, year);
    return this;
  }



  public LoanBuilder withDueDate(int day, int month, int year,
                                     int hour, int min, int sec)
  {
    dueDate_ = dateBuilder(day, month, year, hour, min, sec);
    return this;
  }



  public LoanBuilder makeOverDue()
  {
    state_ = ELoanState.OVERDUE;
    return this;
  }



  public LoanBuilder makeCurrent()
  {
    state_ = ELoanState.CURRENT;
    return this;
  }



  public LoanBuilder makeComplete()
  {
    state_ = ELoanState.COMPLETE;
    return this;
  }



  public LoanBuilder makePending()
  {
    state_ = ELoanState.PENDING;
    return this;
  }



  public LoanBuilder withBook(IBook book)
  {
    book_ = book;
    return this;
  }



  public LoanBuilder withBorrower(IMember borrower)
  {
    borrower_ = borrower;
    return this;
  }

}




