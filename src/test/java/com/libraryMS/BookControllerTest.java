package com.libraryMS;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.controller.BookController;
import libraryMS.domain.Book;
import libraryMS.service.BookService;
import libraryMS.utils.model.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {
//        when(bookService.getAll(new Pagination(1,100,1,"","",true,100), 1).getContent().size()).thenReturn(4);

        mockMvc.perform(get("http://localhost:8000/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("khgj"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = Book.builder().title("khgj").author("elie").ISBN("1234567891234").publicationYear(2024).build();
        book.setId(1);

        when(bookService.getById(1)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("khgj"));
    }

    @Test
    public void testAddBook() throws Exception {
        Book newBook = new Book("Hunger games", "John week", 2000, "1234567899999", null);

        when(bookService.merge(any(Book.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Hunger games"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book updatedBook = new Book("Updated Book", "Updated Author", 2022, "1d34d67891234", null);

        when(bookService.merge(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(bookService).delete(1);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }
}
