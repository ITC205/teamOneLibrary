package test.unit;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import library.daos.BookDAO;
import library.daos.LoanDAO;
import library.entities.Loan;
import library.entities.Book;
import library.entities.Member;
import library.interfaces.EBorrowState;
import library.interfaces.entities.*;
import static org.mockito.Mockito.*;


/**
* The TestMember class implements unit testing
* on methods contained within the Member class
*
* @author  Rebecca Callow
*/
public class TestMember extends TestCase
{
  
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  String validFirstName = "TestFirstName";
  String validLastName = "TestLastName";
  String validContactPhone = "01234567";
  String validEmailAddress = "abc@gef.com";
  int validId = 1;
  Throwable exception = null;
  
  Member validMember = new Member(validFirstName, validLastName, validContactPhone, validEmailAddress, validId);
  
  Loan mockLoan = mock(Loan.class);
  Book mockBook = mock(Book.class);
  BookDAO mockBookDAO = mock(BookDAO.class);
  LoanDAO mockLoanDAO = mock(LoanDAO.class);
  ILoan loan;
  
  
  
  protected void setUp()
  {

    
    when(mockBook.getAuthor()).thenReturn("Author1");
    when(mockBook.getTitle()).thenReturn("Title1");
    when(mockBook.getCallNumber()).thenReturn("ABC123");
    when(mockBook.getState()).thenReturn(EBookState.AVAILABLE);
    when(mockBook.getID()).thenReturn(1);
    mockBookDAO.addBook("Author1", "Title1", "ABC123");
    when(mockBookDAO.getBookByID(1)).thenReturn(mockBook);
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date dateBorrowed;
    Date dueDate;
    try {
      dueDate = dateFormat.parse("15/10/15");
      dateBorrowed = dateFormat.parse("01/10/15");
      mockLoan = new Loan(mockBook, validMember, dateBorrowed, dueDate);
      mockLoanDAO.createLoan(validMember, mockBook);
      
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    when(mockLoanDAO.getLoanByID(1)).thenReturn(mockLoan);

    
    
  }
  
  
  
  // ==========================================================================
  // Methods: Tests
  // ==========================================================================
  
  
  
  // Test that the Member constructor instantiates a valid object, or 
  // returns the required exceptions
  public void testMember()
  {
    String nullFirstName = null;
    String nullLastName = null;
    String nullContactPhone = null;
    String nullEmailAddress = null;
    int zeroMemberId = 0;    
    
    
    assertEquals(validFirstName, validMember.getFirstName());
    assertEquals(validLastName, validMember.getLastName());
    assertEquals(validContactPhone, validMember.getContactPhone());
    assertEquals(validId, validMember.getId());
    assertEquals(validMember.getState(), EMemberState.BORROWING_ALLOWED);
    
    try
    {
    Member memberFirstNameNull = new Member(nullFirstName, validLastName, validContactPhone, validEmailAddress, validId);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
    
    try
    {
    Member memberLastNameNull = new Member(validFirstName, nullLastName, validContactPhone, validEmailAddress, validId);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
    
    try
    {
    Member memberContactPhoneNull = new Member(validFirstName, validLastName, nullContactPhone, validEmailAddress, validId);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
    
    try
    {
    Member memberEmailAddressNull = new Member(validFirstName, validLastName, validContactPhone, nullEmailAddress, validId);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
    
    try
    {
    Member memberZeroId = new Member(validFirstName, validLastName, validContactPhone, validEmailAddress, zeroMemberId);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
  }
  
  
  
  // Test that fines are added accurately
  public void testAddFine()
  {
    validMember.addFine(1.0f);
    assertEquals(1.0f, validMember.getTotalFines());
    
    validMember.addFine(5.0f);
    assertEquals(6.0f, validMember.getTotalFines());

    try
    {
    validMember.addFine(-1.0f);
    }
    
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
  }
  
  
  
  // Test that hasFinesPayable returns true if there are outstanding fines
  public void testHasFinesPayable()
  {
    assertFalse(validMember.hasFinesPayable());
    validMember.addFine(5.0f);
    assertTrue(validMember.hasFinesPayable());
  }
  
  
  
  // Test that hasReachedFineLimit returns true if the member has reached the
  // maximum allowable outstanding fines
  public void testHasReachedFineLimit()
  {
    assertEquals(validMember.getState(), EMemberState.BORROWING_ALLOWED);
    assertFalse(validMember.hasReachedFineLimit());
    validMember.addFine(20.0f);
    assertTrue(validMember.hasReachedFineLimit());
    assertEquals(validMember.getState(), EMemberState.BORROWING_DISALLOWED);
  }
  
  
  
  // Test that paid fines are subtracted from the total of outstanding fines
  // and that the payment cannot exceed the amount owing
  public void testPayFine()
  {
    validMember.addFine(15.0f);
    validMember.payFine(10.0f);
    assertEquals(5.0f, validMember.getTotalFines());
    validMember.payFine(5.0f);
    assertEquals(0.0f, validMember.getTotalFines());
    validMember.addFine(5.0f);
    
    try
    {
    validMember.payFine(6.0f);
    }
    
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
    
    try
    {
    validMember.payFine(-6.0f);
    }
    
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
  }
  
  
  
  // Test that loan objects can be added to the list for a given member
  public void testAddLoan()
  {
    validMember.addLoan(mockLoan);
    assertTrue(validMember.getLoans().size() > 0);

    mockLoan = null;
    try
    {
      validMember.addLoan(mockLoan);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
  }

  
  
  // Test that member state is updated to BORROWING_DISALLOWED 
  // when the loan limit is reached
  public void testHasReachedLoanLimit()
  {
    assertEquals(validMember.getState(), EMemberState.BORROWING_ALLOWED);
    assertFalse(validMember.hasReachedLoanLimit());
    for (int n = 1; n <= 5; n++)
    {
      validMember.addLoan(mockLoan);
    }
    assertTrue(validMember.hasReachedLoanLimit());
    assertEquals(validMember.getState(), EMemberState.BORROWING_DISALLOWED);
  }
  
  
  
  // Ensure that a member's ID is returned correctly
  public void testGetId()
  {
    assertEquals(validMember.getId(), 1);
  }
  
  
  
  // Test that Member state is updated to BORROWING_DISALLOWED if there are overdue loans
  public void testHasOverDueLoans()
  {    
    // mockLoan is not currently overdue
    assertFalse(validMember.hasOverDueLoans());
    
    // Set the loan state of mockLoan to OVERDUE
    setState(ELoanState.OVERDUE);
    validMember.addLoan(mockLoan);
    
    // validMember should now have an overdue loan
    assertTrue(validMember.hasOverDueLoans());
  }
  
  
  
  public void testRemoveLoan()
  {
    // Set the loan state of mockLoan to OVERDUE for access through Member methods
    setState(ELoanState.OVERDUE);
    validMember.addLoan(mockLoan);
    
    // validMember should now have an overdue loan
    assertTrue(validMember.hasOverDueLoans());
    
    validMember.removeLoan(mockLoan);
    assertFalse(validMember.hasOverDueLoans());
  }
  
  
  
  public void testIsRestricted()
  {
    assertFalse(validMember.isRestricted());
    setState(ELoanState.OVERDUE);
    validMember.addLoan(mockLoan);
    assertTrue(validMember.isRestricted());
    
    validMember.removeLoan(mockLoan);
    validMember.addFine(20.0f);
    assertTrue(validMember.isRestricted());
    
    validMember.payFine(20.0f);
    assertFalse(validMember.isRestricted());
    for (int n = 0; n < 5; n++)
    {
      validMember.addLoan(mockLoan);
    }
    assertTrue(validMember.isRestricted());
  }
  
  
  
  public void testLoanHasBecomeOverdue()
  {
    validMember.addLoan(mockLoan);
    
    assertEquals(validMember.getState(), EMemberState.BORROWING_ALLOWED);
    
    setState(ELoanState.OVERDUE);
    
    validMember.loanHasBecomeOverdue(mockLoan);
    
    assertEquals(validMember.getState(), EMemberState.BORROWING_DISALLOWED);
  }
  

  
  private void setState(ELoanState newState) 
  {
      // Use reflection to access BorrowUC_CTL.state

      Class<?> loanClass = mockLoan.getClass();
      try {
        Field state_ = loanClass.getDeclaredField("state_");

      state_.setAccessible(true);

      state_.set(mockLoan, newState);
      }
      catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
  }
}
