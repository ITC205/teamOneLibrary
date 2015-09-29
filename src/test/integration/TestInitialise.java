package test.integration;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import library.BorrowUC_CTL;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

public class TestInitialise extends TestCase
{
  
  ICardReader mockReader;
  IScanner mockScanner;
  IPrinter mockPrinter;
  IDisplay mockDisplay;
  private IBorrowUI mockBorrowUI = mock(IBorrowUI.class);

  IBookDAO bookDAO;
  ILoanDAO loanDAO;
  IMemberDAO memberDAO;
  BorrowUC_CTL testController;

  Throwable exception;
  
  protected void createMocks()
  {
    mockReader = mock(ICardReader.class);
    mockScanner = mock(IScanner.class);
    mockPrinter = mock(IPrinter.class);
    mockDisplay = mock(IDisplay.class);
    bookDAO = new BookDAO(new BookHelper());
    loanDAO = new LoanDAO(new LoanHelper());
    memberDAO = new MemberDAO(new MemberHelper());
  }
  
  

  public void testStateNotCreated()
  {
    createMocks();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    try
    {
      testController.initialise();
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof RuntimeException);
  }




  private void setState(EBorrowState newState) 
  {
      // Use reflection to access BorrowUC_CTL.state

      Class<?> borrowUC_CTLClass = testController.getClass();
      try {
        Field state = borrowUC_CTLClass.getDeclaredField("state");

      state.setAccessible(true);

      state.set(testController, newState);
      }
      catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
  }
  
  
  
  private EBorrowState getState()
  {
    EBorrowState controllerState = null;
    try {
      Class<?> borrowUC_CTLClass = testController.getClass();
      Field state = borrowUC_CTLClass.getDeclaredField("state");
      state.setAccessible(true);
      controllerState = (EBorrowState) state.get(testController);
      return controllerState;
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return controllerState;
  }
  
}
