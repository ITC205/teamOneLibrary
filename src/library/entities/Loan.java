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

  IBook book_;
  IMember borrower_;
  Date borrowDate_;
  Date returnDate_;

  //===========================================================================
  // Constructors
  //===========================================================================

  /**
   *
   */
  public Loan(IBook book, IMember borrower, Date borrowDate, Date returnDate)
  {
    throwIfObjectNull("Book.", book);
    throwIfObjectNull("Borrower.", borrower);
    throwIfObjectNull("Borrowing Date.", borrowDate);
    throwIfObjectNull( "Return Date.", returnDate );
    throwIfReturnDateIsNotAfterBorrowDate( borrowDate, returnDate );
    book_ = book;
    borrower_ = borrower;
    borrowDate_ = borrowDate;
    returnDate_ = returnDate;
  }



  private static <T> void throwIfObjectNull(String parameterName, T object)
  {
    String message = "Cannot create a new Loan with a null ";
    if (object == null) {
      throw new IllegalArgumentException( message + parameterName );
    }
  }

  private void throwIfReturnDateIsNotAfterBorrowDate(Date borrowDate, Date returnDate)
  {
    if (!returnDate.after(borrowDate)) {
      throw new IllegalArgumentException("Cannot create a new Loan when the " +
      "return Date is before r the same as the Borrowing Date.");
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
