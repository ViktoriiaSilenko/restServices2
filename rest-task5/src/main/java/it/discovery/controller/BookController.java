package it.discovery.controller;

import it.discovery.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/book")
public class BookController {

    @RequestMapping(value = "/newBook", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Book getCurrentLocalDate() {

        Book book = new Book();
        book.setId(1);
        book.setName("Test name");
        book.setYear(2017);
        book.setAuthor("Test author");

        return book;
    }


}
