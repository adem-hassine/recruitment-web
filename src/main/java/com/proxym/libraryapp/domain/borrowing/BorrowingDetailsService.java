package com.proxym.libraryapp.domain.borrowing;

import com.proxym.libraryapp.domain.book.BookService;
import com.proxym.libraryapp.domain.book.ISBN;
import com.proxym.libraryapp.domain.member.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BorrowingDetailsService {
    private final BorrowingDetailsRepository borrowingDetailsRepository;
    private final MemberService memberService;
    private final BookService bookService;

    public BorrowingDetailsService(BorrowingDetailsRepository borrowingDetailsRepository, MemberService memberService, BookService bookService) {
        this.borrowingDetailsRepository = borrowingDetailsRepository;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    public boolean isBookAlreadyBorrowed(UUID memberId, long isbnCode) {
        return borrowingDetailsRepository.isBookAlreadyBorrowed(new MemberId(memberId), new ISBN(isbnCode));
    }

    public boolean isAvailableBook(Long isbn) {
        long quantity = bookService.findQuantity(isbn);
        return quantity - borrowingDetailsRepository.countActiveBorrowed(new ISBN(isbn)) > 0L;
    }

    public boolean memberIsLate(UUID memberId) {
        List<BorrowingDetails> borrowerDetails = borrowingDetailsRepository.findBorrowerDetails(new MemberId(memberId));
        Member member = memberService.findById(memberId);
        return handle(new MemberActiveBorrowsLatenessHandler(), member, borrowerDetails);
    }

    public boolean memberIsLate(Member member) {
        List<BorrowingDetails> borrowerDetails = borrowingDetailsRepository.findBorrowerDetails(member.getId());
        return handle(new MemberActiveBorrowsLatenessHandler(), member, borrowerDetails);
    }

    public boolean memberWasLate(UUID memberId) {
        List<BorrowingDetails> borrowerDetails = borrowingDetailsRepository.findBorrowerDetails(new MemberId(memberId));
        Member member = memberService.findById(memberId);
        return handle(new MemberActiveBorrowsLatenessHandler(), member, borrowerDetails);
    }

    public boolean memberWasLate(Member member) {
        List<BorrowingDetails> borrowerDetails = borrowingDetailsRepository.findBorrowerDetails(member.getId());
        return handle(new MemberBorrowsLatenessHandler(), member, borrowerDetails);
    }

    private boolean handle(MemberLatenessHandler handler, Member member, List<BorrowingDetails> borrowerDetails) {
        boolean result;
        switch (member) {
            case ResidentMember resident -> result = handler.wasLate(resident, borrowerDetails);
            case StudentMember student -> result = handler.wasLate(student, borrowerDetails);
        }
        return result;
    }

    public void save(BorrowingDetails borrowingDetails) {
        this.borrowingDetailsRepository.save(borrowingDetails);
    }

    public BorrowingDetails findOne(Long isbn, UUID memberId, boolean active) {
        return borrowingDetailsRepository.findOne(new ISBN(isbn), new MemberId(memberId), active);
    }

    public void update(BorrowingDetails one) {
        borrowingDetailsRepository.update(one);
    }

    public void deleteAll() {
        borrowingDetailsRepository.deleteAll();
    }
    public List<BorrowingDetails> findAll(){
        return borrowingDetailsRepository.findAll();
    }
}
