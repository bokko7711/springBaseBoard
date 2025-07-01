package geonwoo.practice.base.repository;

import geonwoo.practice.base.domain.Comment;
import geonwoo.practice.base.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //save
    //findById
    //findAll
    //delete
    List<Comment> findByPost(Post post);
}
