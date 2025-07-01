package geonwoo.practice.base.controller;

import geonwoo.practice.base.Constants;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.domain.UploadFile;
import geonwoo.practice.base.dto.MemberUpdateDto;
import geonwoo.practice.base.dto.PostUpdateDto;
import geonwoo.practice.base.file.FileForm;
import geonwoo.practice.base.file.FileStore;
import geonwoo.practice.base.service.MemberService;
import geonwoo.practice.base.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    //dependency injected
    private final PostService postService;
    private final MemberService memberService;
    private final FileStore fileStore;

    @GetMapping("/posts")
    public String posts() {
        return "post/posts";
    }

    @GetMapping("/post/{postId}")
    public String post() {
        return "post/post";
    }

    @GetMapping("/post/new")
    public String newPostForm() {
        return "post/new";
    }

    @PostMapping("/post/new")
    public String addNewPost(@Valid @ModelAttribute FileForm form,
                             BindingResult bindingResult,
                             HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) { return "post/new"; }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long id = (Long) session.getAttribute(Constants.MEMBER_ID);
            Member author = memberService.searchMemberById(id).orElseThrow();
            UploadFile uploadFile = fileStore.storeFile(form.getAttachFile());
            Post post = new Post(form.getTitle(), form.getContent(), author, uploadFile);
            postService.addNewPost(post);
        } else {
            log.info("cannot post : there is no author");
        }

        return "redirect:/posts";
    }
//    @PostMapping("/post/new")
//    public String addNewPost(@Valid @ModelAttribute Post post,
//                             BindingResult bindingResult,
//                             HttpServletRequest request) {
//        if (bindingResult.hasErrors()) { return "post/new"; }
//
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            Long id = (Long) session.getAttribute(Constants.MEMBER_ID);
//            Member author = memberService.searchMemberById(id).orElseThrow();
//            post.setAuthor(author);
//            postService.addNewPost(post);
//        } else {
//            log.info("cannot post : there is no author");
//        }
//
//        return "redirect:/posts";
//    }

    @GetMapping("/post/edit/{postId}")
    public String editPostForm(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.searchPostById(postId).orElseThrow();
        PostUpdateDto dto = new PostUpdateDto();
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        model.addAttribute("post", dto);
        return "post/edit";
    }

    @PostMapping("/post/edit/{postId}")
    public String editPost(@Valid @ModelAttribute FileForm form,
                           BindingResult bindingResult,
                           @PathVariable("postId") Long postId,
                           HttpServletRequest request) throws IOException {

        if (bindingResult.hasErrors()) {
            return request.getRequestURI();
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warn("cannot edit: no session");
            return "redirect:/posts";
        }

        Long memberId = (Long) session.getAttribute(Constants.MEMBER_ID);
        Member currentUser = memberService.searchMemberById(memberId).orElseThrow();
        Post post = postService.searchPostById(postId).orElseThrow();

        if (!currentUser.equals(post.getAuthor())) {
            log.warn("cannot edit: not the author");
            return "redirect:/posts";
        }

        // 파일 처리 조건 시작
        UploadFile existingFile = post.getUploadFile(); // 기존 파일 (null일 수 있음)
        MultipartFile newMultipart = form.getAttachFile(); // 새로 들어온 파일 (비어 있을 수 있음)
        UploadFile finalFile = null;

        if (newMultipart != null && !newMultipart.isEmpty()) {
            // 새 파일이 들어온 경우 (기존 파일이 있든 없든 덮어씀)
            finalFile = fileStore.storeFile(newMultipart);
        } else {
            // 새 파일이 비어 있음
            if (existingFile != null) {
                // 기존 파일을 유지
                finalFile = existingFile;
            }
            // else: 기존도 없고 새 것도 없음 → null 유지
        }

        PostUpdateDto updateParams = new PostUpdateDto();
        updateParams.setTitle(form.getTitle());
        updateParams.setContent(form.getContent());
        updateParams.setUploadFile(finalFile);

        postService.updatePostById(postId, updateParams);

        return "redirect:/posts";
    }

//    @PostMapping("/post/edit/{postId}")
//    public String editPost(@Valid @ModelAttribute FileForm form,
//                                 BindingResult bindingResult,
//                                 @PathVariable("postId") Long postId,
//                                 HttpServletRequest request) throws IOException {
//
//        if (bindingResult.hasErrors()) { return request.getRequestURI(); }
//
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            PostUpdateDto updateParams = new PostUpdateDto();
//            Long memberId = (Long) session.getAttribute(Constants.MEMBER_ID);
//            Member currentUser = memberService.searchMemberById(memberId).orElseThrow();
//            Post post = postService.searchPostById(postId).orElseThrow();
//            UploadFile existingFile = post.getUploadFile();
//            UploadFile newFile = fileStore.storeFile(form.getAttachFile());
//            if (currentUser == post.getAuthor()) {
//                updateParams.setTitle(form.getTitle());
//                updateParams.setContent(form.getContent());
//                updateParams.setUploadFile(newFile);
//                postService.updatePostById(postId, updateParams);
//            } else {
//                log.warn("cannot delete : post cannot be edited by wrong access (not author)");
//            }
//        } else {
//            log.warn("cannot delete : there is no author");
//        }
//
//        return "redirect:/posts";
//    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId,
                             HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long memberId = (Long) session.getAttribute(Constants.MEMBER_ID);
            Member currentUser = memberService.searchMemberById(memberId).orElseThrow();
            Post post = postService.searchPostById(postId).orElseThrow();
            if (currentUser == post.getAuthor()) {
                postService.deletePostById(postId);
            } else {
                log.warn("cannot delete : post cannot be deleted by wrong access (not author)");
            }
        } else {
            log.info("cannot delete : there is no author");
        }

        return "redirect:/posts";
    }
}
