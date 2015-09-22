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
  private ILoan firstLoan = stubLoan();
  private ILoan secondLoan = stubLoan();
  private ILoan thirdLoan = stubLoan();
  private ILoan fourthLoan = stubLoan();
  private ILoan fifthLoan = stubLoan();

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



  public void setUpFiveLoans()
  {
    when(firstLoan.getBook()).thenReturn(greatExpectations);
    when(firstLoan.getBorrower()).thenReturn(jim);
    when(firstLoan.getID()).thenReturn(101);

    when(secondLoan.getBook()).thenReturn(prideAndPrejudice);
    when(secondLoan.getBorrower()).thenReturn(sam);
    when(secondLoan.getID()).thenReturn(102);

    when(thirdLoan.getBook()).thenReturn(greatExpectations);
    when(thirdLoan.getBorrower()).thenReturn(jill);
    when(thirdLoan.getID()).thenReturn(103);

    when(fourthLoan.getBook()).thenReturn(fightClub);
    when(fourthLoan.getBorrower()).thenReturn(jim);
    when(fourthLoan.getID()).thenReturn(104);

    when(fifthLoan.getBook()).thenReturn(fearAndLoathingInLasVegas);
    when(fifthLoan.getBorrower()).thenReturn(jill);
    when(fifthLoan.getID()).thenReturn(105);
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

    ILoan loan = dao.createLoan(greatExpectations, jim);

    verify(loanHelper).makeLoan(greatExpectations, jim, today, due);
  }



  @Test
  public void createLoanReturnsILoan()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(greatExpectations, jim, today, due))
        .thenReturn(firstLoan);
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(greatExpectations, jim);

    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void createLoanGetsExpectedLoanFromHelper()
  {
    Date today = ignoreTime(new Date());
    Date due = calculateDueDate(today);
    ILoanHelper loanHelper = stubHelper();
    when(loanHelper.makeLoan(greatExpectations, jim, today, due))
        .thenReturn(firstLoan);
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    ILoan loan = dao.createLoan(greatExpectations, jim);

    assertThat(loan).isSameAs(firstLoan);
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
