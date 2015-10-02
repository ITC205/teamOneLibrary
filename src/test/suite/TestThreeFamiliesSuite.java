package test.suite;

/**
 * Groups test classes to be run as a suite.
 */
@org.junit.runner.RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses(
  {
    test.suite.TestLoanSuite.class,
    test.suite.TestBookSuite.class,
    test.unit.TestSuiteMemberFamily.class

  })
public class TestThreeFamiliesSuite
{
  //
}
