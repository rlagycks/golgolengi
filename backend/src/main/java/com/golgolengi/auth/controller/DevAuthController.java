package com.golgolengi.auth.controller;

import com.golgolengi.auth.dto.request.DevLoginRequest;
import com.golgolengi.auth.dto.response.TokenResponse;
import com.golgolengi.auth.service.AuthService;
import com.golgolengi.global.response.ApiResponse;
import com.golgolengi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevAuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> devLogin(
            @RequestBody(required = false) DevLoginRequest request) {
        String name = request != null ? request.name() : "개발자";
        return ResponseEntity.ok(ApiResponse.ok(authService.devLogin(name)));
    }

    @DeleteMapping("/reset")
    public ResponseEntity<ApiResponse<TokenResponse>> devReset(
            @RequestBody(required = false) DevLoginRequest request) {
        String name = request != null ? request.name() : "개발자";
        TokenResponse tokens = authService.devReset(name);
        return ResponseEntity.ok(ApiResponse.ok(tokens));
    }
}
