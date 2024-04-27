package com.proxym.libraryapp.domain.member;


import static com.proxym.libraryapp.domain.borrowing.MemberLatenessHandler.RESIDENT_LATENESS_IN_DAYS;

public final class ResidentMember extends Member {
    private static final float CHARGE_PER_DAY = 0.1F;
    private static final float LATE_MEMBER_CHARGE_PER_DAY = 0.2F;


    public ResidentMember(float wallet, MemberId uuid) {
        super(wallet, uuid);
    }

    @Override
    protected void shouldPay(int numberOfDays) {
        float toBePaid = numberOfDays > RESIDENT_LATENESS_IN_DAYS ?
                ((numberOfDays - RESIDENT_LATENESS_IN_DAYS) * LATE_MEMBER_CHARGE_PER_DAY) + (RESIDENT_LATENESS_IN_DAYS * CHARGE_PER_DAY) :
                numberOfDays * CHARGE_PER_DAY;
        this.setWallet(getWallet() - toBePaid);
    }

}
