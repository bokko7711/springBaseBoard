package geonwoo.practice.base.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileForm {
    private String title;
    private String content;
    private MultipartFile attachFile;
}
