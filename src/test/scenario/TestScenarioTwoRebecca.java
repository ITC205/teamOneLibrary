package test.scenario;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import junit.framework.TestCase;
import library.BorrowUC_CTL;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.entities.Book;
import library.entities.Member;
import library.interfaces.IBorrowUI;
import library.interfaces.EBorrowState;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.IMember;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import test.helper.IBorrowUIStub;
import static org.mockito.Mockito.*;

//Scenario 2:
//Member:
//  Has no loans
//  Has reached fine limit
//Swipes card
//Clicks Cancel


public class TestScenarioTwoRebecca extends TestCase
{
  
  private ICardReader mockReader;
  private IScanner mockScanner;
  private IPrinter mockPrinter;
  private IDisplay mockDisplay;
  private IMemberDAO memberDAO;
  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  private IMemberHelper memberHelper;
  private IMember member;
  
  private IBorrowUI ui;
  private BorrowUC_CTL controller;


  protected void createMocks()
  {
    mockReader = mock(ICardReader.class);
    mockScanner = mock(IScanner.class);
    mockPrinter = mock(IPrinter.class);
    mockDisplay = mock(IDisplay.class);
  }
  
  
  
  protected void setupObjects()
  {
    bookDAO = new BookDAO(new BookHelper());
    loanDAO = new LoanDAO(new LoanHelper());
    memberDAO = new MemberDAO(new MemberHelper());
    memberDAO.addMember("Bill", "Jones", "123456", "bjones@mail.com");
    controller = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
                      mockDisplay, bookDAO, loanDAO,
                      memberDAO);
    ui = new IBorrowUIStub();
    
    memberDAO.getMemberByID(1).addFine(10.0f);
  }
  
  
  
  // Verify that Constructor and Initialise methods set the controller state as required
  public void testScenarioOne()
  {
    createMocks();
    setupObjects();
    assertEquals(EMemberState.BORROWING_DISALLOWED, memberDAO.getMemberByID(1).getState());
    assertEquals(EBorrowState.CREATED, getState());
    controller.initialise();
    assertEquals(EBorrowState.INITIALIZED, getState());

    controller.cardSwiped(1);
    assertEquals(EBorrowState.BORROWING_RESTRICTED, getState());
    
    assertTrue(memberDAO.getMemberByID(1).hasFinesPayable());
    assertTrue(memberDAO.getMemberByID(1).hasReachedFineLimit());
    assertEquals(memberDAO.getMemberByID(1).getTotalFines(), 10.0f);
    controller.cancelled();
    
    assertEquals(EBorrowState.CANCELLED, getState());
    assertEquals(0, getScanCount());
  }
  
  
  
  private EBorrowState getState()
  {
    EBorrowState controllerState = null;
    try {
      Class<?> borrowUC_CTLClass = controller.getClass();
      Field state = borrowUC_CTLClass.getDeclaredField("state");
      state.setAccessible(true);
      controllerState = (EBorrowState) state.get(controller);
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
