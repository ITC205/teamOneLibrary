package test.unit;

import junit.framework.TestCase;
import library.entities.Member;
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
  
  ILoan mockLoan = mock(ILoan.class);
  
  
  
  // ==========================================================================
  // Methods: Tests
  // ==========================================================================
  
  
  
  // Test that the Member constructor instantiates a valid object, or 
  // returns the required exceptions
  public void testMember()
  {
    String nullFirstName = null;
    String nullContactPhone = null;
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
    validMember.addLoan(mockLoan);
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
  
  
  
  

}
