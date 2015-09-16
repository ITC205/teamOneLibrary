package test.unit;

import junit.framework.TestCase;
import library.daos.BookHelper;
import library.interfaces.entities.IBook;

/**
 * TestBookHelper class
 * 
 * @author Josh Kent
 *
 */
public class TestBookHelper extends TestCase
{
  public TestBookHelper(String methodName)
  {
    super(methodName);
  }



  public void testMakeBook()
  {
    BookHelper helper = new BookHelper();

    IBook testBook = helper.makeBook("Charles Dickens", "Great Expectations",
            "82.023 275 [2011]", 15);

    assertEquals("Charles Dickens", testBook.getAuthor());
    assertEquals("Great Expectations", testBook.getTitle());
    assertEquals("82.023 275 [2011]", testBook.getCallNumber());
    assertEquals(15, testBook.getID());

    testBook = helper.makeBook("Harper Lee", "To Kill a Mockingbird",
            "813.54 TOKI", 338);

    assertEquals("Harper Lee", testBook.getAuthor());
    assertEquals("To Kill a Mockingbird", testBook.getTitle());
    assertEquals("813.54 TOKI", testBook.getCallNumber());
    assertEquals(338, testBook.getID());

  }
}
