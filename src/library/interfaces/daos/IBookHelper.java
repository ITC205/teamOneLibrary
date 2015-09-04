package library.interfaces.daos;

import library.interfaces.entities.IBook;

public interface IBookHelper {
	
	public IBook makeBook(String author, String title, String callNumber, int id);

}
