package test.integration;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import library.BorrowUC_CTL;
import library.interfaces.EBorrowState;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

public class TestCardSwipedMethod extends TestCase
{

  ICardReader mockReader;
  IScanner mockScanner;
  IPrinter mockPrinter;
  IDisplay mockDisplay;
  IBookDAO mockBookDAO;
  ILoanDAO mockLoanDAO;
  IMemberDAO mockMemberDAO;
  BorrowUC_CTL testController;
  
  
  
  protected void createMocks()
  {
  mockReader = mock(ICardReader.class);
  mockScanner = mock(IScanner.class);
  mockPrinter = mock(IPrinter.class);
  mockDisplay = mock(IDisplay.class);
  mockBookDAO = mock(IBookDAO.class);
  mockLoanDAO = mock(ILoanDAO.class);
  mockMemberDAO = mock(IMemberDAO.class);
  
  testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
      mockDisplay, mockBookDAO, mockLoanDAO, mockMemberDAO);
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }
}
