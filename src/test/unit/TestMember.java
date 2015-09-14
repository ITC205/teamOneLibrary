package test.unit;

import junit.framework.*;
import library.entities.Member;

public class TestMember extends TestCase
{
  
  public TestMember(String name) 
  { 
    super(name);
  }
  
  
  
  public void TestMember()
  {
    Member testMember = new Member("testFirstName", "testLastName", "01234567", "abc@gef.com", 1);
  }
  
  
  
  

}
