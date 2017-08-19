package it.discovery.controller;

import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.NumberUtils;
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
    public ResponseEntity<Book> getById(@PathVariable("id") int id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Book foundBook = bookRepository.findById(id);
        if (foundBook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>(foundBook, HttpStatus.OK);
    }

    @GetMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {

        if(id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Book item = bookRepository.findById(id);
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        book.setId(id);
        bookRepository.save(book);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/delete/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") int id) {

        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean deletedBook = bookRepository.delete(id);
        if (deletedBook == false) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        //return new ResponseEntity<>(deletedBook, HttpStatus.OK);
        return ResponseEntity.noContent().build();

    }


}
