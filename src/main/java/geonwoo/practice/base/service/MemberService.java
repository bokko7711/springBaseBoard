package geonwoo.practice.base.service;

import geonwoo.practice.base.dto.MemberUpdateDto;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.repository.MemberDynamicQueryRepository;
import geonwoo.practice.base.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final MemberDynamicQueryRepository dynamicRepository;

    public Member addNewMember(Member member) {
        return repository.save(member);
    }

    public Optional<Member> searchMemberById(Long id) {
        return repository.findById(id);
    }

    public Optional<Member> searchMemberByLoginId(String loginId) {
        return repository.findByLoginId(loginId);
    }

    public List<Member> searchAllMembers() {
        return repository.findAll();
    }

    public List<Member> searchMembersByName(String name) {
        return dynamicRepository.findNameContains(name);
    }

    public void updateMemberById(Long id, MemberUpdateDto updateParams) {
        Member member = repository.findById(id).orElseThrow();
        member.setLoginId(updateParams.getLoginId());
        member.setPassword(updateParams.getPassword());
        member.setName(updateParams.getName());
        member.setAge(updateParams.getAge());
    }

    public void updateMemberByLoginId(String loginId, MemberUpdateDto updateParams) {
        Member member = repository.findByLoginId(loginId).orElseThrow();
        member.setLoginId(updateParams.getLoginId());
        member.setPassword(updateParams.getPassword());
        member.setName(updateParams.getName());
        member.setAge(updateParams.getAge());
    }

    public void deleteMemberById(Long id) {
        Member member = repository.findById(id).orElseThrow();
        repository.delete(member);
    }

    public void deleteMemberByLoginId(String loginId) {
        Member member = repository.findByLoginId(loginId).orElseThrow();
        repository.delete(member);
    }

    public Member login(String id, String password) {
        return repository.findByLoginId(id).filter(member ->
                        member.getPassword().equals(password))
                .orElse(null);
    }
}
