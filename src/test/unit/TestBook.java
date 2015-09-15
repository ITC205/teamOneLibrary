package test.unit;

import java.lang.reflect.Field;

import junit.framework.TestCase;
import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.ILoan;

import static org.mockito.Mockito.*;

public class TestBook extends TestCase
{
  public TestBook(String methodName)
  {
    super(methodName);
  }



  // ==========================================================================
  // Constructor Testing
  // ==========================================================================



  public void testConstructorDefault()
  {
    new Book("author", "title", "callNumber", 1);
  }



  public void testConstructorNullAuthor()
  {
    try {
      new Book(null, "title", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorEmptyAuthor()
  {
    try {
      new Book("", "title", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorNullTitle()
  {
    try {
      new Book("author", null, "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorEmptyTitle()
  {
    try {
      new Book("author", "", "callNumber", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorNullCallNumber()
  {
    try {
      new Book("author", "title", null, 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorEmptyCallNumber()
  {
    try {
      new Book("author", "title", "", 1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorIdNegative()
  {
    try {
      new Book("author", "title", "callNumber", -1);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  public void testConstructorIdZero()
  {
    try {
      new Book("author", "title", "callNumber", 0);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }



  // ==========================================================================
  // Getter Method Testing
  // ==========================================================================



  public void testGetID()
  {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    assertEquals(1, book.getID());

    book = new Book("author", "title", "callNumber", 500);
    assertEquals(500, book.getID());

    book = new Book("author", "title", "callNumber", Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, book.getID());
  }



  public void testGetAuthor()
  {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    assertEquals("Charles Dickens", book.getAuthor());

    book = new Book("Josh Kent", "title", "callNumber", 1);
    assertEquals("Josh Kent", book.getAuthor());

    book = new Book("73287", "title", "callNumber", 1);
    assertEquals("73287", book.getAuthor());

    book = new Book("!.,/ []}", "title", "callNumber", 1);
    assertEquals("!.,/ []}", book.getAuthor());

    book = new Book(" ", "title", "callNumber", 1);
    assertEquals(" ", book.getAuthor());

    book = new Book("A very long string that never ever ever ever ever ever "
            + "ever ever ends", "title", "callNumber", 1);
    assertEquals("A very long string that never ever ever ever ever ever ever"
            + " ever ends", book.getAuthor());
  }



  public void testGetTitle()
  {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    assertEquals("Great Expectations", book.getTitle());

    book = new Book("author", "A Typical Book Title", "callNumber", 1);
    assertEquals("A Typical Book Title", book.getTitle());

    book = new Book("author", "9726488881", "callNumber", 1);
    assertEquals("9726488881", book.getTitle());

    book = new Book("author", ":><#$%)", "callNumber", 1);
    assertEquals(":><#$%)", book.getTitle());

    book = new Book("author", " ", "callNumber", 1);
    assertEquals(" ", book.getTitle());

    book = new Book("author", "A very long book title that is too long to fit "
            + "on the front cover of any book", "callNumber", 1);
    assertEquals("A very long book title that is too long to fit on"
            + " the front cover of any book", book.getTitle());
  }



  public void testGetCallNumber()
  {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    assertEquals("82.023 275 [2011]", book.getCallNumber());

    book = new Book("author", "title", "516.375", 1);
    assertEquals("516.375", book.getCallNumber());

    book = new Book("author", "title", "1234566723", 1);
    assertEquals("1234566723", book.getCallNumber());

    book = new Book("author", "title", "_+=()!@", 1);
    assertEquals("_+=()!@", book.getCallNumber());

    book = new Book("author", "title", " ", 1);
    assertEquals(" ", book.getCallNumber());

    book = new Book("author", "title",
            "1728492399232432.12353218712783299129" + "21321321121", 1);
    assertEquals("1728492399232432.1235321871278329912921321321121",
            book.getCallNumber());
  }



  public void testGetStateDefault()
  {
    // Default state for new Book should be EBookState.AVAILABLE
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    assertEquals(EBookState.AVAILABLE, book.getState());
  }



  public void testGetLoanDefault()
  {
    // Default loan for a new Book should be null
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    assertNull(book.getLoan());
  }
  
  public void testGetLoanWithMock() {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);
    
    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(false);
    
    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN in order to satisfy getLoan() pre-condition

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");
      Field loan = bookClass.getDeclaredField("loan_");

      // Enable direct modification of private fields
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }
      
      if(!loan.isAccessible()) {
        loan.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
      
      // Set book loan
      loan.set(book, mockedLoan);
      
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }
    
    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);
    
    // Call method under test
    ILoan returnedLoan = book.getLoan();
    
    // Confirm returnedLoan is mock
    assertEquals(returnedLoan.getBook(), book);
    assertFalse(returnedLoan.isOverDue());
    
    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }

  // ==========================================================================
  // Testing borrow() method (Uses ILoan mock)
  // ==========================================================================
  
  public void testBorrowFromAvailable() {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);
    
    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);
    
    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.AVAILABLE in order to satisfy borrow() pre-condition

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.AVAILABLE);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);
    
    // Confirm loan_ field is initially null
    assertNull(book.getLoan());

    // Call method under test
    book.borrow(mockedLoan);
    
    // Confirm loan_ field contains mockedLoan
    ILoan returnedLoan = book.getLoan();
    assertEquals(book, returnedLoan.getBook());
    assertTrue(returnedLoan.isOverDue());

    // Confirm state change to EBookState.ON_LOAN
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }
  
  public void testBorrowFromOnLoan() {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);
    
    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);
    
    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);
    
    // Confirm loan_ field is initially null
    assertNull(book.getLoan());

    // Call method under test
    try {
      book.borrow(mockedLoan);
      fail("Should have thrown RuntimeException");
    }
    catch(RuntimeException re) {
      assertTrue(true);
    }
    
    // Confirm loan_ field is still null
    assertNull(book.getLoan());

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }
  
  // ==========================================================================
  // Testing lose() method
  // ==========================================================================



  // TODO Get each state (damaged, lost, etc.)
  public void testLoseFromOnLoan()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN in order to satisfy lose() pre-condition

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Call method under test
    book.lose();

    // Confirm state change to EBookState.LOST
    assertEquals(book.getState(), EBookState.LOST);
  }



  public void testLoseFromAvailable()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.AVAILABLE

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.AVAILABLE);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Call method under test
    try {
      book.lose();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }



  public void testLoseFromLost()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.LOST

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.LOST);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Call method under test
    try {
      book.lose();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.LOST);
  }



  public void testLoseFromDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DAMAGED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DAMAGED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test
    try {
      book.lose();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DAMAGED);
  }



  public void testLoseFromDisposed()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DISPOSED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DISPOSED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Call method under test
    try {
      book.lose();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DISPOSED);
  }



  // ==========================================================================
  // Testing repair() method
  // ==========================================================================

  public void testRepairFromDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DAMAGED in order to satisfy repair() pre-condition

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DAMAGED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test
    book.repair();

    // Confirm state change to EBookState.AVAILABLE
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }



  public void testRepairFromAvailable()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.AVAILABLE

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.AVAILABLE);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Call method under test
    try {
      book.repair();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }



  public void testRepairFromOnLoan()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Call method under test
    try {
      book.repair();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }



  public void testRepairFromLost()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.LOST

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.LOST);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Call method under test
    try {
      book.repair();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.LOST);
  }



  public void testRepairFromDisposed()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DISPOSED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DISPOSED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Call method under test
    try {
      book.repair();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DISPOSED);
  }



