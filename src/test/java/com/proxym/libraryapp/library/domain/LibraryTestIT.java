package com.proxym.libraryapp.library.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proxym.libraryapp.domain.book.Book;
import com.proxym.libraryapp.domain.book.BookService;

import static com.proxym.libraryapp.domain.borrowing.MemberLatenessHandler.RESIDENT_LATENESS_IN_DAYS;
import static com.proxym.libraryapp.domain.borrowing.MemberLatenessHandler.STUDENT_LATENESS_IN_DAYS;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.proxym.libraryapp.domain.borrowing.BorrowingDetails;
import com.proxym.libraryapp.domain.borrowing.BorrowingDetailsService;
import com.proxym.libraryapp.domain.library.BookAlreadyBorrowedException;
import com.proxym.libraryapp.domain.library.HasLateBooksException;
import com.proxym.libraryapp.domain.library.InsufficientBooksException;
import com.proxym.libraryapp.domain.library.Library;
import com.proxym.libraryapp.domain.member.Member;
import com.proxym.libraryapp.domain.member.MemberService;
import com.proxym.libraryapp.library.IntegrationTest;
import com.proxym.libraryapp.library.domain.members.MembersFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Do not forget to consult the README.md :)
 */
@IntegrationTest
public class LibraryTestIT {
    @Autowired
    private Library library;
    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BorrowingDetailsService borrowingDetailsService;
    private static List<Book> books;


    @BeforeEach
    void setup() throws IOException {
        ObjectMapper mapper = JsonMapper.builder() // or different mapper for other format
                .addModule(new JavaTimeModule())
                .build();
        File booksJson = new File("src/test/resources/books.json");
        books = mapper.readValue(booksJson, new TypeReference<>() {
        });
        borrowingDetailsService.deleteAll();
        bookService.deleteAll();
        memberService.deleteAll();
        bookService.saveAll(books);
        memberService.saveAll(List.of(MembersFixture.resident(), MembersFixture.newStudent(), MembersFixture.student()));

    }

    @Test
    void member_can_borrow_a_book_if_book_is_available() {
        Book availableBook = bookService.findOneByQuantityEquals(1L);
        assertThat(library.borrowBook(availableBook.getIsbn().isbnCode(), MembersFixture.residentUuid().get(), LocalDate.now())).isInstanceOf(BorrowingDetails.class);
        assertThatThrownBy(() -> library.borrowBook(availableBook.getIsbn().isbnCode(), MembersFixture.residentUuid().get(), LocalDate.now())).isExactlyInstanceOf(BookAlreadyBorrowedException.class);
        assertThatThrownBy(() -> library.borrowBook(availableBook.getIsbn().isbnCode(), MembersFixture.studentUuid().get(), LocalDate.now())).isExactlyInstanceOf(InsufficientBooksException.class);

    }

    @Test
    void borrowed_book_is_no_longer_available() {
        Book availableBook = bookService.findOneByQuantityEquals(0L);
        assertThatThrownBy(() -> library.borrowBook(availableBook.getIsbn().isbnCode(), MembersFixture.residentUuid().get(), LocalDate.now())).isExactlyInstanceOf(InsufficientBooksException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 60})
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book(int days) {
        assertThat(days).isLessThan(61);
        LocalDate borrowedAt = LocalDate.now().minusDays(days);
        UUID residentId = MembersFixture.residentUuid().get();
        Member resident = memberService.findById(residentId);
        float initialWallet = resident.getWallet();
        Book availableBook = bookService.findAvailableBook();
        library.borrowBook(availableBook.getIsbn().isbnCode(), residentId, borrowedAt);
        library.returnBook(availableBook.getIsbn().isbnCode(), residentId);
        float wallet = memberService.findWalletById(residentId);
        assertThat(wallet).isEqualTo(initialWallet - days * 0.1F);

    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 30})
    void students_pay_10_cents_the_first_30days(int days) {
        assertThat(days).isLessThan(31);
        LocalDate borrowedAt = LocalDate.now().minusDays(days);
        UUID studentId = MembersFixture.studentUuid().get();
        Member student = memberService.findById(studentId);
        float initialWallet = student.getWallet();
        Book availableBook = bookService.findAvailableBook();
        library.borrowBook(availableBook.getIsbn().isbnCode(), studentId, borrowedAt);
        library.returnBook(availableBook.getIsbn().isbnCode(), studentId);
        float wallet = memberService.findWalletById(studentId);
        assertThat(wallet).isEqualTo(initialWallet - days * 0.1F);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20, 60})
    void students_in_1st_year_are_not_taxed_for_the_first_15days(int days) {
        int freeDays = 15;
        LocalDate borrowedAt = LocalDate.now().minusDays(days);
        UUID studentId = MembersFixture.newStudentUuid().get();
        Member student = memberService.findById(studentId);
        float initialWallet = student.getWallet();
        Book availableBook = bookService.findAvailableBook();
        library.borrowBook(availableBook.getIsbn().isbnCode(), studentId, borrowedAt);
        library.returnBook(availableBook.getIsbn().isbnCode(), studentId);
        float wallet = memberService.findWalletById(studentId);
        float expected = days > freeDays ? initialWallet - ((days - freeDays) * 0.1F) : initialWallet;
        assertThat(wallet).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {65, 70})
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(int days) {
        assertThat(days).isGreaterThan(RESIDENT_LATENESS_IN_DAYS);
        LocalDate borrowedAt = LocalDate.now().minusDays(days);
        UUID residentId = MembersFixture.residentUuid().get();
        Member resident = memberService.findById(residentId);
        float initialWallet = resident.getWallet();
        Book availableBook = bookService.findAvailableBook();
        library.borrowBook(availableBook.getIsbn().isbnCode(), residentId, borrowedAt);
        library.returnBook(availableBook.getIsbn().isbnCode(), residentId);
        float wallet = memberService.findWalletById(residentId);
        assertThat(wallet).isEqualTo(initialWallet - (RESIDENT_LATENESS_IN_DAYS * 0.1F) - ((days - RESIDENT_LATENESS_IN_DAYS) * 0.2F));
    }

    @Test
    void members_cannot_borrow_book_if_they_have_late_books() {
        Book availableBook = bookService.findOneByQuantityEquals(2L);
        Book anotherAvailable = bookService.findOneByQuantityEquals(1L);

        UUID residentId = MembersFixture.residentUuid().get();
        UUID studentId = MembersFixture.studentUuid().get();

        assertThat(library.borrowBook(availableBook.getIsbn().isbnCode(), residentId, LocalDate.now().minusDays(RESIDENT_LATENESS_IN_DAYS + 1))).isInstanceOf(BorrowingDetails.class);
        assertThat(library.borrowBook(availableBook.getIsbn().isbnCode(), studentId, LocalDate.now().minusDays(STUDENT_LATENESS_IN_DAYS + 1))).isInstanceOf(BorrowingDetails.class);


        assertThatThrownBy(() -> library.borrowBook(anotherAvailable.getIsbn().isbnCode(), residentId, LocalDate.now()))
                .isInstanceOf(HasLateBooksException.class);

        assertThatThrownBy(() -> library.borrowBook(anotherAvailable.getIsbn().isbnCode(), studentId, LocalDate.now()))
                .isInstanceOf(HasLateBooksException.class);

    }
}
