package geonwoo.practice.base.service;

import geonwoo.practice.base.domain.Comments;
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

    public Comments addNewComment(Comments comments) {
        return repository.save(comments);
    }

    public Optional<Comments> searchCommentById(Long id) {
        return repository.findById(id);
    }

    public List<Comments> searchCommentsByPost(Post post) {
        return repository.findByPost(post);
    }

    public void updateCommentById(Long id, CommentUpdateDto updateParam) {
        Comments comments = repository.findById(id).orElseThrow();
        comments.setContent(updateParam.getContent());
    }

    public void deleteCommentById(Long id) {
        Comments comments = repository.findById(id).orElseThrow();
        repository.delete(comments);
    }
}
