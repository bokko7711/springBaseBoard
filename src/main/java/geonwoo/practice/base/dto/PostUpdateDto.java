package geonwoo.practice.base.dto;

import geonwoo.practice.base.domain.UploadFile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class PostUpdateDto {
    private String title;
    private String content;
    private UploadFile uploadFile;

    public PostUpdateDto() { }

    public PostUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostUpdateDto(String title, String content, UploadFile uploadFile) {
        this.title = title;
        this.content = content;
        this.uploadFile = uploadFile;
    }
}
