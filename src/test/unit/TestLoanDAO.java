package test.unit;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import static test.helper.DateBuilder.*;
import static test.helper.DoubleBuilder.*;
import static test.helper.LoanBuilder.*;
import static test.helper.LoanReflection.*;

import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import library.daos.LoanDAO;

/**
 * Unit tests for LoanDAO.
 *
 * @author nicholasbaldwin
 */
public class TestLoanDAO
{
  //===========================================================================
  // Test fixtures - uses LoanBuilder for stubs, mocks & dates
  //===========================================================================

  private library.interfaces.entities.ILoan firstJimLoansCatch22_ = stubLoan();
  private library.interfaces.entities.ILoan secondSamLoansEmma_ = stubLoan();
  private library.interfaces.entities.ILoan thirdJillLoansCatch22_ = stubLoan();
  private library.interfaces.entities.ILoan fourthJimLoansScoop_ = stubLoan();
  private library.interfaces.entities.ILoan fifthJillLoansDune_ = stubLoan();
  private library.interfaces.entities.ILoan sixthSamLoansEmma_ = stubLoan();

  private IMember jim_ = stubMember();
  private IMember sam_ = stubMember();
  private IMember jill_ = stubMember();
  private IMember bob_ = stubMember();

  private IBook catch22_ = stubBook();
  private IBook emma_ = stubBook();
  private IBook scoop_ = stubBook();
  private IBook dune_ = stubBook();

  public void setUpFirstLoan()
  {
    when(firstJimLoansCatch22_.getBook()).thenReturn(catch22_);
    when(firstJimLoansCatch22_.getBorrower()).thenReturn(jim_);
    when(firstJimLoansCatch22_.getID()).thenReturn(1);
    when(firstJimLoansCatch22_.isCurrent()).thenReturn(false);
    when(firstJimLoansCatch22_.isOverDue()).thenReturn(false);
    when(catch22_.getTitle()).thenReturn("CATCH-22");
    when(catch22_.getAuthor()).thenReturn("Joseph Heller");
    when(catch22_.getState()).thenReturn(library.interfaces.entities.EBookState.AVAILABLE);
    // when just this loan
  }

  public void setUpSecondLoan()
  {
    when(secondSamLoansEmma_.getBook()).thenReturn(emma_);
    when(secondSamLoansEmma_.getBorrower()).thenReturn(sam_);
    when(secondSamLoansEmma_.getID()).thenReturn(2);
    when(secondSamLoansEmma_.isCurrent()).thenReturn(false);
    when(secondSamLoansEmma_.isOverDue()).thenReturn(false);
    when(emma_.getTitle()).thenReturn("Emma");
    when(emma_.getAuthor()).thenReturn("Jane Austen");
    when(emma_.getState()).thenReturn(library.interfaces.entities.EBookState.AVAILABLE);
  }

  public void setUpThirdLoan()
  {
    when(thirdJillLoansCatch22_.getBook()).thenReturn(catch22_);
    when(thirdJillLoansCatch22_.getBorrower()).thenReturn(jill_);
    when(thirdJillLoansCatch22_.getID()).thenReturn(3);
    when(thirdJillLoansCatch22_.isCurrent()).thenReturn(false);
    when(thirdJillLoansCatch22_.isOverDue()).thenReturn(true);
    when(catch22_.getTitle()).thenReturn("CATCH-22");
    when(catch22_.getAuthor()).thenReturn("Joseph Heller");
    when(catch22_.getState()).thenReturn(library.interfaces.entities.EBookState.ON_LOAN);;
  }

  public void setUpFourthLoan()
  {
    when(fourthJimLoansScoop_.getBook()).thenReturn(scoop_);
    when(fourthJimLoansScoop_.getBorrower()).thenReturn(jim_);
    when(fourthJimLoansScoop_.getID()).thenReturn(4);
    when(fourthJimLoansScoop_.isCurrent()).thenReturn(false);
    when(fourthJimLoansScoop_.isOverDue()).thenReturn(true);
    when(scoop_.getTitle()).thenReturn("Scoop");
    when(scoop_.getAuthor()).thenReturn("Evelyn Waugh");
    when(scoop_.getState()).thenReturn(library.interfaces.entities.EBookState.ON_LOAN);
  }

