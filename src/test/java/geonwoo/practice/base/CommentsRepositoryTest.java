package geonwoo.practice.base;

import geonwoo.practice.base.domain.Comments;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.repository.CommentRepository;
import geonwoo.practice.base.repository.MemberRepository;
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
public class CommentsRepositoryTest {

    @Autowired CommentRepository repository;
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    public void create() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comments comments = new Comments("good", member, post);

        //when
        Member savedMember = memberRepository.save(member);
        Post savedPost = postRepository.save(post);
        Comments savedComments = repository.save(comments);

        //then
        assertThat(savedComments.getContent()).isEqualTo("good");
    }

    @Test
    public void read() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comments comments = new Comments("good", member, post);
        memberRepository.save(member);
        postRepository.save(post);
        Comments savedComments = repository.save(comments);

        //when
        Optional<Comments> findComment = repository.findById(savedComments.getId());
        Optional<Comments> noSuchComment = repository.findById(-1L);

        //then
        assertThat(findComment.orElseThrow().getContent()).isEqualTo("good");
        assertThatThrownBy(noSuchComment::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readByPost() {
        //given
        Member member1 = new Member("asdf", "1234", "Gildong Hong", 20);
        Member member2 = new Member("asdfa", "12345", "Gildong Kim", 30);
        Member member3 = new Member("asdfas", "123456", "Baksa Hong", 40);
        Post post1 = new Post("asdf", "1234", member1);
        Post post2 = new Post("asdfa", "12345", member2);
        Post post3 = new Post("asdfas", "123456", member3);
        Comments comments1 = new Comments("good", member2, post1);
        Comments comments2 = new Comments("welcome", member1, post1);
        Comments comments3 = new Comments("blah blah", member1, post2);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        repository.save(comments1);
        repository.save(comments2);
        repository.save(comments3);

        //then
        assertThat(repository.findByPost(post1).size()).isEqualTo(2);
        assertThat(repository.findByPost(post2).size()).isEqualTo(1);
    }

    //update는 serviceTest에서만 가능. 생략

    @Test
    public void delete() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comments comments = new Comments("good", member, post);
        memberRepository.save(member);
        postRepository.save(post);
        Comments savedComments = repository.save(comments);

        //when
        repository.delete(comments);
        Optional<Comments> findComment = repository.findById(savedComments.getId());

        //then
        assertThatThrownBy(findComment::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
