package com.proxym.libraryapp.domain.book;


/**
 * A simple representation of a book
 */
public class Book {
    private String title;
    private String author;
    private ISBN isbn;
    private Long quantity;
    public Book(String title,String author,ISBN isbn,Long quantity){
        this.title = title;
        this.author = author;
        this.isbn= isbn;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    public void setIsbn(ISBN isbn) {
        this.isbn = isbn;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Book() {}
}
