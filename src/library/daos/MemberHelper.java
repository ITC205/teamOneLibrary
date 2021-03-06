package library.daos;

import library.entities.Member;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;


/**
* The MemberHelper class assists in the
* creation of Member objects
*
* @author  Rebecca Callow
*/
public class MemberHelper
  implements IMemberHelper
{
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public MemberHelper()
  {
    
  }
  
  
  
  // ==========================================================================
  // Methods: Primary
  // ==========================================================================
  
  

  @Override
  public IMember makeMember (String firstName, 
                             String lastName, 
                             String contactPhone, 
                             String emailAddress, int id)
  {
    
    return new Member(firstName, lastName, contactPhone, emailAddress, id);
  }
  
}


