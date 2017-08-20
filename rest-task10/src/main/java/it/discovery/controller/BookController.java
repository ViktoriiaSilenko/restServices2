package it.discovery.controller;

import it.discovery.exception.BookNotFoundException;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import it.discovery.exception.BookNotFoundException;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) { // autowiring will be automatically in Spring 4
        this.bookRepository = bookRepository;
    }

    @PostMapping(value = "/save", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Book saveBook(Book book) {

        bookRepository.save(book);

        return book;
    }

    @Cacheable("books")
    @GetMapping(value = "/get/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Resource<ResponseEntity<Book>> getById(@PathVariable("id") int id) {

        if (id <= 0) {
            ResponseEntity<Book> bookBadRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            Resource<ResponseEntity<Book>> resource = new Resource<>(bookBadRequest);
            resource.add(linkTo(methodOn(BookController.class)
                    .getById(bookBadRequest.getBody().getId())).withSelfRel());

            if(!bookBadRequest.getBody().isRented()) {
                resource.add(linkTo(methodOn(BookController.class)
                        .rentBook(bookBadRequest.getBody().getId())).withRel("rent"));
            }

            return resource;

        }

        Book foundBook = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        ResponseEntity<Book> book = new ResponseEntity<>(foundBook, HttpStatus.OK);

        Resource<ResponseEntity<Book>> resource = new Resource<>(book);
        resource.add(linkTo(methodOn(BookController.class)
                .getById(book.getBody().getId())).withSelfRel());

        if (!foundBook.isRented()) {
            resource.add(linkTo(methodOn(BookController.class)
                    .rentBook(book.getBody().getId())).withRel("rent"));
        }

        return resource;

    }

    /*@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Resource<Book>> findBooks() {
        return bookRepository.findAll().stream().map(Resource::new)
                .peek(item ->
                        item.add(linkTo(methodOn(BookController.class).getById(
                                item.getContent().getId())).withSelfRel()))
                .peek(item -> {
                    if(!item.getContent().isRented()) {
                        item.add(linkTo(methodOn(BookController.class).rentBook(
                                item.getContent().getId())).withRel("rent"));
                    }})
                .collect(Collectors.toList());
    }*/

    @Cacheable("books")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Resource<Book>> getAll() {
        List<Book> books = bookRepository.findAll();

        List<Resource<Book>> resources = new ArrayList<>();
        for (Book book : books) {
            Resource<Book> resource = new Resource<>(book);

            resource.add(linkTo(methodOn(BookController.class)
                    .getById(book.getId())).withSelfRel());

            if (!book.isRented()) {
                resource.add(linkTo(methodOn(BookController.class)
                        .rentBook(book.getId())).withRel("rent"));
            }

            resources.add(resource);

        }

        return resources;
    }

    @CacheEvict(cacheNames = "books", allEntries = true)
    @GetMapping("/clear")
    public void clearCache() {

    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book book) {

        if(id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        book.setId(id);
        bookRepository.save(book);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/rent/{id}")
    public ResponseEntity<Book> rentBook(@PathVariable int id) {

        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        if (book.isRented()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        book.setRented(true);
        bookRepository.save(book);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping(value = "/delete/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") int id) {

        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean deletedBook = bookRepository.delete(id);
        if (deletedBook == false) {
            throw new BookNotFoundException();

        }

        return ResponseEntity.noContent().build();

    }


}
