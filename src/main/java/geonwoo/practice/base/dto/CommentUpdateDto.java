package geonwoo.practice.base.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class CommentUpdateDto {
    private String content;

    public CommentUpdateDto() { }

    public CommentUpdateDto(String content) {
        this.content = content;
    }
}
