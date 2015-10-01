package test.helper;

import javax.swing.JPanel;

import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;

/**
 * IBorrowUIStub class
 * 
 * An empty stub class that implements IBorrowUI AND extends JPanel
 * Necessary for Scenario Testing because mock IBorrowUI objects CANNOT BE CAST 
 * to JPanel when this occurs with the 
 * IDisplay.setDisplay(JPanel panel, String id) method which is called during 
 * BorrowUC_CTL.initialise() @ BorrowUC_CTL line 93
 * 
 * @author Josh Kent
 */
public class IBorrowUIStub extends JPanel implements IBorrowUI
{

  /**
   * 
   */
  private static final long serialVersionUID = -4843275284273230849L;



  @Override
  public void setState(EBorrowState state)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayMemberDetails(int memberID, String memberName,
          String memberPhone)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayExistingLoan(String loanDetails)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayOverDueMessage()
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayAtLoanLimitMessage()
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayOutstandingFineMessage(float amountOwing)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayOverFineLimitMessage(float amountOwing)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayScannedBookDetails(String bookDetails)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayPendingLoan(String loanDetails)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayConfirmingLoan(String loanDetails)
  {
    // TODO Auto-generated method stub

  }



  @Override
  public void displayErrorMessage(String errorMesg)
  {
    // TODO Auto-generated method stub

  }

}
