package com.aiblockchain.aiblockchainservice.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return "<h1>VeriLingua Server</h1> <a href='/oauth2/authorization/google'>구글 로그인</a>";
        }

        // 로그인 성공 시 유저 이름 보여주기
        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        return "<h1>로그인 성공! 🎉</h1>" +
                "<p>이름: " + name + "</p>" +
                "<p>이메일: " + email + "</p>";
    }
}