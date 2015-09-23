package test.collaboration;

import java.util.Date;

import java.util.List;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.ILoanDAO;

import library.interfaces.entities.EBookState;

import library.daos.LoanDAO;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;

/**
 *
 */
public class TestLoanFamily
{
  //===========================================================================
  // Test fixtures
  //===========================================================================

  private IMember jim_ = stubMember();
  private IMember sam_ = stubMember();
  private IMember jill_ = stubMember();
  private IMember bob_ = stubMember();

  private IBook catch22_ = stubBook();
  private IBook emma_ = stubBook();
  private IBook scoop_ = stubBook();
  private IBook dune_ = stubBook();

  public void setUpCatch22()
  {
    when(catch22_.getTitle()).thenReturn("CATCH-22");
    when(catch22_.getAuthor()).thenReturn("Joseph Heller");
    when(catch22_.getState()).thenReturn(EBookState.AVAILABLE);
  }

  public void setUpEmma()
  {
    when(emma_.getTitle()).thenReturn("Emma");
    when(emma_.getAuthor()).thenReturn("Jane Austen");
    when(emma_.getState()).thenReturn(EBookState.AVAILABLE);
  }

  public void setUpScoop()
  {
    when(scoop_.getTitle()).thenReturn("Scoop");
    when(scoop_.getAuthor()).thenReturn("Evelyn Waugh");
    when(scoop_.getState()).thenReturn(EBookState.ON_LOAN);
  }

  public void setUpDune()
  {
    when(dune_.getTitle()).thenReturn("Dune");
    when(dune_.getAuthor()).thenReturn("Frank Herbert");
    when(dune_.getState()).thenReturn(EBookState.ON_LOAN);
  }


  public void setUpJim()
  {
    when(jim_.getFirstName()).thenReturn("Jim");
    when(jim_.getLastName()).thenReturn("Johnson");
  }


  public void setUpSam()
  {
    when(sam_.getFirstName()).thenReturn("Sam");
    when(sam_.getLastName()).thenReturn("Malone");
  }


  public void setUpJill()
  {
    when(jill_.getFirstName()).thenReturn("Jill");
    when(jill_.getLastName()).thenReturn("Underhill");
  }


  public void setUpBob()
  {
    when(bob_.getFirstName()).thenReturn("Bob");
    when(bob_.getLastName()).thenReturn("Dylan");
  }



  //===========================================================================
  //
  //===========================================================================

  @Test
  public void createEmptyLibrary()
  {
    ILoanHelper loanHelper = createLoanHelperWithProtectedConstructor();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    List<ILoan> emptyLoanList = dao.listLoans();

    assertThat(emptyLoanList).isEmpty();
  }





}
