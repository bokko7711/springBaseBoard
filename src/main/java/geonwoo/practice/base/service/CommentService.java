package geonwoo.practice.base.service;

import geonwoo.practice.base.domain.Comment;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.dto.CommentUpdateDto;
import geonwoo.practice.base.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;

    public Comment addNewComment(Comment comment) {
        return repository.save(comment);
    }

    public Optional<Comment> searchCommentById(Long id) {
        return repository.findById(id);
    }

    public List<Comment> searchCommentsByPost(Post post) {
        return repository.findByPost(post);
    }

    public void updateCommentById(Long id, CommentUpdateDto updateParam) {
        Comment comment = repository.findById(id).orElseThrow();
        comment.setContent(updateParam.getContent());
    }

    public void deleteCommentById(Long id) {
        Comment comment = repository.findById(id).orElseThrow();
        repository.delete(comment);
    }
}
