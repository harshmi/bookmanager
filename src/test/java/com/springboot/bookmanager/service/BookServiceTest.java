package com.springboot.bookmanager.service;

import com.springboot.bookmanager.models.Book;
import com.springboot.bookmanager.repo.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

public class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

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
    public void testSaveBook_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Test Book", "Author", "9781234567890", date, 19.99);
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.saveBook(book);
        assertEquals(book, savedBook);
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    public void testGetAllBooks_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book1 = createBook(1L, "Book One", "Author One", "9781234567890", date, 19.99);
        Book book2 = createBook(2L, "Book Two", "Author Two", "9780987654321", date, 29.99);
        List<Book> books = Arrays.asList(book1, book2);
        when(bookRepo.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        assertEquals(books, result);
        verify(bookRepo, times(1)).findAll();
    }

    @Test
    public void testGetBookById_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Test Book", "Author", "9781234567890", date, 19.99);
        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);
        assertEquals(book, result.get());
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    public void testGetBookById_Failure_NotFound() {
        when(bookRepo.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(1L);
        assertEquals(Optional.empty(), result);
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    public void testUpdateBookById_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Updated Book", "Updated Author", "9781234567890", date, 19.99);
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.updateBookById(1L, book);
        assertEquals(book, updatedBook);
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    public void testDeleteBook_Success() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01");
        Book book = createBook(1L, "Test Book", "Author", "9781234567890", date, 19.99);
        when(bookRepo.findById(anyLong())).thenReturn(Optional.of(book));
        doNothing().when(bookRepo).deleteById(anyLong());

        bookService.deleteBook(1L);
        verify(bookRepo, times(1)).deleteById(1L);
    }
}