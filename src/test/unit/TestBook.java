package test.unit;

import junit.framework.TestCase;
import library.entities.Book;
import library.interfaces.entities.EBookState;

public class TestBook extends TestCase
{
  public TestBook(String methodName)
  {
    super(methodName);
  }

  
  
  // ==========================================================================
  // Constructor Testing
  // ==========================================================================



  public void testConstructorDefault()
  {
    new Book("author", "title", "callNumber", 1);
  }



  public void testConstructorNullAuthor()
  {
    try {
      new Book(null, "title", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorEmptyAuthor()
  {
    try {
      new Book("", "title", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorNullTitle()
  {
    try {
      new Book("author", null, "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorEmptyTitle()
  {
    try {
      new Book("author", "", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorNullCallNumber()
  {
    try {
      new Book("author", "title", null, 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorEmptyCallNumber()
  {
    try {
      new Book("author", "title", "", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorIdNegative()
  {
    try {
      new Book("author", "title", "callNumber", -1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorIdZero()
  {
    try {
      new Book("author", "title", "callNumber", 0);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }

  
  
  // ==========================================================================
  // Getter Method Testing
  // ==========================================================================

  
  
  public void testGetID() {
    Book book = new Book("author", "title", "callNumber", 1);
    assertEquals(1, book.getID());
    
    book = new Book("author", "title", "callNumber", 500);
    assertEquals(500, book.getID());
    
    book = new Book("author", "title", "callNumber", Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, book.getID());
  }
  
  
  
  public void testGetAuthor() {
    Book book = new Book("author", "title", "callNumber", 1);
    assertEquals("author", book.getAuthor());
    
    book = new Book("Josh Kent", "title", "callNumber", 1);
    assertEquals("Josh Kent", book.getAuthor());
    
    book = new Book("73287", "title", "callNumber", 1);
    assertEquals("73287", book.getAuthor());
    
    book = new Book("!.,/ []}", "title", "callNumber", 1);
    assertEquals("!.,/ []}", book.getAuthor());
    
    book = new Book(" ", "title", "callNumber", 1);
    assertEquals(" ", book.getAuthor());
    
    book = new Book("A very long string that never ever ever ever ever ever "
                    + "ever ever ends", "title", "callNumber", 1);
    assertEquals("A very long string that never ever ever ever ever ever ever"
                 + " ever ends", book.getAuthor());
  }
  
  public void testGetTitle() {
    Book book = new Book("author", "title", "callNumber", 1);
    assertEquals("title", book.getTitle());
    
    book = new Book("author", "A Typical Book Title", "callNumber", 1);
    assertEquals("A Typical Book Title", book.getTitle());
    
    book = new Book("author", "9726488881", "callNumber", 1);
    assertEquals("9726488881", book.getTitle());
    
    book = new Book("author", ":><#$%)", "callNumber", 1);
    assertEquals(":><#$%)", book.getTitle());
    
    book = new Book("author", " ", "callNumber", 1);
    assertEquals(" ", book.getTitle());
    
    book = new Book("author", "A very long book title that is too long to fit "
                    + "on the front cover of any book", "callNumber", 1);
    assertEquals("A very long book title that is too long to fit on"
                 + " the front cover of any book", book.getTitle());
  }
  
  public void testGetCallNumber() {
    Book book = new Book("author", "title", "callNumber", 1);
    assertEquals("callNumber", book.getCallNumber());
    
    book = new Book("author", "title", "516.375", 1);
    assertEquals("516.375", book.getCallNumber());
    
    book = new Book("author", "title", "1234566723", 1);
    assertEquals("1234566723", book.getCallNumber());
    
    book = new Book("author", "title", "_+=()!@", 1);
    assertEquals("_+=()!@", book.getCallNumber());
    
    book = new Book("author", "title", " ", 1);
    assertEquals(" ", book.getCallNumber());
    
    book = new Book("author", "title", "1728492399232432.12353218712783299129"
                    + "21321321121", 1);
    assertEquals("1728492399232432.1235321871278329912921321321121", 
                 book.getCallNumber());
  }
  
  public void testGetDefaultState() {
    Book book = new Book("author", "title", "callNumber", 1);
    assertEquals(EBookState.AVAILABLE, book.getState());
  }
  
  public void testGetDefaultLoan() {
    Book book = new Book("author", "title", "callNumber", 1);
    assertNull(book.getLoan());
  }
  
  // TODO Get each state (damaged, lost, etc.)
  
  // TODO get loan with mock object
  
  
}
