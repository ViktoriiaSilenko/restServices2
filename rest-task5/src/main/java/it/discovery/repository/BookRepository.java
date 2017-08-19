package it.discovery.repository;

import java.util.List;
import java.util.Optional;

import it.discovery.model.Book;
import org.springframework.http.ResponseEntity;

public interface BookRepository {
	Optional<Book> findById(int id);
	
	List<Book> findAll();
	
	void save(Book book);
	
	boolean delete(int id);

}
