package library.entities;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

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
  private int iD_;

  //===========================================================================
  // Constructors
  //===========================================================================

  /**
   *
   */
  public Loan(IBook book, IMember borrower, Date borrowDate, Date dueDate,
              int iD)
  {
    throwIfObjectNull("Book.", book);
    throwIfObjectNull("Borrower.", borrower);
    throwIfObjectNull("Borrowing Date.", borrowDate);
    throwIfObjectNull("Return Date.", dueDate);
    throwIfReturnDateIsNotAfterBorrowDate( borrowDate, dueDate );
    throwIfIDLessThanOrEqualToZero( iD );

    book_ = book;
    borrower_ = borrower;
    borrowDate_ = borrowDate;
    dueDate_ = dueDate;
    iD_ = iD;
  }



  private static <T> void throwIfObjectNull(String parameterName, T object)
  {
    String message = "Cannot create a new Loan with a null ";
    if (object == null) {
      throw new IllegalArgumentException(message + parameterName);
    }
  }

  private void throwIfReturnDateIsNotAfterBorrowDate(Date borrowDate,
                                                     Date returnDate)
  {
    if (!returnDate.after(borrowDate)) {
      throw new IllegalArgumentException("Cannot create a new Loan when the " +
                                         "return Date is before or the same " +
                                         "as the Borrowing Date.");
    }
  }



  private void throwIfIDLessThanOrEqualToZero(int iD)
  {
    if (iD <= 0) {
      throw new IllegalArgumentException("Cannot create a new Loan with an " +
                                         "ID less than or equal to zero.");
    }
  }

  //===========================================================================
  // Getters & setters
  //===========================================================================

  /**
   *
   */
  public IMember getBorrower()
  {
    return borrower_;
  }



  /**
   *
   */
  public IBook getBook()
  {
    return book_;
  }



  /**
   *
   */
  public int getID()
  {
    return 0;
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /**
   *
   */
  public void commit(int id)
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
    return false;
  }



  /**
   *
   */
  public boolean checkOverDue(Date currentDate)
  {
    return false;
  }

}
