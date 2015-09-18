package test.unit;

import java.util.Date;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

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



  public Loan build()
  {
    //TODO: check ok to return Loan rather that ILoan
    Loan loan = new Loan(book_, borrower_, borrowDate_, dueDate_, id_);
    // TODO: check if need to remove setter & use reflection for state
    loan.setState(state_);
    return loan;
  }

  //===========================================================================
  // Helper methods
  //===========================================================================

  public TestLoanBuilder withID(int iD)
  {
    id_ = iD;
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



  public TestLoanBuilder isOverDue()
  {
    state_ = ELoanState.OVERDUE;
    return this;
  }


  public TestLoanBuilder isCurrent()
  {
    state_ = ELoanState.CURRENT;
    return this;
  }

}




