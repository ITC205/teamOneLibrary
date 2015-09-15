package library.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;

/**
 * Book class
 * @author Josh Kent
 *
 */
public class Book implements IBook
{
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private String author_;
  private String title_;
  private String callNumber_;
  
  private int id_;
  
  private EBookState state_ = EBookState.AVAILABLE; 
  
  private ILoan loan_ = null;

  
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public Book(String author, String title, String callNumber, int bookID)
  {    
    if(isStringNullOrEmpty(author)) {
      throw new IllegalArgumentException("Book: constructor: value for 'author' "
                                         + "cannot be null or empty");
    }
    
    if(isStringNullOrEmpty(title)) {
      throw new IllegalArgumentException("Book: constructor: value for 'title' "
                                         + "cannot be null or empty");
    }
    
    if(isStringNullOrEmpty(callNumber)) {
      throw new IllegalArgumentException("Book: constructor: value for "
                                         + "'callNumber' cannot be null or "
                                         + "empty");
    }
    
    if(!isBookIdValid(bookID)) {
      throw new IllegalArgumentException("Book: constructor: value for 'bookID'"
                                         + " must be a positive integer"
                                         + " (>= 0)");
    }
    
    author_ = author;
    title_ = title;
    callNumber_ = callNumber;
    id_ = bookID;
  }
  

  
  // ==========================================================================
  // State Changing Methods
  // ==========================================================================
  
  
  
  @Override
  public void borrow(ILoan loan)
  {
    if(getState() != EBookState.AVAILABLE) {
      throw new RuntimeException("Book: borrow: invalid state transition ["
                                 + getState() 
                                 + " > " 
                                 + EBookState.ON_LOAN + "]");
    }
    loan_ = loan;
    setState(EBookState.ON_LOAN);
  }


  
  @Override
  public void returnBook(boolean damaged)
  {
    boolean isOnLoan = getState() == EBookState.ON_LOAN;
    boolean isLost = getState() == EBookState.LOST;

    if((!isOnLoan) && (!isLost)) {
      throw new RuntimeException("Book: returnBook: invalid state transition ["
                                 + getState() 
                                 + " > " 
                                 + EBookState.AVAILABLE + "/" 
                                 + EBookState.DAMAGED + "]");
    }
    loan_ = null;
    if(damaged) {
      setState(EBookState.DAMAGED);
    }
    else {
      setState(EBookState.AVAILABLE);
    }
  }



  @Override
  public void lose()
  {
    if(getState() != EBookState.ON_LOAN) {
      throw new RuntimeException("Book: lose: invalid state transition ["
                                 + getState() 
                                 + " > " 
                                 + EBookState.LOST + "]");
    }
    setState(EBookState.LOST);
  }



  @Override
  public void repair()
  {
    if(getState() != EBookState.DAMAGED) {
      throw new RuntimeException("Book: repair: invalid state transition ["
                                 + getState() 
                                 + " > " 
                                 + EBookState.AVAILABLE + "]");
    }
    setState(EBookState.AVAILABLE);
  }



  @Override
  public void dispose()
  {
    boolean isAvailable = getState() == EBookState.AVAILABLE;
    boolean isDamaged = getState() == EBookState.DAMAGED;
    boolean isLost = getState() == EBookState.LOST;
    
    if((!isAvailable) && (!isDamaged) && (!isLost)) {
      throw new RuntimeException("Book: dispose: invalid state transition ["
                                 + getState() 
                                 + " > " 
                                 + EBookState.DISPOSED + "]");
    }
    setState(EBookState.DISPOSED);
  }



  // ==========================================================================
  // Getter Methods
  // ==========================================================================



  @Override
  public ILoan getLoan()
  {
    if(getState() != EBookState.ON_LOAN) {
      return null;
    }
    else {
      return loan_;
    }
  }
  
  
  
  @Override
  public EBookState getState()
  {
    return state_;
  }



  @Override
  public String getAuthor()
  {
    return author_;
  }



  @Override
  public String getTitle()
  {
    return title_;
  }



  @Override
  public String getCallNumber()
  {
    return callNumber_;
  }



  @Override
  public int getID()
  {
    return id_;
  }
  
  
  
  // ==========================================================================
  // Setter Methods
  // ==========================================================================
  
  
  
  private void setState(EBookState newState)
  {
    state_ = newState;
  }
  
  
  
  // ==========================================================================
  // Validation Methods
  // ==========================================================================
  
  
  
  private boolean isStringNullOrEmpty(String input) 
  {
    // Check null first to avoid NullPointException 
    if(input == null) {
      return true;
    }
    if(input.isEmpty()) {
      return true;
    }

    return false;
  }
  
  
  
  private boolean isBookIdValid(int id) 
  {
    return id > 0;
  }
  
  
  
  // ==========================================================================
  // Printing Methods
  // ==========================================================================
  
  
  
  @Override
  public String toString() {
    StringBuffer bookString = new StringBuffer();
    
    bookString.append("ID: " + getID() + "\n");
    bookString.append("Author: " + getAuthor() + "\n");
    bookString.append("Title: " + getTitle() + "\n");
    bookString.append("Call Number: " + getCallNumber());
    
    return bookString.toString();
  }
  
}
