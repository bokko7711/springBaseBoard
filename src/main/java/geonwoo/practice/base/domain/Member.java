package geonwoo.practice.base.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString @EqualsAndHashCode
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;

    private String name;
    private int age;

    public Member() { }

    public Member(String loginId, String password, String name, int age) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
    }
}
