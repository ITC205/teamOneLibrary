package library.daos;

import library.entities.Member;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;

public class MemberHelper
  implements IMemberHelper
{
  public MemberHelper()
  {
    
  }

  public IMember makeMember (String firstName, 
                             String lastName, 
                             String contactPhone, 
                             String emailAddress, int id)
  {
    IMember newMember = new Member(firstName, lastName, contactPhone, emailAddress, id);
    return newMember;
  }
}


