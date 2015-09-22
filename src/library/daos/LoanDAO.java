package library.daos;

import java.util.Calendar;
import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

import library.interfaces.daos.ILoanHelper;

import library.entities.Loan;

/**
 * Manages all Loans within the system, from creation of pending loans to
 * committing loans to 'persistence' and provides methods for finding
 * individual loans, identifying overdue loans and updating state of overdue
 * loans.
 */
public class LoanDAO
//  implements ILoanDAO
{
  //===========================================================================
  // Variables
  //===========================================================================

  private ILoanHelper helper_;
  private Calendar calendar_ = Calendar.getInstance();

  //===========================================================================
  // Constructors
  //===========================================================================

  /**
   * Creates new LoanDAO.
   * @param helper ILoanHelper The helper this DAO uses to instantiate Loans.
   * throws IllegalArgumentException if helper is null.
   */
  protected LoanDAO(ILoanHelper helper)
    throws IllegalArgumentException
  {
    if (helper == null) {
      throw new IllegalArgumentException("Cannot create a new LoanDAO with " +
                                         "a null Loan Helper.");
    }
    helper_ = helper;
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /*
   * Uses LoanHelper to create a new Loan with default id of zero, sets
   * borrow date on current system time and sets due date using the standard
   * loan duration (from borrow date).
   * @param borrower IMember The borrower who wishes to create a loan.
   * @param book IBook The book that the borrower wishes to loan.
   * @return Loan A pending loan associated with the borrower and the book,
   * with and id of zero.
   * throws IllegalArgumentException if borrower or book is null.
   */
  // @Override
  public void createLoan(IBook book, IMember borrower)
  {
    // throwIfObjectNull("Book", book);
    // throwIfObjectNull("Borrower", borrower);
    Date borrowDate = ignoreTime(new Date());
    Date dueDate = calculateDueDate(borrowDate);
    // Date dueDate = calculateDueDate(borrowDate);

    helper_.makeLoan(book, borrower, borrowDate, dueDate);
  }


  private <T> void throwIfObjectNull(String parameterName, T object)
      throws IllegalArgumentException
  {
    String message = "Cannot create a new Loan with a null ";
    if (object == null) {
      throw new IllegalArgumentException(message + parameterName);
    }
  }

  //===========================================================================
  // Helper methods
  //===========================================================================


  //===========================================================================
  // Getters & setters
  //===========================================================================




  private Date ignoreTime(Date date)
  {
    java.util.Calendar calendar = java.util.Calendar.getInstance();

    calendar.setTime(date);
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
    calendar.set(java.util.Calendar.MINUTE, 0);
    calendar.set(java.util.Calendar.SECOND, 0);
    calendar.set(java.util.Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }



  private Date calculateDueDate(Date borrowDate)
  {
    calendar_.setTime(borrowDate);
    calendar_.add(java.util.Calendar.DATE, ILoan.LOAN_PERIOD);
    return calendar_.getTime();
  }

}
