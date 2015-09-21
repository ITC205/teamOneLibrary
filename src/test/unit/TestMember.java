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
  
  String validFirstName = "TestFirstName";
  String validLastName = "TestLastName";
  String validContactPhone = "01234567";
  String validEmailAddress = "abc@gef.com";
  int validId = 1;
  Throwable exception = null;
  
  Member validMember = new Member(validFirstName, validLastName, validContactPhone, validEmailAddress, validId);
  
  ILoan mockLoan = mock(ILoan.class);
  
  
  
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
  
  
  
  public void testHasFinesPayable()
  {
    assertFalse(validMember.hasFinesPayable());
    validMember.addFine(5.0f);
    assertTrue(validMember.hasFinesPayable());
  }
  
  
  
  public void testHasReachedFineLimit()
  {
    assertFalse(validMember.hasReachedFineLimit());
    validMember.addFine(20.0f);
    assertTrue(validMember.hasReachedFineLimit());
    assertEquals(validMember.getState(), EMemberState.BORROWING_DISALLOWED);
  }
  
  
  
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
  
  
  
  public void testGetId()
  {
    assertEquals(validMember.getId(), 1);
  }
  
  
  
  

}
