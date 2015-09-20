package test.unit;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import org.junit.Test;
import org.junit.Ignore;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.TestLoanBuilder.*;

/**
 * Unit tests for Loan entity.
 */
public class TestLoan
{
  // ==========================================================================
  // Stub helpers
  // ==========================================================================

  /**
   * Create stub Book with Mockito. Simply provides an explicitly named method
   * to show that stubs are being used (not mocks or other fakes/doubles) :-)
   * @return stub Book.
   */
  public static IBook stubBook()
  {
    return mock(IBook.class);
  }



  /**
   * Create stub Member with Mockito. Simply provides an explicitly named method
   * to show that stubs are being used (not mocks or other fakes/doubles) :-)
   * @return stub Member.
   */
  public static IMember stubMember()
  {
    return mock(IMember.class);
  }

  // ==========================================================================
  // Constructor Testing - with stubs & helper (to check state)
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
    // and valid ID
    int id = 1;

    // When create a valid loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

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
    // and valid ID
    int id = 1;

    // When create a valid loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then loan is instantiated with pending state
    // (have to use reflection to access private variable)
    // which is why this test was added at end :-)
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
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
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
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
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
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
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
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
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
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
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
    // and valid ID
    int id = 1;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    try {
      borrowDate = dateFormat.parse("17/09/2015");
      dueDate = dateFormat.parse("18/09/2015");
    }
    catch (ParseException exception) {
      fail(exception.getMessage());
    }

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then loan is instantiated, and a valid ILoan instance
    assertThat(loan).isInstanceOf(ILoan.class);
  }



  // NOTE in following tests, simply propagating ParseException as this is
  // irrelevant for these tests (and proven ok in above test)

  @Test
  public void createLoanWithDueDateBeforeBorrowDateThrows()
    throws java.text.ParseException
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date borrowDate = dateFormat.parse("17/09/2015");
    // and dueDate is before the borrowDate
    Date dueDate = dateFormat.parse("16/09/2015");
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithDueDateSameAsBorrowDateThrows()
    throws java.text.ParseException
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date borrowDate = dateFormat.parse("17/09/2015");
    // dueDate is same as the borrowDate
    Date dueDate = dateFormat.parse("17/09/2015");
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithDueDateSameAsBorrowDateThrowsWithCorrectMessage()
    throws java.text.ParseException
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date borrowDate = dateFormat.parse("17/09/2015");
    // dueDate is same the borrowDate
    Date dueDate = dateFormat.parse("17/09/2015");
    // and valid ID
    int id = 1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).hasMessage("Cannot create a new Loan when the " +
                                       "Return Date is before or the same as " +
                                       "the Borrowing Date.");
    }
  }

  // TODO: need to check if borrow date later than return date - but same day?

  // TODO: confirm the validity of this test
  // this test was based on the specification, but it appears the spec is
  // incorrect, as pending loans are created with an id of zero
  @Ignore
  public void createLoanWithZeroIdThrows()
    throws java.text.ParseException
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date borrowDate = dateFormat.parse("01/01/2015");
    Date dueDate = dateFormat.parse("12/01/2015");
    // and ID equals zero
    int id = 0;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNegativeIdThrows()
    throws java.text.ParseException
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date borrowDate = dateFormat.parse("01/01/2015");
    Date dueDate = dateFormat.parse("12/01/2015");
    // and ID is negative
    int id = -1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }



  @Test
  public void createLoanWithNegativeIdThrowsWithCorrectMessage()
    throws java.text.ParseException
  {
    // Given stubs for book and member
    IBook book = stubBook();
    IMember borrower = stubMember();
    // with valid dates to be assigned
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date borrowDate = dateFormat.parse("01/01/2015");
    Date dueDate = dateFormat.parse("12/01/2015");
    // and negative ID
    int id = -1;

    // When create a loan
    try {
      ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);
    }

    // Then exception should be thrown
    catch (Exception exception) {
      assertThat(exception).hasMessage("Cannot create a new Loan with an ID " +
                                       "less than or equal to zero.");
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
    int id = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

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
    int id = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

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
    int id = 1;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

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
    int id = 125;

    // When create a loan
    ILoan loan = new Loan(book, borrower, borrowDate, dueDate, id);

    // Then can return ID
    int loanID = loan.getID();
    assertThat(loanID).isEqualTo(id);
  }

  // ==========================================================================
  // Primary methods testing - with stubs, TestLoanBuilder & dateBuilder
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

  // TODO: check status of items after isOverdue() changes status etc

  @Test
  public void checkOverDueWhenLoanDueNextCenturyIsFalse()
  {
    // Given a new 100 year loan!
    ILoan loan = newLoan().withBorrowDate(1,0,2010)
                          .withDueDate(1,0,2110)
                          .makeCurrent().build();

    // When today's date is the borrow date
    Date today = dateBuilder(1,0,2010);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDueNextDayIsFalse()
  {
    // Given a new loan with due date 1st January
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(1,0,2016)
                          .makeCurrent().build();

    // When today's date is 31st December
    Date today = dateBuilder(31,11,2015);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDueSameDayIsFalse()
  {
    // Given a new loan with due date 1st January
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(1,0,2016)
                          .makeCurrent().build();

    // When today's date is 1st January
    Date today = dateBuilder(31,11,2015);

    // Then loan should not be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isFalse();
  }



  @Test
  public void checkOverDueWhenLoanDuePreviousDayIsTrue()
  {
    // Given a new loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31, 11,2015)
                          .makeCurrent().build();

    // When today's date is 1st January
    Date today = dateBuilder(1,1,2016);

    // Then loan should be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isTrue();
  }


  @Test
  public void checkOverDueWhenLoanDuePreviousWeekIsTrue()
  {
    // Given a new loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(25,11,2015)
                          .makeOverDue().build();

    // When today's date is 1st January
    Date today = dateBuilder(1,1,2016);

    // Then loan should be overdue
    boolean shouldSetStatusToOverDue = loan.checkOverDue(today);
    assertThat(shouldSetStatusToOverDue).isTrue();
  }



  @Test
  public void checkOverDueWhenStateCurrentAndLoanNotDueLeavesStateCurrent()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20, 11, 2015)
                          .withDueDate(31, 11, 2015)
                          .makeCurrent().build();

    // When today's date is 31st December
    Date today = dateBuilder(31,11,2015);
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
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
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
  public void checkOverDueWhenStateCompleteThrows()
  {
    // Given an old loan that was completed
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
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
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
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



  // TODO: check if loan is always meant to be constructed with default loan
  // period - in which case need to test that behavior

  // TODO: check using dates with hours and seconds set



  @Test
  public void completeWhenStateCurrentSetsStateToComplete()
  {
    // Given a new current loan with due date 31st December
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
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
    ILoan loan = newLoan().withBorrowDate(20,11,2015)
                          .withDueDate(31,11,2015)
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
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
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
    ILoan loan = newLoan().withBorrowDate(20,11,2014)
                          .withDueDate(31,11,2014)
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
  // Commit() testing - with stubs, simple mocks & TestLoanBuilder
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
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
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
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
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
      assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }


  // TODO: public void commitWhenStatePendingSetsStateCurrent() : reflection

  @Test
  public void commitWhenStatePendingSetsId()
  {
    // Given mock Book and Member
    IBook mockBook = mock(IBook.class);
    IMember mockBorrower = mock(IMember.class);

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
  public void commitWhenStatePendingCallsBookBorrow()
  {
    // Given mock Book and Member
    IBook mockBook = mock(IBook.class);
    IMember mockBorrower = mock(IMember.class);

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
    IBook mockBook = mock(IBook.class);
    IMember mockBorrower = mock(IMember.class);

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
  public void toStringWhenStateCurrent()
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
    // When
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



  // ==========================================================================
  // Helper to check state - uses reflection
  // ==========================================================================

  /*
   * Uses Reflection API to directly access Loan's private state.
   */
  private ELoanState getPrivateState(Loan loan)
  {
    try {
      Class<?> loanClass = loan.getClass();
      java.lang.reflect.Field state = loanClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      return (ELoanState)state.get(loan);
    }

    catch (NoSuchFieldException exception) {
      fail("NoSuchFieldException should not occur");
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
