package geonwoo.practice.base.repository;

import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    //save
    //findById
    //findAll
    //delete
}
