package library.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import library.interfaces.entities.ELoanState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

/**
 * Loan instance is used to associate a borrower (Member) with the Book being
 * borrowed, set the dates of the loan and manage the state of the loan itself.
 *
 * @author nicholasbaldwin
 */
public class Loan
  implements ILoan
{
  //===========================================================================
  // Variables
  //===========================================================================

  private static int DEFAULT_ID = 0;
  private IBook book_;
  private IMember borrower_;
  private Date borrowDate_;
  private Date dueDate_;
  private int id_;

  private ELoanState state_ = ELoanState.PENDING; // default state of new Loan

  //===========================================================================
  // Constructor (& helpers)
  //===========================================================================

  /**
   * Creates a new instance of a Loan, providing parameters are valid.
   * @param book IBook
   * @param borrower IMember
   * @param borrowDate Date
   * @param dueDate Date
   * @throws IllegalArgumentException if:
   *  - any of book, borrower, borrowDate, or dueDate are null
   *  - dueDate is less than borrowDate
   */
  public Loan(IBook book, IMember borrower, Date borrowDate, Date dueDate)
    throws IllegalArgumentException
  {
    throwIfObjectNull("Book.", book);
    throwIfObjectNull("Borrower.", borrower);
    throwIfObjectNull("Borrowing Date.", borrowDate);
    throwIfObjectNull("Due Date.", dueDate);
    throwIfDueDateIsLessThanBorrowDate(borrowDate, dueDate);

    book_ = book;
    borrower_ = borrower;
    borrowDate_ = borrowDate;
    dueDate_ = dueDate;
    id_ = Loan.DEFAULT_ID;
  }



  private <T> void throwIfObjectNull(String parameterName, T object)
    throws IllegalArgumentException
  {
    String message = "Cannot create a new Loan with a null ";
    if (object == null) {
      throw new IllegalArgumentException(message + parameterName);
    }
  }



  private void throwIfDueDateIsLessThanBorrowDate(Date borrowDate,
                                                  Date dueDate)
    throws IllegalArgumentException
  {
    if (ignoreTime(dueDate).before(ignoreTime(borrowDate))) {
      throw new IllegalArgumentException("Cannot create a new Loan when the " +
                                         "Due Date is less than the Borrowing" +
                                         " Date.");
    }
  }



  //===========================================================================
  // Getters & setters
  //===========================================================================

  /**
   * Returns the borrower associated with this Loan.
   * @return IMember The borrower associated with this Loan.
   */
  @Override
  public IMember getBorrower()
  {
    return borrower_;
  }



  /**
   * Returns the Book associated with this Loan.
   * @return IBook The Book associated with this Loan.
   */
  @Override
  public IBook getBook()
  {
    return book_;
  }



  /**
   * Returns the this Loan's ID.
   * @return int The ID of this Loan.
   */
  @Override
  public int getID()
  {
    return id_;
  }

  //===========================================================================
  // Primary methods (& their private helpers)
  //===========================================================================

  /**
   * Commits this pending Loan in the system:
   *  - sets the state of this Loan to CURRENT
   *  - records this Loan on Book instance
   *  - records this Loan on Member instance
   * @param id int The ID of this Loan.
   * @throws RuntimeException if this Loan's state is not (initially) PENDING.
   * @throws IllegalArgumentException if id is less than or equal to 0.
   */
  @Override
  public void commit(int id)
    throws RuntimeException, IllegalArgumentException
  {
    throwIfStateNotPending();
    throwIfIDNotGreaterThanZero(id);

    state_ = ELoanState.CURRENT;
    id_ = id;
    book_.borrow(this);
    borrower_.addLoan(this);
  }



  private void throwIfStateNotPending()
      throws RuntimeException
  {
    if (state_ != ELoanState.PENDING) {
      throw new RuntimeException("Committing a Loan that is not " +
                                 "Pending is invalid.");
    }
  }



  private void throwIfIDNotGreaterThanZero(int id)
      throws IllegalArgumentException
  {
    if (id <= 0) {
      throw new IllegalArgumentException("Cannot commit a Loan with an ID " +
                                         "less than or equal to zero.");
    }
  }



  /**
   * Sets this Loan state to COMPLETE.
   * @throws RuntimeException if this Loan's state is not CURRENT or OVERDUE.
   */
  @Override
  public void complete()
    throws RuntimeException
  {
    boolean isLoanIsCurrentOrOverDue =
        (state_ == ELoanState.CURRENT ||
         state_ == ELoanState.OVERDUE);

    if (isLoanIsCurrentOrOverDue) {
        state_ = ELoanState.COMPLETE;
    } else {
      throw new RuntimeException("Completing a Loan that is not Current or " +
                                 "OverDue is invalid.");
    }
  }



  /**
   * Returns true if this Loan state is CURRENT.
   * @return boolean true if Loan state is CURRENT.
   */
  @Override
  public boolean isCurrent()
  {
    return (state_ == ELoanState.CURRENT);
  }



  /**
   * Returns true if this Loan state is OVERDUE.
   * @return boolean true if Loan state is OVERDUE.
   */
  @Override
  public boolean isOverDue()
  {
    return (state_ == ELoanState.OVERDUE);
  }



  /**
   * Returns true if current date is past due date of this Loan (ignoring any
   * time portion of the dates being compared).
   * @param currentDate Date current date.
   * @return boolean true if current date is past due date of this Loan.
   * @throws RuntimeException if this loan is not current or overDue.
   */
  @Override
  public boolean checkOverDue(Date currentDate)
    throws RuntimeException
  {
    boolean isLoanCurrentOrOverDue =
      (state_ == ELoanState.CURRENT || state_ == ELoanState.OVERDUE);

    if (isLoanCurrentOrOverDue) {
      boolean isAfterDueDate = isAfterDueDate(currentDate);
      if (isAfterDueDate) {
        state_ = ELoanState.OVERDUE;
        // new method to update borrower status
        getBorrower().loanHasBecomeOverdue(this);
      }
      return isAfterDueDate;
    }
    else {
      throw new RuntimeException("Checking a Loan that is not Current or " +
                                 "OverDue is invalid.");
    }
  }



  private boolean isAfterDueDate(Date currentDate)
  {
    Date today = ignoreTime(currentDate);
    Date due = ignoreTime(dueDate_);
    return today.after(due);
  }



  private Date ignoreTime(Date date)
  {
    Calendar calendar = Calendar.getInstance();

    calendar.setTime(date);
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
    calendar.set(java.util.Calendar.MINUTE, 0);
    calendar.set(java.util.Calendar.SECOND, 0);
    calendar.set(java.util.Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }


  /**
   * Returns a formatted description of the loan details.
   * @return String The formatted description of the loan details.
   */
  @Override
  public String toString()
  {
    StringBuffer loanString = new StringBuffer();
    loanString.append("Loan ID:  " + getID() + "\n")
              .append("Author:   " + getBook().getAuthor() + "\n")
              .append("Title:    " + getBook().getTitle() + "\n")
              .append("Borrower: " + getBorrower().getFirstName() + " " +
                                     getBorrower().getLastName() + "\n")
              .append("Borrowed: " + formattedDate(borrowDate_) + "\n")
              .append("Due Date: " + formattedDate(dueDate_));

    return loanString.toString();
  }



  private static String formattedDate(Date date)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateString = dateFormat.format(date);
    return dateString;
  }

}
