package geonwoo.practice.base.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter @ToString @EqualsAndHashCode
@RequiredArgsConstructor
public class LoginForm {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
}
