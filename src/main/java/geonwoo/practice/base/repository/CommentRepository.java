package geonwoo.practice.base.repository;

import geonwoo.practice.base.domain.Comments;
import geonwoo.practice.base.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    //save
    //findById
    //findAll
    //delete
    List<Comments> findByPost(Post post);
}
