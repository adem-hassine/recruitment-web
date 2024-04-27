package com.proxym.libraryapp.domain.member;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public void saveAll(List<Member> members) {
        memberRepository.saveAll(members);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public Member findById(UUID memberId) {
        return memberRepository.findById(new MemberId(memberId));
    }

    public void update(Member member) {
        memberRepository.update(member);
    }

    public boolean existsById(UUID memberId) {
        return memberRepository.existsById(new MemberId(memberId));
    }

    public float findWalletById(UUID residentId) {
        return memberRepository.findWalletById(new MemberId(residentId));
    }
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public void deleteAll() {
        memberRepository.deleteAll();
    }
}
