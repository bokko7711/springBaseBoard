package geonwoo.practice.base.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class MemberUpdateDto {
    private String loginId;
    private String password;
    private String name;
    private int age;

    public MemberUpdateDto() { }

    public MemberUpdateDto(String loginId, String password, String name, int age) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
    }
}
