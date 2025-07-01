package geonwoo.practice.base.service;

import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.dto.PostUpdateDto;
import geonwoo.practice.base.repository.PostDynamicQueryRepository;
import geonwoo.practice.base.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final PostDynamicQueryRepository dynamicRepository;

    public Post addNewPost(Post post) {
        return repository.save(post);
    }

    public Optional<Post> searchPostById(Long id) {
        return repository.findById(id);
    }

    public List<Post> searchAllPosts() {
        return repository.findAll();
    }

    public List<Post> searchPostsByTextInput(String text) {
        return dynamicRepository.findBySearchText(text);
    }

    public void updatePostById(Long id, PostUpdateDto updateParams) {
        Post post = repository.findById(id).orElseThrow();
        post.setTitle(updateParams.getTitle());
        post.setContent(updateParams.getContent());
        post.setUploadFile(updateParams.getUploadFile());
    }

    public void deletePostById(Long id) {
        Post post = repository.findById(id).orElseThrow();
        repository.delete(post);
    }
}
