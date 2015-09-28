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
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
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
  
  
  
  protected void addMembers()
  {
    memberDAO.addMember("fname1", "lastname1", "111111", "email1@email.com");
    memberDAO.addMember("fname2", "lastname2", "222222", "email2@email.com");
    memberDAO.addMember("fname3", "lastname3", "333333", "email3@email.com");
  }
  
  
  
  protected void createBooks()
  {
    bookDAO.addBook("author1", "title1", "callno1");
    bookDAO.addBook("author2", "title2", "callno2");
    bookDAO.addBook("author3", "title3", "callno3");
    bookDAO.addBook("author4", "title4", "callno4");
    bookDAO.addBook("author5", "title5", "callno5");
    bookDAO.addBook("author6", "title6", "callno6");
  }
  
  
  
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  
  
  
  protected void addLoansOverLimit()
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }



  }
  
  
  
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
  
  
  
  public void testMemberDaoIsNull()
  {
    try
    {
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(1);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof RuntimeException);
  }
  
  
  
  public void testMemberDoesNotExist()
  {
    try
    {
    createMocks();
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(10);
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof RuntimeException);   
  }
  
  
  
  public void testAllowedMemberNoLoans()
  {
    createMocks();
    addMembers();
    createBooks();
    addLoans();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(1);
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
  }
  
  
  
  public void testAllowedMemberWithLoans()
  {
    createMocks();
    addMembers();
    createBooks();
    addLoans();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(2);
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
  }

  
  
  public void testAllowedMemberWithFines()
  {
    createMocks();
    addMembers();
    memberDAO.getMemberByID(1).addFine(2.0f);
    createBooks();
    addLoans();
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(1);
    assertEquals(EBorrowState.SCANNING_BOOKS, getState());
  }
  
  
  
  public void testDisallowedMemberNoLoans()
  {
    createMocks();
    addMembers();
    memberDAO.getMemberByID(3).addFine(50.0f);
    testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
        mockDisplay, bookDAO, loanDAO, memberDAO);
    setState(EBorrowState.INITIALIZED);
    testController.cardSwiped(3);
    assertEquals(EBorrowState.BORROWING_RESTRICTED, getState());
  }
  
  
  
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
    assertEquals(EBorrowState.BORROWING_RESTRICTED, getState());
    }
    catch (Throwable ex)
    {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    
  }
  
  
  
  public void testDisallowedMemberWithOverDueLoans()
  {
    
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return controllerState;
  }
}
