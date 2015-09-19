package library.daos;

import java.util.List;
import java.util.Map;

import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;

public class MemberDAO 
  implements IMemberDAO
{
  
  private IMemberHelper helper_;
  private Map<Integer, IMember> memberMap_;
  //Is memberMap keyed by ID? ID is included in Member object
  private static int nextId_;


  public MemberDAO(IMemberHelper helper)
  {
    if (helper != null)
    {
      helper_ = helper;
    }
    else
    {
    throw new IllegalArgumentException("MemberDAO: MemberDAO(): helper is null" +                                   "helper cannot be null");
    }
  }
  
  
  
  public MemberDAO(IMemberHelper helper, Map<Integer, IMember> memberMap)
  {
    helper_ = helper;
    memberMap_ = memberMap;
  }
  
  
  
  @Override
  public IMember addMember(String firstName, String lastName,
      String contactPhone, String emailAddress)
  {
    while(memberMap_.get(nextId_) != null)
    {
      nextId_ += 1;
    }
    IMember newMember = helper_.makeMember(firstName, lastName, contactPhone, emailAddress, nextId_);
    memberMap_.put(nextId_, newMember);
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
  public List<IMember> listMembers() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IMember> findMembersByLastName(String lastName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IMember> findMembersByEmailAddress(String emailAddress) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IMember> findMembersByNames(String firstName, String lastName) {
    // TODO Auto-generated method stub
    return null;
  }

}
