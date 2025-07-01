package geonwoo.practice.base.dto;

import geonwoo.practice.base.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String author;

    public CommentResponseDto() { }

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getAuthor().getName();
    }
}
