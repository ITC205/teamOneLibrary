package test.integration;

import junit.framework.TestCase;
import library.BorrowUC_CTL;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * TestScanBookOperation class
 * 
 * Test class that isolates the Scan Book system operation
 * 
 * Operation Preconditions:
 *  - BorrowUC_CTL object exists
 *  - Scanner exists
 *  - BorrowUC_CTL is added as a listener to scanner
 *  - BorrowUC_CTL state = SCANNING_BOOKS
 *  - BorrowUC_CTL has a reference to a Member (the one who is borrowing)
 *  - bookList and loanList instance variable are initialised
 *  
 * @author Josh Kent
 *
 */
public class TestScanBookOperation extends TestCase
{
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private ICardReader mockedCardReader = mock(ICardReader.class);
  private IScanner mockedScanner = mock(IScanner.class);
  private IPrinter mockedPrinter = mock(IPrinter.class);
  private IDisplay mockedDisplay = mock(IDisplay.class);
  private IBookDAO mockedBookDAO = mock(IBookDAO.class);
  private ILoanDAO mockedLoanDAO = mock(ILoanDAO.class);
  private IMemberDAO mockedMemberDAO = mock(IMemberDAO.class);
  
  private IBorrowUI mockedUI = mock(IBorrowUI.class);
  
  private IBook mockedBookOne = mock(IBook.class);  // AVAILABLE
  private IBook mockedBookTwo = mock(IBook.class);  // AVAILABLE
  private IBook mockedBookThree = mock(IBook.class);// AVAILABLE
  private IBook mockedBookFour = mock(IBook.class); // AVAILABLE
  private IBook mockedBookFive = mock(IBook.class); // AVAILABLE
  private IBook mockedBookSix = mock(IBook.class);  // ON_LOAN
  
  private IMember mockedMemberOne = mock(IMember.class); // Unrestricted
  private IMember mockedMemberTwo = mock(IMember.class);
  private IMember mockedMemberThree = mock(IMember.class);
  
  private ILoan mockedLoanOne = mock(ILoan.class);  // mockedBookOne
  private ILoan mockedLoanTwo = mock(ILoan.class);  // mockedBookTwo
  private ILoan mockedLoanThree = mock(ILoan.class);// mockedBookThree
  private ILoan mockedLoanFour = mock(ILoan.class); // mockedBookFour
  private ILoan mockedLoanFive = mock(ILoan.class); // mockedBookFive
  
  private BorrowUC_CTL testController;
  
  
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public TestScanBookOperation(String methodName) {
    super(methodName);
  }
  
  
  
  // ==========================================================================
  // Test Set-Up Methods
  // ==========================================================================
  
  
  
