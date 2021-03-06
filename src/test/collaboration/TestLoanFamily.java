package test.collaboration;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import static test.helper.DateBuilder.*;
import static test.helper.DoubleBuilder.*;
import static test.helper.LoanReflection.*;

import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.EBookState;

import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.entities.Loan;

/**
 * Tests collaborations of Loan family - Loan, LoanHelper & LoanDAO.
 * Uses DoubleBuilder for creation of stubs & mocks, DateBuilder for all things
 * date related and LoanReflection for checking private fields.
 *
 * @author nicholasbaldwin
 */
public class TestLoanFamily
{
  //===========================================================================
  // Test fixtures
  //===========================================================================

  private IMember jim_ = stubMember();
  private IMember sam_ = stubMember();
  private IMember jill_ = stubMember();
  private IMember bob_ = stubMember();

  private IBook catch22_ = stubBook();
  private IBook emma_ = stubBook();
  private IBook scoop_ = stubBook();
  private IBook dune_ = stubBook();

  public void setUpCatch22()
  {
    when(catch22_.getTitle()).thenReturn("CATCH-22");
    when(catch22_.getAuthor()).thenReturn("Joseph Heller");
    when(catch22_.getState()).thenReturn(EBookState.AVAILABLE);
  }

  public void setUpEmma()
  {
    when(emma_.getTitle()).thenReturn("Emma");
    when(emma_.getAuthor()).thenReturn("Jane Austen");
    when(emma_.getState()).thenReturn(EBookState.AVAILABLE);
  }

  public void setUpScoop()
  {
    when(scoop_.getTitle()).thenReturn("Scoop");
    when(scoop_.getAuthor()).thenReturn("Evelyn Waugh");
    when(scoop_.getState()).thenReturn(EBookState.ON_LOAN);
  }

  public void setUpDune()
  {
    when(dune_.getTitle()).thenReturn("Dune");
    when(dune_.getAuthor()).thenReturn("Frank Herbert");
    when(dune_.getState()).thenReturn(EBookState.ON_LOAN);
  }

  public void setUpJim()
  {
    when(jim_.getFirstName()).thenReturn("Jim");
    when(jim_.getLastName()).thenReturn("Johnson");
  }

  public void setUpSam()
  {
    when(sam_.getFirstName()).thenReturn("Sam");
    when(sam_.getLastName()).thenReturn("Malone");
  }

  public void setUpJill()
  {
    when(jill_.getFirstName()).thenReturn("Jill");
    when(jill_.getLastName()).thenReturn("Underhill");
  }

  public void setUpBob()
  {
    when(bob_.getFirstName()).thenReturn("Bob");
    when(bob_.getLastName()).thenReturn("Dylan");
  }

  //===========================================================================
  // Test main interactions
  //===========================================================================

  @Test
  public void createEmptyLibrary()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    List<ILoan> emptyLoanList = dao.listLoans();

