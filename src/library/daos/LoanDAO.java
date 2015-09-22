package library.daos;

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

  private static LoanDAO instance_ = null;

  //===========================================================================
  // Constructors
  //===========================================================================

  private LoanDAO()
  {
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  public static LoanDAO getInstance()
  {
    if (instance_ == null) {
      instance_ = new LoanDAO();
    }
    return instance_;
  }

  //===========================================================================
  // Helper methods
  //===========================================================================


  //===========================================================================
  // Getters & setters
  //===========================================================================


}
