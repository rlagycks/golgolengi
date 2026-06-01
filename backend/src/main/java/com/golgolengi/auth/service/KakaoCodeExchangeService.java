package com.golgolengi.auth.service;

import com.golgolengi.auth.dto.request.KakaoLoginRequest;
import com.golgolengi.auth.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoCodeExchangeService {

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    @Value("${kakao.rest-api-key}")
    private String restApiKey;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final AuthService authService;
    private final RestTemplate restTemplate;

    public String exchangeAndBuildDeepLink(String code) {
        String kakaoAccessToken = exchangeCodeForToken(code);
        TokenResponse token = authService.loginWithKakao(new KakaoLoginRequest(kakaoAccessToken));

        return "fhos://auth?access_token=" + token.getAccessToken()
                + "&refresh_token=" + token.getRefreshToken()
                + "&member_id=" + token.getMemberId()
                + "&is_new_member=" + token.isNewMember()
                + "&onboarding_completed=" + token.isOnboardingCompleted();
    }

    @SuppressWarnings("unchecked")
    private String exchangeCodeForToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        ResponseEntity<Map> response = restTemplate.exchange(
                KAKAO_TOKEN_URL,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        Map<String, Object> tokenBody = response.getBody();
        if (tokenBody == null || !tokenBody.containsKey("access_token")) {
            throw new RuntimeException("카카오 토큰 교환 실패");
        }
        return (String) tokenBody.get("access_token");
    }
}
