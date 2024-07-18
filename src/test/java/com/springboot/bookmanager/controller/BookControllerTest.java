package com.springboot.bookmanager.controller;

import com.springboot.bookmanager.models.Book;
import com.springboot.bookmanager.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Create Book object
    private Book createBook(Long id, String title, String author, String isbn, Date publishedDate, Double price) {
        return Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .isbn(isbn)
                .publishedDate(publishedDate)
                .price(price)
                .build();
    }

    @Test
    public void testCreateBook_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Test Book", "Author", "9781234567890", date, 19.99);
        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.createBook(book);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).saveBook(book);
    }

    @Test
    public void testGetAllBooks_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book1 = createBook(1L, "Book One", "Author One", "9781234567890", date, 19.99);
        Book book2 = createBook(2L, "Book Two", "Author Two", "9780987654321", date, 29.99);
        List<Book> books = Arrays.asList(book1, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    public void testGetBookById_Success() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Test Book", "Author", "9781234567890", date, 19.99);
        when(bookService.getBookById(anyLong())).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testGetBookById_Failure_NotFound() {
        when(bookService.getBookById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookController.getBookById(1L);
        });

        assertEquals("Book Not Found", exception.getMessage());
    }

    @Test
    public void testUpdateBookById_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Updated Book", "Updated Author", "9781234567890", date, 19.99);
        when(bookService.updateBookById(anyLong(), any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.updateBookById(1L, book);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testDeleteBook_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Test Book", "Author", "9781234567890", date, 19.99);
        when(bookService.getBookById(anyLong())).thenReturn(Optional.of(book));
        doNothing().when(bookService).deleteBook(anyLong());

        String response = bookController.deleteBook(1L);
        assertEquals("Book Deleted Successfully",response);
    }

    @Test
    public void testDeleteBook_Failure_NotFound() {
        when(bookService.getBookById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookController.deleteBook(1L);
        });

        assertEquals("Book Not Found", exception.getMessage());
    }
}