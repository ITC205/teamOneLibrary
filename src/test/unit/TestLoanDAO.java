package test.unit;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.daos.ILoanHelper;

import library.entities.Loan;

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
  private ILoan firstLoan;
  private ILoan secondLoan;
  private ILoan thirdLoan;
  private ILoan fourthLoan;
  private ILoan fifthLoan;

  private IMember jim = stubMember();
  private IMember sam = stubMember();
  private IMember jill = stubMember();

  private IBook greatExpectations = stubBook();
  private IBook prideAndPrejudice = stubBook();
  private IBook fightClub = stubBook();
  private IBook fearAndLoathingInLasVegas = stubBook();

  private Date Jan10 = dateBuilder(10, 0, 2014);
  private Date Jan24 = dateBuilder(24, 0, 2014);
  private Date Mar15 = dateBuilder(15, 2, 2014);
  private Date Mar29 = dateBuilder(29, 2, 2014);
  private Date Jun25 = dateBuilder(25, 5, 2014);
  private Date Jul09 = dateBuilder(9, 6, 2014);


  @org.junit.Before
  public void setUp()
  {
    // when(firstLoan)
  }


  //===========================================================================
  // Test constructor - with LoanBuilder (for stubs) & LoanReflection (to
  // create new LoanDAOs)
  //===========================================================================

  @org.junit.Test
  public void createLoanDao()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    assertThat(dao).isNotNull();
    assertThat(dao).isInstanceOf(LoanDAO.class);
  }



  @org.junit.Test
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
  // Primary methods
  //===========================================================================

  @Test
  public void createLoanCallsLoanHelper()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);

    dao.createLoan(greatExpectations, jim);

    verify(loanHelper).makeLoan(greatExpectations, jim, today, due);
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
