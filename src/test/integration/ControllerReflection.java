package test.integration;

import java.util.Date;
import java.util.Map;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import library.BorrowUC_CTL;

import  library.interfaces.EBorrowState;

import static org.assertj.core.api.Assertions.fail;

/**
 * Provides static helper methods that use reflection to access and set private
 * state and methods in BorrowUC_CTL class.
 */
public class ControllerReflection
{
  //===========================================================================
  // State getter
  //===========================================================================

  /*
   * Uses Reflection API to directly access BorrowUC_CTL's private state and
   * return the current state of the controller.
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

  //===========================================================================
  // ?
  //===========================================================================



  //===========================================================================
  // ?
  //===========================================================================



  }

