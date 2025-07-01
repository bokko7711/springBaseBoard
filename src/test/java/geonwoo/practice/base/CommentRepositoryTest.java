package geonwoo.practice.base;

import geonwoo.practice.base.domain.Comment;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.repository.CommentRepository;
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
public class CommentRepositoryTest {

    @Autowired CommentRepository repository;
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    public void create() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comment comment = new Comment("good", member, post);

        //when
        Member savedMember = memberRepository.save(member);
        Post savedPost = postRepository.save(post);
        Comment savedComment = repository.save(comment);

        //then
        assertThat(savedComment.getContent()).isEqualTo("good");
    }

    @Test
    public void read() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comment comment = new Comment("good", member, post);
        memberRepository.save(member);
        postRepository.save(post);
        Comment savedComment = repository.save(comment);

        //when
        Optional<Comment> findComment = repository.findById(savedComment.getId());
        Optional<Comment> noSuchComment = repository.findById(-1L);

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
        Comment comment1 = new Comment("good", member2, post1);
        Comment comment2 = new Comment("welcome", member1, post1);
        Comment comment3 = new Comment("blah blah", member1, post2);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        repository.save(comment1);
        repository.save(comment2);
        repository.save(comment3);

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
        Comment comment = new Comment("good", member, post);
        memberRepository.save(member);
        postRepository.save(post);
        Comment savedComment = repository.save(comment);

        //when
        repository.delete(comment);
        Optional<Comment> findComment = repository.findById(savedComment.getId());

        //then
        assertThatThrownBy(findComment::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
