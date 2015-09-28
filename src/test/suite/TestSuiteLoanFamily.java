package test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Groups test classes to be run as a suite.
 *
 * @author nicholasbaldwin
 */
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses(
  {
    test.unit.TestLoan.class,
    test.unit.TestLoanHelper.class,
    test.unit.TestLoanDAO.class,
    test.collaboration.TestLoanFamily.class
  })

public class TestSuiteLoanFamily
{
 /* empty class */
}
