package library.daos;

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
  }
  
  
  
  // ==========================================================================
  // Insertion Methods
  // ==========================================================================

  
  
  @Override
  public IBook addBook(String author, String title, String callNo)
  {
    // TODO Auto-generated method stub
    return null;
  }

  

  // ==========================================================================
  // Retrieval Methods
  // ==========================================================================
  
  
  
  @Override
  public IBook getBookByID(int id)
  {
    // TODO Auto-generated method stub
    return null;
  }



  @Override
  public List<IBook> listBooks()
  {
    // TODO Auto-generated method stub
    return null;
  }



  @Override
  public List<IBook> findBooksByAuthor(String author)
  {
    // TODO Auto-generated method stub
    return null;
  }



  @Override
  public List<IBook> findBooksByTitle(String title)
  {
    // TODO Auto-generated method stub
    return null;
  }



  @Override
  public List<IBook> findBooksByAuthorTitle(String author, String title)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
