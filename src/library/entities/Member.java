package library.entities;

import java.util.ArrayList;
import java.util.Date;

import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;


/**
* Instances of the Member class are Members that
* may borrow books from a library
*
* @author  Rebecca Callow
*/
public class Member 
  implements IMember
{
  
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private String firstName_;
  private String lastName_;
  private String contactPhone_;
  private String emailAddress_;
  private int id_;
  private ArrayList<ILoan> loanList_ = new ArrayList<>();
  private EMemberState memberState_ = EMemberState.BORROWING_ALLOWED;
  private float totalFines_ = 0.0f;
  
  
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  

  public Member(String firstName, String lastName, String contactPhone,
                     String emailAddress, int id) 
                     throws IllegalArgumentException
  {
    if (!isValid(firstName))
    {
      throw new IllegalArgumentException("Member: Member: First name cannot be blank or null");
    }
    if(!isValid(lastName))
    {
      throw new IllegalArgumentException("Member: Member: Last name cannot be blank or null");
    }
    if (!isValid(contactPhone))
    {
      throw new IllegalArgumentException("Member: Member: Contact phone cannot be blank or null");
    }
      
    if (!isValid(emailAddress))
    {
      throw new IllegalArgumentException("Member: Member: Email address cannot be blank or null");
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
  
  
  
  // ==========================================================================
  // Methods: Primary
  // ==========================================================================
  
  
  
  @Override
  public boolean hasOverDueLoans() 
  {
    boolean hasOverDueLoans = false;
    
    for(ILoan loan: loanList_)
    {
      if (loan.isOverDue())
      {
        hasOverDueLoans = true;
      }
    }
    if (hasOverDueLoans)
    {
      updateState(EMemberState.BORROWING_DISALLOWED);
    }
    return hasOverDueLoans;
  }



  public boolean hasReachedLoanLimit()
  {
    if (loanList_.size() >= IMember.LOAN_LIMIT)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  
  public boolean hasFinesPayable()
  {
    if (totalFines_ > 0)
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
    if (totalFines_ >= IMember.FINE_LIMIT)
    {
      if (borrowingAllowed())
      {
        updateState(EMemberState.BORROWING_DISALLOWED);
      }
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  
  public void addFine(float amount) throws IllegalArgumentException
  {
    if (amount >= 0)
    {
      totalFines_ += amount;
      if (hasReachedFineLimit() && (borrowingAllowed()))
          {
        updateState(EMemberState.BORROWING_DISALLOWED);
          }
    }
    else
    {
      throw new IllegalArgumentException("Fines cannot be negative");
    }
  }
  
  
  
  public void payFine(float amount) throws IllegalArgumentException
  {
    if ((amount >= 0) && (amount <= totalFines_))
    {
      totalFines_ -= amount;
    }
    else
    {
      throw new IllegalArgumentException("Member: payFine: Payment cannot be negative and " +
                                         "must not exceed total fines owing");
    }
  }
  


  public void addLoan(ILoan loan) throws IllegalArgumentException
  {
    if (borrowingAllowed())
    {
      if (loan != null)
      {
        loanList_.add(loan);
      }
      else
      {
        throw new IllegalArgumentException("Member: addLoan: Loan cannot be null");
      }
      if (hasReachedLoanLimit())
      {
        updateState(EMemberState.BORROWING_DISALLOWED);
      }
    }
    else
    {
      throw new IllegalArgumentException("Member: addLoan: Member state is BORROWING_DISALLOWED");
    }
  }
  
  
  
  @Override
  public String toString()
  {
    return "First Name: " + this.firstName_ + 
           " Last Name: " + this.lastName_ + 
           " Contact Phone: " + this.contactPhone_ + 
           " Email Address: " + this.emailAddress_ + 
           " Member ID: " + this.id_;
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
  
  
  
  @Override
  public void removeLoan(ILoan loan) throws IllegalArgumentException
  {
    if ((loan != null) && (loanList_.contains(loan)))
    {
      loanList_.remove(loan);
    }
    else
    {
      throw new IllegalArgumentException("Loan is null or is not in list");
    }
  }
  
  
  
  private void updateState(EMemberState memberState)
  {
    memberState_ = memberState;
  }
  
  
  
  private boolean borrowingAllowed()
  {
    if (memberState_ == EMemberState.BORROWING_ALLOWED)
    {
      return true;
    }
    else return false;
  }
  
  
  
  // ==========================================================================
  // Methods: Getters and Setters
  // ==========================================================================
  
  
  
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
  
  
  
  public float getTotalFines()
  {
    return totalFines_;
  }
  
}