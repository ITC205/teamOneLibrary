package test.unit;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

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
  // Variables
  //===========================================================================



  //===========================================================================
  // Constructors
  //===========================================================================

  LoanDAO dao = LoanDAO.getInstance();

  assertThat(dao).isInstanceOf(LoanDAO.class);

  //===========================================================================
  // Primary methods
  //===========================================================================


}
