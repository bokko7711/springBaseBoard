package geonwoo.practice.base.controller;

import geonwoo.practice.base.Constants;
import geonwoo.practice.base.dto.MemberUpdateDto;
import geonwoo.practice.base.domain.Member;
import geonwoo.practice.base.login.LoginForm;
import geonwoo.practice.base.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    //dependency injected
    private final MemberService service;

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @PostMapping("/login")
    public String loginFormSubmit(@Valid @ModelAttribute LoginForm form,
                                  BindingResult bindingResult,
                                  @RequestParam(name = "redirectURL", defaultValue = "/index") String redirectURL,
                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) { return "login"; }
        Member loginMember = service.login(form.getLoginId(), form.getPassword());
        if (loginMember == null) { // 해당 id, pw를 가진 멤버 없음
            bindingResult.reject("loginFail", "아이디 / 비밀번호를 확인하세요.");
            return "login/login";
        }
        //login 성공 시 로직
        HttpSession session = request.getSession(); // 세션 저장소에 해당 세션아이디 key - 빈 객체 value 쌍 생성
        session.setAttribute(Constants.LOGIN_MEMBER, loginMember); // value에 해당 attribute 추가
        session.setAttribute(Constants.MEMBER_ID, loginMember.getId());

        return "redirect:" + redirectURL;
    }

    @GetMapping("/join")
    public String home() {
        return "login/join";
    }

    @PostMapping("/join")
    public String joinFormSubmit(@Valid @ModelAttribute Member member,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { return "join"; }

        service.addNewMember(member);

        return "redirect:/login/login";
    }

    @PostMapping("logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login/login";
    }

    @GetMapping("/edit")
    public String edit(HttpServletRequest request, Model model) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Long id = (Long) session.getAttribute(Constants.MEMBER_ID);
                Member member = service.searchMemberById(id).orElseThrow(); // 회원 엔티티를 조회
                MemberUpdateDto dto = new MemberUpdateDto();
                dto.setLoginId(member.getLoginId());
                dto.setPassword(member.getPassword());
                dto.setName(member.getName());
                dto.setAge(member.getAge());
                // 비밀번호는 보통 보여주지 않음 (공백 유지)
                model.addAttribute("memberUpdateDto", dto);
            }
            return "login/edit";
    }

    @PostMapping("/edit")
    public String editFormSubmit(@Valid @ModelAttribute MemberUpdateDto updateParams,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) {

        if (bindingResult.hasErrors()) { return "login/edit"; }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long id = (Long) session.getAttribute(Constants.MEMBER_ID);
            service.updateMemberById(id, updateParams);
        } else {
            log.info("cannot update : there is no such member id");
        }

        return "redirect:/index";
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Long id = (Long) session.getAttribute(Constants.MEMBER_ID);
            service.deleteMemberById(id);
            session.invalidate();
        } else {
            log.info("cannot delete : there is no such member id");
        }

        return "redirect:/login/login";
    }
}
