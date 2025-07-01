package geonwoo.practice.base;

import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.dto.MemberUpdateDto;
import geonwoo.practice.base.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService service;

    @Test
    public void 회원가입(){
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);

        //when
        Member savedMember = service.addNewMember(member);

        //then
        assertThat(savedMember.getLoginId()).isEqualTo("asdf");
    }

    @Test
    public void 멤버찾기() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = service.addNewMember(member);

        //when
        Optional<Member> findMember = service.searchMemberById(savedMember.getId());

        //then
        assertThat(findMember.orElseThrow().getLoginId()).isEqualTo("asdf");
    }

    @Test
    public void 로그인아이디로멤버찾기() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = service.addNewMember(member);

        //when
        Optional<Member> findMember = service.searchMemberByLoginId(savedMember.getLoginId());

        //then
        assertThat(findMember.orElseThrow().getLoginId()).isEqualTo("asdf");
    }

    @Test
    public void 모든멤버찾기() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);

        //when
        service.addNewMember(member1);
        service.addNewMember(member2);
        service.addNewMember(member3);

        //then
        assertThat(service.searchAllMembers().size()).isEqualTo(3);
    }

    @Test
    public void 이름으로멤버검색() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);

        //when
        service.addNewMember(member1);
        service.addNewMember(member2);
        service.addNewMember(member3);

        //then
        assertThat(service.searchMembersByName("Kim").size()).isEqualTo(1);
        assertThat(service.searchMembersByName("Gildong").size()).isEqualTo(2);
    }

    //update는 serviceTest에서만 가능. 생략

    @Test
    public void 멤버수정() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = service.addNewMember(member);
        MemberUpdateDto updateParams = new MemberUpdateDto(
                member.getLoginId(),
                member.getPassword(),
                member.getName(),
                50
        );

        //when
        service.updateMemberById(member.getId(), updateParams);

        //then
        assertThat(service.searchMemberById(member.getId()).orElseThrow().getAge())
                .isEqualTo(50);
    }

    @Test
    public void 로그인아이디로멤버수정() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = service.addNewMember(member);
        MemberUpdateDto updateParams = new MemberUpdateDto(
                member.getLoginId(),
                member.getPassword(),
                member.getName(),
                50
        );

        //when
        service.updateMemberByLoginId(member.getLoginId(), updateParams);

        //then
        assertThat(service.searchMemberById(member.getId()).orElseThrow().getAge())
                .isEqualTo(50);
    }

    @Test
    public void 멤버삭제() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = service.addNewMember(member);

        //when
        service.deleteMemberById(member.getId());

        //then
        assertThatThrownBy(() -> service.searchMemberById(savedMember.getId()).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 로그인아이디로멤버삭제() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = service.addNewMember(member);

        //when
        service.deleteMemberByLoginId(member.getLoginId());

        //then
        assertThatThrownBy(() -> service.searchMemberById(savedMember.getId()).orElseThrow())
                .isInstanceOf(NoSuchElementException.class);
    }
}
