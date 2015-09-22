package library.daos;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.interfaces.daos.ILoanHelper;

/**
 * Manages all Loans within the system, from creation of pending loans to
 * committing loans to 'persistence' and provides methods for finding
 * individual loans, identifying overdue loans and updating state of overdue
 * loans.
 */
public class LoanDAO
{
  //===========================================================================
  // Variables
  //===========================================================================

  private ILoanHelper helper_;

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



  //===========================================================================
  // Helper methods
  //===========================================================================


  //===========================================================================
  // Getters & setters
  //===========================================================================


}
