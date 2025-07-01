package geonwoo.practice.base.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.QMember;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static geonwoo.practice.base.domain.QMember.*;

@Repository
public class MemberDynamicQueryRepository {

    private final JPAQueryFactory query;

    public MemberDynamicQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //add dynamic query here...
    public List<Member> findNameContains(String name) {
        return query
                .select(member)
                .from(member)
                .where(containsName(name))
                .fetch();
    }

    private BooleanExpression containsName(String name) {
        if (StringUtils.hasText(name)) {
            return member.name.like("%" + name + "%");
        }
        return null;
    }
}
