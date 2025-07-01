package geonwoo.practice.base.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.domain.Post;
import geonwoo.practice.base.domain.QPost;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static geonwoo.practice.base.domain.QMember.member;
import static geonwoo.practice.base.domain.QPost.*;

@Repository
public class PostDynamicQueryRepository {

    private final JPAQueryFactory query;

    public PostDynamicQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //add dynamic query here...
    public List<Post> findBySearchText(String search) {
        return query
                .select(post)
                .from(post)
                .where(containsSearch(search))
                .fetch();
    }

    private BooleanExpression containsSearch(String search) {
        if (StringUtils.hasText(search)) {
            return post.title.like("%" + search + "%")
                    .or(post.content.like("%" + search + "%"))
                    .or(post.author.name.like("%" + search + "%"));
        }
        return null;
    }
}