  private void setUpMockBehaviour() {
    // Book behaviour
    when(mockedBookOne.getID()).thenReturn(1);
    when(mockedBookOne.getState()).thenReturn(EBookState.AVAILABLE);
    when(mockedBookOne.toString()).thenReturn("BookOne");
    
    when(mockedBookTwo.getID()).thenReturn(2);
    when(mockedBookTwo.getState()).thenReturn(EBookState.AVAILABLE);
    when(mockedBookTwo.toString()).thenReturn("BookTwo");
    
    when(mockedBookThree.getID()).thenReturn(3);
    when(mockedBookThree.getState()).thenReturn(EBookState.AVAILABLE);
    when(mockedBookThree.toString()).thenReturn("BookThree");
    
    when(mockedBookFour.getID()).thenReturn(4);
    when(mockedBookFour.getState()).thenReturn(EBookState.AVAILABLE);
    when(mockedBookFour.toString()).thenReturn("BookFour");
    
    when(mockedBookFive.getID()).thenReturn(5);
    when(mockedBookFive.getState()).thenReturn(EBookState.AVAILABLE);
    when(mockedBookFive.toString()).thenReturn("BookFive");
    
    when(mockedBookSix.getID()).thenReturn(6);
    when(mockedBookSix.getState()).thenReturn(EBookState.ON_LOAN);
    when(mockedBookSix.toString()).thenReturn("BookSix");
    
    // Loan behaviour
    when(mockedLoanOne.toString()).thenReturn("LoanOne BookOne MemberOne");
    when(mockedLoanTwo.toString()).thenReturn("LoanTwo BookTwo MemberOne");
    when(mockedLoanThree.toString()).thenReturn("LoanThree BookThree MemberOne");
    when(mockedLoanFour.toString()).thenReturn("LoanFour BookFour MemberOne");
    when(mockedLoanFive.toString()).thenReturn("LoanFive BookFive MemberOne");
    
    // BookDAO behaviour
    when(mockedBookDAO.getBookByID(1)).thenReturn(mockedBookOne);
    when(mockedBookDAO.getBookByID(2)).thenReturn(mockedBookTwo);
    when(mockedBookDAO.getBookByID(3)).thenReturn(mockedBookThree);
    when(mockedBookDAO.getBookByID(4)).thenReturn(mockedBookFour);
    when(mockedBookDAO.getBookByID(5)).thenReturn(mockedBookFive);
    when(mockedBookDAO.getBookByID(6)).thenReturn(mockedBookSix);
    when(mockedBookDAO.getBookByID(7)).thenReturn(null);
    
    // LoanDAO behaviour
    when(mockedLoanDAO.createLoan(mockedMemberOne, mockedBookOne))
                      .thenReturn(mockedLoanOne);
    when(mockedLoanDAO.createLoan(mockedMemberOne, mockedBookTwo))
                      .thenReturn(mockedLoanTwo);
    when(mockedLoanDAO.createLoan(mockedMemberOne, mockedBookThree))
                      .thenReturn(mockedLoanThree);
    when(mockedLoanDAO.createLoan(mockedMemberOne, mockedBookFour))
                      .thenReturn(mockedLoanFour);
    when(mockedLoanDAO.createLoan(mockedMemberOne, mockedBookFive))
                      .thenReturn(mockedLoanFive);
    
  }
  
  private void setUpPreconditions() {
    
    testController = new BorrowUC_CTL(mockedCardReader, 
                                      mockedScanner, 
                                      mockedPrinter,
                                      mockedDisplay,
                                      mockedBookDAO,
                                      mockedLoanDAO,
                                      mockedMemberDAO);
    
    setState(EBorrowState.SCANNING_BOOKS);
    setMockedUI(mockedUI);    
  }
  
  
  
  @Override
  protected void tearDown() {
    testController = null;
  }

  
  
  // ==========================================================================
  // Tests
  // ==========================================================================
  
  
  
