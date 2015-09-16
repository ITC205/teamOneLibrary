package test.unit;

import junit.framework.TestCase;
import library.daos.BookDAO;
import library.interfaces.daos.IBookHelper;

import static org.mockito.Mockito.*;

/**
 * TestBookDAO class
 * @author Josh Kent
 *
 */
public class TestBookDAO extends TestCase
{
  private IBookHelper mockedHelper;
  
  protected void setUp() 
  {
    mockedHelper = mock(IBookHelper.class);
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
  
  
  
}
