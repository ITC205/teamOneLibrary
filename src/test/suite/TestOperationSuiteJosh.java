package test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.integration.TestCompleteScansOperation;
import test.integration.TestScanBookOperation;

/**
 *  TestOperationSuite class
 * 
 * Runs every operation test by Josh, including: 
 * TestScanBookOperation, TestCompleteScansOperation
 * 
 * @author Josh Kent
 */
public class TestOperationSuiteJosh extends TestCase
{
  public TestOperationSuiteJosh(String methodName) {
    super(methodName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite();
    
    suite.addTestSuite(TestScanBookOperation.class);
    suite.addTestSuite(TestCompleteScansOperation.class);
    
    return suite;
  }
}
