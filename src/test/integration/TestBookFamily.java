package test.integration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  
  // Deliberately contains one duplicate book
  private final static String[] AUTHORS = {"Harper Lee",
                                           "Harper Lee",
                                           "Charles Dickens",
                                           "Donald Duck",
                                           "Dan Brown",
                                           "Matthew Reilly",
                                           "Matthew Reilly"};
  
  private static final String[] TITLES = {"To Kill a Mockingbird",
                                          "Go Set a Watchman",
                                          "Great Expectations",
                                          "Great Expectations",
                                          "Angels and Demons",
                                          "Hover Car Racer",
                                          "Hover Car Racer"}; 

  private static final String[] CALL_NUMBERS = {"123.11 LEE",
                                                "442.11 LEE",
                                                "453.92 DIC",
                                                "478.11 DUC",
                                                "889.02 BRO",
                                                "773.21 REI",
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
    addTestBooks();
    
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
    addTestBooks();
    
    // Fetch book with ID = 8 (highest ID should be 7)
    IBook returnedBook = bookDAO.getBookByID(8);
    
    // Confirm null is returned
    assertNull(returnedBook);
  }
  
  
  public void testFindBooksByAuthorDefault() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    addTestBooks();
    
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
    addTestBooks();
    
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
    addTestBooks();
    
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
    addTestBooks();
    
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
  
  
  public void testFindBooksByAuthorTitleDefault() {
    
   // Adds six test books to bookDAO (see String[] constant variables)
   addTestBooks(); 
   
   // Search for the two 'Hover Car Racer' books
   List<IBook> booksByAuthorAndTitle = bookDAO.findBooksByAuthorTitle(
                                               "Matthew Reilly", 
                                               "Hover Car Racer");
   
   // Confirm list contains both books
   assertEquals(2, booksByAuthorAndTitle.size());
   
   // Create a set to hold each ID
   Set<Integer> ids = new HashSet<Integer>();
   
   // Confirm book details as expected
   for(IBook book : booksByAuthorAndTitle) {
     if(book.getAuthor().equals("Matthew Reilly")) {
       assertTrue(true);
     }
     else {
       fail("Unexpected author"); 
     }
     
     if(book.getTitle().equals("Hover Car Racer")) {
       assertTrue(true);
     }
     else {
       fail("Unexpected title");
     }
     ids.add(book.getID());
   }
   
   // Confirm that every book is different book with a different ID (there 
   // should not be actual duplicate books, even if they have the same author 
   // and title
   assertEquals(ids.size(), booksByAuthorAndTitle.size());
  }
  
  
  public void testFindBooksByAuthorTitleNoBooksByAuthor() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    addTestBooks(); 
    
    // There are no books by Dean Koontz, but there are books called 'Hover Car
    // Racer
    List<IBook> booksByAuthorAndTitle = bookDAO.findBooksByAuthorTitle(
                                        "Dean Koontz", "Hover Car Racer");
    
    // Confirm no results (title matched but not author)
    assertTrue(booksByAuthorAndTitle.isEmpty());
  }
  
  public void testFindBooksByAuthorTitleNoBooksWithTitle() {
    
    // Adds six test books to bookDAO (see String[] constant variables)
    addTestBooks(); 
    
    // There are no books called 'The Power of One', but there are books called 
    // by Harper Lee
    List<IBook> booksByAuthorAndTitle = bookDAO.findBooksByAuthorTitle(
                                        "Harper Lee", "The Power of One");
    
    // Confirm no results (author matched but not title)
    assertTrue(booksByAuthorAndTitle.isEmpty());
  }
  
  public void testFindBooksByAuthorTitleNoBooks() {
    
    // Try to find 'To Kill a Mockingbird' book
    List<IBook> booksByAuthorAndTitle = bookDAO.findBooksByAuthorTitle(
                                        "Harper Lee", "To Kill a Mockingbird");
    
    // No books should be found as no book were added to bookDAO 
    assertTrue(booksByAuthorAndTitle.isEmpty());
  }
  
  
  // ==========================================================================
  // Helper Methods
  // ==========================================================================
  
  
  
  private void addTestBooks() {
    
    for(int i = 0; i < AUTHORS.length; i++) {
      bookDAO.addBook(AUTHORS[i], TITLES[i], CALL_NUMBERS[i]);
    }
  }
  
}
