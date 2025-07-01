package geonwoo.practice.base;

import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.repository.MemberDynamicQueryRepository;
import geonwoo.practice.base.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired MemberRepository repository;
    @Autowired MemberDynamicQueryRepository dynamicRepository;

    @Test
    public void create() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);

        //when
        Member savedMember = repository.save(member);

        //then
        assertThat(savedMember.getLoginId()).isEqualTo("asdf");
    }

    @Test
    public void read() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = repository.save(member);

        //when
        Optional<Member> findMember = repository.findById(savedMember.getId());
        Optional<Member> noSuchMember = repository.findById(-1L);

        //then
        assertThat(findMember.orElseThrow().getLoginId()).isEqualTo("asdf");
        assertThatThrownBy(noSuchMember::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readByLoginId() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = repository.save(member);

        //when
        Optional<Member> findMember = repository.findByLoginId(savedMember.getLoginId());

        //then
        assertThat(findMember.orElseThrow().getLoginId()).isEqualTo("asdf");
    }

    @Test
    public void readAll() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);

        //when
        repository.save(member1);
        repository.save(member2);
        repository.save(member3);

        //then
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    public void readNameContains() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);

        //when
        repository.save(member1);
        repository.save(member2);
        repository.save(member3);

        //then
        assertThat(dynamicRepository.findNameContains("Kim").size()).isEqualTo(1);
        assertThat(dynamicRepository.findNameContains("Gildong").size()).isEqualTo(2);
    }

    //update는 serviceTest에서만 가능. 생략

    @Test
    public void delete() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Member savedMember = repository.save(member);

        //when
        repository.delete(member);
        Optional<Member> findMember = repository.findById(savedMember.getId());

        //then
        assertThatThrownBy(findMember::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
