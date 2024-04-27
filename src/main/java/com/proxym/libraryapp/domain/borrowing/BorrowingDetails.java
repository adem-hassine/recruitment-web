package com.proxym.libraryapp.domain.borrowing;

import com.proxym.libraryapp.domain.book.ISBN;
import com.proxym.libraryapp.domain.member.MemberId;
import com.proxym.libraryapp.infrastructure.error.assertions.Assert;

import java.time.LocalDate;
import java.util.UUID;

public class BorrowingDetails {

    private BorrowingDetailsId id;
    private LocalDate borrowedAt;
    private LocalDate returnedAt;
    private MemberId borrowerId;
    private ISBN bookId;

    private BorrowingDetails(BorrowingDetailsBuilder builder) {
        assertMandatoryFields(builder);
        id = builder.id;
        borrowedAt = builder.borrowedAt;
        bookId = builder.bookId;
        borrowerId = builder.borrowerId;
    }

    private void assertMandatoryFields(BorrowingDetailsBuilder builder) {
        Assert.notNull("borrowedAt", builder.borrowedAt);
        Assert.notNull("paidDaysNumber", builder.id);
        Assert.notNull("borrowerId", builder.borrowerId);
        Assert.notNull("bookId", builder.bookId);
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public BorrowingDetailsId getId() {
        return this.id;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }

    public MemberId getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(MemberId borrowerId) {
        this.borrowerId = borrowerId;
    }

    public ISBN getBookId() {
        return bookId;
    }

    public void setBookId(ISBN bookId) {
        this.bookId = bookId;
    }

    public boolean isActive() {
        return returnedAt == null;
    }

    public static BorrowingDetailsIdBuilder builder() {
        return new BorrowingDetailsBuilder();
    }

    private static class BorrowingDetailsBuilder
            implements BorrowingDetailsIdBuilder, DetailsBookIdBuilder, DetailsMemberIdBuilder, DetailsBorrowedAtBuilder, DetailsOptionalBuilder {
        private BorrowingDetailsId id;
        private LocalDate borrowedAt;
        private MemberId borrowerId;
        private ISBN bookId;

        @Override
        public DetailsBookIdBuilder id(BorrowingDetailsId id) {
            this.id = id;
            return this;
        }

        @Override
        public DetailsMemberIdBuilder isbn(ISBN isbn) {
            this.bookId = isbn;
            return this;
        }

        @Override
        public DetailsBorrowedAtBuilder memberId(MemberId memberId) {
            this.borrowerId = memberId;
            return this;
        }

        @Override
        public DetailsOptionalBuilder borrowedAt(LocalDate borrowedAt) {
            this.borrowedAt = borrowedAt;
            return this;
        }

        @Override
        public BorrowingDetails build() {
            return new BorrowingDetails(this);
        }


    }

    public interface BorrowingDetailsIdBuilder {
        DetailsBookIdBuilder id(BorrowingDetailsId id);

        default DetailsBookIdBuilder id(UUID id) {
            return id(new BorrowingDetailsId(id));
        }
    }

    public interface DetailsBookIdBuilder {
        DetailsMemberIdBuilder isbn(ISBN isbn);

        default DetailsMemberIdBuilder isbn(long isbn) {
            return isbn(new ISBN(isbn));
        }
    }

    public interface DetailsMemberIdBuilder {
        DetailsBorrowedAtBuilder memberId(MemberId memberId);

        default DetailsBorrowedAtBuilder memberId(UUID memberId) {
            return memberId(new MemberId(memberId));
        }

    }

    public interface DetailsBorrowedAtBuilder {
        DetailsOptionalBuilder borrowedAt(LocalDate borrowedAt);
    }

    public interface DetailsOptionalBuilder {
        BorrowingDetails build();
    }
}
