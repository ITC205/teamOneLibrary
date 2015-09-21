package test.unit;

import java.util.Date;

import java.lang.reflect.Constructor;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import library.daos.LoanHelper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.TestLoanBuilder.*;
import static test.unit.TestLoanReflection.*;

/**
 * Unit tests for Loan Helper.
 */
public class TestLoanHelper
{
  //===========================================================================
  // Primary methods testing - using stubs & dateBuilder (from TestLoanBuilder)
  // & helpers for creating a LoanHelper & check Loan state (TestLoanReflection)
  //===========================================================================

  @Test
  public void makeLoan()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void makeLoanWithNullBorrowerThrows()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

    IBook book = stubBook();
    IMember borrower = null;
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    // When create a loan
    try {
      ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void makeLoanWithDueDateBeforeBorrowDateThrows()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(19, 11, 2015);

    try {
      ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);
    }

    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void makeLoanSetsStatePending()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);
    ELoanState currentState = getPrivateState((Loan)loan);

    assertThat(currentState).isEqualTo(ELoanState.PENDING);
  }



  @Test
  public void makeLoanSetsIdZero()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    assertThat(loan.getID()).isEqualTo(0);
  }


  @Test
  public void makeLoanFactoryCreatesUniqueLoans()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

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


  // TODO: use mocks to create 2 instances to compare
  @Test
  public void makeLoanFactoryCreatesLoansWithDifferentBooks()
  {
    LoanHelper helper = createLoanHelperWithPrivateConstructor();

    IBook book = stubBook();
    IMember firstBorrower = stubMember();
    IMember secondBorrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan firstLoan = helper.makeLoan(book, firstBorrower, borrowDate, dueDate);

    ILoan secondLoan = helper.makeLoan(book, secondBorrower, borrowDate, dueDate);



    // Then can return borrower and verify it is same Member as local instance
    IMember loanBorrower = firstLoan.getBorrower();
    IMember secondLoanBorrower = secondLoan.getBorrower();

    // assertThat().isNotEqualTo();
  }

}
