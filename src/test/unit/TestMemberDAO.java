package test.unit;

import junit.framework.*;
import static org.mockito.Mockito.*;
import library.daos.MemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;


public class TestMemberDAO extends TestCase
{
  IMemberHelper mockHelper;
  IMember mockValidMemberOne;
  IMember mockValidMemberTwo;
  IMember mockValidMemberThree;
  IMember mockValidMemberFour;
  Throwable exception = null;
  
  
  protected void createMocks()
  {
    mockHelper = mock(IMemberHelper.class);
    mockValidMemberOne = mock(IMember.class);
    mockValidMemberTwo = mock(IMember.class);
    mockValidMemberThree = mock(IMember.class);
    mockValidMemberFour = mock(IMember.class);

    when(mockValidMemberOne.getFirstName()).thenReturn("Joe");
    when(mockValidMemberOne.getLastName()).thenReturn("Bloggs");
    when(mockValidMemberOne.getContactPhone()).thenReturn("7654321");
    when(mockValidMemberOne.getEmailAddress()).thenReturn("jbloggs@myemail.com");
    when(mockValidMemberOne.getId()).thenReturn(1);
    when(mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1)).thenReturn(mockValidMemberOne);
    
    when(mockValidMemberTwo.getFirstName()).thenReturn("Frank");
    when(mockValidMemberTwo.getLastName()).thenReturn("Black");
    when(mockValidMemberTwo.getContactPhone()).thenReturn("44332211");
    when(mockValidMemberTwo.getEmailAddress()).thenReturn("fblack@myemail.com");
    when(mockValidMemberTwo.getId()).thenReturn(2);
    when(mockHelper.makeMember("Frank", "Black", "44332211", "fblack@myemail.com", 1)).thenReturn(mockValidMemberTwo);
    
    when(mockValidMemberThree.getFirstName()).thenReturn("Sam");
    when(mockValidMemberThree.getLastName()).thenReturn("Smith");
    when(mockValidMemberThree.getContactPhone()).thenReturn("11111111");
    when(mockValidMemberThree.getEmailAddress()).thenReturn("ssmith@email.com");
    when(mockValidMemberThree.getId()).thenReturn(3);
    when(mockHelper.makeMember("Sam", "Smith", "11111111", "ssmith@email.com", 1)).thenReturn(mockValidMemberThree);
    
    when(mockValidMemberFour.getFirstName()).thenReturn("Julia");
    when(mockValidMemberFour.getLastName()).thenReturn("Grey");
    when(mockValidMemberFour.getContactPhone()).thenReturn("0412123123");
    when(mockValidMemberFour.getEmailAddress()).thenReturn("juliagrey@mail.com");
    when(mockValidMemberFour.getId()).thenReturn(4);
    when(mockHelper.makeMember("Julia", "Grey", "0412123123", "juliagrey@mail.com", 1)).thenReturn(mockValidMemberFour);
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
  
  
  
  public void testMemberDAOValid()
  {
    try
    {
    createMocks();
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception == null);
  }

    
  
  
  public void testAddMember()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    assertEquals(validMemberOne.getFirstName(), testMemberOne.getFirstName());
    
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

  }
  
  
  
  public void testGetMemberByIdValid()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember returnedMember = validMemberDAO.getMemberByID(1);
    assertEquals(1, returnedMember.getId());
  }
  
  
  
  public void testGetMemberByIdInvalid()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember returnedMember = validMemberDAO.getMemberByID(20);
    assertEquals(null, returnedMember);
  }
  
  

}
