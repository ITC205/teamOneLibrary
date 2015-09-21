package test.unit;

import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import library.daos.LoanHelper;

import static org.assertj.core.api.Assertions.fail;

/**
 *
 */
public class TestLoanReflection
{
  //===========================================================================
  // Variables
  //===========================================================================


  //===========================================================================
  // Constructors
  //===========================================================================

  private TestLoanReflection()
  {
  }

  //===========================================================================
  // Primary methods
  //===========================================================================

  /*
   * Uses Reflection API to directly access Loan's private state.
   */
  protected static ELoanState getPrivateState(Loan loan)
  {
    try {
      Class<?> loanClass = loan.getClass();
      java.lang.reflect.Field state = loanClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      return (ELoanState)state.get(loan);
    }

    catch (NoSuchFieldException exception) {
      fail("NoSuchFieldException should not occur");
    }
    catch (IllegalAccessException exception) {
      fail("IllegalAccessException should not occur");
    }
    catch (Exception exception) {
      fail("Exception should not occur");
    }
    return null;
  }



  /*
  * Uses Reflection API to directly set Loan's private state.
  */
  protected static void setPrivateState(ILoan loan, ELoanState newState) {

    try {
      Class<?> loanClass = loan.getClass();
      java.lang.reflect.Field state = loanClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      state.set(loan, newState);
    }
    catch (NoSuchFieldException exception) {
      fail("NoSuchFieldException should not occur");
    }
    catch (IllegalAccessException exception) {
      fail("IllegalAccessException should not occur");
    }
    catch (Exception exception) {
      fail("Exception should not occur");
    }
  }



  /*
  * Uses Reflection API to directly access LoanHelper's private constructor.
  */
  protected static library.daos.LoanHelper createLoanHelperWithPrivateConstructor()
  {
    try {
      java.lang.reflect.Constructor<library.daos.LoanHelper> constructor =
          library.daos.LoanHelper.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      library.daos.LoanHelper loanHelper = constructor.newInstance();
      return loanHelper;
    }

    catch (IllegalAccessException exception) {
      fail("IllegalAccessException should not occur");
    }
    catch (Exception exception) {
      fail("Exception should not occur");
    }
    return null;
  }

}
