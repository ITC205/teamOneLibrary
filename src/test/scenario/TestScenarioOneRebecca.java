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


/**
 * This class provides scenario testing for BorrowUC_CTL
 * 
 *  Scenario 1:
 *  Member:
 *    Has no loans
 *    Has no fines
 *
 * 1) Scans 5 books
 * 2) Clicks Reject
 * 3) Scans 2 books
 * 4) Clicks Completed
 * 5) Clicks Confirm
 *
 * @author  Rebecca Callow
 */
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
    
    // The card reader should be disabled to prevent swiping another member ID card
    verify(mockReader).setEnabled(false);
    // The book scanner should be enabled to allow scanning of books
    verify(mockScanner).setEnabled(true);
    
    // The member scans 5 books
    controller.bookScanned(1);
    // scanCount should be incremented with every book scanned
    assertEquals(1, getScanCount());
    controller.bookScanned(2);
    assertEquals(2, getScanCount());
    controller.bookScanned(3);
    assertEquals(3, getScanCount());
    controller.bookScanned(4);
    assertEquals(4, getScanCount());
    controller.bookScanned(5);
    assertEquals(5, getScanCount());
    
    // All books should remain available until the loans are confirmed
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(6).getState());
    
    // When scanCount reaches 5, the controller state should change to CONFIRMING_LOANS
    // as the member may not scan more than 5 books (IMember.LOAN_LIMIT)
    assertEquals(EBorrowState.CONFIRMING_LOANS, getState());
    
    // The member state should remain as BORROWING_ALLOWED because the loans are not 
    // confirmed
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
    
    // The member rejects the pending loans
    controller.loansRejected();
    
    // The member state should remeain as BORROWING_ALLOWED because the member chose not 
    // to proceed with borrowing the books
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
    
    // scanCount should be reset to 0, because the record of books scanned has been discarded
    assertEquals(0, getScanCount());
    
    // The member may now scan more books
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    
    // The member scans two books. One is the same as previously. This is possible because its
    // state is still AVAILABLE
    controller.bookScanned(1);
    controller.bookScanned(6);
    
    // All books should remain available for loan
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(6).getState());
    
    // The controller state remains as SCANNING_BOOKS, because the member has not reached 
    // the loan limit
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
    // Two books have been scanned
    assertEquals(2, getScanCount());
    
    // The member clicks the 'Completed' button
    controller.scansCompleted();
    
    // The controller state should be changed to CONFIRMING_LOANS. The member may 
    // still choose to reject the loans now if desired.
    assertEquals(EBorrowState.CONFIRMING_LOANS, getState());
    
    // The member clicks the 'Confirm' button to approve the loans
    controller.loansConfirmed();
    
    // The controller state should be changed to COMPLETED
    assertEquals(EBorrowState.COMPLETED, getState());
    
    // The states of the books with IDs 1 and 6 should be changed to ON_LOAN
    assertEquals(EBookState.ON_LOAN, bookDAO.getBookByID(1).getState());
    assertEquals(EBookState.ON_LOAN, bookDAO.getBookByID(6).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(2).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(3).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(4).getState());
    assertEquals(EBookState.AVAILABLE, bookDAO.getBookByID(5).getState());
    
    // The member state remains as BORROWING_ALLOWED, because he did not reach the
    // loan limit
    assertEquals(EMemberState.BORROWING_ALLOWED, memberDAO.getMemberByID(1).getState());
  }
  
  
  
  // ==========================================================================
  // Methods: Accessors for private fields
  // ==========================================================================
  
  
  
  // Use reflection to access the value of borrowUC_CTL.state
  private EBorrowState getState()
  {
    EBorrowState controllerState = null;
    try {
      Class<?> borrowUC_CTLClass = controller.getClass();
      Field state = borrowUC_CTLClass.getDeclaredField("state");
      // Set the private variable to accessible
      state.setAccessible(true);
      // Retrieve the value of state
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
  
  
  
  // Use reflection to access the value of borrowUC_CTL.scanCount
  private int getScanCount() 
  {
    int controllerScanCount = 0;
    try {
      Class<?> borrowUC_CTLClass = controller.getClass();
      Field scanCount = borrowUC_CTLClass.getDeclaredField("scanCount");
      // Set the private variable to accessible
      scanCount.setAccessible(true);
      // Retrieve the value of scanCount
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
