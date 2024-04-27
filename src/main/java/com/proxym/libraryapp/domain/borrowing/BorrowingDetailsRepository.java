package com.proxym.libraryapp.domain.borrowing;

import com.proxym.libraryapp.domain.book.ISBN;
import com.proxym.libraryapp.domain.member.MemberId;
import com.proxym.libraryapp.infrastructure.error.DataNotFoundException;
import com.proxym.libraryapp.infrastructure.error.assertions.Assert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BorrowingDetailsRepository {
    private Map<BorrowingDetailsId, BorrowingDetails> borrowingDetails = new ConcurrentHashMap<>();

    public void saveAll(List<BorrowingDetails> books) {
        books.forEach(this::save);
    }

    public void save(BorrowingDetails book) {
        borrowingDetails.put(book.getId(), book);
    }

    public BorrowingDetails findById(BorrowingDetailsId borrowingDetailsId) {
        return borrowingDetails.get(borrowingDetailsId);
    }

    public List<BorrowingDetails> findAll() {
        return new ArrayList<>(borrowingDetails.values());
    }

    public List<BorrowingDetails> findBorrowerDetails(MemberId memberId) {
        return borrowingDetails.values().stream().filter(val -> val.getBorrowerId().equals(memberId))
                .toList();
    }

    public boolean isBookAlreadyBorrowed(MemberId memberId, ISBN isbn) {
        return borrowingDetails.values().stream().filter(val -> val.getBorrowerId().equals(memberId))
                .anyMatch(val -> val.getBookId().equals(isbn));
    }

    public long countActiveBorrowed(ISBN isbn) {
        return borrowingDetails.values().stream().filter(val -> val.getBookId().equals(isbn))
                .filter(BorrowingDetails::isActive)
                .count();
    }

    public BorrowingDetails findOne(ISBN isbn, MemberId memberId, boolean active) {
        List<BorrowingDetails> list = borrowingDetails.values().stream()
                .filter(val -> !active || val.isActive())
                .filter(val -> val.getBookId().equals(isbn))
                .filter(val -> val.getBorrowerId().equals(memberId))
                .toList();
        Assert.field("borrowingDetails", list).maxSize(1);
        String flag = active ? "active" : "closed";
        return list.stream().findFirst().orElseThrow(() -> new DataNotFoundException(String.format("Unable to locate %s borrowing details for book with  %s and %s", flag, isbn, memberId)));

    }

    public void update(BorrowingDetails one) {
        borrowingDetails.put(one.getId(), one);
    }

    public void deleteAll() {
        borrowingDetails = new ConcurrentHashMap<>();
    }
}