  public void setUpFifthLoan()
  {
    when(fifthJillLoansDune_.getBook()).thenReturn(dune_);
    when(fifthJillLoansDune_.getBorrower()).thenReturn(jill_);
    when(fifthJillLoansDune_.getID()).thenReturn(5);
    when(fifthJillLoansDune_.isCurrent()).thenReturn(true);
    when(fifthJillLoansDune_.checkOverDue(any())).thenReturn(true);
    when(fifthJillLoansDune_.isOverDue()).thenReturn(false);
    when(dune_.getTitle()).thenReturn("Dune");
    when(dune_.getAuthor()).thenReturn("Frank Herbert");
    when(dune_.getState()).thenReturn(library.interfaces.entities.EBookState.ON_LOAN);
  }

  public void setUpSixthLoan()
  {
    when(sixthSamLoansEmma_.getBook()).thenReturn(emma_);
    when(sixthSamLoansEmma_.getBorrower()).thenReturn(sam_);
    when(sixthSamLoansEmma_.getID()).thenReturn(6);
    when(sixthSamLoansEmma_.isCurrent()).thenReturn(true);
    when(sixthSamLoansEmma_.checkOverDue(any())).thenReturn(false);
    when(sixthSamLoansEmma_.isOverDue()).thenReturn(false);
    when(dune_.getTitle()).thenReturn("Emma");
    when(dune_.getAuthor()).thenReturn("Jane Austen");
    when(dune_.getState()).thenReturn(library.interfaces.entities.EBookState.ON_LOAN);
  }

  //===========================================================================
  // Test constructor - with LoanBuilder (for stubs)
  //===========================================================================

  @org.junit.Test
  public void createLoanDao()
  {
    library.interfaces.daos.ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    assertThat(dao).isNotNull();
    assertThat(dao).isInstanceOf(LoanDAO.class);
  }



  @org.junit.Test
  public void createLoanDaoWithNullHelperThrows()
  {
    ILoanHelper loanHelper = null;

    try {
      LoanDAO dao = new LoanDAO(loanHelper);
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }

  //===========================================================================
  // Test createLoan - with LoanBuilder (for stubs & mocks) & fixtures
  // for creating loan
  //===========================================================================

  @org.junit.Test
  public void createLoanCallsLoanHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    library.interfaces.entities.ILoan loan = dao.createLoan(jim_, catch22_);

    verify(loanHelper).makeLoan(catch22_, jim_, today, due);
  }



  @org.junit.Test
  public void createLoanReturnsILoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    when(loanHelper.makeLoan(catch22_, jim_, today, due))
                   .thenReturn(firstJimLoansCatch22_);
    LoanDAO dao = new LoanDAO(loanHelper);

    library.interfaces.entities.ILoan loan = dao.createLoan(jim_, catch22_);

