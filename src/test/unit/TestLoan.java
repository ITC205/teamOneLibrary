package test.unit;

import java.util.List;
import java.util.Date;

import library.interfaces.entities.IBook;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IMember;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import org.junit.Rule;
import org.junit.Ignore;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import static org.junit.Assert.*;


/**
 * Unit tests for Loan entity.
 */
public class TestLoan
{
  // ==========================================================================
  // Constructor Testing
  // ==========================================================================



  @Test
  public void constructNewLoan()
  {
    // Given a valid member and book
    IMember fakeBorrower = new FakeMember();
    IBook fakeBook = new FakeBook();
    // With dates (valid dates to check later)
    Date borrowDate = new Date();
    Date returnDate = new Date();

    // Then can create a loan
    ILoan loan = new Loan(fakeBook, fakeBorrower, borrowDate, returnDate);

    // Naive, initial check that loan was created
    assertTrue(loan instanceof ILoan);
  }


  //===========================================================================
  // Stubs
  //===========================================================================



  public class FakeBook
    implements IBook
  {
    public void borrow(ILoan loan) {}

    public ILoan getLoan() {return null;}

    public void returnBook(boolean damaged) {}

    public void lose() {}

    public void repair() {}

    public void dispose() {}

    public EBookState getState() {return EBookState.AVAILABLE;}

    public String getAuthor() {return "Charles Dickens";}

    public String getTitle() {return "Great Expectations";}

    public String getCallNumber() {return "82.023 275 [2011]";}

    public int getID() {return 1;}
  }



  public class FakeMember
    implements IMember
  {
    public boolean hasOverDueLoans() {return false;}

    public boolean hasReachedLoanLimit() {return false;}

    public boolean hasFinesPayable() {return false;}

    public boolean hasReachedFineLimit() {return false;}

    public float getFineAmount()  {return 0;}

    public void addFine(float fine) {}

    public void payFine(float payment) {}

    public void addLoan(ILoan loan) {}

    public List<ILoan> getLoans() {return null;}

    public void removeLoan(ILoan loan) {}

    public EMemberState getState() {return EMemberState.BORROWING_ALLOWED;}

    public String getFirstName() {return "John";}

    public String getLastName() {return "Smith";}

    public String getContactPhone() {return "0414414414";}

    public String getEmailAddress() {return "johnsmith@uni.edu.au";}

    public int getID() {return 1;}
  }

}
