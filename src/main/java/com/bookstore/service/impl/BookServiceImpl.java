package com.bookstore.service.impl;

import com.bookstore.domain.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        List<Book> bookList = (List<Book>) bookRepository.findAll();
        return bookList.stream().filter(Book::isActive).collect(Collectors.toList());
    }

    @Override
    public Book findOne(Long id) {
        return bookRepository.findOne(id);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> blurrySearch(String title) {
        List<Book> bookList = bookRepository.findByTitleContaining(title);
        return bookList.stream().filter(Book::isActive).collect(Collectors.toList());
    }

    @Override
    public void removeOne(Long id) {
        bookRepository.delete(id);
    }
}
