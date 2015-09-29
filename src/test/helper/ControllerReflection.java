package test.helper;

import java.util.List;
import java.util.Date;
import java.util.Map;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.fail;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
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

  /**
   * Uses Reflection API to directly access the BorrowUC_CTL's private state.
   * @param controller BorrowUC_CTL The main controller.
   * @return EBorrowState The current state of the controller.
   */
  public static EBorrowState getPrivateState(BorrowUC_CTL controller)
  {
    try {
      Field state = BorrowUC_CTL.class.getDeclaredField("state");

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



  /**
   * Uses Reflection API to directly set BorrowUC_CTL's private state.
   * @param controller BorrowUC_CTL The main controller.
   * @param newState EBorrowState The new state to be set on the controller.
   */
  public static void setPrivateState(BorrowUC_CTL controller,
                                     EBorrowState newState)
  {
    try {
      Field state = BorrowUC_CTL.class.getDeclaredField("state");

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
  // borrower getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to directly access BorrowUC_CTL's private borrower.
   * @param controller BorrowUC_CTL The main controller.
   * @return IMember The borrower associated with the pending loans.
   */
  public static IMember getPrivateBorrower(BorrowUC_CTL controller)
  {
    try {
      Field member = BorrowUC_CTL.class.getDeclaredField("borrower");

      // Enable direct modification of private field
      if (!member.isAccessible()) {
        member.setAccessible(true);
      }
      return (IMember)member.get(controller);
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



  /**
   * Uses Reflection API to directly set BorrowUC_CTL's private borrower.
   * @param controller BorrowUC_CTL The main controller.
   * @param borrower IMember The borrower to be set.
   */
  public static void setPrivateBorrower(BorrowUC_CTL controller,
                                        IMember borrower)
  {
    try {
      Field member = BorrowUC_CTL.class.getDeclaredField("borrower");

      // Enable direct modification of private field
      if (!member.isAccessible()) {
        member.setAccessible(true);
      }
      member.set(controller, borrower);
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
  // count getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to directly access BorrowUC_CTL's private count.
   * @param controller BorrowUC_CTL The main controller.
   * @return int The number of loans associated with the current borrower.
   */
  public static int getPrivateCount(BorrowUC_CTL controller)
  {
    try {
      Field count = BorrowUC_CTL.class.getDeclaredField("scanCount");

      // Enable direct modification of private field
      if (!count.isAccessible()) {
        count.setAccessible(true);
      }
      return (int)count.get(controller);
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

    return 0;
  }



  /**
   * Uses Reflection API to directly set BorrowUC_CTL's private borrower.
   * @param controller BorrowUC_CTL The main controller.
   * @param numberLoans int The numberOfLoans associated with the borrower.
   */
  public static void setPrivateCount(BorrowUC_CTL controller,
                                     int numberLoans)
  {
    try {
      Field count = BorrowUC_CTL.class.getDeclaredField("scanCount");

      // Enable direct modification of private field
      if (!count.isAccessible()) {
        count.setAccessible(true);
      }
      count.set(controller, numberLoans);
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

  /**
   * Uses Reflection API to directly access BorrowUC_CTL's private loanList.
   * @param controller BorrowUC_CTL The main controller.
   * @return List<ILoan> The list of pending loans.
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



  /**
   * Uses Reflection API to directly set BorrowUC_CTL's private loanList.
   * @param controller BorrowUC_CTL The main controller.
   * @param pendingLoans List<ILoan> The list of pending loans to be set.
   */
  public static void setPrivateLoanList(BorrowUC_CTL controller,
                                        List<ILoan> pendingLoans)
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
  // ui getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to directly access BorrowUC_CTL's private ui.
   * @param controller BorrowUC_CTL The main controller.
   * @return IBorrowUI The ui.
   */
  public static IBorrowUI getPrivateUI(BorrowUC_CTL controller)
  {
    try {
      Field ui = BorrowUC_CTL.class.getDeclaredField("ui");

      // Enable direct modification of private field
      if (!ui.isAccessible()) {
        ui.setAccessible(true);
      }
      return (IBorrowUI)ui.get(controller);
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



  /**
   * Uses Reflection API to directly set BorrowUC_CTL's private ui.
   * @param controller BorrowUC_CTL The main controller.
   * @param mockUI IBorrowUI The ui to be set.
   */
  public static void setPrivateUI(BorrowUC_CTL controller, IBorrowUI mockUI)
  {
    try {
      Field ui = BorrowUC_CTL.class.getDeclaredField("ui");

      // Enable direct modification of private field
      if (!ui.isAccessible()) {
        ui.setAccessible(true);
      }
      ui.set(controller, mockUI);
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

