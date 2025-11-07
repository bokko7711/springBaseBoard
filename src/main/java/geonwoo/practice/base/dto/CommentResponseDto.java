package geonwoo.practice.base.dto;

import geonwoo.practice.base.domain.Comments;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String author;

    public CommentResponseDto() { }

    public CommentResponseDto(Comments comments) {
        this.id = comments.getId();
        this.content = comments.getContent();
        this.author = comments.getAuthor().getName();
    }
}
