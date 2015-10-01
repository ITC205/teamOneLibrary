package test.helper;

import java.util.Date;
import java.util.Map;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.fail;

import library.interfaces.entities.ELoanState;
import library.interfaces.entities.ILoan;

import library.daos.LoanDAO;
import library.entities.Loan;


/**
 * Provides static helper methods that use reflection to access and set private
 * state of a given Loan, and to instantiate a LoanHelper.
 *
 * @author nicholasbaldwin
 */
public class LoanReflection
{
  //===========================================================================
  // State getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to directly access Loan's private state and return
   * the current state of the Loan.
   * @param loan Loan The loan under test.
   * @return ELoanState The current state of the Loan.
   */
  public static ELoanState getPrivateState(Loan loan)
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



  /**
   * Uses Reflection API to directly set the Loan's private state.
   * @param loan ILoan The loan under test.
   * @param newState The state to set on the Loan.
   */
  public static void setPrivateState(ILoan loan, ELoanState newState)
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

  //===========================================================================
  // Borrow date getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to access & return the Loan's private borrow date.
   * @param loan Loan The loan under test.
   * @return Date The borrowing date of the Loan.
   */
  public static Date getPrivateBorrowDate(Loan loan)
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



  /**
   * Uses Reflection API to directly set the Loan's private borrow date.
   * @param loan Loan The loan under test.
   * @param date Date The date that will be set as the borrow date of the Loan.
   */
  public static void setPrivateBorrowDate(Loan loan, Date date)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field borrowDate = loanClass.getDeclaredField("borrowDate_");

      // Enable direct modification of private field
      if (!borrowDate.isAccessible()) {
        borrowDate.setAccessible(true);
      }
      borrowDate.set(loan, date);
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
  // Due date getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to directly access & return the Loan's private due date.
   * @param loan Loan The loan under test.
   * @return Date The due date of the Loan.
   */
  public static Date getPrivateDueDate(Loan loan)
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



  /**
   * Uses Reflection API to directly set the Loan's private due date.
   * @param loan Loan The loan under test.
   * @param date Date The date that will be set as the due date of the Loan.
   */
  public static void setPrivateDueDate(Loan loan, Date date)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field dueDate = loanClass.getDeclaredField("dueDate_");

      // Enable direct modification of private field
      if (!dueDate.isAccessible()) {
        dueDate.setAccessible(true);
      }
      dueDate.set(loan, date);
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
  // nextID getter & setter
  //===========================================================================

  /**
   * Uses Reflection API to directly access & return the LoanDAO's private
   * nextID field.
   * @param loanDao LoanDAO The LoanDAO being used.
   * @return int The next ID to be used for committing loans.
   */
  public static int getPrivateNextId(LoanDAO loanDao)
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



  /**
   * Uses Reflection API to directly set the LoanDAO's private
   * nextID_ field.
   * @param loanDao LoanDAO The LoanDAO being used.
   * @param newNextID int The desired next ID.
   */
  public static void setPrivateNextId(LoanDAO loanDao, int newNextID)
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

  //===========================================================================
  // ID setter
  //===========================================================================

  /**
   * Uses Reflection API to directly set the Loan's private id.
   * @param loan ILoan The loan under test.
   * @param newId int The id to set on the Loan.
   */
  public static void setPrivateID(ILoan loan, int newId)
  {
    try {
      Class<?> loanClass = loan.getClass();
      Field id = loanClass.getDeclaredField("id_");

      // Enable direct modification of private field
      if (!id.isAccessible()) {
        id.setAccessible(true);
      }
      id.set(loan, newId);
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
  // loanMap setter
  //===========================================================================

  /**
   * Uses Reflection API to directly add Loans to the LoanDAO's private LoanMap.
   * @param loanDao LoanDAO The LoanDAO being used.
   * @param loans ILoan[] The array of Loans to be added.
   */
  public static void setPrivateLoanMap(LoanDAO loanDao, ILoan[] loans)
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

}
