package test.scenario;

import junit.framework.TestCase;
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
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import test.helper.IBorrowUIStub;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;

/**
 * TestScenarioTwoJosh class
 * 
 * Class that tests a complete user 'scenario' without the UI
 * 
 * Scenario Brief Description:  
 *  Member: 
 *     no loans
 *     no fines
 *     
 * Scans 3 books, one of which is a duplicate (Same ID)
 *
 * Clicks complete.
 * Clicks Reject
 * Clicks Cancel.
 *
 * The test is not set up to verify every method call to every interacting object.
 * It focuses on calls triggering 'hardware' changes, state transitions and key 
 * expected post-conditions of each step in the specific scenario. UI display 
 * messages and other details are better verified through UAT testing. 
 * 
 * @author Josh Kent
 */
public class TestScenarioTwoJosh extends TestCase
{
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private ICardReader mockedReader;
  private IScanner mockedScanner;
  private IPrinter mockedPrinter;
  private IDisplay mockedDisplay;

  private IBookHelper bookHelper;
  private ILoanHelper loanHelper;
  private IMemberHelper memberHelper;

  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  private IMemberDAO memberDAO;
  
  private IBorrowUI stubbedUI;
  
  private BorrowUC_CTL ctl;
  
  private List<ILoan> ctlLoanList;
  private List<IBook> ctlBookList;
  
  private IBook scannedBookOne;
  private IBook scannedBookTwo;

  private IMember member;
  
  
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public TestScenarioTwoJosh(String methodName) {
    super(methodName);
  }
  
  
  
  // ==========================================================================
  // Set-up/Tear Down Methods
  // ==========================================================================
  
  
  
  @Override
  protected void setUp() {
    // Mock all UI
    mockedReader = mock(ICardReader.class);
    mockedScanner = mock(IScanner.class);
    mockedPrinter = mock(IPrinter.class);
    mockedDisplay = mock(IDisplay.class);
    stubbedUI = new IBorrowUIStub();

    // Create real objects
    bookHelper = new BookHelper();
    loanHelper = new LoanHelper();
    memberHelper = new MemberHelper();

    bookDAO = new BookDAO(bookHelper);
    loanDAO = new LoanDAO(loanHelper);
    memberDAO = new MemberDAO(memberHelper);
    
    // Add books to be scanned
    scannedBookOne = bookDAO.addBook("Harper Lee", "To Kill A Mockingbird", "LEE 123");
    scannedBookTwo = bookDAO.addBook("Dan Brown", "Digital Fortress", "BRO 410");
    
    // Add member for this scenario
    member = memberDAO.addMember("Josh", "Kent", "62626262", "josh@kent.com");
    
    ctl = new BorrowUC_CTL(mockedReader, mockedScanner, mockedPrinter, 
            mockedDisplay, bookDAO, loanDAO, memberDAO);
    
    // Directly set mock UI
    setMockedUI(stubbedUI);
  }
  
  
  
  @Override
  protected void tearDown() {
    mockedReader = null;
    mockedScanner = null;
    mockedPrinter = null;
    mockedDisplay = null;

    bookHelper = null;
    loanHelper = null;
    memberHelper = null;

    bookDAO = null;
    loanDAO = null;
    memberDAO = null;
    
    ctl = null;
    
    stubbedUI = null;
    
    ctlLoanList = null;
    ctlBookList = null;
    
    scannedBookOne = null;
    scannedBookTwo = null;
    
    member = null;
  }
  
  
  
  // ==========================================================================
  // Test
  // ==========================================================================
  
  
  
