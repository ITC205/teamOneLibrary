package test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
   test.unit.TestMember.class,
   test.unit.TestMemberDAO.class,
   test.unit.TestMemberHelper.class
})

public class TestSuiteMemberFamily {

}