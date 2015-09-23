package test.unit;

import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import library.daos.LoanHelper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;

/**
 * Unit tests for Loan Helper.
 */
public class TestLoanHelper
{
  //===========================================================================
  // Sole method testing - using LoanBuilder (for stubs, & dateBuilder)
  // & LoanReflection (to create LoanHelper and check private Loan properties)
  //===========================================================================

  @Test
  public void makeLoan()
  {
    LoanHelper helper = createLoanHelperWithProtectedConstructor();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void makeLoanSetsExpectedProperties()
  {
    LoanHelper helper = createLoanHelperWithProtectedConstructor();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);
    Date loanBorrowDate = getPrivateBorrowDate((Loan)loan);
    Date loanDueDate = getPrivateDueDate((Loan)loan);

    assertThat(loan.getBook()).isEqualTo(book);
    assertThat(loan.getBorrower()).isEqualTo(borrower);
    assertThat(loanBorrowDate).isEqualTo(borrowDate);
    assertThat(loanDueDate).isEqualTo(dueDate);
  }



  @Test
  public void makeLoanWithNullBorrowerThrows()
  {
    LoanHelper helper = new LoanHelper();

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
    LoanHelper helper = new LoanHelper();

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
    LoanHelper helper = new LoanHelper();

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
    LoanHelper helper = new LoanHelper();

    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(20, 11, 2015);
    Date dueDate = dateBuilder(31, 11, 2015);

    ILoan loan = helper.makeLoan(book, borrower, borrowDate, dueDate);

    assertThat(loan.getID()).isEqualTo(0);
  }



  @Test
  public void makeLoanFactoryCreatesUniqueLoansWhenPropertiesSame()
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



  @Test
  public void makeLoanFactoryCanCreateLoansWithDifferentProperties()
  {
    LoanHelper helper = new LoanHelper();

    IBook firstBook = stubBook();
    when(firstBook.getAuthor()).thenReturn("Charles Dickens");
    when(firstBook.getTitle()).thenReturn("Great Expectations");
    IMember firstBorrower = stubMember();
    when(firstBorrower.getFirstName()).thenReturn("Jimmy");
    when(firstBorrower.getLastName()).thenReturn("Jones");
    Date firstBorrowDate = dateBuilder(10, 11, 2015);
    Date firstDueDate = dateBuilder(21, 11, 2015);

    IBook secondBook = stubBook();
    when(secondBook.getAuthor()).thenReturn("Jane Austen");
    when(secondBook.getTitle()).thenReturn("Pride and Prejudice");
    IMember secondBorrower = stubMember();
    when(secondBorrower.getFirstName()).thenReturn("Sam");
    when(secondBorrower.getLastName()).thenReturn("Smith");
    Date secondBorrowDate = dateBuilder(20, 11, 2015);
    Date secondDueDate = dateBuilder(31, 11, 2015);

    ILoan firstLoan = helper.makeLoan(firstBook, firstBorrower,
                                      firstBorrowDate, firstDueDate);

    ILoan secondLoan = helper.makeLoan(secondBook, secondBorrower,
                                       secondBorrowDate, secondDueDate);

    IBook firstLoanBook = firstLoan.getBook();
    IBook secondLoanBook = secondLoan.getBook();
    IMember firstLoanBorrower = firstLoan.getBorrower();
    IMember secondLoanBorrower = secondLoan.getBorrower();
    Date firstLoanBorrowDate = getPrivateBorrowDate((Loan)firstLoan);
    Date secondLoanBorrowDate = getPrivateBorrowDate((Loan)secondLoan);
    Date firstLoanDueDate = getPrivateDueDate((Loan)firstLoan);
    Date secondLoanDueDate = getPrivateDueDate((Loan)secondLoan);

    assertThat(firstLoanBook).isNotEqualTo(secondLoanBook);
    assertThat(firstLoanBook.getAuthor())
                            .isNotEqualTo(secondLoanBook.getAuthor());

    assertThat(firstLoanBorrower).isNotEqualTo(secondLoanBorrower);
    assertThat(firstLoanBorrower.getLastName())
        .isNotEqualTo(secondLoanBorrower.getLastName());

    assertThat(firstLoanBorrowDate).isNotEqualTo(secondLoanBorrowDate);
    assertThat(firstLoanDueDate).isNotEqualTo(secondLoanDueDate);
  }

}
