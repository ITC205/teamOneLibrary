package test.integration;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import library.BorrowUC_CTL;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.interfaces.EBorrowState;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
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
  
  testController = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
      mockDisplay, bookDAO, loanDAO, memberDAO);
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
    loanDAO.createLoan(memberDAO.getMemberByID(0), bookDAO.getBookByID(0));
    loanDAO.createLoan(memberDAO.getMemberByID(0), bookDAO.getBookByID(1));
    loanDAO.createLoan(memberDAO.getMemberByID(0), bookDAO.getBookByID(2));
    loanDAO.createLoan(memberDAO.getMemberByID(0), bookDAO.getBookByID(3));
    loanDAO.createLoan(memberDAO.getMemberByID(0), bookDAO.getBookByID(4));
    loanDAO.createLoan(memberDAO.getMemberByID(0), bookDAO.getBookByID(5));
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
    
  }
  
  
  
  public void testAllowedMemberNoLoans()
  {
    
  }
  
  
  
  public void testAllowedMemberWithLoans()
  {
    
  }
  
  
  
  public void testAllowedMemberNoFines()
  {
    
  }
  
  
  
  public void testAllowedMemberWithFines()
  {
    
  }
  
  
  
  public void testDisallowedMemberNoLoans()
  {
    
  }
  
  
  
  public void testDisallowedMemberWithLoans()
  {
    
  }
  
  
  
  public void testDisallowedMemberNoFines()
  {
    
  }
  
  
  
  public void testDisallowedMemberWithFines()
  {
    
  }
  
  
  
  public void testDisallowedMemberNoOverdueLoans()
  {
    
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
}
