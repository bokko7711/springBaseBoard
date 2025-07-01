package geonwoo.practice.base.dto;

import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.domain.UploadFile;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private FileResponseDto file;

    public PostResponseDto() { }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor().getName();
        if (post.getUploadFile() != null) {
            this.file = new FileResponseDto(post.getUploadFile());
        }
    }
}
