package geonwoo.practice.base;

import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.repository.MemberDynamicQueryRepository;
import geonwoo.practice.base.repository.MemberRepository;
import geonwoo.practice.base.repository.PostDynamicQueryRepository;
import geonwoo.practice.base.repository.PostRepository;
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
public class PostRepositoryTest {
    @Autowired PostRepository repository;
    @Autowired PostDynamicQueryRepository dynamicRepository;

    @Autowired MemberRepository memberRepository;

    @Test
    public void create() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);

        //when
        Member savedMember = memberRepository.save(member);
        Post savedPost = repository.save(post);

        //then
        assertThat(savedPost.getTitle()).isEqualTo("hello");
    }

    @Test
    public void read() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        memberRepository.save(member);
        Post savedPost = repository.save(post);

        //when
        Optional<Post> findPost = repository.findById(savedPost.getId());
        Optional<Post> noSuchPost = repository.findById(-1L);

        //then
        assertThat(findPost.orElseThrow().getTitle()).isEqualTo("hello");
        assertThatThrownBy(noSuchPost::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readAll() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);
        Post post1 = new Post("asdf", "1234", member1);
        Post post2 = new Post("asdfa", "12345", member2);
        Post post3 = new Post("asdfas", "123456", member3);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        repository.save(post1);
        repository.save(post2);
        repository.save(post3);

        //then
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    public void readBySearchText() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);
        Post post1 = new Post("asdf", "1234", member1);
        Post post2 = new Post("asdfa", "12345", member2);
        Post post3 = new Post("asdfas", "123456", member3);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        repository.save(post1);
        repository.save(post2);
        repository.save(post3);

        //then
        assertThat(dynamicRepository.findBySearchText("Kim").size()).isEqualTo(1);
        assertThat(dynamicRepository.findBySearchText("Gildong").size()).isEqualTo(2);
        assertThat(dynamicRepository.findBySearchText("6").size()).isEqualTo(1);
        assertThat(dynamicRepository.findBySearchText("asd").size()).isEqualTo(3);
    }

    //update는 serviceTest에서만 가능. 생략

    @Test
    public void delete() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        memberRepository.save(member);
        Post savedPost = repository.save(post);

        //when
        repository.delete(post);
        Optional<Post> findPost = repository.findById(savedPost.getId());

        //then
        assertThatThrownBy(findPost::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