    assertThat(emptyLoanList).isEmpty();
  }



  @Test
  public void createLoanDoesNotAddPendingLoanToLibrary()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    ILoan loan = dao.createLoan(jim_, catch22_);

    assertThat(dao.listLoans().size()).isEqualTo(0);
    assertThat(loan.getID()).isEqualTo(0);
    assertThat(loan.isCurrent()).isFalse();
  }



  @Test
  public void commitLoanMakesLoanCurrentAndSetsIDAndAddsToLibrary()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    ILoan loan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(loan);

    assertThat(loan.getID()).isEqualTo(1);
    assertThat(loan.isCurrent()).isTrue();
    assertThat(dao.listLoans().size()).isEqualTo(1);
  }



  @Test
  public void listLoansEmptyLibraryReturnsEmpty()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    assertThat(dao.listLoans()).isEmpty();
  }



  @Test
  public void listLoansReturnsAllLoans()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpJim();
    setUpCatch22();
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    assertThat(dao.listLoans()).hasSize(4);

    List<ILoan> allLoans = dao.listLoans();

    assertThat(allLoans).containsExactly(firstLoan, secondLoan, thirdLoan,
                                         fourthLoan);
  }



  @Test
  public void findOverDueLoansReturnsOverDueLoans()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    Date dueDate = dateBuilder(24, 10, 2010);
    Date today = dateBuilder(25, 10, 2010);

    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    setPrivateDueDate((Loan)firstLoan, dueDate);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    setPrivateDueDate((Loan)secondLoan, dueDate);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(fourthLoan);

    dao.updateOverDueStatus(today);

    assertThat(firstLoan.isOverDue()).isTrue();
    assertThat(secondLoan.isOverDue()).isTrue();
    assertThat(thirdLoan.isOverDue()).isFalse();
    assertThat(fourthLoan.isOverDue()).isFalse();
    assertThat(dao.findOverDueLoans()).isNotEmpty();
    assertThat(dao.findOverDueLoans()).containsExactly(firstLoan, secondLoan);
  }



  @Test
  public void singleLoanCanBeFoundByBorrower()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    ILoan loan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(loan);

    List<ILoan> jimLoans = dao.findLoansByBorrower(jim_);

    assertThat(jimLoans).containsExactly(loan);
  }



  @Test
  public void findLoansByBorrowerReturnsAllBorrowerLoans()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpJim();
    setUpCatch22();
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    ILoan fifthLoan = dao.createLoan(jill_, dune_);
    dao.commitLoan(fifthLoan);
    ILoan sixthLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(sixthLoan);
    assertThat(dao.listLoans()).hasSize(6);

    List<ILoan> jimLoans = dao.findLoansByBorrower(jim_);

    assertThat(jimLoans).containsExactly(firstLoan, fourthLoan);
  }



  @Test
  public void findLoansByBorrowerReturnsEmptyListForNewMember()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    ILoan fifthLoan = dao.createLoan(jill_, dune_);
    dao.commitLoan(fifthLoan);
    ILoan sixthLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(sixthLoan);
    assertThat(dao.listLoans()).hasSize(6);

    List<ILoan> bobLoans = dao.findLoansByBorrower(bob_);

    assertThat(bobLoans).isEmpty();
  }




  @Test
  public void findLoanByBookTitleReturnsCorrectList()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpCatch22();
    setUpEmma();
    setUpScoop();
    setUpDune();
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    ILoan fifthLoan = dao.createLoan(jill_, dune_);
    dao.commitLoan(fifthLoan);
    ILoan sixthLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(sixthLoan);
    assertThat(dao.listLoans()).hasSize(6);

    List<ILoan> catch22Loans = dao.findLoansByBookTitle("CATCH-22");

    assertThat(catch22Loans).containsExactly(firstLoan, thirdLoan);
  }



  @Test
  public void findLoanByBookTitleReturnsEmptyListIfTitleNotMatch()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpCatch22();
    setUpEmma();
    setUpScoop();
    setUpDune();
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    ILoan fifthLoan = dao.createLoan(jill_, dune_);
    dao.commitLoan(fifthLoan);
    ILoan sixthLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(sixthLoan);
    assertThat(dao.listLoans()).hasSize(6);

    List<ILoan> catch22Loans = dao.findLoansByBookTitle("Lucky Jim");

    assertThat(catch22Loans).isEmpty();
  }



  @Test
  public void getLoanByIDReturnsCorrectLoan()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    ILoan fifthLoan = dao.createLoan(jill_, dune_);
    dao.commitLoan(fifthLoan);
    assertThat(dao.listLoans()).hasSize(5);

    ILoan loan = dao.getLoanByID(4);

    assertThat(loan).isEqualTo(fourthLoan);
    assertThat(loan.getID()).isEqualTo(4);
  }



  @Test
  public void getLoanByInvalidIDReturnsNull()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    ILoan firstLoan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(firstLoan);
    ILoan secondLoan = dao.createLoan(sam_, emma_);
    dao.commitLoan(secondLoan);
    ILoan thirdLoan = dao.createLoan(jill_, catch22_);
    dao.commitLoan(thirdLoan);
    ILoan fourthLoan = dao.createLoan(jim_, scoop_);
    dao.commitLoan(fourthLoan);
    ILoan fifthLoan = dao.createLoan(jill_, dune_);
    dao.commitLoan(fifthLoan);
    assertThat(dao.listLoans()).hasSize(5);

    ILoan loan = dao.getLoanByID(6);

    assertThat(loan).isNull();
  }



  @Test
  public void updateOverDueStatusCausesDueLoanToBecomeOverDue()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    Date dueDate = dateBuilder(24, 10, 2015);
    Date today = dateBuilder(25, 10, 2015);

    ILoan loan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(loan);
    setPrivateDueDate((Loan)loan, dueDate);
    assertThat(loan.isOverDue()).isFalse();
    assertThat(dao.findOverDueLoans()).isEmpty();

    dao.updateOverDueStatus(today);

    assertThat(loan.isOverDue()).isTrue();
  }



  @Test
  public void updateOverDueStatusCallsLoanHasBecomeOverDueOnCurrentLoan()
  {
    ILoanHelper loanHelper = new LoanHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    Date dueDate = dateBuilder(24, 10, 2015);
    Date today = dateBuilder(25, 10, 2015);

    ILoan loan = dao.createLoan(jim_, catch22_);
    dao.commitLoan(loan);
    setPrivateDueDate((Loan)loan, dueDate);
    assertThat(loan.isOverDue()).isFalse();
    assertThat(dao.findOverDueLoans()).isEmpty();

    dao.updateOverDueStatus(today);

    assertThat(loan.isOverDue()).isTrue();

    verify(jim_).loanHasBecomeOverdue(loan);
  }

}
