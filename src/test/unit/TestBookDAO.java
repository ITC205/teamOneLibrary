package test.unit;

import junit.framework.TestCase;
import library.daos.BookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import static org.mockito.Mockito.*;

/**
 * TestBookDAO class
 * @author Josh Kent
 *
 */
public class TestBookDAO extends TestCase
{
  private IBookHelper mockedHelper;
  private IBook mockedBook;
  
  protected void setUp() 
  {
    mockedHelper = mock(IBookHelper.class);
    mockedBook = mock(IBook.class);
    
    when(mockedHelper.makeBook("Charles Dickens", "Great Expectations", 
                               "82.023 275 [2011]", 1))
                     .thenReturn(mockedBook);
  }
  
  protected void tearDown()
  {
    mockedHelper = null;
  }
  
  // ==========================================================================
  // Constructor Testing
  // ==========================================================================
  
  public void testConstructorDefault()
  {
    new BookDAO(mockedHelper);
    
  }
  
  public void testConstructorHelperNull() 
  {
    try {
      new BookDAO(null);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  // ==========================================================================
  // Testing addBook(String author, String title, String callNumber) method
  // ==========================================================================
  
  public void testAddBookDefault() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    IBook returnedBook = testBookDAO.addBook("Charles Dickens", 
                                             "Great Expectations", 
                                             "82.023 275 [2011]");
    
    assertEquals(mockedBook, returnedBook);
  }
  
  public void testAddBookNullAuthor() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.addBook(null, "Great Expectations", "82.023 275 [2011]");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testAddBookEmptyAuthor() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.addBook("", "Great Expectations", "82.023 275 [2011]");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testAddBookNullTitle() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.addBook("Charles Dickens", null, "82.023 275 [2011]");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testAddBookEmptyTitle() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.addBook("Charles Dickens", "", "82.023 275 [2011]");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testAddBookNullCallNumber() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.addBook("Charles Dickens", "Great Expectations", null);
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testAddBookEmptyCallNumber()
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.addBook("Charles Dickens", "Great Expectations", "");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
}