    assertThat(loan).isInstanceOf(library.interfaces.entities.ILoan.class);
  }



  @org.junit.Test
  public void createLoanGetsExpectedLoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    when(loanHelper.makeLoan(catch22_, jim_, today, due))
                   .thenReturn(firstJimLoansCatch22_);
    LoanDAO dao = new LoanDAO(loanHelper);

    library.interfaces.entities.ILoan loan = dao.createLoan(jim_, catch22_);

    assertThat(loan).isSameAs(firstJimLoansCatch22_);
  }



  @org.junit.Test
  public void createLoanWithNullBookThrows()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    when(loanHelper.makeLoan(null, jim_, today, due))
                   .thenThrow(new IllegalArgumentException());
    LoanDAO dao = new LoanDAO(loanHelper);

    try {
      library.interfaces.entities.ILoan loan = dao.createLoan(jim_, null);
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @org.junit.Test
  public void createLoanWithNullBorrowerThrows()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    when(loanHelper.makeLoan(catch22_, null, today, due))
        .thenThrow(new IllegalArgumentException());
    LoanDAO dao = new LoanDAO(loanHelper);

    try {
      library.interfaces.entities.ILoan loan = dao.createLoan(null, catch22_);
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }

  //===========================================================================
  // Testing the test fixtures :-)
  // ensure that broken text fixtures do not cause tests to fail/pass
  //===========================================================================

  @org.junit.Test
  public void setUpLoansHelperWorksCorrectly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);

    assertThat(firstJimLoansCatch22_.getBook()).isSameAs(catch22_);
    assertThat(secondSamLoansEmma_.getBook()).isSameAs(emma_);
    assertThat(firstJimLoansCatch22_.getBook())
                                    .isNotSameAs(secondSamLoansEmma_.getBook());
  }

  //===========================================================================
  // Test commitLoan - with LoanBuilder (for stubs & mocks), LoanReflection
  // (to create new LoanDAOs) & fixtures for loans
  //===========================================================================

  @org.junit.Test
  public void commitLoanCallsLoanCommitCorrectly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    library.interfaces.entities.ILoan loan = mockLoan();

    dao.commitLoan(loan);

    verify(loan).commit(anyInt());
  }



  @org.junit.Test
  public void commitLoanCallsLoanCommitWithCorrectId()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    library.interfaces.entities.ILoan loan = mockLoan();

    // using reflection to set private nextID
    setPrivateNextId(dao, 999);
    dao.commitLoan(loan);

    verify(loan).commit(999);
  }



  @org.junit.Test
  public void commitLoanCallsLoanCommitWithCorrectIdInSequence()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    verify(firstJimLoansCatch22_).commit(1);
    verify(secondSamLoansEmma_).commit(2);
    verify(thirdJillLoansCatch22_).commit(3);
  }



  @org.junit.Test
  public void commitLoanAddsLoanToLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<library.interfaces.entities.ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    allLoans = dao.listLoans();

    assertThat(allLoans).isNotEmpty();
    assertThat(allLoans).hasSize(1);
    assertThat(allLoans).containsExactly(firstJimLoansCatch22_);
  }



  @org.junit.Test
  public void commitLoansAddsMultipleLoansToLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<library.interfaces.entities.ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    allLoans = dao.listLoans();

    assertThat(allLoans).isNotEmpty();
    assertThat(allLoans).hasSize(3);
    assertThat(allLoans).containsExactly(firstJimLoansCatch22_,
                                         secondSamLoansEmma_,
                                         thirdJillLoansCatch22_);
  }

  //===========================================================================
  // Test listLoans - with LoanBuilder (for stubs) & LoanReflection (to
  // add Loans to loanMap)
  //===========================================================================

  @org.junit.Test
  public void loanListIsEmptyInitially()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    List<library.interfaces.entities.ILoan> allLoans = dao.listLoans();

    assert(allLoans).isEmpty();
  }



  @org.junit.Test
  public void listLoansReturnsLoansManuallySetInLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    library.interfaces.entities.ILoan[] loans = {firstJimLoansCatch22_, secondSamLoansEmma_};

    setPrivateLoanMap(dao, loans);
    List<library.interfaces.entities.ILoan> allLoans = dao.listLoans();

    assertThat(allLoans).isNotEmpty();
    assertThat(allLoans).hasSize(2);
    assertThat(allLoans).containsExactly(firstJimLoansCatch22_,
                                         secondSamLoansEmma_);
  }



  @org.junit.Test
  public void listLoansReturnsCommittedLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<library.interfaces.entities.ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);
    setUpFourthLoan();
    dao.commitLoan(fourthJimLoansScoop_);
    setUpFifthLoan();
    dao.commitLoan(fifthJillLoansDune_);
    setUpSixthLoan();
    dao.commitLoan(sixthSamLoansEmma_);

    allLoans = dao.listLoans();

    assertThat(allLoans).hasSize(6);
    assertThat(allLoans).containsExactly(firstJimLoansCatch22_,
                                         secondSamLoansEmma_,
                                         thirdJillLoansCatch22_,
                                         fourthJimLoansScoop_,
                                         fifthJillLoansDune_,
                                         sixthSamLoansEmma_);
  }

  //===========================================================================
  // Test getLoanByID - with LoanBuilder (for stubs), LoanReflection
  // (to create new LoanDAOs) & fixtures for loans
  //===========================================================================

  @org.junit.Test
  public void getLoanByIdReturnsNullIfLoanMapEmpty()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<library.interfaces.entities.ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();

    library.interfaces.entities.ILoan loan = dao.getLoanByID(1);

    assertThat(loan).isNull();
  }



  @org.junit.Test
  public void getLoanByIdReturnsLoanIfLoanMapContainsOnlyThatLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    ILoan loan = dao.getLoanByID(1);

    assertThat(loan).isNotNull();
    assertThat(loan).isEqualTo(firstJimLoansCatch22_);
  }



  @org.junit.Test
  public void getLoanByIdReturnsLoanIfLoanMapContainsMultipleLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_); // 3

    ILoan loan = dao.getLoanByID(3);

    assertThat(loan).isNotNull();
    assertThat(loan).isEqualTo(thirdJillLoansCatch22_);
  }



  @org.junit.Test
  public void getLoanByIdReturnsNullIfLoanMapDoesNotContainsLoanWithThatId()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_); // 3

    ILoan loan = dao.getLoanByID(4);

    assertThat(loan).isNull();
  }

  //===========================================================================
  // Test findLoansByBorrower - with LoanBuilder (for stubs & mocks)
  // & fixtures for loans & books
  //===========================================================================

  @org.junit.Test
  public void findLoansByBorrowerReturnsNullIfNoLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();

    List<ILoan> loans = dao.findLoansByBorrower(jim_);

    assertThat(loans).isEmpty();
  }



  @org.junit.Test
  public void findLoansByBorrowerCallsLoanGetBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    dao.findLoansByBorrower(bob_);

    verify(firstJimLoansCatch22_).getBorrower();
    verify(secondSamLoansEmma_).getBorrower();
    verify(thirdJillLoansCatch22_).getBorrower();
    verifyZeroInteractions(fourthJimLoansScoop_);
    verifyZeroInteractions(fifthJillLoansDune_);
    verifyZeroInteractions(sixthSamLoansEmma_);
  }



  @org.junit.Test
  public void findLoansByBorrowerReturnsEmptyIfNoLoansByBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);
    setUpFourthLoan();
    dao.commitLoan(fourthJimLoansScoop_);
    setUpFifthLoan();
    dao.commitLoan(fifthJillLoansDune_);

    List<ILoan> loans = dao.findLoansByBorrower(bob_);

    assertThat(loans).isEmpty();
  }



  @org.junit.Test
  public void findLoansByBorrowerReturnsLoanIfSingleLoanByBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    List<ILoan> loans = dao.findLoansByBorrower(jim_);

    assertThat(loans).isNotNull();
    assertThat(loans).containsExactly(firstJimLoansCatch22_);
  }



  @org.junit.Test
  public void findLoansByBorrowerReturnsLoanIfMultipleLoanByBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_); // jim
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);
    setUpFourthLoan();
    dao.commitLoan(fourthJimLoansScoop_); // jim

    List<ILoan> loans = dao.findLoansByBorrower(jim_);

    assertThat(loans).isNotNull();
    assertThat(loans).containsExactly(firstJimLoansCatch22_,
                                      fourthJimLoansScoop_);
  }

  //===========================================================================
  // Test findLoansByBookTitle - with LoanBuilder (for stubs & mocks)
  // & fixtures for loans & books
  //===========================================================================

  @org.junit.Test
  public void findLoansByBookTitleNullWhenNoLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();

    List<ILoan> loans = dao.findLoansByBookTitle("Scoop");

    assertThat(loans).isEmpty();
  }



  @org.junit.Test
  public void findLoansByBookTitleCallsLoanGetBookGetTitle()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);

    dao.findLoansByBookTitle("Scoop");

    verify(firstJimLoansCatch22_).getBook();
    verify(catch22_).getTitle();
    verify(secondSamLoansEmma_).getBook();
    verify(emma_).getTitle();
    verifyNoMoreInteractions(thirdJillLoansCatch22_);
    verifyNoMoreInteractions(fourthJimLoansScoop_);
    verifyNoMoreInteractions(fifthJillLoansDune_);
    verifyNoMoreInteractions(sixthSamLoansEmma_);
  }



  @org.junit.Test
  public void findLoansByBookTitleEmptyWhenTitleNotInLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    List<ILoan> loans = dao.findLoansByBookTitle("Scoop");

    assertThat(loans).isEmpty();
  }



  @org.junit.Test
  public void findLoansByBookTitleReturnsLoanWhenTitleInOnlyLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    List<ILoan> loans = dao.findLoansByBookTitle("CATCH-22");

    assertThat(loans).containsExactly(firstJimLoansCatch22_);
  }



  @org.junit.Test
  public void findLoansByBookTitleReturnsLoansWhenTitleInMultipleLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    List<ILoan> loans = dao.findLoansByBookTitle("CATCH-22");

    assertThat(loans).containsExactly(firstJimLoansCatch22_,
                                      thirdJillLoansCatch22_);
  }



  @org.junit.Test
  public void findLoansByBookTitleIsCaseInsensitive()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_); // 'CATCH-22'

    List<ILoan> loans = dao.findLoansByBookTitle("Catch-22");

    assertThat(loans).containsExactly(firstJimLoansCatch22_);
  }



  @org.junit.Test
  public void findLoansByBookTitleIsExactMatchOnly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    List<ILoan> loans = dao.findLoansByBookTitle("CATCH*");

    assertThat(loans).isEmpty();
  }

  //===========================================================================
  // Test updateOverDueStatus - with LoanBuilder (for stubs & mocks)
  // & fixtures for loans & books
  //===========================================================================

  // TODO: check these, once Jim responds
  @org.junit.Test
  public void updateOverDueStatusDoesNotCallLoanCheckOverDueOnCompleteLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    Date today = dateBuilder(10, 2, 2015);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    dao.updateOverDueStatus(today);

    verify(firstJimLoansCatch22_, never()).checkOverDue(any());
  }



  @org.junit.Test
  public void updateOverDueStatusOnMultipleLoansCallsOnlyOnCurrentLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);
    setUpFourthLoan();
    dao.commitLoan(fourthJimLoansScoop_);
    setUpFifthLoan();
    dao.commitLoan(fifthJillLoansDune_);
    setUpSixthLoan();
    dao.commitLoan(sixthSamLoansEmma_);
    Date today = dateBuilder(10, 2, 2015);

    dao.updateOverDueStatus(today);

    verify(firstJimLoansCatch22_, never()).checkOverDue(any());
    verify(secondSamLoansEmma_, never()).checkOverDue(any());
    verify(thirdJillLoansCatch22_, never()).checkOverDue(any());
    verify(fourthJimLoansScoop_, never()).checkOverDue(any());
    verify(fifthJillLoansDune_).checkOverDue(today);
    verify(sixthSamLoansEmma_).checkOverDue(today);
  }







  //===========================================================================
  // Test findOverDueLoans - with LoanBuilder (for stubs & mocks) & fixtures
  // for loans & books
  //===========================================================================

  @org.junit.Test
  public void findOverDueLoansEmptyIfNoLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);

    List<ILoan> overDueLoans = dao.findOverDueLoans();

    assertThat(overDueLoans).isEmpty();
  }



  @org.junit.Test
  public void findOverDueLoansEmptyIfOneCompleteLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    List<ILoan> overDueLoans = dao.findOverDueLoans();

    assertThat(overDueLoans).isEmpty();
  }



  @org.junit.Test
  public void findOverDueLoansCallsLoanIsOverDue()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpFifthLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    dao.findOverDueLoans();

    verify(firstJimLoansCatch22_).isOverDue();
    verify(secondSamLoansEmma_).isOverDue();
    verify(thirdJillLoansCatch22_).isOverDue();
    verifyNoMoreInteractions(fourthJimLoansScoop_);
    verifyNoMoreInteractions(fifthJillLoansDune_);
    verifyNoMoreInteractions(sixthSamLoansEmma_);
  }



  @org.junit.Test
  public void findOverDueLoansEmptyIfLoansNotDue()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpFifthLoan();
    dao.commitLoan(fifthJillLoansDune_);

    List<ILoan> overDueLoans = dao.findOverDueLoans();

    assertThat(overDueLoans).isEmpty();
  }



  @org.junit.Test
  public void findOverDueLoansReturnsOneLoanIfOneOverDueLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);

    List<ILoan> overDueLoans = dao.findOverDueLoans();

    assertThat(overDueLoans).containsExactly(thirdJillLoansCatch22_);
  }



  @org.junit.Test
  public void findOverDueLoansReturnsMultipleIfMultipleOverDueLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = new LoanDAO(loanHelper);
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_);
    setUpFourthLoan();
    dao.commitLoan(fourthJimLoansScoop_);
    setUpFifthLoan();
    dao.commitLoan(fifthJillLoansDune_);

    List<ILoan> overDueLoans = dao.findOverDueLoans();

    assertThat(overDueLoans).containsExactly(thirdJillLoansCatch22_,
                                             fourthJimLoansScoop_);
  }


  //===========================================================================
  // Test helpers
  //===========================================================================

  // TODO: extract helpers?

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
    java.util.Calendar calendar = java.util.Calendar.getInstance();

    calendar.setTime(borrowDate);
    calendar.add(java.util.Calendar.DATE, ILoan.LOAN_PERIOD);
    return calendar.getTime();
  }

}
