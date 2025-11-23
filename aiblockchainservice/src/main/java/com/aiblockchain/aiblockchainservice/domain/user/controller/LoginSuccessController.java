package com.aiblockchain.aiblockchainservice.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginSuccessController {

    @GetMapping("/login-success")
    public String loginSuccess(@RequestParam("accessToken") String accessToken) {
        return "<h1>로그인 성공! 🚀</h1>" +
                "<h3>받은 Access Token:</h3>" +
                "<p>" + accessToken + "</p>" +
                "<p>(이 토큰을 복사해서 헤더에 넣고 요청을 보내보세요!)</p>";
    }
}