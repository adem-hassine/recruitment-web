package com.proxym.libraryapp.domain.borrowing;

import com.proxym.libraryapp.domain.member.ResidentMember;
import com.proxym.libraryapp.domain.member.StudentMember;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MemberBorrowsLatenessHandler implements MemberLatenessHandler{
    @Override
    public boolean wasLate(ResidentMember member, List<BorrowingDetails> borrowingDetails) {
        LocalDate now = LocalDate.now();
        return borrowingDetails.stream().anyMatch(val -> ChronoUnit.DAYS.between(val.getBorrowedAt(), now) > RESIDENT_LATENESS_IN_DAYS);
    }

    @Override
    public boolean wasLate(StudentMember member, List<BorrowingDetails> borrowingDetails) {
        LocalDate now = LocalDate.now();
        return borrowingDetails.stream().filter(BorrowingDetails::isActive)
                .anyMatch(val -> ChronoUnit.DAYS.between(val.getBorrowedAt(), now) > STUDENT_LATENESS_IN_DAYS);
    }
}
