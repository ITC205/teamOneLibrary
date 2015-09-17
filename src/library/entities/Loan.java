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
    if (borrower == null) {
      throw new IllegalArgumentException( "" );
    }

    if (book == null) {
      throw new IllegalArgumentException( "" );
    }


    book_ = book;
    borrower_ = borrower;
    borrowDate_ = borrowDate;
    returnDate_ = returnDate;
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
