package geonwoo.practice.base.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString @EqualsAndHashCode
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(length = 255) //생략 가능
    private String title;
    @Column(length = 1000)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY) // 여러 게시물 - 한 명의 회원
    //@JoinColumn(name = "author_id") //생략 가능
    private Member author;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UploadFile uploadFile;

    public Post() { }

    public Post(String title, String content, Member author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Post(String title, String content, Member author, UploadFile uploadFile) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.uploadFile = uploadFile;
    }
}
