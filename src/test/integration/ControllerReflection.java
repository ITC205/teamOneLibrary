package test.integration;

import java.util.List;
import java.util.Date;
import java.util.Map;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import library.BorrowUC_CTL;

import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import  library.interfaces.EBorrowState;


import library.entities.Loan;



import static org.assertj.core.api.Assertions.fail;

/**
 * Provides static helper methods that use reflection to access and set private
 * state and methods in BorrowUC_CTL class.
 */
public class ControllerReflection
{
  //===========================================================================
  // State getter & setter
  //===========================================================================

  /*
 * Uses Reflection API to directly access the BorrowUC_CTL's private state.
 * @param controller BorrowUC_CTL The main controller.
 * @return EBorrowState The current state of the controller.
 */
  public static EBorrowState getPrivateState(BorrowUC_CTL controller)
  {
    try {
      Class<?> controllerClass = controller.getClass();
      Field state = controllerClass.getDeclaredField("state");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      return (EBorrowState)state.get(controller);
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
   * Uses Reflection API to directly set BorrowUC_CTL's private state.
   * @param controller BorrowUC_CTL The main controller.
   * @param newState EBorrowState The new state to be set on the controller.
   */
  public static void setPrivateState(BorrowUC_CTL controller, EBorrowState newState)
  {
    try {
      Class<?> controllerClass = controller.getClass();
      Field state = controllerClass.getDeclaredField("state");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      state.set(controller, newState);
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




  //===========================================================================
  // ?
  //===========================================================================

  /*
 * Uses Reflection API to directly set BorrowUC_CTL's private loanList.
 * @param controller BorrowUC_CTL The main controller.
 * @param pendingLoans List<ILoan> The list of pending loans to be set.
 */
  public static List<ILoan> getPrivateLoanList(BorrowUC_CTL controller)
  {
    try {
      Field loanList = BorrowUC_CTL.class.getDeclaredField("loanList");

      // Enable direct modification of private field
      if (!loanList.isAccessible()) {
        loanList.setAccessible(true);
      }

      return (List<ILoan>)loanList.get(controller);
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
   * Uses Reflection API to directly set BorrowUC_CTL's private loanList.
   * @param controller BorrowUC_CTL The main controller.
   * @param pendingLoans List<ILoan> The list of pending loans to be set.
   */
  public static void setPrivateLoanList(BorrowUC_CTL controller, List<ILoan> pendingLoans)
  {
    try {
      Field loanList = BorrowUC_CTL.class.getDeclaredField("loanList");

      // Enable direct modification of private field
      if (!loanList.isAccessible()) {
        loanList.setAccessible(true);
      }

      loanList.set(controller, pendingLoans);
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


  //===========================================================================
  // State setter
  //===========================================================================

  /*
 * Uses Reflection API to directly access the BorrowUC_CTL's private borrower.
 * @param controller BorrowUC_CTL The main controller.
 * @return IMember The current borrower set in the controller.
 */
  public static IMember getPrivateBorrower(BorrowUC_CTL controller)
  {
    try {
      Field borrower = BorrowUC_CTL.class.getDeclaredField("borrower");

      // Enable direct modification of private field
      if (!borrower.isAccessible()) {
        borrower.setAccessible(true);
      }

      return (IMember)borrower.get(controller);
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



}

