package test.unit;

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
  // Test correct initialization of singleton
  //===========================================================================

  @Test
  public void getInstance()
  {
    LoanDAO dao = LoanDAO.getInstance();

    assertThat(dao).isInstanceOf(LoanDAO.class);
  }



  @Test
  public void getInstanceReturnsSingleton()
  {
    LoanDAO firstDao = LoanDAO.getInstance();
    LoanDAO secondDao = LoanDAO.getInstance();

    assertThat(firstDao).isSameAs(secondDao);
  }

  //===========================================================================
  // Primary methods
  //===========================================================================


}
