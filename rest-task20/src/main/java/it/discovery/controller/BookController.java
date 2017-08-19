package it.discovery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.discovery.repository.BookRepository;

@RestController
@RequestMapping("/book")
public class BookController {
	@Autowired
	private BookRepository bookRepository;
	
}
