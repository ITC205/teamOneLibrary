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



  public void testGetMemberByIdInvalid()
  {
    createMocks();
    MemberDAO validMemberDAO = new MemberDAO(mockHelper);
    IMember testMemberOne = validMemberDAO.addMember("Joe", "Bloggs", "76543210", "jbloggs@myemail.com");
    IMember returnedMember = validMemberDAO.getMemberByID(20);
    assertEquals(null, returnedMember);
  }



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
    assertEquals(memberList.get(1).getFirstName(), "Frank");
  }



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
