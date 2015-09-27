package test.collaboration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Groups test classes to be run as a suite.
 */
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses(
  {
    test.unit.TestLoan.class,
    test.unit.TestLoanHelper.class,
    test.unit.TestLoanDAO.class,
    TestLoanFamily.class
  })

public class TestSuiteLoanFamilyUnitAndCollaboration
{
 /* empty class */
}
