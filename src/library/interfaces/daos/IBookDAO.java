package library.interfaces.daos;

import java.util.List;

import library.interfaces.entities.IBook;

public interface IBookDAO {
	
	public IBook addBook(String author, String title, String callNo);
	
	public IBook getBookByID(int id);
	
	public List<IBook> listBooks();
	
	public List<IBook> findBooksByAuthor(String author);

	public List<IBook> findBooksByTitle(String title);
	
	public List<IBook> findBooksByAuthorTitle(String author, String title);

}
