package com.golgolengi.auth.controller;

import com.golgolengi.auth.dto.request.KakaoLoginRequest;
import com.golgolengi.auth.dto.response.TokenResponse;
import com.golgolengi.auth.service.AuthService;
import com.golgolengi.auth.service.KakaoCodeExchangeService;
import com.golgolengi.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final AuthService authService;
    private final KakaoCodeExchangeService kakaoCodeExchangeService;

    @PostMapping("/oauth/kakao/callback")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoCallback(
            @RequestBody @Valid KakaoLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.loginWithKakao(request)));
    }

    @GetMapping("/login/oauth2/code/kakao")
    public void kakaoCodeCallback(
            @RequestParam String code,
            HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoCodeExchangeService.exchangeAndBuildDeepLink(code);
        response.sendRedirect(redirectUrl);
    }
}
