package library.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;


/**
* Instances of the MemberDAO class contain a
* map of Member objects. This class provides 
* methods to access and manipulate this map
*
* @author  Rebecca Callow
*/
public class MemberDAO 
  implements IMemberDAO
{

  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private IMemberHelper helper_;
  private Map<Integer, IMember> memberMap_;

  private int nextId_;
  
  
  
  // ==========================================================================
  // Constructors
  // ==========================================================================
  


  public MemberDAO(IMemberHelper helper)
  {
    if (helper != null)
    {
      helper_ = helper;
      memberMap_ = new HashMap<Integer, IMember>();
      nextId_ = 1;
    }
    else
    {
      throw new IllegalArgumentException("MemberDAO: MemberDAO(): helper cannot be null");
    }
  }



  public MemberDAO(IMemberHelper helper, Map<Integer, IMember> memberMap)
  {
    helper_ = helper;
    memberMap_ = memberMap;
  }
  
  
  
  // ==========================================================================
  // Methods: Primary
  // ==========================================================================



  @Override
  public IMember addMember(String firstName, String lastName,
                           String contactPhone, String emailAddress)
  {
    IMember newMember = helper_.makeMember(firstName, lastName, contactPhone, 
                                           emailAddress, getNextId());
    memberMap_.put(newMember.getId(), newMember);
    nextId_ += 1;
    return newMember;
  }
  


  @Override
  public IMember getMemberByID(int id) 
  {
    IMember member = memberMap_.get(id);
    if (memberMap_.get(id) != null)
    {
      return member;
    }
    else
    {
      return null;
    }
  }



  @Override
  public List<IMember> listMembers() 
  {
    List<IMember> memberList = new ArrayList<IMember>(memberMap_.values());
    return memberList;
  }



  @Override
  public List<IMember> findMembersByLastName(String lastName) {
    ArrayList<IMember> membersByLastName = new ArrayList<>();

    for (int n = 1; n <= memberMap_.size(); n++)
    {
      if (memberMap_.get(n).getLastName().equals(lastName))
      {
        membersByLastName.add(memberMap_.get(n));
      }
    }
    return membersByLastName;
  }



  @Override
  public List<IMember> findMembersByEmailAddress(String emailAddress) {
    ArrayList<IMember> membersByEmailAddress = new ArrayList<>();

    for (int n = 1; n <= memberMap_.size(); n++)
    {
      if (memberMap_.get(n).getEmailAddress().equals(emailAddress))
      {
        membersByEmailAddress.add(memberMap_.get(n));
      }
    }
    return membersByEmailAddress;
  }

  
  
  @Override
  public List<IMember> findMembersByNames(String firstName, String lastName) {
    ArrayList<IMember> membersByNames = new ArrayList<>();

    for (int n = 1; n <= memberMap_.size(); n++)
    {
      if ((memberMap_.get(n).getFirstName().equals(firstName)) && (memberMap_.get(n).getLastName().equals(lastName)))
      {
        membersByNames.add(memberMap_.get(n));
      }
    }
    return membersByNames;
  }
  
  
  
  // ==========================================================================
  // Methods: Getters
  // ==========================================================================




  private int getNextId()
  {
    return nextId_;
  }

}
