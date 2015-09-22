package test.unit;

import library.interfaces.daos.ILoanHelper;

import library.daos.LoanDAO;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;

/**
 * Unit tests for LoanDAO.
 */
public class TestLoanDAO
{
  //===========================================================================
  // Test constructor - with LoanBuilder (for stubs) & LoanReflection (to
  // create new LoanDAOs)
  //===========================================================================

  @Test
  public void createLoanDao()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);

    assertThat(dao).isInstanceOf(LoanDAO.class);
  }



  @Test
  public void createLoanDaoWithNullHelperThrows()
  {
    ILoanHelper loanHelper = null;

    try {
      LoanDAO dao = createLoanDaoWithProtectedConstructor(loanHelper);
    }
    catch (Exception exception) {
    assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }
  }


  //===========================================================================
  // Primary methods
  //===========================================================================


}
