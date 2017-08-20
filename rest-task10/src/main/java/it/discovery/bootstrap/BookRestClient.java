package it.discovery.bootstrap;

import it.discovery.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;


public class BookRestClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Book> getAll() {
        return (List<Book>) restTemplate.getForObject("http://localhost:9000/book/get", List.class);
    }

    public ResponseEntity<Book> getById(int id) {
        return restTemplate.getForEntity("http://localhost:9000/book/get/" + id, Book.class);
    }

    public URI saveBook(Book book) {
        return restTemplate.postForLocation("http://localhost:9000/book/save", book);
    }

    public void updateBook(Book book, int id) {
        restTemplate.put("http://localhost:9000/book/update/" + id, book);
    }

    public static void main(String[] args) {
        Book book = new Book();
        book.setName("Java");
        book.setYear(2017);
        book.setAuthor("Author");

        BookRestClient bookRestClient = new BookRestClient();

        bookRestClient.saveBook(book);
        bookRestClient.getAll();
        bookRestClient.getById(1);

        book.setAuthor("Author2");
        bookRestClient.updateBook(book,1);


    }
}
