package com.libraryhub.api.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryhub.identity.dto.LoginResponseDto;
import com.libraryhub.identity.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String registrationId =token.getAuthorizedClientRegistrationId();
        ResponseEntity<LoginResponseDto> loginResponseDtoEntity = authService.handleOAuthLogin2LoginRequest(oAuth2User,registrationId);

        Cookie cookie = new Cookie("jwt", loginResponseDtoEntity.getBody().getJwt());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);
        response.sendRedirect("http://localhost:3000");

    }
    //http://localhost:8081/library-hub/api/oauth2/authorization/github
}
