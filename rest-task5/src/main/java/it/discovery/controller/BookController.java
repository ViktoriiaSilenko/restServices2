package it.discovery.controller;

import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private static int id = 1;

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) { // autowiring will be automatically in Spring 4
        this.bookRepository = bookRepository;
    }

    @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Book getSampleBook() {

        Book book = new Book();
        book.setId(id);
        book.setName("Test name");
        book.setYear(2017);
        book.setAuthor("Test author");

        bookRepository.save(book);

        id++;

        return book;
    }

    @GetMapping(value = "/get/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Book getById(@PathVariable("id") int id) {
        return bookRepository.findById(id);
    }

    @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Book> getById() {
        return bookRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public void updateBook(@PathVariable int id, @RequestBody Book book) {
        Book item = bookRepository.findById(id);
        item.setId(id);
        bookRepository.save(book);
    }

    @DeleteMapping(value = "/delete/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public boolean deleteById(@PathVariable("id") int id) {
        return bookRepository.delete(id);
    }


}
