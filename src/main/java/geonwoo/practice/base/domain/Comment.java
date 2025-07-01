package geonwoo.practice.base.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString @EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글 - 한 명의 멤버
    //@JoinColumn(name = "author_id") //생략 가능
    private Member author;
    @ManyToOne(fetch = FetchType.LAZY) // 여러 댓글 - 한 개의 게시물
    //@JoinColumn(name = "post_id") //생략 가능
    private Post post;

    public Comment() { }

    public Comment(String content, Member author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
    }
}
