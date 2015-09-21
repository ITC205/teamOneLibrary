package test.integration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.unit.TestBook;
import test.unit.TestBookDAO;
import test.unit.TestBookHelper;

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
