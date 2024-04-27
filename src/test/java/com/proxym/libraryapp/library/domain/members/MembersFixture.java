package com.proxym.libraryapp.library.domain.members;

import com.proxym.libraryapp.domain.member.MemberId;
import com.proxym.libraryapp.domain.member.ResidentMember;
import com.proxym.libraryapp.domain.member.StudentMember;

import java.time.LocalDate;
import java.util.UUID;

public class MembersFixture {
    public static ResidentMember resident() {
        return new ResidentMember(500, residentUuid());
    }

    public static StudentMember newStudent() {
        return new StudentMember(500, LocalDate.now(), newStudentUuid());
    }

    public static StudentMember student() {
        return new StudentMember(500, LocalDate.now().minusYears(2), studentUuid());
    }

    public static MemberId residentUuid() {
        return new MemberId(UUID.fromString("5ea9bbb1-3a55-4701-9006-3bbd2878f241"));
    }

    public static MemberId newStudentUuid() {
        return new MemberId(UUID.fromString("5ea9bbb1-3a56-4701-9006-3bbd2878f241"));
    }

    public static MemberId studentUuid() {
        return new MemberId(UUID.fromString("5ea9bbb1-3a57-4701-9006-3bbd2878f241"));
    }
}
