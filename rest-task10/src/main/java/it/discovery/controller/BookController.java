package it.discovery.controller;

import it.discovery.exception.BookNotFoundException;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

    @CacheResult(cacheName = "books")
    @GetMapping(value = "/get/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    //public ResponseEntity<Resource<Book>> getById(@PathVariable("id") int id) {
    public ResponseEntity<Book> getById(@PathVariable("id") int id) {

        /*if (id <= 0) {
            ResponseEntity<Resource<Book>> bookBadRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            bookBadRequest.add(linkTo(methodOn(BookController.class)
                    .getById(bookBadRequest.getBody().getId())).withSelfRel());

            if(!bookBadRequest.getBody().isRented()) {
                resource.add(linkTo(methodOn(BookController.class)
                        .rentBook(bookBadRequest.getBody().getId())).withRel("rent"));
            }

            return resource;

        }*/

        /*  !!!
        Book foundBook = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        Resource<Book> resource = new Resource<Book>(foundBook);

        ResponseEntity<Resource<Book>> responce =  ResponseEntity.ok(resource);
        resource.add(linkTo(methodOn(BookController.class)
                .getById(foundBook.getId())).withSelfRel());

        if (!foundBook.isRented()) {
            resource.add(linkTo(methodOn(BookController.class)
                    .rentBook(foundBook.getId())).withRel("rent"));
        }

        return responce;*/

        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        return new ResponseEntity<>(book, HttpStatus.OK);

    }

   /* @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @Secured("ROLE_ADMIN")
    @CacheResult(cacheName = "books")
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

    @CacheRemove(cacheName = "books")
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
    @Secured("ROLE_ADMIN")
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
