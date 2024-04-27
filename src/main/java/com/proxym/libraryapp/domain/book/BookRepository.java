package com.proxym.libraryapp.domain.book;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The book repository emulates a database via 2 HashMaps
 */
@Repository
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new ConcurrentHashMap<>();

    public void saveAll(List<Book> books){
        books.forEach(this::save);
    }

    public void save(Book book){
        availableBooks.put(book.getIsbn(), book);
    }

    public Book findBook(ISBN isbnCode) {
        return availableBooks.get(isbnCode);
    }

    public Book findOneByQuantityGreaterThanOrEqual(Long quantity) {

        return availableBooks.values().stream().filter(val -> val.getQuantity() >= quantity).findFirst().orElseThrow();
    }

    public Book findOneByQuantityEquals(Long quantity) {
        return availableBooks.values().stream().filter(val -> val.getQuantity().equals(quantity)).findFirst().orElseThrow();
    }


    public List<Book> findAll() {
        return new ArrayList<>(availableBooks.values());
    }

    public long findQuantity(ISBN isbn) {
        return availableBooks.get(isbn).getQuantity();
    }

    public boolean existsById(ISBN isbnCode) {
        return availableBooks.containsKey(isbnCode);
    }

    public void deleteAll() {
        availableBooks = new ConcurrentHashMap<>();
    }
}
