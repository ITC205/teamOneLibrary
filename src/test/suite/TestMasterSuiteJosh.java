package test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestMasterSuiteJosh
 * 
 * Runs EVERY test that I have written, including the following test classes:
 * TestBook, TestBookHelper, TestBookDAO, TestBookFamily, 
 * TestScanBookOperation, TestCompleteScansOperation,
 * TestScenarioOneJosh, TestScenarioTwoJosh, TestScenarioThreeJosh
 * 
 * @author Josh Kent
 */
public class TestMasterSuiteJosh extends TestCase
{
  public TestMasterSuiteJosh(String methodName) {
    super(methodName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite();
    
    suite.addTestSuite(TestBookSuite.class);
    suite.addTestSuite(TestOperationSuiteJosh.class);
    suite.addTestSuite(TestScenarioSuiteJosh.class);
    
    return suite;
  }
}
