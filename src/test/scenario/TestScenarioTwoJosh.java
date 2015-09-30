package test.scenario;

import junit.framework.TestCase;
import library.BorrowUC_CTL;
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

import static org.mockito.Matchers.anyString;
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
  private IBook scannedBookThree;
  
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
    //TODO
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
    scannedBookThree = null;
    
    member = null;
  }
  
  
  
  // ==========================================================================
  // Test
  // ==========================================================================
  
  
  
  public void testScenario() {
    //TODO
    
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
