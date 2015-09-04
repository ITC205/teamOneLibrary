package library.interfaces.entities;

public interface IBook {
	
	public void borrow(ILoan loan);
	
	public ILoan getLoan();
	
	public void returnBook(boolean damaged);
	
	public void lose();
	
	public void repair();
	
	public void dispose();	
	
	public EBookState getState();
	
	public String getAuthor();
	
	public String getTitle();
	
	public String getCallNumber();
	
	public int getID();

}
