package geonwoo.practice.base;

import geonwoo.practice.base.domain.Comment;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.dto.CommentUpdateDto;
import geonwoo.practice.base.dto.PostUpdateDto;
import geonwoo.practice.base.repository.MemberRepository;
import geonwoo.practice.base.repository.PostRepository;
import geonwoo.practice.base.service.CommentService;
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
public class CommentServiceTest {
    @Autowired CommentService service;
    @Autowired PostRepository postRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 댓글쓰기() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comment comment = new Comment("good", member, post);

        //when
        Member savedMember = memberRepository.save(member);
        Post savedPost = postRepository.save(post);
        Comment savedComment = service.addNewComment(comment);

        //then
        assertThat(savedComment.getContent()).isEqualTo("good");
    }

    @Test
    public void 댓글하나조회() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comment comment = new Comment("good", member, post);
        Member savedMember = memberRepository.save(member);
        Post savedPost = postRepository.save(post);
        Comment savedComment = service.addNewComment(comment);

        //when
        Optional<Comment> findComment = service.searchCommentById(savedComment.getId());
        Optional<Comment> noSuchComment = service.searchCommentById(-1L);

        //then
        assertThat(findComment.orElseThrow().getContent()).isEqualTo("good");
        assertThatThrownBy(noSuchComment::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 포스트의모든댓글조회() {
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
        service.addNewComment(comment1);
        service.addNewComment(comment2);
        service.addNewComment(comment3);

        //then
        assertThat(service.searchCommentsByPost(post1).size()).isEqualTo(2);
        assertThat(service.searchCommentsByPost(post2).size()).isEqualTo(1);
    }

    @Test
    public void 댓글수정() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comment comment = new Comment("good", member, post);
        Member savedMember = memberRepository.save(member);
        Post savedPost = postRepository.save(post);
        Comment savedComment = service.addNewComment(comment);
        CommentUpdateDto updateParam = new CommentUpdateDto("better");

        //when
        service.updateCommentById(comment.getId(),updateParam);

        //then
        assertThat(service.searchCommentById(comment.getId()).orElseThrow().getContent())
                .isEqualTo("better");
    }

    @Test
    public void 댓글삭제() {
        //given
        Member member = new Member("asdf", "1234", "Gildong Hong", 20);
        Post post = new Post("hello", "my name is Gildong Hong", member);
        Comment comment = new Comment("good", member, post);
        memberRepository.save(member);
        postRepository.save(post);
        Comment savedComment = service.addNewComment(comment);

        //when
        service.deleteCommentById(comment.getId());
        Optional<Comment> findComment = service.searchCommentById(savedComment.getId());

        //then
        assertThatThrownBy(findComment::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }
}
