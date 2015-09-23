package test.unit;

import java.util.Date;

import java.util.List;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.daos.ILoanHelper;

import library.interfaces.entities.EBookState;

import library.daos.LoanDAO;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;

/**
 * Unit tests for LoanDAO.
 */
public class TestLoanDAO
{
  //===========================================================================
  // Test fixtures
  //===========================================================================

  private ILoan firstJimLoansCatch22_ = stubLoan();
  private ILoan secondSamLoansEmma_ = stubLoan();
  private ILoan thirdJillLoansCatch22_ = stubLoan();
  private ILoan fourthJimLoansScoop_ = stubLoan();
  private ILoan fifthJillLoansDune_ = stubLoan();

  private IMember jim_ = stubMember();
  private IMember sam_ = stubMember();
  private IMember jill_ = stubMember();
  private IMember bob_ = stubMember();

  private IBook catch22_ = stubBook();
  private IBook emma_ = stubBook();
  private IBook scoop_ = stubBook();
  private IBook dune_ = stubBook();

  private Date Jan10 = dateBuilder(10, 0, 2014);
  private Date Jan24 = dateBuilder(24, 0, 2014);
  private Date Mar15 = dateBuilder(15, 2, 2014);
  private Date Mar29 = dateBuilder(29, 2, 2014);
  private Date Jun25 = dateBuilder(25, 5, 2014);
  private Date Jul09 = dateBuilder(9, 6, 2014);

  public void setUpFirstLoan()
  {
    when(firstJimLoansCatch22_.getBook()).thenReturn(catch22_);
    when(firstJimLoansCatch22_.getBorrower()).thenReturn(jim_);
    when(firstJimLoansCatch22_.getID()).thenReturn(1);
    when(firstJimLoansCatch22_.isCurrent()).thenReturn(false);
    when(catch22_.getTitle()).thenReturn("CATCH-22");
    when(catch22_.getAuthor()).thenReturn("Joseph Heller");
    when(catch22_.getState()).thenReturn(EBookState.AVAILABLE); // TODO: check!
  }

  public void setUpSecondLoan()
  {
    when(secondSamLoansEmma_.getBook()).thenReturn(emma_);
    when(secondSamLoansEmma_.getBorrower()).thenReturn(sam_);
    when(secondSamLoansEmma_.getID()).thenReturn(2);
    when(secondSamLoansEmma_.isCurrent()).thenReturn(false);
    when(emma_.getTitle()).thenReturn("Emma");
    when(emma_.getAuthor()).thenReturn("Jane Austen");
    when(emma_.getState()).thenReturn(EBookState.AVAILABLE);
  }

  public void setUpThirdLoan()
  {
    when(thirdJillLoansCatch22_.getBook()).thenReturn(catch22_);
    when(thirdJillLoansCatch22_.getBorrower()).thenReturn(jill_);
    when(thirdJillLoansCatch22_.getID()).thenReturn(3);
    when(thirdJillLoansCatch22_.isCurrent()).thenReturn(true);
    when(catch22_.getTitle()).thenReturn("CATCH-22");
    when(catch22_.getAuthor()).thenReturn("Joseph Heller");
    when(catch22_.getState()).thenReturn(EBookState.ON_LOAN);;
  }

  public void setUpFourthLoan()
  {
    when(fourthJimLoansScoop_.getBook()).thenReturn(scoop_);
    when(fourthJimLoansScoop_.getBorrower()).thenReturn(jim_);
    when(fourthJimLoansScoop_.getID()).thenReturn(4);
    when(fourthJimLoansScoop_.isCurrent()).thenReturn(true);
    when(scoop_.getTitle()).thenReturn("Scoop");
    when(scoop_.getAuthor()).thenReturn("Evelyn Waugh");
    when(scoop_.getState()).thenReturn(EBookState.ON_LOAN);
  }

