package test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Groups test classes to be run as a suite.
 */
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses(
  {
      TestLoan.class,
      TestLoanHelper.class
  })

public class TestSuiteLoanFamily
{
 /* empty class */
}