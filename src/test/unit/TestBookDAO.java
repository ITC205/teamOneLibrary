package test.unit;

import junit.framework.TestCase;
import library.daos.BookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import static org.mockito.Mockito.*;

import java.util.List;

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
    
    // Fixed ID value means that 'Great Expectations' should always be used as
    // the first mockedBook
    when(mockedHelper.makeBook("Charles Dickens", "Great Expectations", 
                               "82.023 275 [2011]", 1))
                     .thenReturn(mockedBook);
    
    // 'To Kill a Mockingbird' should be used as the second book
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
    
    assertEquals(1, returnedBook.getID());
    
    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");
    
    returnedBook = testBookDAO.getBookByID(2);
    
    assertEquals(mockedBookTwo, returnedBook);    
    
    assertEquals(2, returnedBook.getID());
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
  
  public void testListBooksDefault() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    List<IBook> bookList = testBookDAO.listBooks();
    
    // Confirm empty list
    assertTrue(bookList.isEmpty());
    
    // Add two books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
                        "82.023 275 [2011]");
    
    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");
    
    // Get list of added books
    bookList = testBookDAO.listBooks();
    
    // Confirm only two items in list
    assertEquals(2, bookList.size());
    
    // Confirm first book 
    assertEquals(mockedBook, bookList.get(0));
    
    // Confirm second book
    assertEquals(mockedBookTwo, bookList.get(1));

  }
  
  public void testListBooksNoBooks() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    List<IBook> bookList = testBookDAO.listBooks();
    
    // Confirm empty list
    assertTrue(bookList.isEmpty());
  }
   
  public void testListBooksOneBook() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    List<IBook> bookList = testBookDAO.listBooks();
    
    // Confirm empty list
    assertTrue(bookList.isEmpty());
    
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
                        "82.023 275 [2011]");
    
    // Get list of added books
    bookList = testBookDAO.listBooks();
    
    // Confirm only one item in list
    assertEquals(1, bookList.size());
    
    // Confirm first book in list equals 'Great Expectations' 
    assertEquals(mockedBook, bookList.get(0));
    
  }
  
  // ==========================================================================
  // Testing findBooksByAuthor(String author) method
  // ==========================================================================
  
  public void testFindBooksByAuthorDefault() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    // Add two books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
                        "82.023 275 [2011]");
    
    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");
    
    // Try to find Harper Lee book
    List<IBook> bookListByAuthor = testBookDAO.findBooksByAuthor("Harper Lee");
    
    // Confirm only one item in list
    assertEquals(1, bookListByAuthor.size());
    
    IBook book = bookListByAuthor.get(0);
    
    // Confirm book matches expected book
    assertEquals(mockedBookTwo, book);
    
    // Confirm book author matches input
    assertEquals("Harper Lee", book.getAuthor());
    
    // Try to find Charles Dickens book
    bookListByAuthor = testBookDAO.findBooksByAuthor("Charles Dickens");
    
    // Confirm only one item in list
    assertEquals(1, bookListByAuthor.size());
    
    book = bookListByAuthor.get(0);
    
    // Confirm book matches expected book
    assertEquals(mockedBook, book);
    
    // Confirm book author matches input
    assertEquals("Charles Dickens", book.getAuthor()); 
  }
  
  public void testFindBooksByAuthorNullAuthor() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.findBooksByAuthor(null);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testFindBooksByAuthorEmptyAuthor()
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.findBooksByAuthor("");
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testFindBooksByAuthorNonExistentAuthor() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    // Add two books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
                        "82.023 275 [2011]");
    
    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");
    
    // Try to find Dean Koontz book
    List<IBook> bookListByAuthor = testBookDAO.findBooksByAuthor("Dean Koontz");
    
    // Confirm list empty
    assertTrue(bookListByAuthor.isEmpty());
  }
  
  public void testFindBooksByAuthorNoBooks() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    // Try to find Dean Koontz book
    List<IBook> bookListByAuthor = testBookDAO.findBooksByAuthor("Dean Koontz");
    
    // Confirm list empty
    assertTrue(bookListByAuthor.isEmpty());
  }
  
  // ==========================================================================
  // Testing findBooksByTitle() method
  // ==========================================================================
  
  
  
  
  
  
  // ==========================================================================
  // Testing findBooksByAuthorTitle(String author, String title) method
  // ==========================================================================
}