  public void testBookScannedSingleBookDefault() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan one book
    testController.bookScanned(1);
    // Verify expected method calls
    verify(mockedUI).displayErrorMessage("");
    verify(mockedBookDAO).getBookByID(1);
    verify(mockedBookOne).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertLoanListContains(mockedLoanOne);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookOne");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
  }
  
  
  public void testBookScannedTwoBooksDefault() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan first book
    testController.bookScanned(1);
    // Verify expected method calls
    verify(mockedBookDAO).getBookByID(1);
    verify(mockedBookOne).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertLoanListContains(mockedLoanOne);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookOne");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan second book
    testController.bookScanned(2);
    // Should call this displayErrorMessage exactly the same way twice (once for
    // each book
    verify(mockedUI, times(2)).displayErrorMessage("");
    // Verify expected method calls
    verify(mockedBookDAO).getBookByID(2);
    verify(mockedBookTwo).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(2);
    
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertBookListContains(mockedBookTwo);
    assertLoanListContains(mockedLoanOne);
    assertLoanListContains(mockedLoanTwo);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookTwo");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne\n\n"
                                        + "LoanTwo BookTwo MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
  }
  
  public void testBookScannedFiveBooks() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan first book
    testController.bookScanned(1);
    // Verify expected method calls
    verify(mockedBookDAO).getBookByID(1);
    verify(mockedBookOne).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertLoanListContains(mockedLoanOne);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookOne");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan second book =======================================================
    testController.bookScanned(2);
    // Verify expected method calls
    verify(mockedBookDAO).getBookByID(2);
    verify(mockedBookTwo).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(2);
    
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertBookListContains(mockedBookTwo);
    
    assertLoanListContains(mockedLoanOne);
    assertLoanListContains(mockedLoanTwo);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookTwo");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne\n\n"
                                        + "LoanTwo BookTwo MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan third book ========================================================
    testController.bookScanned(3);
    
    // Verify expected method calls
    verify(mockedBookDAO).getBookByID(3);
    verify(mockedBookThree).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(3);
    
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertBookListContains(mockedBookTwo);
    assertBookListContains(mockedBookThree);
    
    assertLoanListContains(mockedLoanOne);
    assertLoanListContains(mockedLoanTwo);
    assertLoanListContains(mockedLoanThree);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookThree");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne\n\n"
                                        + "LoanTwo BookTwo MemberOne\n\n"
                                        + "LoanThree BookThree MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan fourth book ========================================================
    testController.bookScanned(4);
    verify(mockedBookDAO).getBookByID(4);
    verify(mockedBookFour).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(4);
    
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertBookListContains(mockedBookTwo);
    assertBookListContains(mockedBookThree);
    assertBookListContains(mockedBookFour);
    
    assertLoanListContains(mockedLoanOne);
    assertLoanListContains(mockedLoanTwo);
    assertLoanListContains(mockedLoanThree);
    assertLoanListContains(mockedLoanFour);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookFour");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne\n\n"
                                        + "LoanTwo BookTwo MemberOne\n\n"
                                        + "LoanThree BookThree MemberOne\n\n"
                                        + "LoanFour BookFour MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan fifth book ========================================================
    testController.bookScanned(5);
    // Should call this displayErrorMessage exactly the same way (once for
    // each book
    verify(mockedUI, times(5)).displayErrorMessage("");
    // Verify expected method calls
    verify(mockedBookDAO).getBookByID(5);
    verify(mockedBookFive).getState();
    
    // Confirm scan count increment
    assertScanCountEquals(5);
    
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertBookListContains(mockedBookTwo);
    assertBookListContains(mockedBookThree);
    assertBookListContains(mockedBookFour);
    assertBookListContains(mockedBookFive);
    
    assertLoanListContains(mockedLoanOne);
    assertLoanListContains(mockedLoanTwo);
    assertLoanListContains(mockedLoanThree);
    assertLoanListContains(mockedLoanFour);
    assertLoanListContains(mockedLoanFive);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookFive");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne\n\n"
                                        + "LoanTwo BookTwo MemberOne\n\n"
                                        + "LoanThree BookThree MemberOne\n\n"
                                        + "LoanFour BookFour MemberOne\n\n"
                                        + "LoanFive BookFive MemberOne");
    
    
    // Scan count five should trigger state change 
    // Confirm state change
    assertControllerStateEquals(EBorrowState.CONFIRMING_LOANS);
    
    // Verify displayConfirmingLoan call
    verify(mockedUI).displayConfirmingLoan("LoanOne BookOne MemberOne\n\n"
            + "LoanTwo BookTwo MemberOne\n\n"
            + "LoanThree BookThree MemberOne\n\n"
            + "LoanFour BookFour MemberOne\n\n"
            + "LoanFive BookFive MemberOne");
    
    // Verify disabling of hardware
    verify(mockedCardReader).setEnabled(false);
    verify(mockedScanner).setEnabled(false);
  }
  
  
  
  public void testBookScannedFromInvalidState() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Set invalid state
    setState(EBorrowState.CREATED);
    assertControllerStateEquals(EBorrowState.CREATED);
    
    // Attempt to scan a book
    try {
      testController.bookScanned(1);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }
  }
  
  
  
  public void testBookScannedBookNotFound() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan book not in DAO
    testController.bookScanned(7);
    // Confirm expected method calls
    verify(mockedUI).displayErrorMessage("");
    verify(mockedUI).displayErrorMessage("Book not found");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    //Confirm no scanCount increment
    assertScanCountEquals(0);
  }
  
  
  
  public void testBookScannedBookNotAvailable() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan book that is ON_LOAN
    testController.bookScanned(6);
    // Confirm expected method calls
    verify(mockedUI).displayErrorMessage("");
    verify(mockedUI).displayErrorMessage("Book not available");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    //Confirm no scanCount increment
    assertScanCountEquals(0);
  }
  
  
  
  public void testBookScannedBookAlreadyScanned() {
    // Standard set-up
    setUpMockBehaviour();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMockedMember(mockedMemberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan valid book
    testController.bookScanned(1);
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(mockedBookOne);
    assertLoanListContains(mockedLoanOne);
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("BookOne");
    verify(mockedUI).displayPendingLoan("LoanOne BookOne MemberOne");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan same book a second time
    testController.bookScanned(1);
    
    // Verify expected method calls (twice
    verify(mockedUI, times(2)).displayErrorMessage("");
    verify(mockedBookDAO, times(2)).getBookByID(1);
    verify(mockedBookOne, times(2)).getState();
    
    // Verify expected method call once
    verify(mockedUI).displayErrorMessage("Book already scanned");
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    //Confirm no scanCount increment after second scan
    assertScanCountEquals(1);
  }
  
  
  
  // ==========================================================================
  // Custom Asserts
  // ==========================================================================
  
  
  
  public void assertScanCountEquals(int scanCount) {
    try {
      // Using Reflection to directly set private field 'scanCount'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field scanCountField = borrowUC_CTLClass.getDeclaredField("scanCount");

      // Enable direct modification of private field
      if (!scanCountField.isAccessible()) {
        scanCountField.setAccessible(true);
      }

      int scanCountFieldValue = scanCountField.getInt(testController);
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
  
  
  
  public void assertLoanListContains(ILoan loan) {
    try {
      // Using Reflection to directly set private field 'scanCount'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field loanListField = borrowUC_CTLClass.getDeclaredField("loanList");

      // Enable direct modification of private field
      if (!loanListField.isAccessible()) {
        loanListField.setAccessible(true);
      }

      @SuppressWarnings("unchecked")
      List<ILoan> loanListValue = (List<ILoan>) loanListField.get(testController);
      assertTrue(loanListValue.contains(loan));

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
  
  
  
  public void assertBookListContains(IBook book) {
    try {
      // Using Reflection to directly set private field 'scanCount'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field bookListField = borrowUC_CTLClass.getDeclaredField("bookList");

      // Enable direct modification of private field
      if (!bookListField.isAccessible()) {
        bookListField.setAccessible(true);
      }

      @SuppressWarnings("unchecked")
      List<IBook> bookListValue = (List<IBook>) bookListField.get(testController);
      assertTrue(bookListValue.contains(book));

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
  
  
  
  private void setMockedMember(IMember mockedMember) {
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
  
  
  
  private void setBookList(List<IBook> bookList) {
    try {
      // Using Reflection to directly set private field 'borrower'

      Class<?> borrowUC_CTLClass = testController.getClass();
      Field bookListField = borrowUC_CTLClass.getDeclaredField("bookList");

      // Enable direct modification of private field
      if (!bookListField.isAccessible()) {
        bookListField.setAccessible(true);
      }
      
      // Set bookList
      bookListField.set(testController, bookList);
      
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
  
  
  
  private void setLoanList(ArrayList<ILoan> loanList) {
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
  
}
