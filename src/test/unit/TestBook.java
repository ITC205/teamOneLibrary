package test.unit;

import junit.framework.TestCase;
import library.entities.Book;

public class TestBook extends TestCase
{
  public TestBook(String methodName) 
  {
    super(methodName);
  }
  
  public void testConstructorDefault() 
  {
    new Book("author", "title", "callNumber", 1);
  }
  
  public void testConstructorNullAuthor() {
    try {
      new Book(null, "title", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorEmptyAuthor() {
    try {
      new Book("", "title", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorNullTitle() {
    try {
      new Book("author", null, "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorEmptyTitle() {
    try {
      new Book("author", "", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorNullCallNumber() {
    try {
      new Book("author", "title", null, 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorEmptyCallNumber() {
    try {
      new Book("author", "title", "", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorIdNegative() {
    try {
      new Book("author", "title", "callNumber", -1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  public void testConstructorIdZero() {
    try {
      new Book("author", "title", "callNumber", 0);
      fail("Should have thrown IllegalArgumentException");
    }
    catch(IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
  
  
}
