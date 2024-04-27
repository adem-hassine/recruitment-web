package com.proxym.libraryapp.domain.member;

import com.proxym.libraryapp.infrastructure.error.assertions.Assert;

import java.util.UUID;

public record MemberId(UUID memberId) {
    public UUID get() {
        return memberId();
    }
    public MemberId {
        Assert.notNull("id", memberId);
    }
}
