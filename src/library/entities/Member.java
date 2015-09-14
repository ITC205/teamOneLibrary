package library.entities;

import java.util.ArrayList;

import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;

public class Member 
{
  private String firstName_;
  private String lastName_;
  private String contactPhone_;
  private String emailAddress_;
  private int id_;
  private ArrayList<ILoan> loanList_ = new ArrayList<>();
  private EMemberState memberState_ = EMemberState.BORROWING_ALLOWED;
  private float finesOwing_ = 0.0f;
  
  //Check value of LOAN_LIMIT
  private final int LOAN_LIMIT = 10;

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
  
  
  
  public void addLoan(ILoan loan) throws IllegalArgumentException
  {
    if ((loan != null) && (getState() != EMemberState.BORROWING_DISALLOWED))
    {
      loanList_.add(loan);
    }
    else
    {
      throw new IllegalArgumentException("Loan is null or member state is BORROWING_DISALLOWED");
    }
  }
  
  
  
  public void addFine(float amount) throws IllegalArgumentException
  {
    if (amount >= 0)
    {
      finesOwing_ += amount;
    }
    else
    {
      throw new IllegalArgumentException("Fines cannot be negative");
    }
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
  
  
  
  public EMemberState getState()
  {
    return memberState_;
  }
  
  
  
  public ArrayList<ILoan> getLoans()
  {
    return loanList_;
  }
  
  
  
  public float getFineAmount()
  {
    return finesOwing_;
  }
}