package library.entities;

public class Member 
{
  private String firstName_;
  private String lastName_;
  private String contactPhone_;
  private String emailAddress_;
  private int id_;

  public Member(String firstName, String lastName, String contactPhone,
                     String emailAddress, int id) 
                     throws IllegalArgumentException
  {
    if (!isValid(firstName) || !isValid(lastName) || !isValid(contactPhone) || !isValid(emailAddress))
    {
      throw new IllegalArgumentException("Fields cannot be blank or null");
    }
    if (id <= 0)
    {
      throw new IllegalArgumentException("Member ID must be greater than or equal to 1");
    }
      firstName_ = firstName;
      lastName_ = lastName;
      contactPhone_ = contactPhone;
      emailAddress_ = emailAddress;
      id_ = id;
  }
  
  
  
  private boolean isValid(String memberDetails)
  {
    if (memberDetails != null && memberDetails.length() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  
  public String getFirstName()
  {
    return firstName_;
  }



  public String getLastName()
  {
    return lastName_;
  }



  public String getContactPhone()
  {
    return contactPhone_;
  }



  public String getEmailAddress()
  {
    return emailAddress_;
  }



  public int getId()
  {
    return id_;
  }
}