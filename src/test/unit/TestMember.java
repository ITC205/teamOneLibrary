package test.unit;

import junit.framework.*;
import library.entities.Member;

public class TestMember extends TestCase
{
  
  String validFirstName = "TestFirstName";
  String validLastName = "TestLastName";
  String validContactPhone = "01234567";
  String validEmailAddress = "abc@gef.com";
  int validId = 1;
  Throwable exception = null;
  
  Member validMember = new Member(validFirstName, validLastName, validContactPhone, validEmailAddress, validId);
  
  
  
  public TestMember(String name) 
  { 
    super(name);
  }
  
  
  
  public void testMember()
  {
    String nullFirstName = null;
    String nullContactPhone = null;
    int zeroMemberId = 0;    
    
    
    assertEquals(validFirstName, validMember.getFirstName());
    assertEquals(validLastName, validMember.getLastName());
    assertEquals(validContactPhone, validMember.getContactPhone());
    assertEquals(validId, validMember.getId());
    
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
    assertEquals(1.0f, validMember.getFineAmount());
    

    try
    {
    validMember.addFine(-1.0f);
    }
    
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    exception = null;
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
  }
  
  public void testAddLoan()
  {
    
  }
  
  
  
  

}
