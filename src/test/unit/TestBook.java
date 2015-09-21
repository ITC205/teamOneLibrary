package test.unit;

import java.lang.reflect.Field;

import junit.framework.TestCase;
import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.ILoan;

import static org.mockito.Mockito.*;

/**
 * TestBook class
 * 
 * Isolated test class for the Book class
 * Mock loans used where necessary
 * 
 * @author Josh Kent
 *
 */
public class TestBook extends TestCase
{
  // ==========================================================================
  // Constructor
  // ==========================================================================
  
  
  
  public TestBook(String methodName)
  {
    super(methodName);
  }



  // ==========================================================================
  // Constructor Testing
  // ==========================================================================


  
  // TB-01
  public void testConstructorDefault()
  {
    new Book("author", "title", "callNumber", 1);
  }



  // TB-02
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



  // TB-03
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



  // TB-04
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



  // TB-05
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



  // TB-06
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



  // TB-07
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


  // TB-08
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


  // TB-09
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



  //TB-10
  public void testGetID()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    assertEquals(1, book.getID());

    book = new Book("author", "title", "callNumber", 500);
    assertEquals(500, book.getID());

    book = new Book("author", "title", "callNumber", Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, book.getID());
  }



  //TB-11
  public void testGetAuthor()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
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



  //TB-12
  public void testGetTitle()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
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



  //TB-13
  public void testGetCallNumber()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
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



  //TB-14
  public void testGetStateDefault()
  {
    // Default state for new Book should be EBookState.AVAILABLE
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    assertEquals(EBookState.AVAILABLE, book.getState());
  }



  //TB-15
  public void testGetLoanDefault()
  {
    // Default loan for a new Book should be null
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    assertNull(book.getLoan());
  }



  //TB-16
  public void testGetLoanWithMock()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(false);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);
    
    // Set mockedLoan as value for 'loan_' field
    setBookLoan(book, mockedLoan);

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

  
  
  //TB-17
  public void testBorrowFromAvailableLoanNotNull()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Confirm 'loan_' field is initially null
    assertNull(book.getLoan());

    // Call method under test
    book.borrow(mockedLoan);

    // Confirm 'loan_' field contains mockedLoan
    ILoan returnedLoan = book.getLoan();
    assertEquals(book, returnedLoan.getBook());
    assertTrue(returnedLoan.isOverDue());

    // Confirm state change to EBookState.ON_LOAN
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }



  //TB-18
  public void testBorrowFromAvailableLoanIsNull()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Confirm 'loan_' field is initially null
    assertNull(book.getLoan());

    // Call method under test
    try {
      book.borrow(null);
      fail("Should have thrown IllegalArgumentException");
    }
    catch (IllegalArgumentException iae) {
      assertTrue(true);
    }

    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }



  //TB-19
  public void testBorrowFromOnLoan()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Confirm 'loan_' field is initially null
    assertNull(book.getLoan());

    // Call method under test
    try {
      book.borrow(mockedLoan);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.ON_LOAN);
  }



  //TB-20
  public void testBorrowFromDamaged()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DAMAGED);

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Confirm 'loan_' field is initially null
    assertNull(book.getLoan());

    // Call method under test
    try {
      book.borrow(mockedLoan);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DAMAGED);
  }



  //TB-21
  public void testBorrowFromLost()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.LOST);

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Confirm 'loan_' field is initially null
    assertNull(book.getLoan());

    // Call method under test
    try {
      book.borrow(mockedLoan);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.LOST);
  }



  //TB-22
  public void testBorrowFromDisposed()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DISPOSED);

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Confirm 'loan_' field is initially null
    assertNull(book.getLoan());

    // Call method under test
    try {
      book.borrow(mockedLoan);
      fail("Should have thrown RuntimeException");
    }
    catch (RuntimeException re) {
      assertTrue(true);
    }

    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());

    // Confirm state unchanged
    assertEquals(book.getState(), EBookState.DISPOSED);
  }

  
  
  // ==========================================================================
  // Testing lose() method
  // ==========================================================================



  //TB-23
  public void testLoseFromOnLoan()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);

    // Call method under test
    book.lose();

    // Confirm state change to EBookState.LOST
    assertEquals(book.getState(), EBookState.LOST);
  }



  //TB-24
  public void testLoseFromAvailable()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

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



  //TB-25
  public void testLoseFromLost()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.LOST);

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



  //TB-26
  public void testLoseFromDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DAMAGED);

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



  //TB-27
  public void testLoseFromDisposed()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DISPOSED);

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

  
  
  // TB-28
  public void testRepairFromDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DAMAGED);

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test
    book.repair();

    // Confirm state change to EBookState.AVAILABLE
    assertEquals(book.getState(), EBookState.AVAILABLE);
  }



  // TB-29
  public void testRepairFromAvailable()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

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



  // TB-30
  public void testRepairFromOnLoan()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    
    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);

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



  // TB-31
  public void testRepairFromLost()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.LOST);

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


  // TB-32
  public void testRepairFromDisposed()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DISPOSED);

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

  
  
  // TB-33
  public void testDisposeFromAvailable()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);

    // Call method under test
    book.dispose();

    // Confirm state change to EBookState.DISPOSED
    assertEquals(book.getState(), EBookState.DISPOSED);
  }



  // TB-34
  public void testDisposeFromDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DAMAGED);

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);

    // Call method under test
    book.dispose();

    // Confirm state change to EBookState.DISPOSED
    assertEquals(book.getState(), EBookState.DISPOSED);
  }



  // TB-35
  public void testDisposeFromLost()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.LOST);

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);

    // Call method under test
    book.dispose();

    // Confirm state change to EBookState.DISPOSED
    assertEquals(book.getState(), EBookState.DISPOSED);
  }



  // TB-36
  public void testDisposeFromOnLoan()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);

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



  // TB-37
  public void testDisposeFromDisposed()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DISPOSED);

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

  
  
  // TB-38 (Uses ILoan mock)
  public void testReturnBookFromOnLoanDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);

    // Set mockedLoan as value for 'loan_' field
    setBookLoan(book, mockedLoan);

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);
    
    // Confirm 'loan_' field contains mockedLoan
    ILoan returnedLoan = book.getLoan();
    assertEquals(book, returnedLoan.getBook());
    assertTrue(returnedLoan.isOverDue());

    // Call method under test using 'true' argument
    book.returnBook(true);

    // Confirm state change to EBookState.DAMAGED
    assertEquals(book.getState(), EBookState.DAMAGED);
    
    // Confirm 'loan_' field set to null
    assertNull(book.getLoan());    
  }



  // TB-39 (Uses ILoan mock)
  public void testReturnBookFromOnLoanUndamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    
    ILoan mockedLoan = mock(ILoan.class);

    // Set some behaviour for mockedLoan
    when(mockedLoan.getBook()).thenReturn(book);
    when(mockedLoan.isOverDue()).thenReturn(true);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.ON_LOAN);

    // Set mockedLoan as value for 'loan_' field
    setBookLoan(book, mockedLoan);

    // Confirm EBookState.ON_LOAN state
    assertEquals(book.getState(), EBookState.ON_LOAN);
    
    // Confirm 'loan_' field contains mockedLoan
    ILoan returnedLoan = book.getLoan();
    assertEquals(book, returnedLoan.getBook());
    assertTrue(returnedLoan.isOverDue());

    // Call method under test using 'false' argument
    book.returnBook(false);

    // Confirm state change to EBookState.AVAILABLE
    assertEquals(book.getState(), EBookState.AVAILABLE);
    
    // Confirm 'loan_' field set to null
    assertNull(book.getLoan());   
  }



  // TB-40
  public void testReturnBookFromLostDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    
    // Ensure book state is correct for this test
    setBookState(book, EBookState.LOST);

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());
    
    // Call method under test using 'true' argument
    book.returnBook(true);

    // Confirm state change to EBookState.DAMAGED
    assertEquals(book.getState(), EBookState.DAMAGED);
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }



  // TB-41
  public void testReturnBookFromLostUndamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    
    // Ensure book state is correct for this test
    setBookState(book, EBookState.LOST);

    // Confirm EBookState.LOST state
    assertEquals(book.getState(), EBookState.LOST);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());

    // Call method under test using 'false' argument
    book.returnBook(false);

    // Confirm state change to EBookState.AVAILABLE
    assertEquals(book.getState(), EBookState.AVAILABLE);
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }



  // TB-42
  public void testReturnBookFromAvailableDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());

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
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }



  // TB-43
  public void testReturnBookFromAvailableUndamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.AVAILABLE);

    // Confirm EBookState.AVAILABLE state
    assertEquals(book.getState(), EBookState.AVAILABLE);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());

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
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }


  // TB-44
  public void testReturnBookFromDamagedDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DAMAGED);

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());

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
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }



  // TB-45
  public void testReturnBookFromDamagedUndamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DAMAGED);

    // Confirm EBookState.DAMAGED state
    assertEquals(book.getState(), EBookState.DAMAGED);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());

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
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }


  
  // TB-46
  public void testReturnBookFromDisposedDamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DISPOSED);

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);

    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());
    
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
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }



  // TB-47
  public void testReturnBookFromDisposedUndamaged()
  {
    // Create test Book object
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);

    // Ensure book state is correct for this test
    setBookState(book, EBookState.DISPOSED);

    // Confirm EBookState.DISPOSED state
    assertEquals(book.getState(), EBookState.DISPOSED);
    
    // Confirm 'loan_' field is null as it should be in this state
    assertNull(book.getLoan());

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
    
    // Confirm 'loan_' field is still null
    assertNull(book.getLoan());
  }



  // ==========================================================================
  // Testing toString() method
  // ==========================================================================

  
  
  // TB-48
  public void testToString()
  {
    Book book = new Book("Charles Dickens", "Great Expectations",
                         "82.023 275 [2011]", 1);
    String expectedString = "ID: 1\n" 
                            + "Author: Charles Dickens\n"
                            + "Title: Great Expectations\n" 
                            + "Call Number: 82.023 275 [2011]";

    assertEquals(expectedString, book.toString());
  }
  
  
  
  // ==========================================================================
  // Helper Methods (Not Tests)
  // ==========================================================================
  
  
  
  private Book setBookState(Book book, EBookState newState) {
    
    try {
      // Using Reflection to directly set private field 'state_'

      Class<?> bookClass = book.getClass();
      Field state = bookClass.getDeclaredField("state_");

      // Enable direct modification of private field
      if (!state.isAccessible()) {
        state.setAccessible(true);
      }

      // Set book state
      state.set(book, newState);
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
    
    return book;
  }
  
  private Book setBookLoan(Book book, ILoan mockedLoan) {
    try {
      
      // Using Reflection to directly set private field 'loan_'

      Class<?> bookClass = book.getClass();
      Field loan = bookClass.getDeclaredField("loan_");

      // Enable direct modification of private field
      if (!loan.isAccessible()) {
        loan.setAccessible(true);
      }

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
    
    return book;
  }

}
