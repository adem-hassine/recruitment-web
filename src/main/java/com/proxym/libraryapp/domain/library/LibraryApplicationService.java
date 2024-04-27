package com.proxym.libraryapp.domain.library;

import com.proxym.libraryapp.domain.book.BookService;
import com.proxym.libraryapp.domain.borrowing.BorrowingDetails;
import com.proxym.libraryapp.domain.borrowing.BorrowingDetailsService;
import com.proxym.libraryapp.domain.member.Member;
import com.proxym.libraryapp.domain.member.MemberService;
import com.proxym.libraryapp.infrastructure.error.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class LibraryApplicationService implements Library {
    private final Logger log = LoggerFactory.getLogger(LibraryApplicationService.class);

    private final MemberService memberService;
    private final BookService bookService;
    private final BorrowingDetailsService borrowingDetailsService;

    public LibraryApplicationService(MemberService memberService, BookService bookService, BorrowingDetailsService borrowingDetailsService) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.borrowingDetailsService = borrowingDetailsService;
    }

    @Override
    public BorrowingDetails borrowBook(Long isbnCode, UUID memberId, LocalDate borrowedAt) throws HasLateBooksException {
        if (borrowingDetailsService.isBookAlreadyBorrowed(memberId, isbnCode)) {
            throw new BookAlreadyBorrowedException("Book already borrowed by member " + memberId);
        }
        if (!borrowingDetailsService.isAvailableBook(isbnCode)) {
            throw new InsufficientBooksException("There is no insufficient books to be borrowed for given isbn " + isbnCode);
        }
        if (borrowingDetailsService.memberIsLate(memberId)) {
            throw new HasLateBooksException();
        }
        if (!memberService.existsById(memberId)) {
            throw new DataNotFoundException(String.format("Unable to locate member %s", memberId));
        }
        if (!bookService.existsById(isbnCode)) {
            throw new DataNotFoundException(String.format("Unable to locate book %s", isbnCode));
        }
        BorrowingDetails borrowingDetails = BorrowingDetails.builder()
                .id(UUID.randomUUID())
                .isbn(isbnCode)
                .memberId(memberId)
                .borrowedAt(borrowedAt)
                .build();
        borrowingDetailsService.save(borrowingDetails);
        log.info("Book with {} successfully borrowed to member {}", isbnCode, memberId);
        return borrowingDetails;
    }

    @Override
    public void returnBook(Long isbn, UUID memberId) {
        BorrowingDetails one = borrowingDetailsService.findOne(isbn, memberId, true);
        LocalDate now = LocalDate.now();
        int daysToPay = (int) ChronoUnit.DAYS.between(one.getBorrowedAt(), now);
        Member member = memberService.findById(memberId);
        member.payBook(daysToPay);
        memberService.update(member);
        one.setReturnedAt(now);
        borrowingDetailsService.update(one);
    }
}
