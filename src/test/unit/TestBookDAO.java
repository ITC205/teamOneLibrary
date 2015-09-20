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
  private IBook mockedBookThree;
  private IBook mockedBookFour;

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

    mockedBookThree = mock(IBook.class);
    when(mockedBookThree.getID()).thenReturn(3);
    when(mockedBookThree.getAuthor()).thenReturn("Harper Lee");
    when(mockedBookThree.getTitle()).thenReturn("Go Set a Watchman");
    when(mockedBookThree.getCallNumber()).thenReturn("982.441 LEE");


    mockedBookFour = mock(IBook.class);
    when(mockedBookFour.getID()).thenReturn(4);
    when(mockedBookFour.getAuthor()).thenReturn("Donald Duck");
    when(mockedBookFour.getTitle()).thenReturn("Great Expectations");
    when(mockedBookFour.getCallNumber()).thenReturn("124.41 DUCK");

    // Fixed ID value means that 'Great Expectations' should always be used as
    // the first mockedBook
    when(mockedHelper.makeBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]", 1))
    .thenReturn(mockedBook);

    // 'To Kill a Mockingbird' should be used as the second book
    when(mockedHelper.makeBook("Harper Lee", "To Kill a Mockingbird", 
            "813.54 TOKI", 2))
    .thenReturn(mockedBookTwo);

    // 'Go Set a Watchman' should be used as the third book
    when(mockedHelper.makeBook("Harper Lee", "Go Set a Watchman", 
            "982.441 LEE", 3))
    .thenReturn(mockedBookThree);

    // Donald Duck 'Great Expectations' should be used as the fourth book
    when(mockedHelper.makeBook("Donald Duck", "Great Expectations", 
            "124.41 DUCK", 4))
    .thenReturn(mockedBookFour);
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

  public void testFindBooksByAuthorMulitpleBooksByAuthor()
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Add three books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");

    testBookDAO.addBook("Harper Lee", "Go Set a Watchman", "982.441 LEE");

    // Try to find Harper Lee books
    List<IBook> bookListByAuthor = testBookDAO.findBooksByAuthor("Harper Lee");

    // Confirm two items in list
    assertEquals(2, bookListByAuthor.size());

    IBook toKillBook = bookListByAuthor.get(0);
    IBook goSetBook = bookListByAuthor.get(1);

    // Confirm items match expected books
    assertEquals(mockedBookTwo, toKillBook);
    assertEquals(mockedBookThree, goSetBook);

    // Confirm authors of books matches input
    assertEquals("Harper Lee", toKillBook.getAuthor());
    assertEquals("Harper Lee", goSetBook.getAuthor());
  }

  // ==========================================================================
  // Testing findBooksByTitle() method
  // ==========================================================================

  public void testFindBooksByTitleDefault() 
  {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Add two books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");

    // Try to find 'To Kill a Mockingbird' book
    List<IBook> bookListByTitle = testBookDAO.findBooksByTitle("To Kill a "
            + "Mockingbird");

    // Confirm only one item in list
    assertEquals(1, bookListByTitle.size());

    IBook book = bookListByTitle.get(0);

    // Confirm book matches expected book
    assertEquals(mockedBookTwo, book);

    // Confirm book title matches input
    assertEquals("To Kill a Mockingbird", book.getTitle());

    // Try to find 'Great Expectations' book
    bookListByTitle = testBookDAO.findBooksByTitle("Great Expectations");

    // Confirm only one item in list
    assertEquals(1, bookListByTitle.size());

    book = bookListByTitle.get(0);

    // Confirm book matches expected book
    assertEquals(mockedBook, book);

    // Confirm book title matches input
    assertEquals("Great Expectations", book.getTitle()); 
  }

  public void testFindBooksByTitleEmptyTitle() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Add a book
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    try {
      testBookDAO.findBooksByTitle("");
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }

  public void testFindBooksByTitleNullTitle() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Add a book
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    try {
      testBookDAO.findBooksByTitle(null);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }

  public void testFindBooksByTitleNonExistentTitle() {
    {
      BookDAO testBookDAO = new BookDAO(mockedHelper);

      // Add two books 
      testBookDAO.addBook("Charles Dickens", "Great Expectations", 
              "82.023 275 [2011]");

      testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");

      // Try to find 'Digital Fortress' book
      List<IBook> bookListByTitle = testBookDAO.findBooksByTitle("Digital "
              + "Fortress");

      // Confirm list empty
      assertTrue(bookListByTitle.isEmpty());
    }
  }

  public void testFindBooksByTitleNoBooks() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Try to find 'Great Expectations' book
    List<IBook> bookListByTitle = testBookDAO.findBooksByTitle("Great "
            + "Expectations");

    // Confirm list empty
    assertTrue(bookListByTitle.isEmpty());
  }

  public void testFindBooksByTitleMultipleBooksSameTitle() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Add four books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");

    testBookDAO.addBook("Harper Lee", "Go Set a Watchman", "982.441 LEE");

    testBookDAO.addBook("Donald Duck", "Great Expectations", "124.41 DUCK");

    // Try to find 'Great Expectations' books
    List<IBook> bookListByTitle = testBookDAO.findBooksByTitle("Great "
            + "Expectations");
    // Confirm list contains both books
    assertEquals(2, bookListByTitle.size());

    // Confirm the two books are the expected books
    assertTrue(bookListByTitle.contains(mockedBook));     // Charles Dickens
    assertTrue(bookListByTitle.contains(mockedBookFour)); // Donald Duck
  }


  // ==========================================================================
  // Testing findBooksByAuthorTitle(String author, String title) method
  // ==========================================================================

  public void testFindBooksByAuthorTitleDefault() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    // Add four books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");

    testBookDAO.addBook("Harper Lee", "Go Set a Watchman", "982.441 LEE");

    testBookDAO.addBook("Donald Duck", "Great Expectations", "124.41 DUCK");

    // Try to find 'Go Set a Watchman' book
    List<IBook> bookListByTitleAndAuthor = testBookDAO.findBooksByAuthorTitle(
            "Harper Lee", "Go Set a Watchman");

    // Confirm list contains only one element
    assertEquals(1, bookListByTitleAndAuthor.size());

    // Confirm book inside list is the expected book
    assertTrue(bookListByTitleAndAuthor.contains(mockedBookThree));

    // Try to find Donald Duck 'Great Expectations' book
    bookListByTitleAndAuthor = testBookDAO.findBooksByAuthorTitle(
            "Donald Duck", "Great Expectations");

    // Confirm list contains only one element
    assertEquals(1, bookListByTitleAndAuthor.size());

    // Confirm book inside list is the expected book
    assertTrue(bookListByTitleAndAuthor.contains(mockedBookFour));
  }

  public void testFindBooksByAuthorTitleEmptyAuthor() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    try {
      testBookDAO.findBooksByAuthorTitle("", "To Kill a Mockingbird");
      fail("Should have thrown IllegalArgumentExceptiion");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }

  public void testFindBooksByAuthorTitleNullAuthor() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);

    try {
      testBookDAO.findBooksByAuthorTitle(null, "To Kill a Mockingbird");
      fail("Should have thrown IllegalArgumentExceptiion");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }

  public void testFindBooksByAuthorTitleEmptyTitle() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.findBooksByAuthorTitle("Harper Lee", "");
      fail("Should have thrown IllegalArgumentExceptiion");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testFindBooksByAuthorTitleNullTitle() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    try {
      testBookDAO.findBooksByAuthorTitle("Harper Lee", null);
      fail("Should have thrown IllegalArgumentExceptiion");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testFindBooksByAuthorTitleSingleMatchOnly() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    // Add four books 
    testBookDAO.addBook("Charles Dickens", "Great Expectations", 
            "82.023 275 [2011]");

    testBookDAO.addBook("Harper Lee", "To Kill a Mockingbird", "813.54 TOKI");

    testBookDAO.addBook("Harper Lee", "Go Set a Watchman", "982.441 LEE");

    testBookDAO.addBook("Donald Duck", "Great Expectations", "124.41 DUCK");
    
    // Individually the title and author would match, but together they should
    // not return any results
    List<IBook> booksByAuthorAndTitle = testBookDAO.findBooksByAuthorTitle(
                                        "Harper Lee", "Great Expectations");
    
    // Confirm no results
    assertTrue(booksByAuthorAndTitle.isEmpty());
  }
  
  public void testFindBooksByAuthorTitleNoBooks() {
    BookDAO testBookDAO = new BookDAO(mockedHelper);
    
    // Try to find books
    List<IBook> booksByAuthorAndTitle = testBookDAO.findBooksByAuthorTitle(
                                        "Harper Lee", "Go Set a Watchman");
    
    // Confirm no results
    assertTrue(booksByAuthorAndTitle.isEmpty());
  }
}
