package library.interfaces.daos;

import java.util.Date;
import java.util.List;

import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public interface ILoanDAO {
		
	public ILoan createLoan(IMember borrower, IBook book);

	public void commitLoan(ILoan loan);
	
	public ILoan getLoanByID(int id);
	
	public ILoan getLoanByBook(IBook book);
	
	public List<ILoan> listLoans();
	
	public List<ILoan> findLoansByBorrower(IMember borrower);

	public List<ILoan> findLoansByBookTitle(String title);
	
	public void updateOverDueStatus(Date currentDate);

	public List<ILoan> findOverDueLoans();

}

