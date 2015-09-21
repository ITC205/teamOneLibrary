package test.unit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.integration.TestBookFamily;

/**
 * TestBookSuite class
 * 
 * Runs every test for related to Book, including: 
 * TestBook, TestBookHelper, TestBookDAO, TestBookFamily
 * 
 * @author Josh Kent
 *
 */
public class TestBookSuite extends TestCase
{
  public TestBookSuite(String methodName) {
    super(methodName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite();
    
    suite.addTestSuite(TestBook.class);
    suite.addTestSuite(TestBookHelper.class);
    suite.addTestSuite(TestBookDAO.class);
    suite.addTestSuite(TestBookFamily.class);
    
    return suite;
  }
}
