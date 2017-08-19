package it.discovery.repository;

import java.util.*;

import org.springframework.stereotype.Repository;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

import it.discovery.model.Book;

@Repository
public class SimpleBookRepository implements BookRepository {
	private final Map<Integer, Book> books = new HashMap<>();

	private int counter = 0;

	private final GaugeService gaugeService;

	private final CounterService counterService;

	public SimpleBookRepository(GaugeService gaugeService,
								CounterService counterService) {
		this.gaugeService = gaugeService;
		this.counterService = counterService;
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
		counterService.increment("book.count");
		//gaugeService.submit("book.count", counter);
	}

	@Override
	public boolean delete(int id) {
		if (!books.containsKey(id)) {
			return false;
		}

		books.remove(id);
		System.out.println("*** Book with id=" + id + " was deleted");

		counterService.decrement("book.count");
		gaugeService.submit("book.count", counter);
		return true;
	}

}
