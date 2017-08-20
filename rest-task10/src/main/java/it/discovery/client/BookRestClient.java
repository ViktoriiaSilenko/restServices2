package it.discovery.client;

import it.discovery.model.Book;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;


public class BookRestClient {

    private final TestRestTemplate restTemplate = new TestRestTemplate("admin", "admin");

    public List<Book> getAll() {
        return (List<Book>) restTemplate.getForObject("http://localhost:8080/book/get", List.class);
    }

    public void clearCache() {
        restTemplate.getForEntity("http://localhost:8080/book/clear", Void.class);
    }

    public ResponseEntity<Book> getById(int id) {
        return restTemplate.getForEntity("http://localhost:8080/book/get/" + id, Book.class);
    }

    public URI saveBook(Book book) {
        return restTemplate.postForLocation("http://localhost:8080/book/save", book);
        /*MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

        HttpEntity<Book> request = new HttpEntity<>(book, map);

        return restTemplate.postForLocation("http://localhost:9000/book/save", request);*/
    }

    public void updateBook(Book book, int id) {
        restTemplate.put("http://localhost:8080/book/update/" + id, book);
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
