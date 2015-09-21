package test.unit;

import java.util.Date;

import java.lang.reflect.Constructor;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;

import library.daos.LoanHelper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.TestLoanBuilder.*;

/**
 * Factory class for producing new
 */
public class TestLoanHelper
{
  //===========================================================================
  // Variables
  //===========================================================================



  //===========================================================================
  // Primary methods testing - with stubs & dateBuilder (from TestLoanBuilder)
  //===========================================================================

  @Test
  public void makeLoan()
  {
    LoanHelper helper = new LoanHelper();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    // Then loan should be instantiated
    assertThat(loan).isInstanceOf(ILoan.class);
  }


  @Test
  public void makeLoanHasDefaultIdZero()
  {
    LoanHelper helper = new LoanHelper();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    // Then loan should have default ID of zero
    assertThat(loan.getID()).isEqualTo(0);
  }


  @Test
  public void makeLoanIsFactoryForCreatingUniqueLoans()
  {
    LoanHelper helper = new LoanHelper();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan firstLoan = helper.makeLoan(book, borrower, borrowDate, dueDate);
    ILoan secondLoan = helper.makeLoan(book, borrower, borrowDate, dueDate);
    ILoan thirdLoan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    // Then loans are all unique instances (even if properties are the same)
    assertThat(firstLoan).isNotSameAs(secondLoan);
    assertThat(firstLoan).isNotSameAs(thirdLoan);
  }




  // TODO: check if LoanHelper constructor to be private & thus need this method
  /*
   * Uses Reflection API to directly access LoanHelper's private constructor.
   */
  private LoanHelper createLoanHelperWithPrivateConstructor()
  {
    try {
      Constructor constructor = LoanHelper.class.getConstructor(String.class);
      LoanHelper loanHelper = (LoanHelper)constructor.newInstance();
      return loanHelper;
    }

    catch (IllegalAccessException exception) {
      fail("IllegalAccessException should not occur");
    }
    catch (Exception exception) {
      fail("Exception should not occur");
    }
    return null;
  }

}
