package com.golgolengi.auth.dto.request;

public record DevLoginRequest(String name) {
    public DevLoginRequest {
        if (name == null || name.isBlank()) name = "개발자";
    }
}
