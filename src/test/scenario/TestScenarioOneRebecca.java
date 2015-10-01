package test.scenario;

import static org.mockito.Mockito.mock;
import junit.framework.TestCase;
import library.BorrowUC_CTL;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.entities.Member;
import library.interfaces.IBorrowUI;
import library.interfaces.EBorrowState;
import library.interfaces.entities.IMember;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import test.helper.IBorrowUIStub;
import static org.mockito.Mockito.*;

//Scenario 1:
//Member:
//  Has no loans
//  Has no fines
//Scans 5 books
//Clicks Completed
//Clicks Reject
//Scans 2 books
//Clicks Completed
//Clicks Confirm


public class TestScenarioOneRebecca 
{
  
  private ICardReader mockReader;
  private IScanner mockScanner;
  private IPrinter mockPrinter;
  private IDisplay mockDisplay;
  private IMemberDAO memberDAO;
  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  private IMemberHelper memberHelper;
  private IMember member;
  
  private IBorrowUI ui;
  private BorrowUC_CTL ctl;


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
  
  
  
  protected void setupObjects()
  {
    memberDAO.addMember("Bill", "Jones", "123456", "bjones@mail.com");
    ctl = new BorrowUC_CTL(mockReader, mockScanner, mockPrinter,
                      mockDisplay, bookDAO, loanDAO,
                      memberDAO);
  }
}