  public void testScenario() {
    // Every time a method should have enabled/disabled a reader or scanner
    // it will be counted
    int readerEnabledCount = 0;
    int readerDisabledCount = 0;
    int scannerDisabledCount = 0;
    int scannerEnabledCount = 0;
    
    // Controller should initially be CREATED
    assertControllerStateEquals(EBorrowState.CREATED);
    
    // Verify hardware calls
    verify(mockedReader).addListener(ctl);
    verify(mockedScanner).addListener(ctl);
    
    //Confirm scanCount = 0
    assertScanCountEquals(0);
    
    
    // Click Borrow ===========================================================
    ctl.initialise();
    
    // Controller should now be INITIALIZED
    assertControllerStateEquals(EBorrowState.INITIALIZED);
    
    // Reader should be enabled, scanner should be disabled here
    readerEnabledCount++;
    scannerDisabledCount++;
    
    // Grab references to bookList and loanList (created during initialise())
    ctlBookList = getBookList();
    ctlLoanList = getLoanList();
    // Confirm empty lists
    assertTrue(ctlLoanList.isEmpty());
    assertTrue(ctlBookList.isEmpty());
    
    
    // Swipe member card ======================================================
    ctl.cardSwiped(1);
    
    // Controller should now be SCANNING_BOOKS
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Reader should be disabled, scanner should be enabled here
    readerDisabledCount++;
    scannerEnabledCount++;
    
    
    // Scan first book ========================================================
    ctl.bookScanned(1);
    
    // Confirm book has been added to bookList
    assertTrue(ctlBookList.contains(scannedBookOne));
    
    // Confirm scanCount incremented
    assertScanCountEquals(1);
    
    // Confirm a pending loan has been added to loanList
    assertEquals(1, ctlLoanList.size());
    
    
    // Scan first book (again) =================================================
    ctl.bookScanned(1);
    
    // Confirm first book is still in bookList
    assertTrue(ctlBookList.contains(scannedBookOne));

    // Confirm bookList online contains one book
    assertEquals(1, ctlBookList.size());
    
    // Confirm scanCount unchanged
    assertScanCountEquals(1);
    
    // Confirm loanList unchanged
    assertEquals(1, ctlLoanList.size());
    
    
    // Scan second book ========================================================
    ctl.bookScanned(2);
    
    // Confirm book has been added to bookList (without affecting other books)
    assertTrue(ctlBookList.contains(scannedBookOne));
    assertTrue(ctlBookList.contains(scannedBookTwo));
    
    // Confirm scanCount incremented
    assertScanCountEquals(2);
    
    // Confirm a pending loan has been added to loanList
    assertEquals(2, ctlLoanList.size());
    
    
    // Complete book scanning =================================================
    ctl.scansCompleted();
    
    // Controller should now be CONFIRMING_LOANS
    assertControllerStateEquals(EBorrowState.CONFIRMING_LOANS);
    
    // Scanner and reader should be disabled here
    readerDisabledCount++;
    scannerDisabledCount++;
    
    
    // Reject loans ===========================================================
    ctl.loansRejected();
    
    // Controller should revert to SCANNING_BOOKS
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // scanCount should be reset to borrowers existing loan count (0 in this case)
    assertScanCountEquals(0);
    
    // loanList and bookList should be reset
    assertTrue(ctlLoanList.isEmpty());
    assertTrue(ctlBookList.isEmpty());
    
    // Card reader disabled and scanner enabled
    readerDisabledCount++;
    scannerEnabledCount++;
    
    
    // Cancel borrowing =======================================================
    ctl.cancelled();
    
    // Controller state should be CANCELLED
    assertControllerStateEquals(EBorrowState.CANCELLED);
    
    // Scanner and card reader both disabled
    readerDisabledCount++;
    scannerDisabledCount++;
    
    // Verify no calls to printer
    verify(mockedPrinter, never()).print(anyString());
    
    // Use expected counts to verify number of calls to scanner and reader 
    // setEnabled(true) and setEnabled(false)
    verify(mockedReader, times(readerDisabledCount)).setEnabled(false);
    verify(mockedScanner, times(scannerDisabledCount)).setEnabled(false);
    verify(mockedReader, times(readerEnabledCount)).setEnabled(true);
    verify(mockedScanner, times(scannerEnabledCount)).setEnabled(true);
    
    // No loans should have been associated with the member, as the borrowing 
    // was cancelled. The member did not have any existing loans.
    List<ILoan> memberLoanList = member.getLoans();
    assertTrue(memberLoanList.isEmpty());
    
  }
  
  
  
  // ==========================================================================
  // Custom Asserts
  // ==========================================================================
  
  
  
  public void assertScanCountEquals(int scanCount) {
    try {
      // Using Reflection to directly access private field 'scanCount'

      Class<?> borrowUC_CTLClass = ctl.getClass();
      Field scanCountField = borrowUC_CTLClass.getDeclaredField("scanCount");

      // Enable direct modification of private field
      if (!scanCountField.isAccessible()) {
        scanCountField.setAccessible(true);
      }
      
      // Confirm scanCount field contains the expected value
      int scanCountFieldValue = scanCountField.getInt(ctl);
      assertEquals(scanCount, scanCountFieldValue);

    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }
  }
  
  
  
  public void assertControllerStateEquals(EBorrowState state) {
    try {
      // Using Reflection to access 'state' field

      Class<?> borrowUC_CTLClass = ctl.getClass();
      Field stateField = borrowUC_CTLClass.getDeclaredField("state");

      // Enable direct modification of private field
      if (!stateField.isAccessible()) {
        stateField.setAccessible(true);
      }
      
      // Confirm state matches the expected value
      EBorrowState stateValue = (EBorrowState) stateField.get(ctl);
      assertEquals(state, stateValue);

    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }
    catch (ClassCastException e) {
      fail("ClassCastException should not occur");
    }
  }
  
  
  
  // ==========================================================================
  // Directly Modifying Helper Methods (Using Reflection)
  // ==========================================================================
  
  
  
  private void setMockedUI(IBorrowUI mockedUI) {
    try {
      // Using Reflection to directly set private field 'ui'

      Class<?> borrowUC_CTLClass = ctl.getClass();
      Field ui = borrowUC_CTLClass.getDeclaredField("ui");

      // Enable direct modification of private field
      if (!ui.isAccessible()) {
        ui.setAccessible(true);
      }

      // Set ui
      ui.set(ctl, mockedUI);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }
  }
  
  
  
  public List<ILoan> getLoanList() {
    try {
      // Using Reflection to directly access 'loanList'

      Class<?> borrowUC_CTLClass = ctl.getClass();
      Field loanListField = borrowUC_CTLClass.getDeclaredField("loanList");

      // Enable direct modification of private field
      if (!loanListField.isAccessible()) {
        loanListField.setAccessible(true);
      }

      @SuppressWarnings("unchecked")
      List<ILoan> loanListValue = (List<ILoan>) loanListField.get(ctl);
      return loanListValue;
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }
    catch (ClassCastException e) {
      fail("ClassCastException should not occur");
    }
    return null;
  }
  
  
  
  public List<IBook> getBookList() {
    try {
      // Using Reflection to directly access 'bookList'

      Class<?> borrowUC_CTLClass = ctl.getClass();
      Field bookListField = borrowUC_CTLClass.getDeclaredField("bookList");

      // Enable direct modification of private field
      if (!bookListField.isAccessible()) {
        bookListField.setAccessible(true);
      }

      @SuppressWarnings("unchecked")
      List<IBook> bookListValue = (List<IBook>) bookListField.get(ctl);
      return bookListValue;
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }
    catch (ClassCastException e) {
      fail("ClassCastException should not occur");
    }
    return null;
  }
  
}