  // ==========================================================================
  // Testing dispose() method
  // ==========================================================================

  public void testDisposeFromAvailable()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.AVAILABLE in order to satisfy one of the dispose()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.AVAILABLE);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Call method under test
    book.dispose();

    // Confirm state change to EBookState.DISPOSED
    assertEquals(book.getState(), EBookState.DISPOSED);
  }
  
  public void testDisposeFromDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DAMAGED in order to satisfy one of the dispose()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DAMAGED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test
    book.dispose();

    // Confirm state change to EBookState.DISPOSED
    assertEquals(book.getState(), EBookState.DISPOSED);
  }
  
  public void testDisposeFromLost()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.LOST in order to satisfy one of the dispose()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.LOST);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Call method under test
    book.dispose();

    // Confirm state change to EBookState.DISPOSED
    assertEquals(book.getState(), EBookState.DISPOSED);
  }

  public void testDisposeFromOnLoan()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Call method under test
    try {
      book.dispose();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }
  
  public void testDisposeFromDisposed() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DISPOSED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DISPOSED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Call method under test
    try {
      book.dispose();
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DISPOSED);
  }
  
  // ==========================================================================
  // Testing returnBook(boolean damaged) method
  // ==========================================================================
  
  public void testReturnBookFromOnLoanDamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN in order to satisfy one of the returnBook()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Call method under test using 'true' argument
    book.returnBook(true);

    // Confirm state change to EBookState.DAMAGED
    assertEquals(book.getState(), EBookState.DAMAGED);
  }
  
  public void testReturnBookFromOnLoanUndamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.ON_LOAN in order to satisfy one of the returnBook()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.ON_LOAN);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Call method under test using 'false' argument
    book.returnBook(false);

    // Confirm state change to EBookState.AVAILABLE
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }
  
  public void testReturnBookFromLostDamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.LOST in order to satisfy one of the returnBook()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.LOST);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Call method under test using 'true' argument
    book.returnBook(true);

    // Confirm state change to EBookState.DAMAGED
    assertEquals(book.getState(), EBookState.DAMAGED);
  }
  
  public void testReturnBookFromLostUndamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.LOST in order to satisfy one of the returnBook()
      // pre-conditions

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.LOST);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Call method under test using 'false' argument
    book.returnBook(false);

    // Confirm state change to EBookState.AVAILABLE
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }
  
  public void testReturnBookFromAvailableDamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.AVAILABLE

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.AVAILABLE);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Call method under test using 'true' argument
    try {
      book.returnBook(true);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }
  
  public void testReturnBookFromAvailableUndamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.AVAILABLE

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.AVAILABLE);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Call method under test using 'false' argument
    try {
      book.returnBook(false);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }
  
  public void testReturnBookFromDamagedDamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DAMAGED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DAMAGED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test using 'true' argument
    try {
      book.returnBook(true);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DAMAGED);
  }
  
  public void testReturnBookFromDamagedUndamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DAMAGED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DAMAGED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test using 'false' argument
    try {
      book.returnBook(false);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DAMAGED);
  }
  
  public void testReturnBookFromDisposedDamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DISPOSED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DISPOSED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Call method under test using 'true' argument
    try {
      book.returnBook(true);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DISPOSED);
  }
  
  public void testReturnBookFromDisposedUndamaged() {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);

    try {
      // Using Reflection to directly set private field 'state_' to
      // EBookState.DISPOSED

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, EBookState.DISPOSED);
    }
    catch (NoSuchFieldException e) {
      fail("NoSuchFieldException should not occur");
    }
    catch (SecurityException e) {
      fail("SecurityException should not occur");
    }
    catch (IllegalArgumentException e) {
      fail("IllegalArgumentException should not occur");
    }
    catch (IllegalAccessException e) {
      fail("IllegalAccessException should not occur");
    }

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Call method under test using 'false' argument
    try {
      book.returnBook(false);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DISPOSED);
  }


  // ==========================================================================
  // Testing toString() method
  // ==========================================================================
  
  public void testToString() {
    Book book = new Book("Charles Dickens", "Great Expectations", "82.023 275 [2011]", 1);
    String expectedString = "ID: 1\n"
                          + "Author: Charles Dickens\n"
                          + "Title: Great Expectations\n"
                          + "Call Number: 82.023 275 [2011]";
    
    assertEquals(expectedString, book.toString());
  }
  
  
  
  
  
  // TODO get loan with mock object
}
