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
  private float fineAmount_ = 0.0f;
  
  //Check value of LOAN_LIMIT
  private final int LOAN_LIMIT_ = 10;
  //Check value of FINE_MAX
  private final float FINE_MAX_ = 20.0f;

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
      fineAmount_ += amount;
    }
    else
    {
      throw new IllegalArgumentException("Fines cannot be negative");
    }
  }
  
  
  
  public boolean hasFinesPayable()
  {
    if (fineAmount_ > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  
  public boolean hasReachedFineLimit()
  {
    if (fineAmount_ >= FINE_MAX_)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  
  public void payFine(float amount) throws IllegalArgumentException
  {
    if ((amount >= 0) && (amount <= fineAmount_))
    {
      fineAmount_ -= amount;
    }
    else
    {
      throw new IllegalArgumentException("Payment cannot be negative and " +
                                     "must not exceed total fines owing");
    }
  }
  
  
  
  public boolean hasReachedLoanLimit()
  {
    if (getLoans().size() >= LOAN_LIMIT_)
    {
      return true;
    }
    else
    {
      return false;
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
    return fineAmount_;
  }
}