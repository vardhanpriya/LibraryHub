package com.libraryhub.api.controller.identity;




import com.libraryhub.identity.dto.LoginRequestDto;
import com.libraryhub.identity.dto.LoginResponseDto;
import com.libraryhub.identity.dto.SignupRequestDto;
import com.libraryhub.identity.dto.SignupResponseDto;
import com.libraryhub.identity.service.AuthService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>  login(@RequestBody LoginRequestDto loginRequestDto){

        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto>  signup(@RequestBody SignupRequestDto signupRequestDto){

        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

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