package test.helper;

import java.util.List;
import java.util.Date;
import java.util.Map;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.fail;

import library.interfaces.EBorrowState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import library.entities.Loan;

import library.BorrowUC_CTL;

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
  public static library.interfaces.EBorrowState getPrivateState(library.BorrowUC_CTL controller)
  {
    try {
      Field state = library.BorrowUC_CTL.class.getDeclaredField("state");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      return (library.interfaces.EBorrowState)state.get(controller);
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
  public static void setPrivateState(library.BorrowUC_CTL controller,
                                     library.interfaces.EBorrowState newState)
  {
    try {
      Field state = library.BorrowUC_CTL.class.getDeclaredField("state");

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
  // loanList getter & setter
  //===========================================================================

  /*
   * Uses Reflection API to directly set BorrowUC_CTL's private loanList.
   * @param controller BorrowUC_CTL The main controller.
   * @return List<ILoan> The list of pending loans.
   */
  public static List<ILoan> getPrivateLoanList(library.BorrowUC_CTL controller)
  {
    try {
      Field loanList = library.BorrowUC_CTL.class.getDeclaredField("loanList");

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
  public static void setPrivateLoanList(library.BorrowUC_CTL controller, List<ILoan> pendingLoans)
  {
    try {
      Field loanList = library.BorrowUC_CTL.class.getDeclaredField("loanList");

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

}

