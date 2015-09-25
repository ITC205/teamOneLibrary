package test.unit;


import java.util.List;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;

import library.daos.MemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;


/**
* The TestMemberDAO class implements unit testing
* on methods contained within the MemberDAO class
*
* @author  Rebecca Callow
*/
public class TestMemberDAO extends TestCase
{

  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  IMemberHelper mockHelper;
  IMemberHelper mockHelperTwo;
  IMember mockValidMemberOne;
  IMember mockValidMemberTwo;
  IMember mockValidMemberThree;
  IMember mockValidMemberFour;
  IMember mockInvalidMember;
  Throwable exception = null;



  // ==========================================================================
  // Methods: Set-up
  // ==========================================================================
  
  
  
  //Create mock objects and classes to isolate the MemberDAO class
  protected void createMocks()
  {
    mockHelper = mock(IMemberHelper.class);
    mockHelperTwo = mock(IMemberHelper.class);
    mockValidMemberOne = mock(IMember.class);
    mockValidMemberTwo = mock(IMember.class);
    mockValidMemberThree = mock(IMember.class);
    mockValidMemberFour = mock(IMember.class);
    mockInvalidMember = mock(IMember.class);

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
    when(mockHelper.makeMember("Frank", "Black", "44332211", "fblack@myemail.com", 2)).thenReturn(mockValidMemberTwo);

    when(mockValidMemberThree.getFirstName()).thenReturn("Sam");
    when(mockValidMemberThree.getLastName()).thenReturn("Smith");
    when(mockValidMemberThree.getContactPhone()).thenReturn("11111111");
    when(mockValidMemberThree.getEmailAddress()).thenReturn("ssmith@email.com");
    when(mockValidMemberThree.getId()).thenReturn(3);
    when(mockHelper.makeMember("Sam", "Smith", "11111111", "ssmith@email.com", 3)).thenReturn(mockValidMemberThree);

    when(mockValidMemberFour.getFirstName()).thenReturn("Julia");
    when(mockValidMemberFour.getLastName()).thenReturn("Grey");
    when(mockValidMemberFour.getContactPhone()).thenReturn("0412123123");
    when(mockValidMemberFour.getEmailAddress()).thenReturn("juliagrey@mail.com");
    when(mockValidMemberFour.getId()).thenReturn(4);
    when(mockHelper.makeMember("Julia", "Grey", "0412123123", "juliagrey@mail.com", 4)).thenReturn(mockValidMemberFour);
  
    when(mockInvalidMember.getFirstName()).thenReturn("Bob");
    when(mockInvalidMember.getLastName()).thenReturn("");
    when(mockInvalidMember.getContactPhone()).thenReturn("123456");
    when(mockInvalidMember.getEmailAddress()).thenReturn("bob@email.com");
    when(mockInvalidMember.getId()).thenReturn(5);
    when(mockHelperTwo.makeMember("Bob", "", "123456", "bob@email.com", 5)).thenReturn(mockInvalidMember); 
  }



  // ==========================================================================
  // Methods: Tests
  // ==========================================================================
  
  
  
  //Test that the correct exception is thrown when null is passed 
  //to the constructor for MemberDAO
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



  //Test that no exception is thrown when a valid helper object is
  //passed to the MemberDAO constructor
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



  // Test that the addMember method adds multiple members to 
  // the member map without error
  public void testAddMember()
  {
    createMocks();
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    assertEquals(testMemberOne.getFirstName(), testMemberOne.getFirstName());

    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

    assertEquals("Frank", testMemberTwo.getFirstName());
    assertEquals("Smith", testMemberThree.getLastName());
    assertEquals("11111111", testMemberThree.getContactPhone());
    assertEquals("juliagrey@mail.com", testMemberFour.getEmailAddress());
  }



  // Test that the correct member is returned when the member list is 
  // searched by ID
  public void testGetMemberByIdValid()
  {
    createMocks();
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

    IMember returnedMember = validMemberDAO.getMemberByID(3);
    assertEquals(3, returnedMember.getId());
    assertEquals("Sam", returnedMember.getFirstName());
  }



  // Test that when the member ID does not exist in the member map,
  // the getMemberById method returns null
  public void testGetMemberByIdInvalid()
  {
    createMocks();
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember returnedMember = validMemberDAO.getMemberByID(20);
    assertEquals(null, returnedMember);
  }



  // Test that a list of members is returned accurately
  public void testListMembers()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

    List<IMember> memberList = validMemberDAO.listMembers();
    assertEquals(4, memberList.size());
    assertEquals("Bloggs", memberList.get(0).getLastName());
    assertEquals("Frank", memberList.get(1).getFirstName());
    assertEquals("ssmith@email.com", memberList.get(2).getEmailAddress());
    assertEquals("0412123123", memberList.get(3).getContactPhone());
  }



  // Test that the correct member is returned when searched for by last name
  public void testFindMembersByLastName()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

    List<IMember> memberList = validMemberDAO.findMembersByLastName("Black");
    assertEquals(1, memberList.size());
    assertEquals("Black", memberList.get(0).getLastName());
  }



  // Test that a list of length zero is returned if the last name searched
  // for does not exist
  public void testFindMembersByLastNameDoesNotExist()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");
    
    List<IMember> memberList = validMemberDAO.findMembersByLastName("Blue");
    assertEquals(0, memberList.size());
  }



  // Test that the correct member is returned when searched for by email address
  public void testFindMembersByEmailAddress()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

    List<IMember> memberList = validMemberDAO.findMembersByEmailAddress("jbloggs@myemail.com");
    assertEquals(1, memberList.size());
    assertEquals("jbloggs@myemail.com", memberList.get(0).getEmailAddress());
  }



  // Test that the correct member is returned when searched for by
  // first and last names
  public void testFindMembersByNames()
  {
    createMocks();
    IMember validMemberOne = mockHelper.makeMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com", 1);
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);

    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember testMemberTwo = validMemberDAO.addMember("Frank", "Black", "44332211", "fblack@myemail.com");
    IMember testMemberThree = validMemberDAO.addMember("Sam", "Smith", "11111111", "ssmith@email.com");
    IMember testMemberFour = validMemberDAO.addMember("Julia", "Grey", "0412123123", "juliagrey@mail.com");

    List<IMember> memberList = validMemberDAO.findMembersByNames("Julia", "Grey");
    assertEquals(1, memberList.size());
    assertEquals("Grey", memberList.get(0).getLastName());
  }

}
