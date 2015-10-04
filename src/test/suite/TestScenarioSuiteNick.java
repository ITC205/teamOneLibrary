package test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Groups test classes to be run as a suite.
 *
 * @author nicholasbaldwin
 */
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses(
  {
    test.scenario.MemberWithOverDueLoansIsRestricted.class
  })

public class TestScenarioSuiteNick
{
 /* empty class */
}
