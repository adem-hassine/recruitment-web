package com.proxym.libraryapp.domain.book;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void saveAll(List<Book> books) {
        this.bookRepository.saveAll(books);
    }

    public void save(Book book) {
        this.bookRepository.save(book);
    }

    public Book findBook(Long isbnCode) {
        return bookRepository.findBook(new ISBN(isbnCode));
    }

    public long findQuantity(Long isbnCode) {
        return bookRepository.findQuantity(new ISBN(isbnCode));
    }

    public boolean existsById(Long isbnCode) {
        return bookRepository.existsById(new ISBN(isbnCode));
    }

    public Book findOneByQuantityEquals(long qtty) {
        return bookRepository.findOneByQuantityEquals(qtty);
    }

    public Book findOneByQuantityGreaterThanOrEqual(long qtty) {
        return bookRepository.findOneByQuantityGreaterThanOrEqual(qtty);
    }

    public Book findAvailableBook() {
        return bookRepository.findOneByQuantityGreaterThanOrEqual(1L);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
