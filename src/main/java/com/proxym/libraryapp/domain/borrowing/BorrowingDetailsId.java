package com.proxym.libraryapp.domain.borrowing;

import com.proxym.libraryapp.infrastructure.error.assertions.Assert;

import java.util.UUID;

public record BorrowingDetailsId(UUID id) {

    public BorrowingDetailsId {
        Assert.notNull("id", id);
    }
}
