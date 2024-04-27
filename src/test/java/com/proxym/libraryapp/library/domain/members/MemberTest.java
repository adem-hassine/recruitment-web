package com.proxym.libraryapp.library.domain.members;


import com.proxym.libraryapp.domain.member.MemberRepository;
import com.proxym.libraryapp.library.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;


@UnitTest
public class MemberTest {

    private MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        memberRepository = new MemberRepository();
    }

    @Test
    void should_save_member() {
        memberRepository.save(MembersFixture.student());
        assertThat(memberRepository.findById(MembersFixture.studentUuid())).usingRecursiveComparison().isEqualTo(MembersFixture.student());
    }

    @Test
    void should_not_exists() {
        assertThat(memberRepository.existsById(MembersFixture.studentUuid())).isEqualTo(false);
    }
}
