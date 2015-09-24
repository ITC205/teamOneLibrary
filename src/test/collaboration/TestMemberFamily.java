package test.collaboration;

import junit.framework.TestCase;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.entities.Member;
import library.interfaces.entities.*;
import static org.mockito.Mockito.*;


/**
* The TestMember class implements unit testing
* on methods contained within the Member class
*
* @author  Rebecca Callow
*/
public class TestMemberFamily extends TestCase
{
	Throwable exception = null;

		String[] firstNames = {"Sam","Joe","Bill","Sarah","Jodie","Samantha"};
		String[] lastNames = {"Jones","Jenkins","Bloggs","Doe","Woods","Cook"};
		String[] contactPhoneNumbers = {"123456","1","321","0412123123","55555555","12341234"};
		String[] emailAddresses = {"sjones@mail.com","jjenkins@email.com","bbloggs@email.com",
				                   "sdoe@email.com","jwoods@email.com","scook@email.com"};
		
		
	
	
	  public void testMemberDAOValid()
	  {
		  try
		  {
		  MemberHelper helper = new MemberHelper();
		  MemberDAO memberDAO = new MemberDAO(helper);
		  }
		  catch (Throwable ex)
		  {
			  exception = ex;
		  }
		  assertTrue(exception == null);
	  }
	  
	  
	  
	  public void testAddMember()
	  {
		  MemberHelper helper = new MemberHelper();
		  MemberDAO memberDAO = new MemberDAO(helper);
		  memberDAO.addMember(firstNames[1],lastNames[1],contactPhoneNumbers[1],emailAddresses[1]);
		  memberDAO.addMember(firstNames[2],lastNames[2],contactPhoneNumbers[2],emailAddresses[2]);
		  IMember returnedMemberIdOne = memberDAO.getMemberByID(1);
		  assertEquals(firstNames[1],returnedMemberIdOne.getFirstName());
		  assertEquals(lastNames[1],returnedMemberIdOne.getLastName());
		  assertEquals(contactPhoneNumbers[1],returnedMemberIdOne.getContactPhone());
		  assertEquals(emailAddresses[1],returnedMemberIdOne.getEmailAddress());
		  
		  IMember returnedMemberIdTwo = memberDAO.getMemberByID(2);
		  assertEquals(firstNames[2],returnedMemberIdTwo.getFirstName());
	  }
	  
	  
	  
	  public void testGetMemberByIdValid()
	  {
		  MemberHelper helper = new MemberHelper();
		  MemberDAO memberDAO = new MemberDAO(helper);
		  memberDAO.addMember(firstNames[1],lastNames[1],contactPhoneNumbers[1],emailAddresses[1]);
		  memberDAO.addMember(firstNames[2],lastNames[2],contactPhoneNumbers[2],emailAddresses[2]);
		  memberDAO.addMember(firstNames[3],lastNames[3],contactPhoneNumbers[3],emailAddresses[3]);
		  memberDAO.addMember(firstNames[4],lastNames[4],contactPhoneNumbers[4],emailAddresses[4]);
	      IMember returnedMemberIdThree = memberDAO.getMemberByID(3);
	      assertEquals(firstNames[3], returnedMemberIdThree.getFirstName());
	      assertEquals(lastNames[3], returnedMemberIdThree.getLastName());
	      assertEquals(contactPhoneNumbers[3], returnedMemberIdThree.getContactPhone());
	      assertEquals(emailAddresses[3], returnedMemberIdThree.getEmailAddress());
	  
	  }
	  
	  
	  
	  public void testGetMemberByIdInvalid()
	  {
		  
	  }
	  
	  
	  
	  public void testListMembers()
	  {
		  
	  }
	  
	  
	  
	  public void testFindMembersByLastName()
	  {
		  
	  }
	  
	  
	  
	  public void testFindMembersByLastNameDoesNotExist()
	  {
		  
	  }
	  
	  
	  
	  public void testFindMembersByEmailAddress()
	  {
		  
	  }
	  
	  
	  
	  public void testFindMembersByNames()
	  {
		  
	  }
	
}