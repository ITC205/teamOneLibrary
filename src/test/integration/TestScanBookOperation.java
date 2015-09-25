package test.integration;

import junit.framework.TestCase;
import library.BorrowUC_CTL;
import library.daos.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * TestScanBookOperation class
 * 
 * Test class that isolates the Scan Book system operation
 * Code: BBUC_Op3
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
  private IBorrowUI mockedUI = mock(IBorrowUI.class);
  
  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  private IMemberDAO memberDAO;
  
  private IBook bookOne;  // AVAILABLE
  private IBook bookTwo;  // AVAILABLE
  private IBook bookThree;// AVAILABLE
  private IBook bookFour; // AVAILABLE
  private IBook bookFive; // AVAILABLE
  private IBook bookSix;  // ON_LOAN
  
  private IMember memberOne; // Unrestricted, no loans
  
  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
  private String borrowDateString;
  private String dueDateString;
  
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
  
  
  
  private void initialiseVariables() {
    
    bookDAO = new BookDAO(new BookHelper());
    memberDAO = new MemberDAO(new MemberHelper());
    loanDAO = new LoanDAO(new LoanHelper());
    
    
    // Fresh books default to AVAILABLE
    bookOne = bookDAO.addBook("authorOne", "titleOne", "callOne");
    bookTwo = bookDAO.addBook("authorTwo", "titleTwo", "callTwo");
    bookThree = bookDAO.addBook("authorThree", "titleThree", "callThree");
    bookFour = bookDAO.addBook("authorFour", "titleFour", "callFour");
    bookFive = bookDAO.addBook("authorFive", "titleFive", "callFive");
    bookSix = bookDAO.addBook("authorSix", "titleSix", "callSix");
    
    
    // bookSix is ON_LOAN
    bookSix.borrow(mock(ILoan.class));
    assertEquals(EBookState.ON_LOAN, bookSix.getState());
    
    memberOne = memberDAO.addMember("fNameOne", "lNameOne", "0263636363", 
                           "person@address.com");
   
    // Establish expected borrow and due dates for any loans created by tests
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(new Date());
    borrowDateString = dateFormat.format(calendar.getTime());
    calendar.add(Calendar.DATE, ILoan.LOAN_PERIOD);
    dueDateString = dateFormat.format(calendar.getTime());
    
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
    bookFour = null;
    bookFive = null;
    bookSix = null;
    
    memberOne = null;
    
    borrowDateString = null;
    dueDateString = null;
  }

  
  
  // ==========================================================================
  // Tests
  // ==========================================================================
  
  
  
  public void testBookScannedSingleBookDefault() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
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
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    List<ILoan> loanList = getLoanList();
    assertEquals(1, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 1\n"
                                              + "Author: authorOne\n"
                                              + "Title: titleOne\n"
                                              + "Call Number: callOne");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
                                       + "Author:   authorOne\n"
                                       + "Title:    titleOne\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString);
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
  }
  
  
  public void testBookScannedTwoBooksDefault() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan first book
    testController.bookScanned(1);
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    List<ILoan> loanList = getLoanList();
    assertEquals(1, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 1\n"
                                              + "Author: authorOne\n"
                                              + "Title: titleOne\n"
                                              + "Call Number: callOne");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
                                       + "Author:   authorOne\n"
                                       + "Title:    titleOne\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString);
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan second book
    testController.bookScanned(2);
    
    // Should call this displayErrorMessage exactly the same way twice (once for
    // each book
    verify(mockedUI, times(2)).displayErrorMessage("");

    // Confirm scan count increment
    assertScanCountEquals(2);
    
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    assertBookListContains(bookTwo);
    loanList = getLoanList();
    assertEquals(2, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 2\n"
                                              + "Author: authorTwo\n"
                                              + "Title: titleTwo\n"
                                              + "Call Number: callTwo");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
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
                                       + "Due Date: " + dueDateString);
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
  }
  
  public void testBookScannedFiveBooks() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
    // Set lists to empty
    setBookList(new ArrayList<IBook>());
    setLoanList(new ArrayList<ILoan>());
    
    // Confirm scan count is zero
    assertScanCountEquals(0);
    // Confirm correct state
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan first book
    testController.bookScanned(1);
    
    // Confirm scan count increment
    assertScanCountEquals(1);
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    List<ILoan> loanList = getLoanList();
    assertEquals(1, loanList.size());
    
    // Confirm display calls with expected values
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 1\n"
                                              + "Author: authorOne\n"
                                              + "Title: titleOne\n"
                                              + "Call Number: callOne");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
                                       + "Author:   authorOne\n"
                                       + "Title:    titleOne\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString);
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan second book =======================================================
    testController.bookScanned(2);
    
    // Confirm scan count increment
    assertScanCountEquals(2);
    
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    assertBookListContains(bookTwo);
    
    loanList = getLoanList();
    assertEquals(2, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 2\n"
                                              + "Author: authorTwo\n"
                                              + "Title: titleTwo\n"
                                              + "Call Number: callTwo");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
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
                                       + "Due Date: " + dueDateString);
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan third book ========================================================
    testController.bookScanned(3);
    
    // Confirm scan count increment
    assertScanCountEquals(3);
    
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    assertBookListContains(bookTwo);
    assertBookListContains(bookThree);
    
    loanList = getLoanList();
    assertEquals(3, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 3\n"
                                              + "Author: authorThree\n"
                                              + "Title: titleThree\n"
                                              + "Call Number: callThree");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
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
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan fourth book ========================================================
    testController.bookScanned(4);
    
    // Confirm scan count increment
    assertScanCountEquals(4);
    
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    assertBookListContains(bookTwo);
    assertBookListContains(bookThree);
    assertBookListContains(bookFour);
    
    loanList = getLoanList();
    assertEquals(4, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 4\n"
                                              + "Author: authorFour\n"
                                              + "Title: titleFour\n"
                                              + "Call Number: callFour");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
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
                                       + "Due Date: " + dueDateString
                                       + "\n\n"
                                       + "Loan ID:  0\n"
                                       + "Author:   authorFour\n"
                                       + "Title:    titleFour\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString);
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan fifth book ========================================================
    testController.bookScanned(5);
    
    // Should call this displayErrorMessage exactly the same way (once for
    // each book
    verify(mockedUI, times(5)).displayErrorMessage("");
    
    // Confirm scan count increment
    assertScanCountEquals(5);
    
    // Confirm lists contain expected values
    assertBookListContains(bookOne);
    assertBookListContains(bookTwo);
    assertBookListContains(bookThree);
    assertBookListContains(bookFour);
    assertBookListContains(bookFive);
    
    loanList = getLoanList();
    assertEquals(5, loanList.size());
    
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 5\n"
                                              + "Author: authorFive\n"
                                              + "Title: titleFive\n"
                                              + "Call Number: callFive");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
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
                                       + "Due Date: " + dueDateString
                                       + "\n\n"
                                       + "Loan ID:  0\n"
                                       + "Author:   authorFour\n"
                                       + "Title:    titleFour\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString
                                       + "\n\n"
                                       + "Loan ID:  0\n"
                                       + "Author:   authorFive\n"
                                       + "Title:    titleFive\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString);
    
    
    // Scan count five should trigger state change 
    // Confirm state change
    assertControllerStateEquals(EBorrowState.CONFIRMING_LOANS);
    
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
                                          + "Due Date: " + dueDateString
                                          + "\n\n"
                                          + "Loan ID:  0\n"
                                          + "Author:   authorFour\n"
                                          + "Title:    titleFour\n"
                                          + "Borrower: fNameOne lNameOne\n"
                                          + "Borrowed: " + borrowDateString +"\n"
                                          + "Due Date: " + dueDateString
                                          + "\n\n"
                                          + "Loan ID:  0\n"
                                          + "Author:   authorFive\n"
                                          + "Title:    titleFive\n"
                                          + "Borrower: fNameOne lNameOne\n"
                                          + "Borrowed: " + borrowDateString +"\n"
                                          + "Due Date: " + dueDateString);
    
    // Verify disabling of hardware
    verify(mockedCardReader).setEnabled(false);
    verify(mockedScanner).setEnabled(false);
  }
  
  
  
  public void testBookScannedFromInvalidState() {
    // Standard set-up
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
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
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
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
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
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
    initialiseVariables();
    setUpPreconditions();
    // Set mockedMemberOne (member has no loans or restrictions)
    setMember(memberOne);
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
    assertBookListContains(bookOne);
    List<ILoan> loanList = getLoanList();
    assertEquals(1, loanList.size());
    
    // Confirm display calls with expected values
    // Confirm display calls with expected values
    verify(mockedUI).displayScannedBookDetails("ID: 1\n"
                                              + "Author: authorOne\n"
                                              + "Title: titleOne\n"
                                              + "Call Number: callOne");
    
    verify(mockedUI).displayPendingLoan("Loan ID:  0\n"
                                       + "Author:   authorOne\n"
                                       + "Title:    titleOne\n"
                                       + "Borrower: fNameOne lNameOne\n"
                                       + "Borrowed: " + borrowDateString +"\n"
                                       + "Due Date: " + dueDateString);
    
    // Confirm no state change
    assertControllerStateEquals(EBorrowState.SCANNING_BOOKS);
    
    // Scan same book a second time
    testController.bookScanned(1);
    
    // Verify expected method call (twice)
    verify(mockedUI, times(2)).displayErrorMessage("");
    
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
  
  
  
  public void assertBookListContains(IBook book) {
    try {
      // Using Reflection to directly access bookList

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
