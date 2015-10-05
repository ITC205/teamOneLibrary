package test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
   test.scenario.TestScenarioOneRebecca.class,
   test.scenario.TestScenarioTwoRebecca.class
})

public class TestScenarioSuiteRebecca {

}