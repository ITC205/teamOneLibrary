package test.scenario;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import junit.framework.TestCase;
import library.BorrowUC_CTL;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.interfaces.IBorrowUI;
import library.interfaces.EBorrowState;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.EMemberState;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;


// Scenario 1:
// Member:
//    Has no loans
//    Has no fines

// 1) Scans 5 books
// 2) Clicks Reject
// 3) Scans 2 books
// 4) Clicks Completed
// 5) Clicks Confirm


public class TestScenarioOneRebecca extends TestCase
{
  
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private ICardReader mockReader;
  private IScanner mockScanner;
  private IPrinter mockPrinter;
  private IDisplay mockDisplay;
  private IMemberDAO memberDAO;
  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  
  private BorrowUC_CTL controller;


  
  // ==========================================================================
  // Methods: Preparation for tests
  // ==========================================================================
  
  
  
  // Create mock objects for IO
  protected void createMocks()
  {
    mockReader = mock(ICardReader.class);
    mockScanner = mock(IScanner.class);
    mockPrinter = mock(IPrinter.class);
    mockDisplay = mock(IDisplay.class);
  }
  
  
  
  // Create objects for testing
  protected void setupObjects()
  {
    bookDAO = new BookDAO(new BookHelper());
    loanDAO = new LoanDAO(new LoanHelper());
    memberDAO = new MemberDAO(new MemberHelper());
    
    // Only one member is required for this test
    memberDAO.addMember("Bill", "Jones", "123456", "bjones@mail.com");
    
    // Set up controller
    controller = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
                      mockDisplay, bookDAO, loanDAO,
                      memberDAO);
    
    // Create and add books to bookDAO. All are available for loan by default
    bookDAO.addBook("Tolkien", "The Two Towers", "TOL002");
    bookDAO.addBook("Aldous Huxley", "Brave New World", "HUX001");
    bookDAO.addBook("George Orwell", "1984", "ORW001");
    bookDAO.addBook("Dean Koontz", "Winter Moon", "KOO001");
    bookDAO.addBook("Dean Koontz", "The Mask", "KOO002");
    bookDAO.addBook("George Martin", "A Game of Thrones", "MAR001");
    bookDAO.addBook("Charlotte Bronte", "Jane Eyre", "BRO001");
  }
  
  
  
  // ==========================================================================
  // Methods: Testing
  // ==========================================================================
  
  
  
  // Testing of Scenario One
  public void testScenarioOne()
  {
    // Prepare for testing
    createMocks();
    setupObjects();
    
    // The member has no loans and no fines, so member state should be BORROWING_ALLOWED
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
    
    // Verify thata controller stste is CREATED
    assertEquals(EBorrowState.CREATED, getState());
    
    // Initialise controller and verify that the state has changed.
    controller.initialise();
    assertEquals(EBorrowState.INITIALIZED, getState());

    // Read member card. The member ID is 1
    controller.cardSwiped(1);
    
    // The member has no current loans, and has not scanned any books, so scanCount 
    // is initialised to 0
    assertEquals(0, getScanCount());
    
    // The member is not restricted, so controller state should change to SCANNING_BOOKS
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    
    verify(mockReader).setEnabled(false);
    verify(mockScanner).setEnabled(true);
    
    controller.bookScanned(1);
    assertEquals(1, getScanCount());
    controller.bookScanned(2);
    assertEquals(2, getScanCount());
    controller.bookScanned(3);
    assertEquals(3, getScanCount());
    controller.bookScanned(4);
    assertEquals(4, getScanCount());
    controller.bookScanned(5);
    assertEquals(5, getScanCount());
    
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(6).getState());
    
    assertEquals(EBorrowState.CONFIRMING_LOANS, getState());
    
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
    
    controller.loansRejected();
    
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
    
    assertEquals(0, getScanCount());
    
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    
    controller.bookScanned(1);
    controller.bookScanned(6);
    
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(6).getState());
    
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
    
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
  }
  
  
  
  // ==========================================================================
  // Methods: Accessors for private fields
  // ==========================================================================
  
  
  
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
  
  
  
  private void setMockBorrowUI(IBorrowUI mockBorrowUI) 
  {
      Class<?> borrowUC_CTLClass = controller.getClass();
      Field ui;
      try 
      {
        ui = borrowUC_CTLClass.getDeclaredField("ui");
        ui.setAccessible(true);
        ui.set(controller, mockBorrowUI);
      } 
      catch (NoSuchFieldException e) 
      {
        e.printStackTrace();
      } 
      catch (SecurityException e)
      {
        e.printStackTrace();
      }
      catch (IllegalArgumentException e) 
      {
        e.printStackTrace();
      } 
      catch (IllegalAccessException e) 
      {
        e.printStackTrace();
      }
  }
  
  
  
  private int getScanCount() 
  {
    int controllerScanCount = 0;
    try {
      Class<?> borrowUC_CTLClass = controller.getClass();
      Field scanCount = borrowUC_CTLClass.getDeclaredField("scanCount");
      scanCount.setAccessible(true);
      controllerScanCount = (int) scanCount.get(controller);
      return controllerScanCount;
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return controllerScanCount;
  }
}
