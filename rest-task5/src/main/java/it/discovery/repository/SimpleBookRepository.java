package it.discovery.repository;

import java.util.*;

import org.springframework.stereotype.Repository;

import org.springframework.boot.actuate.metrics.GaugeService;

import it.discovery.model.Book;

@Repository
public class SimpleBookRepository implements BookRepository {
	private final Map<Integer, Book> books = new HashMap<>();

	private int counter = 0;

	private final GaugeService gaugeService;

	public SimpleBookRepository(GaugeService gaugeService) {
		this.gaugeService = gaugeService;
	}

	@Override
	public Optional<Book> findById(int id) {
		return Optional.ofNullable(books.get(id));
	}

	@Override
	public List<Book> findAll() {
		return new ArrayList<>(books.values());
	}

	@Override
	public void save(Book book) {
		if (book.getId() == 0) {
			counter++;
			book.setId(counter);
			books.put(counter, book);
			System.out.println("*** Book with id=" + book.getId() + " was created");
		} else {
			books.put(book.getId(), book);
			System.out.println("*** Book with id=" + book.getId() + " was updated");
		}

		gaugeService.submit("book.count", books.size());
	}

	@Override
	public boolean delete(int id) {
		if (!books.containsKey(id)) {
			return false;
		}

		books.remove(id);
		System.out.println("*** Book with id=" + id + " was deleted");

		gaugeService.submit("book.count", books.size());
		return true;
	}

}
