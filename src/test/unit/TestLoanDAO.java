package test.unit;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.daos.ILoanHelper;

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

  private ILoan firstLoan_ = stubLoan();
  private ILoan secondLoan_ = stubLoan();
  private ILoan thirdLoan_ = stubLoan();
  private ILoan fourthLoan_ = stubLoan();
  private ILoan fifthLoan_ = stubLoan();

  private IMember jim_ = stubMember();
  private IMember sam_ = stubMember();
  private IMember jill_ = stubMember();

  private IBook greatExpectations_ = stubBook();
  private IBook prideAndPrejudice_ = stubBook();
  private IBook fightClub_ = stubBook();
  private IBook fearAndLoathingInLasVegas_ = stubBook();

  private Date Jan10 = dateBuilder(10, 0, 2014);
  private Date Jan24 = dateBuilder(24, 0, 2014);
  private Date Mar15 = dateBuilder(15, 2, 2014);
  private Date Mar29 = dateBuilder(29, 2, 2014);
  private Date Jun25 = dateBuilder(25, 5, 2014);
  private Date Jul09 = dateBuilder(9, 6, 2014);
  
  public void setUpFirstLoan()
  {
    when(firstLoan_.getBook()).thenReturn(greatExpectations_);
    when(firstLoan_.getBorrower()).thenReturn(jim_);
    when(firstLoan_.getID()).thenReturn(101);
    when(greatExpectations_.getTitle()).thenReturn("Great Expectations");
  }

  public void setUpSecondLoan()
  {
    when(secondLoan_.getBook()).thenReturn(prideAndPrejudice_);
    when(secondLoan_.getBorrower()).thenReturn(sam_);
    when(secondLoan_.getID()).thenReturn(102);
    when(prideAndPrejudice_.getTitle()).thenReturn("Pride and Prejudice");
  }

  public void setUpThirdLoan()
  {
    when(thirdLoan_.getBook()).thenReturn(greatExpectations_);
    when(thirdLoan_.getBorrower()).thenReturn(jill_);
    when(thirdLoan_.getID()).thenReturn(103);
    when(greatExpectations_.getTitle()).thenReturn("Great Expectations");
  }

  public void setUpFourthLoan()
  {
    when(fourthLoan_.getBook()).thenReturn(fightClub_);
    when(fourthLoan_.getBorrower()).thenReturn(jim_);
    when(fourthLoan_.getID()).thenReturn(104);
    when(fightClub_.getTitle()).thenReturn("Fight Club");
  }

  public void setUpFifthLoan()
  {
    when(fifthLoan_.getBook()).thenReturn(fearAndLoathingInLasVegas_);
    when(fifthLoan_.getBorrower()).thenReturn(jill_);
    when(fifthLoan_.getID()).thenReturn(105);
    when(fearAndLoathingInLasVegas_.getTitle())
                                  .thenReturn("Fear and Loathing in Las Vegas");
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
  // LoanReflection (to create new LoanDAOs)
  //===========================================================================

  @Test
  public void createLoanCallsLoanHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = mockHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(greatExpectations_, jim_);

    verify(loanHelper).makeLoan(greatExpectations_, jim_, today, due);
  }



  @Test
  public void createLoanReturnsILoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(greatExpectations_, jim_, today, due))
        .thenReturn(firstLoan_);
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(greatExpectations_, jim_);

    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void createLoanGetsExpectedLoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(greatExpectations_, jim_, today, due))
        .thenReturn(firstLoan_);
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(greatExpectations_, jim_);

    assertThat(loan).isSameAs(firstLoan_);
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
      ILoan loan = dao.createLoan(greatExpectations_, jim_);
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
    when(loanHelper.makeLoan(greatExpectations_, null, today, due))
        .thenThrow(new IllegalArgumentException());
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    try {
      ILoan loan = dao.createLoan(greatExpectations_, jim_);
    }
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void setUpLoansHelperWorksCorrectly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    setUpFirstLoan();
    dao.commitLoan(firstLoan_);
    setUpSecondLoan();
    dao.commitLoan(secondLoan_);

    assertThat(firstLoan_.getBook()).isSameAs(greatExpectations_);
    assertThat(secondLoan_.getBook()).isSameAs(prideAndPrejudice_);
    assertThat(firstLoan_.getBook()).isNotSameAs(secondLoan_.getBook());
  }



  @Test
  public void commitLoanCallsLoanCommitCorrectly()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    ILoan loan = mockLoan();

    dao.commitLoan(loan);

    // works because new LoanDAO has nextID set to 1 intially
    verify(loan).commit(1);
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
    dao.commitLoan(firstLoan_);
    setUpSecondLoan();
    dao.commitLoan(secondLoan_);
    setUpThirdLoan();
    dao.commitLoan(thirdLoan_);

    verify(firstLoan_).commit(1);
    verify(secondLoan_).commit(2);
    verify(thirdLoan_).commit(3);
  }



  @Test
  public void commitLoanStoresLoan()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    setUpFirstLoan();
    dao.commitLoan(firstLoan_);

    // assertThat().has(loan);
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
