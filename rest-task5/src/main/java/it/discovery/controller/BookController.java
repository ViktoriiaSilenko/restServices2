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

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        method = RequestMethod.POST)
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

    @RequestMapping(value = "/get/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Book getById(@PathVariable("id") int id) {
        return bookRepository.findById(id);
    }

    @RequestMapping(value = "/get", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Book> getById() {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/delete/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
            method = RequestMethod.DELETE)
    public boolean deleteById(@PathVariable("id") int id) {
        return bookRepository.delete(id);
    }


}
