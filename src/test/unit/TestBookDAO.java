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
  private IBook mockedBookTwo;
  
  protected void setUp() 
  {
    mockedHelper = mock(IBookHelper.class);
    mockedBook = mock(IBook.class);
    when(mockedBook.getID()).thenReturn(1);
    when(mockedBook.getAuthor()).thenReturn("Charles Dickens");
    when(mockedBook.getTitle()).thenReturn("Great Expectations");
    when(mockedBook.getCallNumber()).thenReturn("82.023 275 [2011]");
    
    mockedBookTwo = mock(IBook.class);
    when(mockedBookTwo.getID()).thenReturn(2);
    when(mockedBookTwo.getAuthor()).thenReturn("Harper Lee");
    when(mockedBookTwo.getTitle()).thenReturn("To Kill a Mockingbird");
    when(mockedBookTwo.getCallNumber()).thenReturn("813.54 TOKI");
    
    when(mockedHelper.makeBook("Charles Dickens", "Great Expectations", 
                               "82.023 275 [2011]", 1))
                     .thenReturn(mockedBook);
    
    when(mockedHelper.makeBook("Harper Lee", "To Kill a Mockingbird", 
                               "813.54 TOKI", 2))
                     .thenReturn(mockedBookTwo);
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
  
  // ==========================================================================
  // Testing getBookByID(int bookID) method
  // ==========================================================================
  
  public void testGetBookDefault() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
                        "82.023 275 [2011]");
    
    IBook returnedBook = testBookDAO.getBookByID(1);
    
    assertEquals(mockedBook, returnedBook);
    
    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");
    
    returnedBook = testBookDAO.getBookByID(2);
    
    assertEquals(mockedBookTwo, returnedBook);    
  }
  
  public void testGetBookByIDNegativeId() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    try {
      @SuppressWarnings("unused")
      IBook returnedBook = testBookDAO.getBookByID(-1);    
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testGetBookByIDZeroId()
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    try {
      @SuppressWarnings("unused")
      IBook returnedBook = testBookDAO.getBookByID(0);    
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testGetBookByIDNonExistentId()
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    // Try to get ID 1 with no books added
    IBook returnedBook = testBookDAO.getBookByID(1);
    
    assertNull(returnedBook);
    
    // Try to get ID 15 with no books added
    returnedBook = testBookDAO.getBookByID(15);
    
    assertNull(returnedBook);
  }
  
  // ==========================================================================
  // Testing listBooks() method
  // ==========================================================================
  
  
  
  
  
  
  
  
  // ==========================================================================
  // Testing findBooksByAuthor(String author) method
  // ==========================================================================
  
  
  
  
  
  
  
  // ==========================================================================
  // Testing findBooksByTitle() method
  // ==========================================================================
  
  
  
  
  
  
  // ==========================================================================
  // Testing findBooksByAuthorTitle(String author, String title) method
  // ==========================================================================
}
