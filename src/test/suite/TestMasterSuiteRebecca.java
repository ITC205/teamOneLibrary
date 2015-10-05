package test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
   test.suite.TestSuiteMemberFamily.class,
   test.suite.TestOperationsSuiteRebecca.class,
   test.suite.TestScenarioSuiteRebecca.class
})

public class TestMasterSuiteRebecca {

}