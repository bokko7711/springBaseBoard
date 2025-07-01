package geonwoo.practice.base;

import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.dto.MemberUpdateDto;
import geonwoo.practice.base.dto.PostUpdateDto;
import geonwoo.practice.base.repository.MemberRepository;
import geonwoo.practice.base.service.PostService;
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
public class PostServiceTest {
    @Autowired PostService service;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 포스트등록() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);

        //when
        Member savedMember = memberRepository.save(member);
        Post savedPost = service.addNewPost(post);

        //then
        assertThat(savedPost.getTitle()).isEqualTo("hello");
    }

    @Test
    public void 포스트조회() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        memberRepository.save(member);
        Post savedPost = service.addNewPost(post);

        //when
        Optional<Post> findPost = service.searchPostById(savedPost.getId());
        Optional<Post> noSuchPost = service.searchPostById(-1L);

        //then
        assertThat(findPost.orElseThrow().getTitle()).isEqualTo("hello");
        assertThatThrownBy(noSuchPost::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 모든포스트조회() {
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
        service.addNewPost(post1);
        service.addNewPost(post2);
        service.addNewPost(post3);

        //then
        assertThat(service.searchAllPosts().size()).isEqualTo(3);
    }

    @Test
    public void 포스트검색() {
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
        service.addNewPost(post1);
        service.addNewPost(post2);
        service.addNewPost(post3);

        //then
        assertThat(service.searchPostsByTextInput("Kim").size()).isEqualTo(1);
        assertThat(service.searchPostsByTextInput("Gildong").size()).isEqualTo(2);
        assertThat(service.searchPostsByTextInput("6").size()).isEqualTo(1);
        assertThat(service.searchPostsByTextInput("asd").size()).isEqualTo(3);
    }

    //update는 serviceTest에서만 가능. 생략
    @Test
    public void 포스트수정() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        memberRepository.save(member);
        Post savedPost = service.addNewPost(post);
        PostUpdateDto updateParams = new PostUpdateDto(
                "hello world",
                post.getContent()
        );

        //when
        service.updatePostById(post.getId(), updateParams);

        //then
        assertThat(service.searchPostById(post.getId()).orElseThrow().getTitle())
                .isEqualTo("hello world");
    }

    @Test
    public void 포스트삭제() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        memberRepository.save(member);
        Post savedPost = service.addNewPost(post);

        //when
        service.deletePostById(savedPost.getId());
        Optional<Post> findPost = service.searchPostById(savedPost.getId());

        //then
        assertThatThrownBy(findPost::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
