package library.daos;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.daos.ILoanHelper;

import library.entities.Loan;

/**
 * Creates a new Loan instance.
 */
public class LoanHelper
  implements ILoanHelper
{
  //===========================================================================
  // Variables
  //===========================================================================

  private final int DEFAULT_ID = 0; // new items have id of zero by default

  //===========================================================================
  // Constructors
  //===========================================================================

  /*
   * Constructor protected, to restrict use as a helper for Loan DAO.
   */
  protected LoanHelper()
  {
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /*
   * Creates a new Loan instance, with a default ID.
   * @param book IBook The Book associated with Loan.
   * @param borrower IMember The Member associated with Loan.
   * @param borrowDate Date The date the Loan was created.
   * @param dueDate Date The date the loan is due.
   * @return ILoan A new Loan instance, with the default ID.
   */
  @Override
  public ILoan makeLoan(IBook book, IMember borrower,
                        Date borrowDate, Date dueDate)
  {
    return new Loan(book, borrower,
                    borrowDate, dueDate,
                    DEFAULT_ID);
  }

}
