package test.integration;

import java.util.List;

import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.EBookState;

import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.ILoanDAO;

import library.entities.Loan;

import library.daos.LoanDAO;
import library.daos.LoanHelper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import static test.unit.LoanBuilder.*;
import static test.unit.LoanReflection.*;


/**
 * Test Confirm Loans operation.
 * Completes the borrowing process
 *
 * Pre-conditions:
 *  - BorrowingBookCTL class exists
 *  - Pending loan list exists
 *  - BorrowingBookCTL == CONFIRMING_LOANS
 *
 * Post-conditions:
 *  - Main Menu is displayed
 *  - All pending loans are committed and recorded
 *  - Loan slip of committed loans printed
 *  - cardReader is disabled
 *  - scanner is disabled
 *  - BorrowBookCTL state == COMPLETED
 */
public class TestConfirmLoansOperation
{
  //===========================================================================
  // Fixtures
  //===========================================================================

  //===========================================================================
  // Tests
  //===========================================================================

  @Test
  public void test()
  {


  }



  //===========================================================================
  // ?
  //===========================================================================


  //===========================================================================
  // ?
  //===========================================================================


  //===========================================================================
  // ?
  //===========================================================================


}