  public void setUpFifthLoan()
  {
    when(fifthJillLoansDune_.getBook()).thenReturn(dune_);
    when(fifthJillLoansDune_.getBorrower()).thenReturn(jill_);
    when(fifthJillLoansDune_.getID()).thenReturn(5);
    when(fifthJillLoansDune_.isCurrent()).thenReturn(true);
    when(dune_.getTitle()).thenReturn("Dune");
    when(dune_.getAuthor()).thenReturn("Frank Herbert");
    when(dune_.getState()).thenReturn(EBookState.ON_LOAN);
  }

  //===========================================================================
  // Test constructor - with LoanBuilder (for stubs) & LoanReflection (to
  // create new LoanDAOs)
  //===========================================================================

  @Test
  public void createLoanDao()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    assertThat(dao).isNotNull();
    assertThat(dao).isInstanceOf(LoanDAO.class);
  }



  @Test
  public void createLoanDaoWithNullHelperThrows()
  {
    ILoanHelper loanHelper = null;

    try {
      LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    }
    catch (Exception exception) {
    assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }

  //===========================================================================
  // Test createLoan -  - with LoanBuilder (for stubs & mocks) &
  // LoanReflection (to create new LoanDAOs) & fixtures for creating loan
  //===========================================================================

  @Test
  public void createLoanCallsLoanHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(jim_, catch22_);

    verify(loanHelper).makeLoan(catch22_, jim_, today, due);
  }



  @Test
  public void createLoanReturnsILoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(catch22_, jim_, today, due))
        .thenReturn(firstJimLoansCatch22_);
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(jim_, catch22_);

    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void createLoanGetsExpectedLoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(catch22_, jim_, today, due))
        .thenReturn(firstJimLoansCatch22_);
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(jim_, catch22_);

    assertThat(loan).isSameAs(firstJimLoansCatch22_);
  }


  @Test
  public void createLoanWithNullBookThrows()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(null, jim_, today, due))
                   .thenThrow(new IllegalArgumentException());
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    try {
      ILoan loan = dao.createLoan(jim_, null);
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNullBorrowerThrows()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(catch22_, null, today, due))
        .thenThrow(new IllegalArgumentException());
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    try {
      ILoan loan = dao.createLoan(null, catch22_);
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }

  //===========================================================================
  // Test listLoans - with LoanBuilder (for stubs) & LoanReflection (to create
  // LoanDAO & add Loans to loanMap)
  //===========================================================================

  @Test
  public void loanListIsNullInitially()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    List<ILoan> allLoans = dao.listLoans();

    assert(allLoans).isEmpty();
  }



  @Test
  public void loanListReturnsLoansManuallySetInLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    ILoan[] loans = { firstJimLoansCatch22_, secondSamLoansEmma_};

    setPrivateLoanMap(dao, loans);
    List<ILoan> allLoans = dao.listLoans();

    assertThat(allLoans).isNotEmpty();
    assertThat(allLoans).hasSize(2);
    assertThat(allLoans).containsExactly(firstJimLoansCatch22_, secondSamLoansEmma_);
  }



  @Test
  public void loanListReturnsInSequenceLoansManuallySetInLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    ILoan[] loans = {thirdJillLoansCatch22_, secondSamLoansEmma_,
                     firstJimLoansCatch22_ };

    setPrivateLoanMap(dao, loans);
    List<ILoan> allLoans = dao.listLoans();

    assertThat(allLoans).isNotEmpty();
    assertThat(allLoans).hasSize(3);
    assertThat(allLoans).containsSequence(thirdJillLoansCatch22_,
                                          secondSamLoansEmma_,
                                          firstJimLoansCatch22_ );
  }

  //===========================================================================
  // Testing the test fixtures :-)
  // ensure that broken text fixtures do not cause tests to fail/pass
  //===========================================================================

  @Test
  public void setUpLoansHelperWorksCorrectly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

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

  @Test
  public void commitLoanCallsLoanCommitCorrectly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    ILoan loan = mockLoan();

    dao.commitLoan(loan);

    // works because new LoanDAO has nextID set to 1 initially
    verify(loan).commit(anyInt());
  }



  @Test
  public void commitLoanCallsLoanCommitWithCorrectId()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    ILoan loan = mockLoan();

    // using reflection to set private nextID
    setPrivateNextId(dao, 999);
    dao.commitLoan(loan);

    verify(loan).commit(999);
  }



  @Test
  public void commitLoanCallsLoanCommitWithCorrectIdInSequence()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
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



  @Test
  public void commitLoanAddsLoanToLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    allLoans = dao.listLoans();

    assertThat(allLoans).isNotEmpty();
    assertThat(allLoans).hasSize(1);
    assertThat(allLoans).containsExactly(firstJimLoansCatch22_);
  }



  @Test
  public void commitLoansAddsMultipleLoansToLoanMap()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
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
  // Test getLoanByID - with LoanBuilder (for stubs), LoanReflection
  // (to create new LoanDAOs) & fixtures for loans
  //===========================================================================

  @Test
  public void getLoanByIdReturnsNullIfLoanMapEmpty()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();

    ILoan loan = dao.getLoanByID(1);

    assertThat(loan).isNull();
  }



  @Test
  public void getLoanByIdReturnsLoanIfLoanMapContainsOnlyThatLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_);

    ILoan loan = dao.getLoanByID(1);

    assertThat(loan).isNotNull();
    assertThat(loan).isEqualTo(firstJimLoansCatch22_);
  }



  @Test
  public void getLoanByIdReturnsLoanIfLoanMapContainsMultipleLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
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



  @Test
  public void getLoanByIdReturnsNullIfLoanMapDoesNotContainsLoanWithThatId()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
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
  // Test getLoanByBook - with LoanBuilder (for stubs), LoanReflection
  // (to create new LoanDAOs) & fixtures for loans & books
  //===========================================================================

  @Test
  public void getLoanByBookReturnsNullIfNoLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();

    ILoan loan = dao.getLoanByBook(catch22_);

    assertThat(loan).isNull();
  }



  @Test
  public void getLoanByBookReturnsNullIfNoCurrentLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_); // returned
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);

    ILoan loan = dao.getLoanByBook(catch22_);

    assertThat(loan).isNull();
  }



  @Test
  public void getLoanByBookReturnsCurrentLoanWhenOnlyLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_); // current

    ILoan loan = dao.getLoanByBook(catch22_);

    assertThat(loan).isSameAs(thirdJillLoansCatch22_);
  }



  // TODO: complete getLoanByBook tests
  @Test
  public void getLoanByBookIsNotNullIfCurrentLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_); // returned
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_); // current

    ILoan loan = dao.getLoanByBook(catch22_);

    assertThat(loan).isNotNull();
  }



  @Test
  public void getLoanByBookReturnsCurrentLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_); // returned
    setUpSecondLoan();
    dao.commitLoan(secondSamLoansEmma_);
    setUpThirdLoan();
    dao.commitLoan(thirdJillLoansCatch22_); // current

    ILoan loan = dao.getLoanByBook(catch22_);

    assertThat(loan).isNotSameAs(firstJimLoansCatch22_);
    assertThat(loan).isSameAs(thirdJillLoansCatch22_);
  }

  //===========================================================================
  // Test findLoansByBorrower - with LoanBuilder (for stubs), LoanReflection
  // (to create new LoanDAOs) & fixtures for loans & books
  //===========================================================================

  @Test
  public void findLoansByBorrowerReturnsNullIfNoLoans()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();

    List<ILoan> loans = dao.findLoansByBorrower(jim_);

    assertThat(loans).isNull();
  }



  @Test
  public void findLoansByBorrowerReturnsNullIfNoLoansByBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
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



  @Test
  public void findLoansByBorrowerReturnsLoanIfSingleLoanByBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    List<ILoan> allLoans = dao.listLoans();
    assertThat(allLoans).isEmpty();
    setUpFirstLoan();
    dao.commitLoan(firstJimLoansCatch22_); // our book

    List<ILoan> loans = dao.findLoansByBorrower(jim_);

    assertThat(loans).isNotNull();
    assertThat(loans).containsExactly(firstJimLoansCatch22_);
  }



  @Test
  public void findLoansByBorrowerReturnsLoanIfMultipleLoanByBorrower()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
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
