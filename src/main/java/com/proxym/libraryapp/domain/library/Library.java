package com.proxym.libraryapp.domain.library;

import com.proxym.libraryapp.domain.book.Book;
import com.proxym.libraryapp.domain.book.BookRepository;
import com.proxym.libraryapp.domain.book.ISBN;
import com.proxym.libraryapp.domain.borrowing.BorrowingDetails;
import com.proxym.libraryapp.domain.member.Member;

import java.time.LocalDate;
import java.util.UUID;

/**
 * The library class is in charge of stocking the books and managing the return delays and members
 *
 * The books are available via the {@link BookRepository}
 */
public interface Library {

    /**
     * A member is borrowing a book from our library.
     *
     * @param isbnCode the isbn code of the book
     * @param memberId borrower member id
     * @param borrowedAt the date when the book was borrowed
     *
     * @return the book the member wishes to obtain if found
     * @throws HasLateBooksException in case the member has books that are late
     *
     * @see ISBN
     * @see Member
     */
    BorrowingDetails borrowBook(Long isbnCode, UUID memberId, LocalDate borrowedAt) throws HasLateBooksException;

    /**
     * A member returns a book to the library.
     * We should calculate the tarif and probably charge the member
     *
     * @param isbnCode the {@link Book} they return
     * @param memberId the {@link Member} who is returning the book
     *
     * @see Member#payBook(int)
     */
    void returnBook(Long isbnCode, UUID memberId);
}
