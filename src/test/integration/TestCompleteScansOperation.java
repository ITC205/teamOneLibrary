package test.integration;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import test.unit.LoanBuilder;

/**
 * TestCompletScansOperation class
 * 
 * Test class that isolates the Complete Scans system operation
 * Code: BBUC_Op4
 * 
 * Operation Preconditions:
 * - BorrowUC_CTL object exists
 * - Pending loan list exists
 * - BorrowUC_CTL state = SCANNING_BOOKS
 * 
 * @author admin
 *
 */
public class TestCompleteScansOperation extends TestCase
{
  // ==========================================================================
  // Variables
  // ==========================================================================



  private ICardReader mockedCardReader = mock(ICardReader.class);
  private IScanner mockedScanner = mock(IScanner.class);
  private IPrinter mockedPrinter = mock(IPrinter.class);
  private IDisplay mockedDisplay = mock(IDisplay.class);
  private IBorrowUI mockedUI = mock(IBorrowUI.class);

  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  private IMemberDAO memberDAO;
  
  private IBook bookOne;    // AVAILABLE
  private IBook bookTwo;    // AVAILABLE
  private IBook bookThree;  // AVAILABLE

  private IMember memberOne; // Unrestricted, no loans

  private List<ILoan> testLoanList; // Contains 3 loans by default
  
  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
  private String borrowDateString;
  private String dueDateString;

  private BorrowUC_CTL testController;



  // ==========================================================================
  // Constructor
  // ==========================================================================



  public TestCompleteScansOperation(String methodName) {
    super(methodName);
  }



  // ==========================================================================
  // Test Set-Up Methods
  // ==========================================================================
  
  
  
  private void initialiseVariables() {
    
    bookDAO = new BookDAO(new BookHelper());
    memberDAO = new MemberDAO(new MemberHelper());
    loanDAO = new LoanDAO(new LoanHelper());
    
    
    // Fresh books default to AVAILABLE
    bookOne = bookDAO.addBook("authorOne", "titleOne", "callOne");
    bookTwo = bookDAO.addBook("authorTwo", "titleTwo", "callTwo");
    bookThree = bookDAO.addBook("authorThree", "titleThree", "callThree");
    
    memberOne = memberDAO.addMember("fNameOne", "lNameOne", "0263636363", 
                           "person@address.com");
   
    // Establish expected borrow and due dates for any loans created by tests
    borrowDateString = dateFormat.format(LoanBuilder.DEFAULT_BORROW_DATE);
    dueDateString = dateFormat.format(LoanBuilder.DEFAULT_DUE_DATE);
    
    // Create a default loan list
    testLoanList = new ArrayList<>();
    
    testLoanList.add(LoanBuilder.newLoan()
                                .withBook(bookOne)
                                .withBorrower(memberOne)
                                .build());
    
    testLoanList.add(LoanBuilder.newLoan()
                                .withBook(bookTwo)
                                .withBorrower(memberOne)
                                .build());
    
    testLoanList.add(LoanBuilder.newLoan()
                                .withBook(bookThree)
                                .withBorrower(memberOne)
                                .build());
  }
  
  
  
  private void setUpPreconditions() {
    
    testController = new BorrowUC_CTL(mockedCardReader, 
                                      mockedScanner, 
                                      mockedPrinter,
                                      mockedDisplay,
                                      bookDAO,
                                      loanDAO,
                                      memberDAO);
    
    setState(EBorrowState.SCANNING_BOOKS);
    setMockedUI(mockedUI);    
    setMember(memberOne);
  }
  
  
  
  @Override
  protected void tearDown() {
    testController = null;
    
    bookDAO = null;
    loanDAO = null;
    memberDAO = null;
    
    bookOne = null;
    bookTwo = null;
    bookThree = null;
    
    memberOne = null;
    
    borrowDateString = null;
    dueDateString = null;
    
    testLoanList = null;
  }
  
  
  
  // ==========================================================================
  // Tests
  // ==========================================================================
  
  
  
  public void testScansCompletedDefault() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    
    // Set loanList to demo list
    setLoanList(testLoanList);
    
    // Confirm valid state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Confirm list contain three demo loans
    List<ILoan> loanList = getLoanList();
    assertEquals(3, loanList.size());
    
    // Call method under test
    testController.scansCompleted();
    
    // Confirm state change
    assertControllerStateEquals(EBorrowState.CONFIRMING_LOANS);
    
    // Verify disabling of hardware
    verify(mockedCardReader).setEnabled(false);
    verify(mockedScanner).setEnabled(false);
    
