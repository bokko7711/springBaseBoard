package geonwoo.practice.base.controller;

import geonwoo.practice.base.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    //dependency injected
    private final MemberService service;

    @GetMapping("/")
    public String home() {
        return "login/home";
    }

    @GetMapping("/index")
    public String index() {
        return "login/index";
    }
}
