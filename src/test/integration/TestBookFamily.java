package test.integration;

import java.util.List;

import junit.framework.TestCase;
import library.daos.BookDAO;
import library.daos.BookHelper;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

/**
 * TestBookFamily class
 * 
 * Tests the low-level integration of concrete 
 * Book, BookHelper and BookDAO objects (no mocks)
 * 
 * @author Josh Kent
 *
 */
public class TestBookFamily extends TestCase 
{
  // ==========================================================================
  // Variables
  // ==========================================================================
  
  
  
  private IBookHelper helper;
  private IBookDAO bookDAO;
  
  private final static String[] AUTHORS = {"Harper Lee",
                                           "Harper Lee",
                                           "Charles Dickens",
                                           "Donald Duck",
                                           "Dan Brown",
                                           "Matthew Reilly"};
  
  private static final String[] TITLES = {"To Kill a Mockingbird",
                                          "Go Set a Watchman",
                                          "Great Expectations",
                                          "Great Expectations",
                                          "Angels and Demons",
                                          "Hover Car Racer"};

  private static final String[] CALL_NUMBERS = {"123.11 LEE",
                                                "442.11 LEE",
                                                "453.92 DIC",
                                                "478.11 DUC",
                                                "889.02 BRO",
                                                "773.21 REI"};
  
  
  
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public TestBookFamily(String methodName) {
    super(methodName);
  }
  
  
  
  // ==========================================================================
  // Per-test Set-Up and Tear Down
  // ==========================================================================
  
  
  
  @Override
  protected void setUp() {
    helper = new BookHelper();
    bookDAO = new BookDAO(helper);
  }
  
  
  
  @Override
  protected void tearDown() {
    helper = null;
    bookDAO = null;
  }
  
  
  public void testAddBook() {
    
    List<IBook> allBooks = bookDAO.listBooks();
    
    // There should be no books returned, bookDAO should be empty
    assertTrue(allBooks.isEmpty());
    
    // Add a book
    IBook addedBook = bookDAO.addBook(AUTHORS[0], TITLES[0],CALL_NUMBERS[0]);
    
    // Confirm book details as expected
    assertEquals("Harper Lee", addedBook.getAuthor());
    assertEquals("To Kill a Mockingbird", addedBook.getTitle());
    assertEquals("123.11 LEE", addedBook.getCallNumber());
    
    // ID should be 1, since this is the first book added
    assertEquals(1, addedBook.getID());
    
    allBooks = bookDAO.listBooks();
    
    // Confirm added book is in the list
    assertEquals(1, allBooks.size());
    
    // Add a second book
    addedBook = bookDAO.addBook(AUTHORS[2], TITLES[2], CALL_NUMBERS[2]);
    
    // Confirm book details as expected
    assertEquals("Charles Dickens", addedBook.getAuthor());
    assertEquals("Great Expectations", addedBook.getTitle());
    assertEquals("453.92 DIC", addedBook.getCallNumber());
    
    // ID should be 2, since this is the second book added
    assertEquals(2, addedBook.getID());
    
    allBooks = bookDAO.listBooks();
    
    // Confirm added book is in the list
    assertEquals(2, allBooks.size());
  }
  
  
  public void testGetBookByIDDefault() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    createTestBooks();
    
    // Fetch book with ID = 2
    IBook returnedBook = bookDAO.getBookByID(2);
    
    // Confirm book is as expected
    assertEquals("Harper Lee", returnedBook.getAuthor());
    assertEquals("Go Set a Watchman", returnedBook.getTitle());
    assertEquals("442.11 LEE", returnedBook.getCallNumber());
    assertEquals(2, returnedBook.getID());    
  }
  
  public void testGetBookByIDOutOfRangeID() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    createTestBooks();
    
    // Fetch book with ID = 7 (highest ID should be 6)
    IBook returnedBook = bookDAO.getBookByID(7);
    
    // Confirm null is returned
    assertNull(returnedBook);
  }
  
  
  public void testFindBooksByAuthorDefault() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    createTestBooks();
    
    // Try to find Harper Lee books
    List<IBook> booksByAuthor = bookDAO.findBooksByAuthor("Harper Lee");
    
    // Should contain two books
    assertEquals(2, booksByAuthor.size());
    
    // Check authors and titles to confirm books returned are as expected
    for(IBook book : booksByAuthor) {
      assertEquals("Harper Lee", book.getAuthor());
      
      if(book.getTitle().equals("To Kill a Mockingbird")) {
        assertTrue(true);
      }
      else if(book.getTitle().equals("Go Set a Watchman")) {
        assertTrue(true);
      }
      else {
        fail("Only valid books are 'To Kill a Mockingbird' and 'Go Set a "
           + "Watchman'"); 
      }
    }
  }
  
  public void testFindBooksByAuthorNoBooksByAuthor() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    createTestBooks();
    
    // Try to find Dean Koontz books
    List<IBook> booksByAuthor = bookDAO.findBooksByAuthor("Dean Koontz");
    
    // Confirm list is empty (no Dean Koontz books exist)
    assertTrue(booksByAuthor.isEmpty());
  }
  
  public void testFindBooksByAuthorNoBooks() {
    
    // Try to find Dean Koontz books
    List<IBook> booksByAuthor = bookDAO.findBooksByAuthor("Dean Koontz");
    
    // Confirm list is empty (no books have been added)
    assertTrue(booksByAuthor.isEmpty());
  }
  
  public void testFindBooksByTitleDefault() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    createTestBooks();
    
    // Try to find Harper Lee books
    List<IBook> booksByTitle = bookDAO.findBooksByTitle("Great Expectations");
    
    // Should contain two books
    assertEquals(2, booksByTitle.size());
    
    // Check authors and titles to confirm books returned are as expected
    for(IBook book : booksByTitle) {
      assertEquals("Great Expectations", book.getTitle());
      
      if(book.getAuthor().equals("Charles Dickens")) {
        assertTrue(true);
      }
      else if(book.getAuthor().equals("Donald Duck")) {
        assertTrue(true);
      }
      else {
        fail("Only valid authors are 'Charles Dickens' and 'Donald Duck'"); 
      }
    }
  }
  
  public void testFindBooksByTitleNoBooksWithTitle() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    createTestBooks();
    
    // Try to find Dean Koontz books
    List<IBook> booksByTitle = bookDAO.findBooksByTitle("Bone Season");
    
    // Confirm list is empty (book titled 'Bone Season' does not exist)
    assertTrue(booksByTitle.isEmpty());
  }
  
  public void testFindBooksByTitleNoBooks() {
    // Try to find 'Angels and Demons'
    List<IBook> booksByTitle = bookDAO.findBooksByTitle("Angels and Demons");
    
    // Confirm list is empty (no books have been added)
    assertTrue(booksByTitle.isEmpty());
  }
  
  
  // ==========================================================================
  // Helper Methods
  // ==========================================================================
  
  
  
  private void createTestBooks() {
    
    for(int i = 0; i < AUTHORS.length; i++) {
      bookDAO.addBook(AUTHORS[i], TITLES[i], CALL_NUMBERS[i]);
    }
  }
  
}
