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

  private ILoanHelper loanHelper_;

  //===========================================================================
  // Constructors
  //===========================================================================

  protected LoanDAO(ILoanHelper loanHelper)
  {
    loanHelper_ = loanHelper;
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
