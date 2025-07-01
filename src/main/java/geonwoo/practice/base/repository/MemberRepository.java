package geonwoo.practice.base.repository;

import geonwoo.practice.base.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //save
    //findById
    //findAll
    //delete
    Optional<Member> findByLoginId(String loginId);
}
