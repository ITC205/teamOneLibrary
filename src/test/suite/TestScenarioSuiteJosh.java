package test.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.scenario.TestScenarioOneJosh;
import test.scenario.TestScenarioThreeJosh;
import test.scenario.TestScenarioTwoJosh;

/**
 * TestScenarioSuiteJosh class
 * 
 * Runs every scenario test by Josh, including: 
 * TestScenarioOneJosh, TestScenarioTwoJosh, TestScenarioThreeJosh
 * 
 * @author Josh Kent
 */
public class TestScenarioSuiteJosh extends TestCase
{
  public TestScenarioSuiteJosh(String methodName) {
    super(methodName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite();
    
    suite.addTestSuite(TestScenarioOneJosh.class);
    suite.addTestSuite(TestScenarioTwoJosh.class);
    suite.addTestSuite(TestScenarioThreeJosh.class);
    
    return suite;
  }
}
