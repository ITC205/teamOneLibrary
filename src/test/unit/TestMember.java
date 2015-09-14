package test.unit;

import junit.framework.*;
import library.entities.Member;

public class TestMember extends TestCase
{
  
  public TestMember(String name) 
  { 
    super(name);
  }
  
  
  
  public void testMember()
  {
    String firstName = "TestFirstName";
    String lastName = "TestLastName";
    String contactPhone = "01234567";
    String emailAddress = "abc@gef.com";
    Throwable exception = null;
    String nullFirstName = null;
    int id = 1;
    String nullContactPhone = null;
    int zeroMemberId = 0;
    
    Member testMember = new Member(firstName, lastName, contactPhone, emailAddress, id);
    assertEquals(firstName, testMember.getFirstName());
    assertEquals(lastName, testMember.getLastName());
    assertEquals(contactPhone, testMember.getContactPhone());
    assertEquals(id, testMember.getId());
    
    try
    {
    Member memberFirstNameNull = new Member(nullFirstName, lastName, contactPhone, emailAddress, id);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    
    try
    {
    Member memberContactPhoneNull = new Member(firstName, lastName, nullContactPhone, emailAddress, id);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    
    try
    {
    Member memberZeroId = new Member(firstName, lastName, contactPhone, emailAddress, zeroMemberId);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
  }
  
  
  
  
  
  
  
  

}
