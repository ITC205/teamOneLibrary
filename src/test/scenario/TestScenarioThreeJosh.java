package test.scenario;

import junit.framework.TestCase;

/**
 * TestScenarioThreeJosh class
 * 
 * Class that tests a complete user 'scenario' without the UI
 * 
 * Scenario Brief Description:  
 *  Member: 
 *     no loans
 *     no fines
 * 
 * Member initially scans non-existing ID, then scans valid ID
 *     
 * Scans 3 books, one of which doesn't exist (invalid ID), one is unavailable 
 * (DAMAGED), and one is valid
 *
 * Clicks complete.
 * Clicks Confirm.
 *
 * The test is not set up to verify every method call to every interacting object.
 * It focuses on calls triggering 'hardware' changes, state transitions and key 
 * expected post-conditions of each step in the specific scenario. UI display 
 * messages and other details are better verified through UAT testing. 
 * 
 * 
 * @author Josh Kent
 *
 */
public class TestScenarioThreeJosh extends TestCase
{

}
