package com.proxym.libraryapp.domain.member;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class StudentMember extends Member {
    private LocalDate registrationDate;
    private static final float CHARGE_PER_DAY = 0.1F;
    private static final Integer FREE_DAYS_FOR_FIRST_YEAR = 15;

    public StudentMember(float wallet, LocalDate registrationDate,MemberId id) {
        super(wallet,id);
        this.registrationDate = registrationDate;
    }

    @Override
    protected void shouldPay(int numberOfDays) {
        LocalDate now = LocalDate.now();
        int shouldBePaidDays = numberOfDays;
        if (ChronoUnit.YEARS.between(registrationDate, now) < 1L) {
            shouldBePaidDays = shouldBePaidDays - FREE_DAYS_FOR_FIRST_YEAR;
        }
        if (shouldBePaidDays > 0) {
            this.setWallet(getWallet() - shouldBePaidDays * CHARGE_PER_DAY);
        }
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}

