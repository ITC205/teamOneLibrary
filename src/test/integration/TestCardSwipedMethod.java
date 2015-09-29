package test.integration;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import library.BorrowUC_CTL;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.entities.Loan;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.ELoanState;
import library.interfaces.entities.ILoan;
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
  private IBorrowUI mockBorrowUI = mock(IBorrowUI.class);

  IBookDAO bookDAO;
  ILoanDAO loanDAO;
  IMemberDAO memberDAO;
  BorrowUC_CTL testController;

  Throwable exception;



  protected void createMocks()
  {
    mockReader = mock(ICardReader.class);
    mockScanner = mock(IScanner.class);
    mockPrinter = mock(IPrinter.class);
    mockDisplay = mock(IDisplay.class);
    bookDAO = new BookDAO(new BookHelper());
    loanDAO = new LoanDAO(new LoanHelper());
    memberDAO = new MemberDAO(new MemberHelper());
  }

  
  
  // Add new members to memberDAO
  protected void addMembers()
  {
    memberDAO.addMember("fname1", "lastname1", "111111", "email1@email.com");
    memberDAO.addMember("fname2", "lastname2", "222222", "email2@email.com");
    memberDAO.addMember("fname3", "lastname3", "333333", "email3@email.com");
  }
  
  
  
  // Add new books to bookDAO
  protected void createBooks()
  {
    bookDAO.addBook("author1", "title1", "callno1");
    bookDAO.addBook("author2", "title2", "callno2");
    bookDAO.addBook("author3", "title3", "callno3");
    bookDAO.addBook("author4", "title4", "callno4");
    bookDAO.addBook("author5", "title5", "callno5");
    bookDAO.addBook("author6", "title6", "callno6");
  }
  
  
  
  // Create and add loans to loanDAO
  protected void addLoans()
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date dateBorrowed;
    try {
      dateBorrowed = dateFormat.parse("01/10/15");
      Date dateDue = dateFormat.parse("15/10/15");
      
      ILoan loan1 = new Loan(bookDAO.getBookByID(1), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan2 = new Loan(bookDAO.getBookByID(2), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan3 = new Loan(bookDAO.getBookByID(3), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan4 = new Loan(bookDAO.getBookByID(4), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(1));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(2));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(3));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(4));
      
      loanDAO.commitLoan(loan1);
      loanDAO.commitLoan(loan2);
      loanDAO.commitLoan(loan3);
      loanDAO.commitLoan(loan4);

      
    } catch (ParseException e) {
      e.printStackTrace();
    }

  }
  
  
  
  // Add too many loans to a member
  protected void addLoansOverLimit()
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    try {
      Date dateBorrowed = dateFormat.parse("01/10/15");
      Date dateDue = dateFormat.parse("15/10/15");
      
      ILoan loan1 = new Loan(bookDAO.getBookByID(1), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan2 = new Loan(bookDAO.getBookByID(2), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan3 = new Loan(bookDAO.getBookByID(3), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan4 = new Loan(bookDAO.getBookByID(4), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan5 = new Loan(bookDAO.getBookByID(5), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      ILoan loan6 = new Loan(bookDAO.getBookByID(6), memberDAO.getMemberByID(2), dateBorrowed, dateDue);
      
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(1));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(2));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(3));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(4));
      loanDAO.createLoan(memberDAO.getMemberByID(2), bookDAO.getBookByID(5));
      
      loanDAO.commitLoan(loan1);
      loanDAO.commitLoan(loan2);
      loanDAO.commitLoan(loan3);
      loanDAO.commitLoan(loan4);
      loanDAO.commitLoan(loan5);
      loanDAO.commitLoan(loan6);
      
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
  
  
  
  // Test that controller state must be EBorrowState.INITIALIzED
  public void testStateNotInitialized()
  {
    try
    {
      createMocks();
    setState(EBorrowState.CREATED);
    testController.cardSwiped(1);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof RuntimeException);
  }
  
  
  
  // Check that RuntimeException is thrown if MemberDAO is null
  public void testMemberDaoIsNull()
  {
    try
    {
      memberDAO = null;
      setState(EBorrowState.INITIALIZED);
      testController.cardSwiped(1);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof RuntimeException);
  }



  // Check that RuntimeException is thrown if member does not exist in list
  public void testMemberDoesNotExist()
  {
    try
    {
      createMocks();
      setState(EBorrowState.INITIALIZED);
      // MemberID 10 does not exist
      testController.cardSwiped(10);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof RuntimeException);   
  }

  
  
  // Check that an unrestricted member can scan books
  public void testAllowedMemberNoLoans()
  {
    createMocks();
    addMembers();
    createBooks();
    addLoans();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setMockBorrowUI(mockBorrowUI);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(1);
    // Verify that member details are displayed
    verify(mockBorrowUI).displayMemberDetails(1, "fname1 lastname1", "111111");
    // Check that controller state is set to SCANNING_BOOKS
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
  }
  
  
  
  // Check that an unrestricted member with current loans can scan books
  public void testAllowedMemberWithLoans()
  {
    createMocks();
    addMembers();
    createBooks();
    addLoans();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setMockBorrowUI(mockBorrowUI);
    setState(EBorrowState.INITIALIZED);
    // MemberID 2 has not reached the loan limit
    testController.cardSwiped(2);
    // Verify that member details are displayed
    verify(mockBorrowUI).displayMemberDetails(2, "fname2 lastname2", "222222");
    // Check that controller state is set to SCANNING_BOOKS
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
  }

  
  
  // Test that an unrestricted member with current fines can scan books
  public void testAllowedMemberWithFines()
  {
    createMocks();
    addMembers();
    // A fine of 2.0f is below the fine limit
    memberDAO.getMemberByID(1).addFine(2.0f);
    createBooks();
    addLoans();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setMockBorrowUI(mockBorrowUI);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(1);
    // Verify that current outstanding fines are displayed
    verify(mockBorrowUI).displayOutstandingFineMessage(2.0f);
    // Check that controller state is set to SCANNING_BOOKS
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
  }
  
  
  
  // Test that a restricted member with no loans cannot scan books
  public void testDisallowedMemberNoLoans()
  {
    createMocks();
    addMembers();
    // 50.0f is greater than the fine limit. Member should be BORROWING_RESTRICTED
    memberDAO.getMemberByID(3).addFine(50.0f);
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setMockBorrowUI(mockBorrowUI);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(3);
    // Verify that current outstanding fines are displayed
    verify(mockBorrowUI).displayOutstandingFineMessage(50.0f);
    // Check that controller state is set to BORROWING_RESTRICTED
    assertEquals(EBorrowState.BORROWING_RESTRICTED, getState());
  }
  
  
  
  // Test that a restricted member with loans cannot scan books
  public void testDisallowedMemberWithLoans()
  {
    createMocks();
    addMembers();
    createBooks();
    try
    {
    addLoansOverLimit();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(2);
    // Check that controller state is set to BORROWING_RESTRICTED
    assertEquals(EBorrowState.BORROWING_RESTRICTED, getState());
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    // An exception is thrown because members cannot borrow more than 5 books
    assertTrue(exception instanceof IllegalArgumentException);
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
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
  }
  
  
  
  private EBorrowState getState()
  {
    EBorrowState controllerState = null;
    try {
      Class<?> borrowUC_CTLClass = testController.getClass();
      Field state = borrowUC_CTLClass.getDeclaredField("state");
      state.setAccessible(true);
      controllerState = (EBorrowState) state.get(testController);
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
      Class<?> borrowUC_CTLClass = testController.getClass();
      Field ui;
      try 
      {
        ui = borrowUC_CTLClass.getDeclaredField("ui");
        ui.setAccessible(true);
        ui.set(testController, mockBorrowUI);
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
  
}
