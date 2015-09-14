package library.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

/**
 * BookMapDAO class
 * @author Josh Kent
 *
 */
public class BookMapDAO implements IBookDAO
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
  
  
  
  public BookMapDAO(IBookHelper helper) {
    if(helper == null) {
      throw new IllegalArgumentException("BookMapDAO: constructor: value for "
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
  public IBook addBook(String author, String title, String callNo)
  {
    IBook newBook = helper_.makeBook(author, title, callNo, getNextId());
    bookMap_.put(newBook.getID(), newBook);
    updateNextId();
    return newBook;
  }

  

  // ==========================================================================
  // Retrieval Methods
  // ==========================================================================
  
  
  
  @Override
  public IBook getBookByID(int id)
  {
    // get method of HashMap returns the value associated with the given key
    // or null if there is no value associated with the given key (by default)
    return bookMap_.get(id);
  }



  @Override
  public List<IBook> listBooks()
  {
    return new ArrayList<IBook>(bookMap_.values());
  }



  @Override
  public List<IBook> findBooksByAuthor(String author)
  {
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
  
}
