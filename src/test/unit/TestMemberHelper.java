package test.unit;

import junit.framework.*;
import library.daos.MemberHelper;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;


/**
* The TestMemberHelper class implements unit testing
* on the MemberHelper class
*
* @author  Rebecca Callow
*/
public class TestMemberHelper extends TestCase
{
  
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  IMemberHelper helper = new MemberHelper();



  // ==========================================================================
  // Methods: Tests
  // ==========================================================================
  
  
  
  // Test that a valid Member object is returned when makeMember is called
  public void testMakeMember()
  {
    IMember newMember = helper.makeMember("Jane", "Doe", "01234567", "jdoe@email.com", 1);
    assertTrue(newMember != null);
    
    assertEquals("Jane", newMember.getFirstName());
    assertEquals("Doe", newMember.getLastName());
    assertEquals("01234567", newMember.getContactPhone());
    assertEquals("jdoe@email.com", newMember.getEmailAddress());
    assertEquals(1, newMember.getId());
  }
  
}
