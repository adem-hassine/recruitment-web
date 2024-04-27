package com.proxym.libraryapp.domain.member;

import com.proxym.libraryapp.domain.library.Library;
import com.proxym.libraryapp.infrastructure.error.assertions.Assert;

import java.util.UUID;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public sealed abstract class Member permits StudentMember, ResidentMember {
    /**
     * An initial sum of money the member has
     */
    private MemberId id;
    private float wallet;


    public Member(float wallet, MemberId uuid) {
        this.id = uuid;
        this.wallet = wallet;
    }
    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    protected abstract void shouldPay(int numberOfDays);

    public void payBook(int numberOfDays) {
        Assert.field("numberOfDays", numberOfDays).positive();
        this.shouldPay(numberOfDays);
    }

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        Assert.field("wallet", wallet).positive();
        this.wallet = wallet;
    }

    public MemberId getId() {
        return this.id;
    }
}
