package test.unit;

import junit.framework.*;
import library.daos.MemberDAO;
import library.interfaces.entities.*;
import library.daos.MemberHelper;

public class TestMemberDAO extends TestCase
{
  MemberHelper helper = new MemberHelper();
  Throwable exception = null;
  
  public TestMemberDAO(String name) 
  { 
    super(name);
  }
  
  
  
  public void testMemberDAO()
  {
    try
    {
    MemberDAO nullMemberDAO = new MemberDAO(null);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    
  }
  
  
  
  public void testAddMember()
  {
    
  }
  
  

}
