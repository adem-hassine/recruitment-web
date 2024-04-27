package com.proxym.libraryapp.domain.borrowing;

import com.proxym.libraryapp.domain.member.ResidentMember;
import com.proxym.libraryapp.domain.member.StudentMember;

import java.util.List;

public interface MemberLatenessHandler {
    Integer RESIDENT_LATENESS_IN_DAYS = 60;
    Integer STUDENT_LATENESS_IN_DAYS = 30;

    boolean wasLate(ResidentMember member, List<BorrowingDetails> borrowingDetails);

    boolean wasLate(StudentMember member, List<BorrowingDetails> borrowingDetails);
}
