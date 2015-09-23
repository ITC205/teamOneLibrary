package library.daos;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;

import library.entities.Loan;

/**
 * Manages all Loans within the system, from creation of pending loans to
 * committing loans to 'persistence' and provides methods for finding
 * individual loans, identifying overdue loans and updating state of overdue
 * loans.
 */
public class LoanDAO
  implements ILoanDAO
{
  //===========================================================================
  // Variables
  //===========================================================================

  // TODO: static? not hardcoded?
  private int nextID_ = 1;

  private ILoanHelper helper_;
  private Calendar calendar_ = Calendar.getInstance();
  private Map<Integer, ILoan> loanMap_ = new HashMap<>();


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

  /*
   * Uses LoanHelper to create a new Loan with default id of zero, sets
   * borrow date on current system time and sets due date using the standard
   * loan duration (from borrow date).
   * @param borrower IMember The borrower who wishes to create a loan.
   * @param book IBook The book that the borrower wishes to loan.
   * @return Loan A pending loan associated with the borrower and the book,
   * with and id of zero.
   * throws IllegalArgumentException if borrower or book is null (propagated
   * from loan constructor).
   */
  @Override
  public ILoan createLoan(IMember borrower, IBook book)
    throws IllegalArgumentException
  {
    Date borrowDate = ignoreTime(new Date());
    Date dueDate = calculateDueDate(borrowDate);

    return helper_.makeLoan(book, borrower, borrowDate, dueDate);
  }



  /*
   * Assigns the Loan a unique id and stores the Loan.
   * @param loan ILoan The Loan to be committed.
   */
  @Override
  public void commitLoan(ILoan loan)
  {
    loan.commit(nextID_);
    loanMap_.put(nextID_, loan);
    nextID_++;
  }



  /*
   * Returns a list of all loans in the committed loan collection.
   * @return List<ILoan> The list of all Loans in the committed loan collection.
   */
  @Override
  public List<ILoan> listLoans()
  {
    return new ArrayList<ILoan>(loanMap_.values());
  }


  /*
   * Returns the loan in the committed loan collection identified by id, or
   * null if loan not found.
   * @param id int The id of the Loan to return.
   * @return ILoan The loan in the committed loan collection with the given id,
   * or null if a loan with that id does not exist.
   */
  @Override
  public ILoan getLoanByID(int id)
  {
    if (loanMap_.containsKey(id)) {
      return loanMap_.get(id);
    }
    else {
      return null;
    }
  }



  //TODO: equals, override equals, or use ==?
  /*
   * Returns a list of all loans in the committed loan collection associated
   * with the given borrower.
   * List<ILoan>
   * @param borrower IMember The borrower associated with the loans.
   */
  @Override
  public List<ILoan> findLoansByBorrower(IMember borrower)
  {
    List<ILoan> borrowerLoans= new ArrayList<>();
    for (ILoan loan : loanMap_.values()) {
      if (loan.getBorrower() == borrower) {
        borrowerLoans.add(loan);
      }
    }
    return borrowerLoans;
  }



  /*
   * Returns a list of all loans in the committed loan collection associated
   * with books with the given title.
   * @param title String The title of the book(s).
   * @return List<ILoan> The list of all loans in the committed loan collection
   * associated with books with the given title.
   */
  @Override
  public List<ILoan> findLoansByBookTitle(String title)
  {
    List<ILoan> bookTitleLoans = new ArrayList<>();
    for (ILoan loan : loanMap_.values()) {
      if (loan.getBook().getTitle().equalsIgnoreCase(title)) {
        bookTitleLoans.add(loan);
      }
    }
    return bookTitleLoans;
  }



  /*
   * Iterates through the committed loan collection updating the overdue
   * status of current loans according to date.
   * @param date Date The current date, used to check if each current loan is
   * overdue.
   */
  @Override
  public void updateOverDueStatus(Date date)
  {
    for (ILoan loan : loanMap_.values()) {
      if (loan.isCurrent()) {
        loan.checkOverDue(date);
      }
    }
  }



  /*
   * Returns a list of all loans in the committed loan collection which are
   * currently overdue (according to state).
   * @return List<ILoan> All loans in the committed loan collection which are
   * currently overdue.
   */
  @Override
  public List<ILoan> findOverDueLoans()
  {
    List<ILoan> overdueLoans = new ArrayList<>();
    for (ILoan loan : loanMap_.values()) {
      if (loan.isOverDue()) {
        overdueLoans.add(loan);
      }
    }
    return overdueLoans;
  }

  //===========================================================================
  // Helper methods
  //===========================================================================


  //===========================================================================
  // Getters & setters
  //===========================================================================




  private Date ignoreTime(Date date)
  {
    java.util.Calendar calendar = java.util.Calendar.getInstance();

    calendar.setTime(date);
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
    calendar.set(java.util.Calendar.MINUTE, 0);
    calendar.set(java.util.Calendar.SECOND, 0);
    calendar.set(java.util.Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }



  private Date calculateDueDate(Date borrowDate)
  {
    calendar_.setTime(borrowDate);
    calendar_.add(java.util.Calendar.DATE, ILoan.LOAN_PERIOD);
    return calendar_.getTime();
  }

}
