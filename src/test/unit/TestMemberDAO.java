package test.unit;

import junit.framework.*;
import static org.mockito.Mockito.*;
import library.daos.MemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;


public class TestMemberDAO extends TestCase
{
  IMemberHelper mockHelper;
  IMember mockValidMember;
  Throwable exception = null;
  
  
  protected void createMocks()
  {

  }
  
  
  
  public void testMemberDAONull()
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
    mockHelper = mock(IMemberHelper.class);
    mockValidMember = mock(IMember.class);

    when(mockValidMember.getFirstName()).thenReturn("Joe");
    when(mockValidMember.getLastName()).thenReturn("Bloggs");
    when(mockValidMember.getContactPhone()).thenReturn("7654321");
    when(mockValidMember.getEmailAddress()).thenReturn("jbloggs@myemail.com");
    when(mockValidMember.getId()).thenReturn(1);
    when(mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1)).thenReturn(mockValidMember);
    
    IMember validMember = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    IMember testMember = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    assertEquals(validMember.getFirstName(), testMember.getFirstName());
    
  }
  
  

}
