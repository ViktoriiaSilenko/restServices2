package it.discovery.controller.unit_tests;

import it.discovery.bootstrap.RestApplication;
import it.discovery.model.Book;
import it.discovery.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitWebConfig(RestApplication.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        Book book = new Book();
        book.setId(1);
        book.setName("Java");
        book.setYear(2017);
        book.setAuthor("Author");

        given(bookRepository.findAll()).willReturn(Arrays.asList(book));
        given(bookRepository.findById(1)).willReturn(Optional.of(book));
        given(bookRepository.findById(2)).willReturn(Optional.empty());
    }


    @Test
    public void testGetAll_Storage_is_not_empty() throws Exception {
        ResultActions result = mockMvc.perform(get("/book/get"));

        assertAll(() -> result.andExpect(status().isOk()),
                  () -> result.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)),
                  () -> result.andExpect(jsonPath("$", Matchers.hasSize(1))),
                  () -> result.andExpect(jsonPath("$.[0].name", Matchers.equalTo("Java"))));

    }

    @Test
    public void testFindById_book_exists() throws Exception {

        ResultActions result = mockMvc.perform(get("/book/get/1"));

        assertAll(() -> result.andExpect(status().isOk()),
                  () -> result.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)),
                  () -> result.andExpect(jsonPath("$.name", Matchers.equalTo("Java"))));

    }

    @Test
    public void findBookById_InvalidId_NotFoundReturned() throws Exception {
        ResultActions result = mockMvc.perform(get("/book/get/2"));

        assertAll(() -> result.andExpect(content().string("")),
                  () -> result.andExpect(status().isNotFound()));
    }





}

