package test.unit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import static test.helper.DateBuilder.*;
import static test.helper.DoubleBuilder.*;
import static test.helper.LoanBuilder.*;
import static test.helper.LoanReflection.*;

import library.interfaces.entities.ELoanState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import library.entities.Loan;

/**
 * Unit tests for Loan entity.
 * Uses DoubleBuilder for creation of stubs & mocks, DateBuilder for all things
 * date related and LoanReflection for checking private fields.
 *
 * @author nicholasbaldwin
 */
public class TestLoan
{
  // ==========================================================================
  // Constructor Testing - with stubs & LoanReflection (to check state)
  // ==========================================================================

  @Test
  public void createLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a valid loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then loan is instantiated and a valid Loan instance
    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void newLoanHasPendingState()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a valid loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then loan is instantiated with pending state
    // (have to use reflection to access private variable)
    // which is why this test was added as one of last tests :-)
    ELoanState loanState = getPrivateState((Loan)loan);
    assertThat(loanState).isEqualTo(ELoanState.PENDING);
  }



  @Test
  public void createLoanWithNullBookThrows()
  {
    // Given null book and stub for member
    IBook book = null;
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNullBorrowerThrows()
  {
    // Given stub for book and null member
    IBook book = stubBook();
    IMember borrower = null;
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNullBorrowDateThrows()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With null borrowDate
    Date borrowDate = null;
    Date dueDate = new Date(2);

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNullDueDateThrows()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With null due date
    Date borrowDate = new Date(1);
    Date dueDate = null;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNullBookThrowsWithCorrectMessage()
  {
    // Given null book and stub for member
    IBook book = null;
    IMember borrower = stubMember();
    // With valid, but very simple dates in millis
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).hasMessage("Cannot create a new Loan with a " +
                                           "null Book.");
    }
  }



  @Test
  public void createLoanWithNicelyFormattedDates()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with formatted dates to be assigned
    Date borrowDate = null;
    Date dueDate = null;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("18/09/2015");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then loan is instantiated, and a valid ILoan instance
    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void createLoanWithDateBuilder()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();

    // with same dates as in previous test (using formatted dates) - this time
    // using the DateBuilder heper
    Date borrowDate = dateBuilder(17, 8, 2015);
    Date dueDate = dateBuilder(18, 8, 2015);

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then loan is instantiated, and a valid ILoan instance
    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void createLoanWithDueDateSameAsBorrowDate()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // valid borrow date
    Date borrowDate = dateBuilder(17, 8, 2015);
    // and dueDate is same the borrowDate
    Date dueDate = dateBuilder(17, 8, 2015);

    // When create a loan

    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then loan is instantiated, and a valid ILoan instance
    assertThat(loan).isInstanceOf(ILoan.class);
  }



  @Test
  public void createLoanWithDueDateBeforeBorrowDateThrows()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // valid borrow date
    Date borrowDate = dateBuilder(17, 8, 2015);
    // but dueDate is before the borrowDate
    Date dueDate = dateBuilder(16, 8, 2015);

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithDueDateBeforeBorrowDateThrowsWithCorrectMessage()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // valid borrow date
    Date borrowDate = dateBuilder(17, 8, 2015);
    // but dueDate is before the borrowDate
    Date dueDate = dateBuilder(16, 8, 2015);

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).hasMessage("Cannot create a new Loan when the " +
                                       "Due Date is less than the Borrowing " +
                                       "Date.");
    }
  }

  // ==========================================================================
  // Getters & setters testing - with stubs
  // ==========================================================================

  @Test
  public void getBorrowerFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();

    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then can return borrower and verify it is same Member as local instance
    IMember loanBorrower = loan.getBorrower();
    assertThat(loanBorrower).isSameAs(borrower);
  }



  @Test
  public void getBorrowerFirstNameFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    String borrowerFirstName = "Jim";
    when(borrower.getFirstName()).thenReturn(borrowerFirstName);

    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then can return borrower and verify it is same Member as local instance
    IMember loanBorrower = loan.getBorrower();
    String loanBorrowerFirstName = loanBorrower.getFirstName();
    assertThat(loanBorrower).isSameAs(borrower);
    assertThat(loanBorrowerFirstName).isEqualTo(borrowerFirstName);
  }



  @Test
  public void getBookFromLoan()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    String author = "Charles Dickens";
    when(book.getAuthor()).thenReturn(author);
    IMember borrower = stubMember();

    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then can return book and verify it is same Member as local instance
    IBook loanBook = loan.getBook();
    String loanBookAuthor = loanBook.getAuthor();
    assertThat(loanBook).isSameAs(book);
    assertThat(loanBookAuthor).isEqualTo(author);
  }



  @Test
  public void getID()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // With valid dates and ID
    Date borrowDate = new Date(1);
    Date dueDate = new Date(2);

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // Then can return ID
    int loanID = loan.getID();
    assertThat(loanID).isEqualTo(0);
  }

  // ==========================================================================
  // isOverDue() testing - with stubs, LoanBuilder.newLoan() (for generating
  // custom Loans) & LoanBuilder.dateBuilder() (for simpler date creation)
  // ==========================================================================

  @Test
  public void isOverDueWhenStateOverDueIsTrue()
  {
    // Given a manually set overdue loan
    ILoan loan = newLoan().makeOverDue().build();

    // Then loan state should be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isTrue();
  }



  @Test
  public void isOverDueWhenDefaultLoanIsFalse()
  {
    // Given a default loan (state is not overdue)
    ILoan loan = newLoan().build();

    // Then loan state should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
  }



  @Test
  public void isOverDueWhenStateCurrentIsFalse()
  {
    // Given a loan with a manually set status of current
    ILoan loan = newLoan().makeCurrent().build();

    // Then loan state should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
  }



  // ==========================================================================
  // isCurrent() testing - with stubs & LoanBuilder
  // ==========================================================================

  @Test
  public void isCurrentWhenStateCurrentIsTrue()
  {
    // Given a current loan
    ILoan loan = newLoan().withBorrowDate(1, 0, 2015)
                          .withDueDate(15, 0, 2015)
                          .makeCurrent().build();

    boolean isCurrent = loan.isCurrent();

    assertThat(isCurrent).isTrue();
  }



  @Test
  public void isCurrentWhenStatePendingIsFalse()
  {
    // Given a current loan
    ILoan loan = newLoan().withBorrowDate(1, 0, 2015)
                          .withDueDate(15, 0, 2015)
                          .makePending().build();

    boolean isCurrent = loan.isCurrent();

    assertThat(isCurrent).isFalse();
  }



  @Test
  public void isCurrentWhenStateOverDueIsFalse()
  {
    // Given a current loan
    ILoan loan = newLoan().withBorrowDate(1, 0, 2015)
                          .withDueDate(15, 0, 2015)
                          .makeOverDue().build();

    boolean isCurrent = loan.isCurrent();

    assertThat(isCurrent).isFalse();
  }



  @Test
  public void isCurrentWhenStateCompleteIsFalse()
  {
    // Given a current loan
    ILoan loan = newLoan().withBorrowDate(1, 0, 2015)
                          .withDueDate(15, 0, 2015)
                          .makeComplete().build();

    boolean isCurrent = loan.isCurrent();

    assertThat(isCurrent).isFalse();
  }



  // ==========================================================================
  // checkOverDue() testing - with stubs & LoanBuilder
  // ==========================================================================

  @Test
  public void checkOverDueWhenLoanDueNextCenturyIsFalse()
  {
    // Given a new 100 year loan!
    ILoan loan = newLoan().withBorrowDate(1, 0, 2010)
                          .withDueDate(1, 0, 2110)
                          .makeCurrent().build();

    // When today's date is the borrow date
    Date today = dateBuilder(1, 0, 2010);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDueNextDayIsFalse()
  {
    // Given a new loan with due date 1st January
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(1, 0, 2016)
                          .makeCurrent().build();

    // When today's date is 31st December
    Date today = dateBuilder(31, 11, 2015);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDueLaterSameDayIsFalse()
  {
    // Given a new loan with due date 31st December (@ 8pm)
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015, 20, 0, 0)
                          .withID(99)
                          .makeCurrent().build();

    // When today's date is 31st December (@ 10am)
    Date today = dateBuilder(31, 11, 2015, 10, 0, 0);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDueEarlierSameDayIsFalse()
  {
    // Given a new loan with due date 31st December (@ 2:45am)
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015, 10, 45, 00)
                          .withDueDate(31, 11, 2015, 2, 45, 00)
                          .withID(99)
                          .makeCurrent().build();

    // When today's date is 31st December (@ 11:55pm)
    Date today = dateBuilder(31, 11, 2015, 23, 55, 00);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDuePreviousDayIsTrue()
  {
    // Given a new loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();

    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);

    // Then loan should be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isTrue();
  }



  @Test
  public void checkOverDueWhenLoanDuePreviousWeekIsTrue()
  {
    // Given a new loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(25, 11, 2015)
                          .makeOverDue().build();

    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);

    // Then loan should be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isTrue();
  }



  @Test
  public void checkOverDueWhenStateCurrentAndLoanNotDueLeavesStateCurrent()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015, 0, 0, 0)
                          .makeCurrent().build();

    // When today's date is 31st December
    Date today = dateBuilder(31, 11, 2015, 15, 45, 00);
    // Then loan status should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
    // and loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();

    // Then loan state should not be overdue now (after checkOverDue)
    isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenStateCurrentButPastDueDateSetsStateOverDue()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();

    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);
    // and loan state is not yet overdue (check has not been run)
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
    // and loan is actually overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isTrue();

    // Then loan state should now be overdue (after checkOverDue)
    isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isTrue();
  }



  @Test
  public void checkOverDue_StateCurrentAndLoanNotDue_BorrowerStateAllowed()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015, 0, 0, 0)
                          .makeCurrent().build();

    // When today's date is 31st December
    Date today = dateBuilder(31, 11, 2015, 15, 45, 00);
    // Then loan status should not be overdue
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
    // thus borrower status should not be restricted
    boolean isBorrowerRestricted = loan.getBorrower().isRestricted();
    assertThat(isBorrowerRestricted).isFalse();
    // and loan should not be due
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();

    // Then borrower state should not be restricted now (after checkOverDue)
    isBorrowerRestricted = loan.getBorrower().isRestricted();
    assertThat(isBorrowerRestricted).isFalse();
  }



  @Test
  public void checkOverDue_StateCurrentButPastDueDate_BorrowerRestricted()
  {
    IMember mrMock = mockMember();
    when(mrMock.isRestricted()).thenReturn(false).thenReturn(true);
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrower(mrMock)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();
    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);
    // and loan state is not yet overdue (check has not been run)
    boolean isStateOverDue = loan.isOverDue();
    assertThat(isStateOverDue).isFalse();
    // and this borrower status should not yet be restricted
    boolean isBorrowerRestricted = loan.getBorrower().isRestricted();
    assertThat(isBorrowerRestricted).isFalse();
    // but loan is actually due
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isTrue();
    when(mockMember().isRestricted()).thenReturn(true);

    // Then borrower state should now be restricted (after checkOverDue)
    isBorrowerRestricted = loan.getBorrower().isRestricted();
    assertThat(isBorrowerRestricted).isTrue();
  }



  @Test
  public void checkOverDueWhenStateCompleteThrows()
  {
    // Given an old loan that was completed
    ILoan loan = newLoan().withBorrowDate(20, 11, 2014)
                          .withDueDate(31, 11, 2014)
                          .makeComplete().build();

    // When today's date is 1st January
    Date today = dateBuilder(1, 0, 2016);

    // When checkOverDue
    try {
      boolean isOverDue = loan.checkOverDue(today);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }



  @Test
  public void checkOverDueWhenStatePendingThrows()
  {
    // Given a pending loan
    ILoan loan = newLoan().withBorrowDate(20, 11, 2014)
                          .withDueDate(31, 11, 2014)
                          .makePending().build();

    // When today's date is 20th December (borrowing date)
    Date today = dateBuilder(20, 11, 2014);

    // When checkOverDue
    try {
      boolean isOverDue = loan.checkOverDue(today);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }

  // ==========================================================================
  // complete() testing - with stubs & LoanBuilder
  // ==========================================================================

  @Test
  public void completeWhenStateCurrentSetsStateToComplete()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();

    // When complete loan
    loan.complete();

    // Then loan state should change to complete
    // (have to use reflection to access private variable)
    boolean isLoanStateComplete =
            getPrivateState((Loan)loan) == ELoanState.COMPLETE;
    assertThat(isLoanStateComplete).isTrue();
  }



  @Test
  public void completeWhenStateOverDueSetsStateToComplete()
  {
    // Given an overdue loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeOverDue().build();

    // When complete loan
    loan.complete();

    // Then loan state should change to complete
    // (have to use reflection to access private variable)
    boolean isLoanStateComplete =
            getPrivateState((Loan)loan) == ELoanState.COMPLETE;
    assertThat(isLoanStateComplete).isTrue();
  }



  @Test
  public void completeWhenStateCompleteThrows()
  {
    // Given an old loan that was completed
    ILoan loan = newLoan().withBorrowDate(20, 11, 2014)
                          .withDueDate(31, 11, 2014)
                          .makeComplete().build();

    // When attempting to complete loan
    try {
      loan.complete();
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }



  @Test
  public void completeWhenStatePendingThrows()
  {
    // Given a pending loan
    ILoan loan = newLoan().withBorrowDate(20, 11, 2014)
                          .withDueDate(31, 11, 2014)
                          .makePending().build();

    // When attempting to complete loan
    try {
      loan.complete();
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }

  // ==========================================================================
  // commit() testing - with stubs, simple mocks & LoanBuilder
  // ==========================================================================

  @Test
  public void commitWhenStateCurrentThrows()
  {
    // Given a current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();

    // When attempting to complete loan
    try {
      loan.commit(999);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }



  @Test
  public void commitWhenStateOverDueThrows()
  {
    // Given an overdue loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeOverDue().build();

    // When attempting to complete loan
    try {
      loan.commit(999);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }



  @Test
  public void commitWhenStateCompleteThrows()
  {
    // Given a complete loan
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeComplete().build();

    // When attempting to complete loan
    try {
      loan.commit(999);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(RuntimeException.class);
    }
  }



  @Test
  public void commitWhenStatePendingSetsId()
  {
    // Given mock Book and Member
    IBook mockBook = mockBook();
    IMember mockBorrower = mockMember();

    // as part of a pending loan
    ILoan loan = newLoan().withBook(mockBook)
                          .withBorrower(mockBorrower)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makePending().build();
    int id = 999;
    loan.commit(id);

    // Then loan id should be set
    assertThat(loan.getID()).isEqualTo(999);
  }



  @Test
  public void commitLoanWithZeroIdThrows()
  {
    // Given loan using stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(1, 1, 2015);
    Date dueDate = dateBuilder(15, 1, 2015);
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // when commit with an id of 0
    try {
      loan.commit(0);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void commitLoanWithNegativeIdThrows()
  {
    // Given loan using stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(1, 1, 2015);
    Date dueDate = dateBuilder(15, 1, 2015);
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // when commit with a negative id
    try {
      loan.commit(-1);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void commitLoanWithNegativeIdThrowsWithCorrectMessage()
      throws ParseException
  {
    // Given loan using stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    Date borrowDate = dateBuilder(1, 1, 2015);
    Date dueDate = dateBuilder(15, 1, 2015);
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate);

    // when commit with a negative id
    try {
      loan.commit(-1);
    }

    // Then exception should be thrown
    catch (IllegalArgumentException exception) {
      assertThat(exception).hasMessage("Cannot commit a Loan with an ID " +
                                       "less than or equal to zero.");
    }
  }



  @Test
  public void commitWhenStatePendingSetsStateCurrent()
  {
    // Given mock Book and Member
    IBook mockBook = mockBook();
    IMember mockBorrower = mockMember();

    // as part of a pending loan
    ILoan loan = newLoan().withBook(mockBook)
                          .withBorrower(mockBorrower)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makePending().build();
    int id = 999;
    loan.commit(id);
    // Then loan state should change to current
    // (have to use reflection to access private variable)
    boolean isLoanStateCurrent =
        getPrivateState((Loan)loan) == ELoanState.CURRENT;
    assertThat(isLoanStateCurrent).isTrue();
  }



  @Test
  public void commitWhenStatePendingCallsBookBorrow()
  {
    // Given mock Book and Member
    IBook mockBook = mockBook();
    IMember mockBorrower = mockMember();

    // as part of a pending loan
    ILoan loan = newLoan().withBook(mockBook)
                          .withBorrower(mockBorrower)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makePending().build();
    int id = 999;
    loan.commit(id);

    // Then expect book.borrow called correctly
    verify(mockBook).borrow(loan);
  }



  @Test
  public void commitWhenStatePendingCallsBorrowerAddLoan()
  {
    // Given mock Book and Member
    IBook mockBook = mockBook();
    IMember mockBorrower = mockMember();

    // as part of a pending loan
    ILoan loan = newLoan().withBook(mockBook)
                          .withBorrower(mockBorrower)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makePending().build();
    int id = 999;
    loan.commit(id);

    // Then expect borrower.addLoan called correctly
    verify(mockBorrower).addLoan(loan);
  }


  // ==========================================================================
  // toString() testing - with stubs & LoanBuilder
  // ==========================================================================

  @Test
  public void toStringWhenStatePending()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    when(book.getAuthor()).thenReturn("Charles Dickens");
    when(book.getTitle()).thenReturn("Great Expectations");

    IMember borrower = stubMember();
    when(borrower.getFirstName()).thenReturn("Neil");
    when(borrower.getLastName()).thenReturn("Armstrong");

    ILoan loan = newLoan().withBook(book)
                          .withBorrower(borrower)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makePending().build();
    // When
    String loanString = loan.toString();

    // Then expect loanString to match (note differences in Date months)
    // and loan id is 0 (default)
    String expectedString = "Loan ID:  0\n" +
                            "Author:   Charles Dickens\n" +
                            "Title:    Great Expectations\n" +
                            "Borrower: Neil Armstrong\n" +
                            "Borrowed: 20/12/2015\n" +
                            "Due Date: 31/12/2015";
    assertThat(loanString).isEqualTo(expectedString);
  }



  @Test
  public void toStringWhenStateCurrentAndIdSet()
  {
    // Given stubs for book and member
    IBook book = stubBook();
    when(book.getAuthor()).thenReturn("Charles Dickens");
    when(book.getTitle()).thenReturn("Great Expectations");

    IMember borrower = stubMember();
    when(borrower.getFirstName()).thenReturn("Neil");
    when(borrower.getLastName()).thenReturn("Armstrong");

    ILoan loan = newLoan().withBook(book)
                          .withBorrower(borrower)
                          .withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .withID(99)
                          .makeCurrent().build();

    String loanString = loan.toString();

    // Then expect loanString to match (note differences in Date months)
    // including explicitly set id
    String expectedString = "Loan ID:  99\n" +
                            "Author:   Charles Dickens\n" +
                            "Title:    Great Expectations\n" +
                            "Borrower: Neil Armstrong\n" +
                            "Borrowed: 20/12/2015\n" +
                            "Due Date: 31/12/2015";
    assertThat(loanString).isEqualTo(expectedString);
  }

}