    // Verify displayConfirmingLoan call
    verify(mockedUI).displayConfirmingLoan("Loan ID:  0\n"
                                          + "Author:   authorOne\n"
                                          + "Title:    titleOne\n"
                                          + "Borrower: fNameOne lNameOne\n"
                                          + "Borrowed: " + borrowDateString +"\n"
                                          + "Due Date: " + dueDateString
                                          + "\n\n"
                                          + "Loan ID:  0\n"
                                          + "Author:   authorTwo\n"
                                          + "Title:    titleTwo\n"
                                          + "Borrower: fNameOne lNameOne\n"
                                          + "Borrowed: " + borrowDateString +"\n"
                                          + "Due Date: " + dueDateString
                                          + "\n\n"
                                          + "Loan ID:  0\n"
                                          + "Author:   authorThree\n"
                                          + "Title:    titleThree\n"
                                          + "Borrower: fNameOne lNameOne\n"
                                          + "Borrowed: " + borrowDateString +"\n"
                                          + "Due Date: " + dueDateString);
  }
  
  
  
  public void testScansCompletedOneLoan() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    
    // Create and set alternate test loan list (only one Loan)
    List<ILoan> singleLoanList = new ArrayList<>();
    singleLoanList.add(LoanBuilder.newLoan()
                                .withBook(bookOne)
                                .withBorrower(memberOne)
                                .build());
    setLoanList(singleLoanList);
    
    // Confirm valid state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Confirm list contain only one loan
    List<ILoan> loanList = getLoanList();
    assertEquals(1, loanList.size());
    
    // Call method under test
    testController.scansCompleted();
    
    // Confirm state change
    assertControllerStateEquals(EBorrowState.CONFIRMING_LOANS);
    
    // Verify disabling of hardware
    verify(mockedCardReader).setEnabled(false);
    verify(mockedScanner).setEnabled(false);
    
    // Verify displayConfirmingLoan call
    verify(mockedUI).displayConfirmingLoan("Loan ID:  0\n"
                                          + "Author:   authorOne\n"
                                          + "Title:    titleOne\n"
                                          + "Borrower: fNameOne lNameOne\n"
                                          + "Borrowed: " + borrowDateString +"\n"
                                          + "Due Date: " + dueDateString);
  }
  
  
  
  public void testScansCompletedNoLoans() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    
    // Create and set alternate empty loan list
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm valid state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Confirm list is empty
    List<ILoan> loanList = getLoanList();
    assertTrue(loanList.isEmpty());
    
    // Call method under test
    try {
      testController.scansCompleted();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }
    
    // Confirm state unchanged
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Verify no method calls to hardware
    verify(mockedCardReader, never()).setEnabled(false);
    verify(mockedScanner, never()).setEnabled(false);
    
    // Verify displayConfirmingLoan call not made
    verify(mockedUI, never()).displayConfirmingLoan(anyString());  
  }
  
  
  
  public void testScansCompletedFromInvalidState() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    
    // Set loanList to demo list
    setLoanList(testLoanList);
    
    // Set and confirm invalid state
    setState(EBorrowState.COMPLETED);
    assertControllerStateEquals(EBorrowState.COMPLETED);
    
    // Call method under test
    try {
      testController.scansCompleted();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }
    
    // Confirm state unchanged
    assertControllerStateEquals(EBorrowState.COMPLETED);
    
    // Verify no method calls to hardware
    verify(mockedCardReader, never()).setEnabled(false);
    verify(mockedScanner, never()).setEnabled(false);
    
    // Verify displayConfirmingLoan call not made
    verify(mockedUI, never()).displayConfirmingLoan(anyString()); 
  }
  
  
  
  // ==========================================================================
  // Custom Asserts
  // ==========================================================================
  

  
  public void assertControllerStateEquals(EBorrowState state) {
    try {
      // Using Reflection to directly set private field 'scanCount'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field stateField = borrowUC_CTLClass.getDeclaredField("state");

      // Enable direct modification of private field
      if (!stateField.isAccessible()) {
        stateField.setAccessible(true);
      }
      
      
      EBorrowState stateValue = (EBorrowState) stateField.get(testController);
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
  
  
  
  private void setState(EBorrowState newState) {
    try {
      // Using Reflection to directly set private field 'state'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field state = borrowUC_CTLClass.getDeclaredField("state");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set controller state
      state.set(testController, newState);
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
  
  
  
  private void setMockedUI(IBorrowUI mockedUI) {
    try {
      // Using Reflection to directly set private field 'ui'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field ui = borrowUC_CTLClass.getDeclaredField("ui");

      // Enable direct modification of private field
      if (!ui.isAccessible()) {
        ui.setAccessible(true);
      }

      // Set ui
      ui.set(testController, mockedUI);
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
  
  
  
  private void setMember(IMember mockedMember) {
    try {
      // Using Reflection to directly set private field 'borrower'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field borrower = borrowUC_CTLClass.getDeclaredField("borrower");

      // Enable direct modification of private field
      if (!borrower.isAccessible()) {
        borrower.setAccessible(true);
      }

      // Set borrower
      borrower.set(testController, mockedMember);
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
  
  
  
  private void setLoanList(List<ILoan> loanList) {
    try {
      // Using Reflection to directly set private field 'borrower'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field loanListField = borrowUC_CTLClass.getDeclaredField("loanList");

      // Enable direct modification of private field
      if (!loanListField.isAccessible()) {
        loanListField.setAccessible(true);
      }

      // Set loanList
      loanListField.set(testController, loanList);
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
      // Using Reflection to directly access loanList

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field loanListField = borrowUC_CTLClass.getDeclaredField("loanList");

      // Enable direct modification of private field
      if (!loanListField.isAccessible()) {
        loanListField.setAccessible(true);
      }

      @SuppressWarnings("unchecked")
      List<ILoan> loanListValue = (List<ILoan>) loanListField.get(testController);
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
}
