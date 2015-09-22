package test.unit;

import java.util.Date;
import java.util.Map;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import library.interfaces.entities.ILoan;
import library.interfaces.entities.ELoanState;

import library.interfaces.daos.ILoanHelper;


import library.entities.Loan;

import library.daos.LoanHelper;
import library.daos.LoanDAO;

import static org.assertj.core.api.Assertions.fail;

/**
 * Provides static helper methods that use reflection to access and set private
 * state of a given Loan, and to instantiate a LoanHelper.
 */
public class LoanReflection
{
  //===========================================================================
  // Constructors
  //===========================================================================

  private LoanReflection()
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
  * Uses Reflection API to directly access & return the LoanDAO's private
  * nextID_ field.
  * @param loanDao LoanDAO The LoanDAO being used.
  * @return int The next ID to be used for committing loans.
  */
  protected static int getPrivateNextId(LoanDAO loanDao)
  {
    try {
      Class<?> loanDaoClass = loanDao.getClass();
      Field nextID = loanDaoClass.getDeclaredField("nextID_");

      // Enable direct modification of private field
      if (!nextID.isAccessible()) {
        nextID.setAccessible(true);
      }

      return (int)nextID.get(loanDao);
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
  * Uses Reflection API to directly set the LoanDAO's private
  * nextID_ field.
  * @param loanDao LoanDAO The LoanDAO being used.
  * @param newNextID int The desired next ID.
  * @return int The next ID to be used for committing loans.
  */
  protected static void setPrivateNextId(LoanDAO loanDao, int newNextID)
  {
    try {
      Class<?> loanDaoClass = loanDao.getClass();
      Field nextID = loanDaoClass.getDeclaredField("nextID_");

      // Enable direct modification of private field
      if (!nextID.isAccessible()) {
        nextID.setAccessible(true);
      }

      nextID.set(loanDao, newNextID);
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
  * Uses Reflection API to directly add Loans to the LoanDAO's private LoanMap.
  * @param loanDao LoanDAO The LoanDAO being used.
  * @param loan ILoan The Loan to be added.
  */
  protected static void setPrivateLoanMap(LoanDAO loanDao, ILoan[] loans)
  {
    try {
      Class<?> loanDaoClass = loanDao.getClass();
      Field loanMap = loanDaoClass.getDeclaredField("loanMap_");

      // Enable direct modification of private field
      if (!loanMap.isAccessible()) {
        loanMap.setAccessible(true);
      }
      Map<Integer, ILoan> newLoanMap = new java.util.HashMap<>();
      for (int i = 0; i < loans.length; i++) {
        newLoanMap.put(i, loans[i]);
      }
      loanMap.set(loanDao, newLoanMap);
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
  protected static LoanHelper createLoanHelperWithProtectedConstructor()
  {
    try {
      Constructor<LoanHelper> constructor =
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



  /*
* Uses Reflection API to directly access LoanHelper's private constructor
* and return a new LoanHelper.
* @return LoanHelper.
*/
  protected static LoanDAO
    createLoanDaoWithProtectedConstructor(ILoanHelper loanHelper)
      throws IllegalArgumentException
  {
    try {
      Constructor<LoanDAO> constructor =
          LoanDAO.class.getDeclaredConstructor(ILoanHelper.class);
      constructor.setAccessible(true);
      LoanDAO loanDao = constructor.newInstance(loanHelper);
      return loanDao;
    }

    catch (IllegalAccessException exception) {
      fail("IllegalAccessException should not occur");
    }
    catch (NoSuchMethodException exception) {
      fail("NoSuchMethodException should not occur");
    }
    catch (InstantiationException exception) {
      fail("InstantiationException should not occur");
    }
    catch (java.lang.reflect.InvocationTargetException exception) {
      throw new IllegalArgumentException("Null parameter passed in " +
                                         "constructor");
    }
    return null;
  }

}
