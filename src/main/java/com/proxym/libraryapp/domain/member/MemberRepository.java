package com.proxym.libraryapp.domain.member;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemberRepository {
    private Map<MemberId, Member> availableMembers = new ConcurrentHashMap<>();

    public void saveAll(List<Member> members) {
        members.forEach(this::save);
    }

    public void save(Member member) {
        availableMembers.put(member.getId(), member);
    }

    public Member findById(MemberId memberId) {
        return availableMembers.get(memberId);
    }

    public boolean existsById(MemberId memberId) {
        return availableMembers.containsKey(memberId);
    }

    public void update(Member member) {
        availableMembers.put(member.getId(), member);
    }

    public float findWalletById(MemberId residentId) {
        Member one = findById(residentId);
        return one.getWallet();
    }

    public List<Member> findAll() {
        return new ArrayList<>(availableMembers.values());
    }

    public void deleteAll() {
        availableMembers = new ConcurrentHashMap<>();
    }
}
