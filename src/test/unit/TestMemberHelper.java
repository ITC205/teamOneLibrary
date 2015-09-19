package test.unit;

import junit.framework.*;
import library.daos.MemberHelper;
import library.interfaces.entities.IMember;

public class TestMemberHelper extends TestCase
{
  MemberHelper helper = new MemberHelper();

  public TestMemberHelper(String name) 
  { 
    super(name);
  }


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
