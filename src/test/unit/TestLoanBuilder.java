package test.unit;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

import library.entities.Loan;

import static org.mockito.Mockito.mock;

/**
 *
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
  public static final int DEFAULT_ID = 125;

  private IBook book_ = DEFAULT_BOOK;
  private IMember borrower_ = DEFAULT_BORROWER;
  private Date borrowDate_ = DEFAULT_BORROW_DATE;
  private Date dueDate_ = DEFAULT_DUE_DATE;
  private int iD_ = DEFAULT_ID;

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
    System.out.println(dateString);
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

  public static TestLoanBuilder newLoan()
  {
    return new TestLoanBuilder();
  }



  public ILoan build()
  {
    return new Loan(book_, borrower_, borrowDate_, dueDate_, iD_);
  }

  //===========================================================================
  // Helper methods
  //===========================================================================

  public TestLoanBuilder withID(int iD)
  {
    iD_ = iD;
    return this;
  }


}




