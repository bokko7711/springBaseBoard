package geonwoo.practice.base.repository;

import geonwoo.practice.base.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    //save
    //findById
    //findAll
    //delete
}
