package library.entities;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import java.text.SimpleDateFormat;

/**
 *
 */
public class Loan
  implements ILoan
{
  //===========================================================================
  // Variables
  //===========================================================================

  private IBook book_;
  private IMember borrower_;
  private Date borrowDate_;
  private Date dueDate_;
  private int id_;

  private ELoanState state_;

  //===========================================================================
  // Constructor (& helpers)
  //===========================================================================

  /**
   * Creates a new instance of a Loan, providing parameters are valid.
   * @param book IBook
   * @param borrower IMember
   * @param borrowDate Date
   * @param dueDate Date
   * @param id int
   * Throws an IllegalArgumentException if:
   *  - any of book, borrower, borrowDate, or dueDate are null
   *  - dueDate is less than borrowDate
   *  - loanID is less than or equal to zero
   */
  public Loan(IBook book, IMember borrower, Date borrowDate, Date dueDate,
              int id)
    throws IllegalArgumentException
  {
    throwIfObjectNull("Book.", book);
    throwIfObjectNull("Borrower.", borrower);
    throwIfObjectNull("Borrowing Date.", borrowDate);
    throwIfObjectNull("Due Date.", dueDate);
    throwIfReturnDateIsNotAfterBorrowDate(borrowDate, dueDate);
    throwIfIDLessThanZero(id);

    book_ = book;
    borrower_ = borrower;
    borrowDate_ = borrowDate;
    dueDate_ = dueDate;
    id_ = id;
  }



  private <T> void throwIfObjectNull(String parameterName, T object)
    throws IllegalArgumentException
  {
    String message = "Cannot create a new Loan with a null ";
    if (object == null) {
      throw new IllegalArgumentException(message + parameterName);
    }
  }



  private void throwIfReturnDateIsNotAfterBorrowDate(Date borrowDate,
                                                     Date returnDate)
    throws IllegalArgumentException
  {
    if (!returnDate.after(borrowDate)) {
      throw new IllegalArgumentException("Cannot create a new Loan when the " +
                                         "Return Date is before or the same " +
                                         "as the Borrowing Date.");
    }
  }



  private void throwIfIDLessThanZero(int id)
    throws IllegalArgumentException
  {
    if (id < 0) {
      throw new IllegalArgumentException("Cannot create a new Loan with an " +
                                         "ID less than or equal to zero.");
    }
  }

  //===========================================================================
  // Getters & setters
  //===========================================================================

  /**
   * Returns the borrower associated with this Loan.
   * @return IMember The borrower associated with this Loan.
   */
  public IMember getBorrower()
  {
    return borrower_;
  }



  /**
   * Returns the Book associated with this Loan.
   * @return IBook The Book associated with this Loan.
   */
  public IBook getBook()
  {
    return book_;
  }



  /**
   * Returns the this Loan's ID.
   * @return int The ID of this Loan.
   */
  public int getID()
  {
    return id_;
  }






  // TODO: remove and use reflection?
  /**
   * Returns the state of this loan.
   * @return ELoanState The state (enum) of this Loan.
   */
  public ELoanState getState()
  {
    return state_;
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /**
   * Commits this pending Loan in the system:
   *  - sets the state of this Loan to CURRENT
   *  - records this Loan on Book instance
   *  - records this Loan on Member instance
   * Throws a RuntimeException if this Loan's state is not (initially) PENDING.
   * @param iD int The ID of this Loan.
   */
  public void commit(int iD)
  {
    if (state_ == ELoanState.PENDING) {
      state_ = ELoanState.CURRENT;
      id_ = iD;
      book_.borrow(this);
      borrower_.addLoan(this);
    }
    else {
      throw new IllegalArgumentException("Committing a Loan that is not " +
                                         "Pending is invalid.");
    }
  }



  /**
   * Sets this Loan state to COMPLETE.
   * Throws a RuntimeException if this Loan's state is not CURRENT or OVERDUE.
   */
  public void complete()
  {
    boolean isLoanIsCurrentOrOverDue =
        (state_ == ELoanState.CURRENT || state_ == ELoanState.OVERDUE);

    if (isLoanIsCurrentOrOverDue) {
        state_ = ELoanState.COMPLETE;
    } else {
      throw new RuntimeException("Completing a Loan that is not Current or " +
                                 "OverDue is invalid.");
    }
  }



  /**
   * Returns true if this Loan state is OVERDUE.
   * @return boolean true if Loan state is OVERDUE.
   */
  public boolean isOverDue()
  {
    return (state_ == ELoanState.OVERDUE);
  }



  /**
   * Returns true if current date is past due date of this Loan.
   * @param currentDate Date current date.
   * @return boolean true if current date is past due date of this Loan.
   */
  public boolean checkOverDue(Date currentDate)
  {
    boolean checkLoanOnlyIfCurrentOrOverDue =
        (state_ == ELoanState.CURRENT || state_ == ELoanState.OVERDUE);

    if (checkLoanOnlyIfCurrentOrOverDue) {
      boolean isOverDue = currentDate.after(dueDate_);
      if (isOverDue) {
        state_ = ELoanState.OVERDUE;
      }
      return isOverDue;
    } else {
      throw new RuntimeException("Checking a Loan that is not Current or " +
                                 "OverDue is invalid.");
    }
  }



  // TODO: are there getters for dates?
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
