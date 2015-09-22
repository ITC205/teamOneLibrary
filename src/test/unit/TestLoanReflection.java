package test.unit;

import java.util.Date;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.entities.Loan;

import library.daos.LoanHelper;

import static org.assertj.core.api.Assertions.fail;

/**
 * Provides static helper methods that use reflection to access and set private
 * state of a given Loan, and to instantiate a LoanHelper.
 */
public class TestLoanReflection
{
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
   * Uses Reflection API to directly access Loan's private state and return
   * the current state of the Loan.
   * @param loan Loan The loan under test.
   * @return ELoanState The current state of the Loan.
   */
  protected static ELoanState getPrivateState(Loan loan)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field state = loanClass.getDeclaredField("state_");

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
   * Uses Reflection API to access & return the Loan's private borrow date.
   * @param loan Loan The loan under test.
   * @return Date The borrowing date of the Loan.
   */
  protected static Date getPrivateBorrowDate(Loan loan)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field borrowDate = loanClass.getDeclaredField("borrowDate_");

      // Enable direct modification of private field
      if (!borrowDate.isAccessible()) {
        borrowDate.setAccessible(true);
      }

      return (Date)borrowDate.get(loan);
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
  * Uses Reflection API to directly access & return the Loan's private due date.
  * @param loan Loan The loan under test.
  * @return Date The due date of the Loan.
  */
  protected static Date getPrivateDueDate(Loan loan)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field dueDate = loanClass.getDeclaredField("dueDate_");

      // Enable direct modification of private field
      if (!dueDate.isAccessible()) {
        dueDate.setAccessible(true);
      }

      return (Date)dueDate.get(loan);
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
   * Uses Reflection API to directly set the Loan's private state.
   * @param loan ILoan The loan under test.
   * @param ELoanState The state to set on the Loan.
   */
  protected static void setPrivateState(ILoan loan, ELoanState newState)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field state = loanClass.getDeclaredField("state_");

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
  * Uses Reflection API to directly access LoanHelper's private constructor
  * and return a new LoanHelper.
  * @return LoanHelper.
  */
  protected static LoanHelper createLoanHelperWithPrivateConstructor()
  {
    try {
      java.lang.reflect.Constructor<LoanHelper> constructor =
          LoanHelper.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      LoanHelper loanHelper = constructor.newInstance();
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
