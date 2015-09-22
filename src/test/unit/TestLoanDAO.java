package test.unit;

import library.interfaces.daos.ILoanHelper;

import library.daos.LoanDAO;

import org.junit.Test;
import org.junit.Ignore;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;

/**
 * Unit tests for LoanDAO.
 */
public class TestLoanDAO
{
  //===========================================================================
  // Test constructor - with LoanBuilder (for stubs)
  //===========================================================================

  @org.junit.Test
  public void createLoanDao()
  {
    ILoanHelper loanHelper = stubHelper();
    LoanDAO dao = createLoanDaoWithPrivateConstructor(loanHelper);

    assertThat(dao).isInstanceOf(library.daos.LoanDAO.class);
  }





  //===========================================================================
  // Primary methods
  //===========================================================================


}
