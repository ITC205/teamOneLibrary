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
    book_ = throwIfObjectNull("Book.", book);
    borrower_ = throwIfObjectNull("Borrower.", borrower);
    borrowDate_ = throwIfObjectNull("Borrowing Date.", borrowDate);
    // returnDate_ = throwIfObjectNull("Return Date.", returnDate);
  }



  private static <T> T throwIfObjectNull(String parameterName, T object) {
    String message = "Cannot create a new Loan with a null ";
    if(object == null) {
      throw new IllegalArgumentException(message + parameterName);
    }
    return object;
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
