package library.entities;

public class Member 
{
  private String firstName_;
  private String lastName_;
  private String contactPhone_;
  private String emailAddress_;
  private int id_;

  public void Member(String firstName, String lastName, String contactPhone,
                     String emailAddress, int id) 
                     throws IllegalArgumentException
  {
    if ((firstName.length() >= 1) && (lastName.length() >= 1) && (contactPhone.length() >= 1) && 

        (emailAddress.length() >= 1) && 
        (id >= 1))
    {
      firstName_ = firstName;
      lastName_ = lastName;
      contactPhone_ = contactPhone;
      emailAddress_ = emailAddress;
      id_ = id;
    }
    else
    {
      throw new IllegalArgumentException("Fields cannot be blank or null. ID cannot be zero or less");
    }
  }
}
