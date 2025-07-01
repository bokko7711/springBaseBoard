package geonwoo.practice.base.controller;

import geonwoo.practice.base.Constants;
import geonwoo.practice.base.domain.Comment;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.domain.UploadFile;
import geonwoo.practice.base.dto.CommentResponseDto;
import geonwoo.practice.base.dto.CommentUpdateDto;
import geonwoo.practice.base.dto.PostResponseDto;
import geonwoo.practice.base.file.FileStore;
import geonwoo.practice.base.repository.UploadFileRepository;
import geonwoo.practice.base.service.CommentService;
import geonwoo.practice.base.service.MemberService;
import geonwoo.practice.base.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Arrays.stream;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    //dependency injected
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    private final FileStore fileStore;
    private final UploadFileRepository uploadFileRepository;

    @GetMapping("/profile")
    public Member profile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long id = (Long) session.getAttribute(Constants.MEMBER_ID);
            return memberService.searchMemberById(id).orElseThrow();
        } else {
            return new Member(); //default value member instance
        }
    }

    @GetMapping("/post/all")
    public List<PostResponseDto> searchAllPosts() {
        return postService.searchAllPosts().stream()
                .map(post -> new PostResponseDto(post)).toList();
    }

    @GetMapping("/post/query")
    public List<PostResponseDto> searchConditionPosts(
            @RequestParam("condition") String condition
    ) {
        return postService.searchPostsByTextInput(condition).stream()
                .map(post -> new PostResponseDto(post)).toList();
    }

    @GetMapping("/post/{postId}")
    public PostResponseDto post(@PathVariable("postId") Long id) {
        return new PostResponseDto(
                postService.searchPostById(id).orElseThrow());
    }

    @GetMapping("/comments/{postId}")
    public List<CommentResponseDto> comments(@PathVariable("postId") Long id) {
        Post post = postService.searchPostById(id).orElseThrow();
        return commentService.searchCommentsByPost(post).stream()
                .map(comment -> new CommentResponseDto(comment)).toList();
    }

    @PostMapping("/comment/new/{postId}")
    public String addNewComment(@Valid @ModelAttribute Comment comment,
                             BindingResult bindingResult,
                                @PathVariable("postId") Long postId,
                             HttpServletRequest request) {
        if (bindingResult.hasErrors()) { return request.getHeader("referer"); }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long memberId = (Long) session.getAttribute(Constants.MEMBER_ID);
            Member author = memberService.searchMemberById(memberId).orElseThrow();
            comment.setAuthor(author);
            Post post = postService.searchPostById(postId).orElseThrow();
            comment.setPost(post);
            commentService.addNewComment(comment);
        } else {
            log.info("cannot add comment : there is no author or post");
        }

        return "comment add success";
    }

    @PostMapping("/comment/edit/{commentId}")
    public String editComment(@Valid @RequestBody CommentUpdateDto updateParam,
                              BindingResult bindingResult,
                              @PathVariable("commentId") Long commentId,
                              HttpServletRequest request) {
        if (bindingResult.hasErrors()) { return request.getHeader("referer"); }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long memberId = (Long) session.getAttribute(Constants.MEMBER_ID);
            Member currentUser = memberService.searchMemberById(memberId).orElseThrow();
            Comment comment = commentService.searchCommentById(commentId).orElseThrow();
            if (currentUser == comment.getAuthor()) {
                commentService.updateCommentById(commentId, updateParam);
            } else {
                log.warn("cannot update : comment cannot be updated by wrong access (not author)");
            }
        } else {
            log.info("cannot add comment : there is no author or post");
        }

        return "comment add success";
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId,
                             HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long memberId = (Long) session.getAttribute(Constants.MEMBER_ID);
            Member currentUser = memberService.searchMemberById(memberId).orElseThrow();
            Comment comment = commentService.searchCommentById(commentId).orElseThrow();
            if (currentUser == comment.getAuthor()) {
                commentService.deleteCommentById(commentId);
            } else {
                log.warn("cannot delete : comment cannot be deleted by wrong access (not author)");
            }
        } else {
            log.info("cannot delete : there is no author");
        }

        return "comment delete success";
    }

    @GetMapping("/attach/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) throws MalformedURLException {
        UploadFile file = uploadFileRepository.findById(fileId).orElseThrow();
        String storeFileName = file.getStoreFileName();
        String uploadFileName = file.getUploadFileName();
        UrlResource resource = new UrlResource("file:" +
                fileStore.getFullPath(storeFileName));
        String encodedUploadFileName = UriUtils.encode(uploadFileName,
                StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" +
                encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
