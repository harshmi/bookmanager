package com.springboot.bookmanager.repo;

import com.springboot.bookmanager.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {

}
