package com.springboot.bookmanager.service;

import com.springboot.bookmanager.models.Book;
import com.springboot.bookmanager.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepo bookRepo;

    public Book saveBook(Book book) {
        return bookRepo.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepo.findById(id);
    }

    public Book updateBookById(Long id,Book book){
        book.setId(id);
        return bookRepo.save(book);
    }

    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }
}
