package com.libraryhub.api.controller.identity;




import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.identity.dto.LoginRequestDto;
import com.libraryhub.identity.dto.LoginResponseDto;
import com.libraryhub.identity.dto.SignupRequestDto;
import com.libraryhub.identity.dto.SignupResponseDto;
import com.libraryhub.identity.service.AuthService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthService authService;

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResponseDto>> login(
                @RequestBody LoginRequestDto loginRequestDto) {

            LoginResponseDto response = authService.login(loginRequestDto);

            return ResponseEntity.ok(
                    ApiResponseBuilder.success(response)
            );
        }

        @PostMapping("/signup")
        public ResponseEntity<ApiResponse<SignupResponseDto>> signup(
                @RequestBody SignupRequestDto signupRequestDto) {
            SignupResponseDto response = authService.signup(signupRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseBuilder.success(response));
        }

        @GetMapping("/verify")
        public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
            try {
                String response = authService.verifyEmailToken(token);

                URI successUri = URI.create("http://localhost:3000/email-verified");
                return ResponseEntity.status(HttpStatus.FOUND).location(successUri).build();
            }catch (Exception ex){
                URI errorUri = URI.create("http://localhost:3000/email-verification-failed");
                return ResponseEntity.status(HttpStatus.FOUND).location(errorUri).build();
            }
        }

        /*
         String jwt = jwtService.generateToken(user);

    ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
            .httpOnly(true)
            .secure(true) // true in production (HTTPS)
            .path("/")
            .maxAge(Duration.ofDays(7))
            .sameSite("Strict")
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    URI redirectUri = URI.create("https://libraryhub.com/dashboard");

    return ResponseEntity.status(HttpStatus.FOUND)
            .location(redirectUri)
            .build();
         */


//    @PostMapping("/signup/email")
//    public ResponseEntity<?> signupWithEmail(
//            @Valid @RequestBody EmailSignupRequest request) {
//
//        User user = userService.signupWithEmail(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
//    }
//
//    @PostMapping("/signup/google")
//    public ResponseEntity<?> signupWithGoogle(
//            @Valid @RequestBody GoogleSignupRequest request) {
//
//        User user = userService.signupWithGoogle(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
//    }


}