package test.unit;

import java.util.Date;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

/**
 * Helper class for building customized Loans for test cases.
 * Provides a fluent API, and allows all parameters of Loan constructor - as
 * well as private state to be passed in.
 *  - e.g.: IBook book = newLoan().withBook(book).withBorrower(borrower).build()
 */
public class TestLoanBuilder
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



  public static Date dateBuilder(int day, int month, int year)
  {
    java.util.Calendar calendar = new java.util.GregorianCalendar();
    calendar.set(year, month, day);
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

  private TestLoanBuilder()
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
  public static TestLoanBuilder newLoan()
  {
    return new TestLoanBuilder();
  }

  

  /*
   * Generates the Loan to be built. Usage example:
   *   IBook book = newLoan().withBook(book).withBorrower(borrower).build()
   */
  public ILoan build()
  {
    ILoan loan = new Loan(book_, borrower_, borrowDate_, dueDate_, id_);
    // use helper to set state of new Loan
    setState(loan, state_);
    return loan;
  }

  //===========================================================================
  // Helper methods
  //===========================================================================

  public TestLoanBuilder withID(int id)
  {
    id_ = id;
    return this;
  }



  public TestLoanBuilder withBorrowDate(int day, int month, int year)
  {
    borrowDate_ = dateBuilder(day, month, year);
    return this;
  }



  public TestLoanBuilder withDueDate(int day, int month, int year)
  {
    dueDate_ = dateBuilder(day, month, year);
    return this;
  }



  public TestLoanBuilder makeOverDue()
  {
    state_ = ELoanState.OVERDUE;
    return this;
  }



  public TestLoanBuilder makeCurrent()
  {
    state_ = ELoanState.CURRENT;
    return this;
  }



  public TestLoanBuilder makeComplete()
  {
    state_ = ELoanState.COMPLETE;
    return this;
  }



  public TestLoanBuilder makePending()
  {
    state_ = ELoanState.PENDING;
    return this;
  }



  public TestLoanBuilder withBook(IBook book)
  {
    book_ = book;
    return this;
  }



  public TestLoanBuilder withBorrower(IMember borrower)
  {
    borrower_ = borrower;
    return this;
  }



  /*
   * Uses Reflection API to directly set Loan's private state.
   */
  private void setState(ILoan loan, ELoanState newState) {

    try {
      Class<?> loanClass = loan.getClass();
      java.lang.reflect.Field state = loanClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      state.set(loan, newState);
    }
    catch (NoSuchFieldException exception) {
      fail("NoSuchFieldException should not occur");
    }
    catch (IllegalAccessException exception) {
      fail("IllegalAccessException should not occur");
    }
    catch (Exception exception) {
      fail("Exception should not occur");
    }
  }

}




