package geonwoo.practice.base.login;

import geonwoo.practice.base.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        HttpSession session = request.getSession(false);
        if (session == null ||
                session.getAttribute(Constants.LOGIN_MEMBER) == null) {
            response.sendRedirect("/login?redirectURL=" + url);
            return false;
        }

        return true;
    }
}
