package geonwoo.practice.base.dto;

import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.domain.UploadFile;
import lombok.Getter;
@Getter
public class FileResponseDto {
    private Long id;
    private String uploadFileName;
    private String storeFileName;

        public FileResponseDto(UploadFile uploadFile) {
            this.id = uploadFile.getId();
            this.uploadFileName = uploadFile.getUploadFileName();
            this.storeFileName = uploadFile.getStoreFileName();
        }
}
