package test.scenario;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;

/**
 * TestScenarioTwoJosh class
 * 
 * Class that tests a complete user 'scenario' without the UI
 * 
 * Scenario Brief Description:  
 *  Member: 
 *     no loans
 *     no fines
 *     
 * Scans 3 books, one of which is a duplicate (Same ID)
 *
 * Clicks complete.
 * Clicks Reject
 * Clicks Cancel.
 *
 * The test is not set up to verify every method call to every interacting object.
 * It focuses on calls triggering 'hardware' changes, state transitions and key 
 * expected post-conditions of each step in the specific scenario. UI display 
 * messages and other details are better verified through UAT testing. 
 * 
 * @author Josh Kent
 */
public class TestScenarioTwoJosh extends TestCase
{

}
