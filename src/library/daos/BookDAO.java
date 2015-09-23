package library.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

/**
 * BookDAO class
 * @author Josh Kent
 *
 */
public class BookDAO implements IBookDAO
{
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private int nextId_;
  private Map<Integer, IBook> bookMap_;
  private IBookHelper helper_;
  
  
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public BookDAO(IBookHelper helper) {
    if(helper == null) {
      throw new IllegalArgumentException("BookDAO: constructor: value for "
                                         + "'helper' cannot be null");
    }
    helper_ = helper;
    nextId_ = 1;
    bookMap_ = new HashMap<Integer, IBook>();
  }
  
  
  
  // ==========================================================================
  // Insertion Methods
  // ==========================================================================

  
  
  @Override
  public IBook addBook(String author, String title, String callNumber)
  {
    if(isStringNullOrEmpty(author)) {
      throw new IllegalArgumentException("BookDAO: addBook: value for 'author' "
                                         + "cannot be null or empty");
    }
    
    if(isStringNullOrEmpty(title)) {
      throw new IllegalArgumentException("BookDAO: addBook: value for 'title' "
                                         + "cannot be null or empty");
    }
    
    if(isStringNullOrEmpty(callNumber)) {
      throw new IllegalArgumentException("BookDAO: addBook: value for "
                                         + "'callNumber' cannot be null or "
                                         + "empty");
    }
    
    IBook newBook = helper_.makeBook(author, title, callNumber, getNextId());
    bookMap_.put(newBook.getID(), newBook);
    updateNextId();
    return newBook;
  }

  

  // ==========================================================================
  // Retrieval Methods
  // ==========================================================================
  
  
  
  @Override
  public IBook getBookByID(int bookID)
  {
    if(!isBookIdValid(bookID)) {
      throw new IllegalArgumentException("BookDAO: getBookByID: value for "
                                         + "'bookID' must be a positive integer"
                                         + " (>= 0)");
    }
    // get method of HashMap returns the value associated with the given key
    // or null if there is no value associated with the given key (by default)
    return bookMap_.get(bookID);
  }



  @Override
  public List<IBook> listBooks()
  {
    return new ArrayList<IBook>(bookMap_.values());
  }



  @Override
  public List<IBook> findBooksByAuthor(String author)
  {
    if(isStringNullOrEmpty(author)) {
      throw new IllegalArgumentException("BookDAO: findBooksByAuthor: value "
                                         + "for 'author' cannot be null or "
                                         + "empty");
    }
    
    ArrayList<IBook> booksByAuthor = new ArrayList<>();
    
    for(IBook book: bookMap_.values()) {
      if(book.getAuthor().equals(author)) {
        booksByAuthor.add(book);
      }
    }
    return booksByAuthor;
  }



  @Override
  public List<IBook> findBooksByTitle(String title)
  {
    if(isStringNullOrEmpty(title)) {
      throw new IllegalArgumentException("BookDAO: findBooksByTitle: value for "
                                         + "'title' cannot be null or empty");
    }
    
    ArrayList<IBook> booksByTitle = new ArrayList<>();
    
    for(IBook book: bookMap_.values()) {
      if(book.getTitle().equals(title)) {
        booksByTitle.add(book);
      }
    }
    return booksByTitle;
  }



  @Override
  public List<IBook> findBooksByAuthorTitle(String author, String title)
  {
    if(isStringNullOrEmpty(author)) {
      throw new IllegalArgumentException("BookDAO: findBooksByAuthorTitle: "
                                         + "value for 'author' cannot be null "
                                         + "or empty");
    }
    
    if(isStringNullOrEmpty(title)) {
      throw new IllegalArgumentException("BookDAO: findBooksByAuthorTitle: "
                                         + "value for 'title' cannot be null "
                                         + "or empty");
    }
    
    ArrayList<IBook> booksByTitleAndAuthor = new ArrayList<>();
    
    for(IBook book: bookMap_.values()) {
      if(book.getAuthor().equals(author) && book.getTitle().equals(title)) {
        booksByTitleAndAuthor.add(book);
      }
    }
    return booksByTitleAndAuthor;
  }
  
  
  
  // ==========================================================================
  // Helper Methods
  // ==========================================================================
  
  
  
  private int getNextId()
  {
    return nextId_;
  }
  
  
  
  private void updateNextId() 
  {
    nextId_++;
  }
  
  
  
  // ==========================================================================
  // Validation Methods
  // ==========================================================================
  
  
  
  private boolean isStringNullOrEmpty(String input) 
  {
    // Check null first to avoid NullPointerException 
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
}
