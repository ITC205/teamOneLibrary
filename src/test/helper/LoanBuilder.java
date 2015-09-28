package test.helper;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.fail;

import static test.helper.DateBuilder.*;
import static test.helper.LoanReflection.*;
import static test.helper.DoubleBuilder.*;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

/**
 * Helper class for building customized Loans for test cases.
 * Provides a fluent API, and allows all parameters of Loan constructor - as
 * well as private state to be passed in.
 * e.g.: IBook book = newLoan().withBook(book).withBorrower(borrower).build()
 *
 * @author nicholasbaldwin
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

  /**
   * Provides a new TestLoanBuilder instance that can then be customized
   * through the use of the helper methods to set desired attributes of the
   * Loan to be built. Usage example:
   *   IBook book = newLoan().withBook(book).withBorrower(borrower).build()
   */
  public static LoanBuilder newLoan()
  {
    return new LoanBuilder();
  }



  /**
   * Generates the Loan to be built. Usage example:
   *   IBook book = newLoan().withBook(book).withBorrower(borrower).build()
   */
  public ILoan build()
  {
    ILoan loan = new Loan(book_, borrower_, borrowDate_, dueDate_);
    // use helper to set state of new Loan
    setPrivateState(loan, state_);
    setPrivateID(loan, id_);
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




