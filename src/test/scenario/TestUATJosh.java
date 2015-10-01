package test.scenario;

import library.BorrowUC_CTL;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.daos.LoanDAO;
import library.daos.LoanHelper;
import library.daos.MemberDAO;
import library.daos.MemberHelper;
import library.hardware.CardReader;
import library.hardware.Display;
import library.hardware.Printer;
import library.hardware.Scanner;
import library.interfaces.IMainListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.panels.MainPanel;

/**
 * TestUATJosh class
 * 
 * This class is provided as an alternative for executing the 'Main' class and is 
 * specifically for users performing UAT testing. This class simulates the 'Main'
 * class and uses all real object and UIs. The only point of difference is that 
 * this class will ensure users carrying out UAT test scripts have the correct 
 * test data available to them. 
 * 
 * Most of 'Main' has been reproduced here; the reason that 'Main' wasn't used 
 * directly was due to the setupTestData() call in the 'Main' constructor that 
 * would create test data not relevant to my UATS. 
 * 
 * Hence, the only difference between this class and 'Main' is the implementation 
 * of the setupTestData() method. It is convenient to keep the original 'Main' 
 * class as it is, so that my UAT testing does not interfere with other members 
 * of the group.  
 * 
 * @author Josh Kent
 */
public class TestUATJosh
  implements IMainListener {

  //===========================================================================
  // Variables - modified
  //===========================================================================

  private CardReader reader;
  private Scanner scanner;
  private Printer printer;
  private Display display;

  private IBookHelper bookHelper;
  private ILoanHelper loanHelper;
  private IMemberHelper memberHelper;

  private IBookDAO bookDAO;
  private ILoanDAO loanDAO;
  private IMemberDAO memberDAO;

  //===========================================================================
  // Variables - modified
  //===========================================================================

  public TestUATJosh() {
    reader = new CardReader();
    scanner = new Scanner();
    printer = new Printer();
    display = new Display();

    bookHelper = new BookHelper();
    loanHelper = new LoanHelper();
    memberHelper = new MemberHelper();

    bookDAO = new BookDAO(bookHelper);
    loanDAO = new LoanDAO(loanHelper);
    memberDAO = new MemberDAO(memberHelper);

    setupTestData();
  }


  public void showGUI() {   
    reader.setVisible(true);
    scanner.setVisible(true);
    printer.setVisible(true);
    display.setVisible(true);
  }

  //===========================================================================
  // borrowBooks() - modified
  //===========================================================================

  @Override
  public void borrowBooks() {
    BorrowUC_CTL ctl = new BorrowUC_CTL(reader, scanner, printer, display,
                                        bookDAO, loanDAO, memberDAO);
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        ctl.initialise();
      }
    });
  }


  
  private void setupTestData() {
    // Add books ==============================================================
    IBook disposedBook = bookDAO.addBook("Harper Lee", "To Kill A Mockingbird", "LEE 123");
    bookDAO.addBook("Harper Lee", "Go Set a Watchman", "LEE 445");
    bookDAO.addBook("Dan Brown", "Digital Fortress", "BRO 410");
    bookDAO.addBook("Tom Grace", "Quantum", "GRA 888");
    
    IBook loanBookOne = bookDAO.addBook("Dean Crawford", "Immortal", "CRA 111");
    IBook loanBookTwo = bookDAO.addBook("Stephen King", "It", "KIN 109");
    
    // Add members ============================================================
    IMember memberOne = memberDAO.addMember("Josh", "Kent", "62626262", "josh@kent.com");
    memberDAO.addMember("John", "Kent", "63636363", "john@kent.com");
    memberDAO.addMember("Jason", "Kent", "64646464", "jason@kent.com");
    
    // Setup Member Conditions =================================================
    // Two loans added to memberOne
    ILoan loanOne = loanDAO.createLoan(memberOne, loanBookOne);
    loanDAO.commitLoan(loanOne);
    ILoan loanTwo = loanDAO.createLoan(memberOne, loanBookTwo);
    loanDAO.commitLoan(loanTwo);
  
    // Add $3.00 fine to memberOne
    memberOne.addFine(3.0F);
    
    // Setup Book Conditions ==================================================
    disposedBook.dispose();
    
  }

  
  public static void main(String[] args) {
    
        // start the GUI
    TestUATJosh main = new TestUATJosh();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              main.display.setDisplay(new MainPanel(main), "Main Menu");
                main.showGUI();
            }
        });
  }
}
