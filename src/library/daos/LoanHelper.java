package library.daos;

import java.util.Date;

import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import library.entities.Loan;

/**
 * Creates a new Loan instance.
 *
 * @author nicholasbaldwin
 */
public class LoanHelper
  implements ILoanHelper
{
  //===========================================================================
  // Constructors
  //===========================================================================

  /**
   * Creates new LoanHelper.
   */
  public LoanHelper()
  {
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /**
   * Creates a new Loan instance, with a default ID.
   * @param book IBook The Book associated with Loan.
   * @param borrower IMember The Member associated with Loan.
   * @param borrowDate Date The date the Loan was created.
   * @param dueDate Date The date the loan is due.
   * @return ILoan A new Loan instance, with the default ID.
   * @throws IllegalArgumentException if:
   *  - any of book, borrower, borrowDate, or dueDate are null
   *  - dueDate is less than borrowDate
   *  - (propagated from loan constructor)
   */
  @Override
  public ILoan makeLoan(IBook book, IMember borrower,
                        Date borrowDate, Date dueDate)
    throws IllegalArgumentException
  {
    return new Loan(book, borrower, borrowDate, dueDate);
  }

}
