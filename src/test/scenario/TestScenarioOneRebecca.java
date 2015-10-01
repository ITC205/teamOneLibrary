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

//Scenario 1:
//Member:
//  Has no loans
//  Has no fines
//Scans 5 books
//Clicks Reject
//Scans 2 books
//Clicks Completed
//Clicks Confirm


public class TestScenarioOneRebecca extends TestCase
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
    
    bookDAO.addBook("Tolkien", "The Two Towers", "TOL002");
    bookDAO.addBook("Aldous Huxley", "Brave New World", "HUX001");
    bookDAO.addBook("George Orwell", "1984", "ORW001");
    bookDAO.addBook("Dean Koontz", "Winter Moon", "KOO001");
    bookDAO.addBook("Dean Koontz", "The Mask", "KOO002");
    bookDAO.addBook("George Martin", "A Game of Thrones", "MAR001");
    bookDAO.addBook("Charlotte Bronte", "Jane Eyre", "BRO001");
  }
  
  
  
  // Verify that Constructor and Initialise methods set the controller state as required
  public void testScenarioOne()
  {
    createMocks();
    setupObjects();
    assertEquals(EBorrowState.CREATED, getState());
    controller.initialise();
    assertEquals(EBorrowState.INITIALIZED, getState());

    controller.cardSwiped(1);
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    
    controller.bookScanned(1);
    controller.bookScanned(2);
    controller.bookScanned(3);
    controller.bookScanned(4);
    controller.bookScanned(5);
    
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(6).getState());
    
    assertEquals(EBorrowState.CONFIRMING_LOANS, getState());
    
    controller.loansRejected();
    
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    
    controller.bookScanned(1);
    controller.bookScanned(6);
    
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    
    controller.scansCompleted();
    
    assertEquals(EBorrowState.CONFIRMING_LOANS, getState());
    
    controller.loansConfirmed();
    
    assertEquals(EBorrowState.COMPLETED, getState());
    
    assertEquals(EBookState.ON_LOAN, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.ON_LOAN, bookDAO.getBookByID(6).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
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
