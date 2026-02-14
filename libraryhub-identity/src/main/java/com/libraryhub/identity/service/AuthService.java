package com.libraryhub.identity.service;


import com.libraryhub.identity.dto.LoginRequestDto;
import com.libraryhub.identity.dto.LoginResponseDto;
import com.libraryhub.identity.dto.SignupRequestDto;
import com.libraryhub.identity.dto.SignupResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    SignupResponseDto signup(SignupRequestDto signupRequestDto);

    ResponseEntity<LoginResponseDto> handleOAuthLogin2LoginRequest(OAuth2User oAuth2User, String registrationId);
}
