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

  }
  
  
  
  @Override
  public IMember addMember(String firstName, String lastName,
      String ContactPhone, String emailAddress) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IMember getMemberByID(int id) {
    // TODO Auto-generated method stub
    return null;
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
