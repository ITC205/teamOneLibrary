package library.entities;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

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

  private boolean overDue_;
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
    throwIfObjectNull("Return Date.", dueDate);
    throwIfReturnDateIsNotAfterBorrowDate(borrowDate, dueDate);
    throwIfIDLessThanOrEqualToZero(id);

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



  private void throwIfIDLessThanOrEqualToZero(int id)
    throws IllegalArgumentException
  {
    if (id <= 0) {
      throw new IllegalArgumentException("Cannot create a new Loan with an " +
                                         "ID less than or equal to zero.");
    }
  }

  //===========================================================================
  // Getters & setters
  //===========================================================================

  /**
   * Returns the borrower associated with this loan.
   * @return IMember The borrower associated with this loan.
   */
  public IMember getBorrower()
  {
    return borrower_;
  }



  /**
   * Returns the book associated with this loan.
   * @return IBook The book associated with this loan.
   */
  public IBook getBook()
  {
    return book_;
  }



  /**
   * Returns the this loan's ID.
   * @return int The ID of this loan.
   */
  public int getID()
  {
    return id_;
  }


  /**
   * Sets the state of this loan.
   * @param state  ELoanState Enum state of this loan.
   */
  public void setState(ELoanState state)
  {
    state_ = state;
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /**
   * Commits this pending Loan in the system:
   *  - sets the state of this Loan to CURRENT
   *  - records Loan instance on Book instance: book.borrow(loan)
   *  - adds Loan instance to Borrower instance: borrower.addLoan(loan)
   * Throws a RuntimeException if this LoanState is not PENDING
   * @param iD int The ID of this Loan.
   */
  public void commit(int iD)
  {

  }



  /**
   *
   */
  public void complete()
  {

  }



  /**
   *
   */
  public boolean isOverDue()
  {
    return (state_ == ELoanState.OVERDUE);
  }



  /**
   *
   */
  public boolean checkOverDue(Date currentDate)
  {
    return false;
  }

}
