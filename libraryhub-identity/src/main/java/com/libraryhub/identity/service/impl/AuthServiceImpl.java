package com.libraryhub.identity.service.impl;

import com.libraryhub.identity.dto.LoginRequestDto;
import com.libraryhub.identity.dto.LoginResponseDto;
import com.libraryhub.identity.dto.SignupRequestDto;
import com.libraryhub.identity.dto.SignupResponseDto;
import com.libraryhub.identity.entity.Role;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.enums.AuthProviderType;
import com.libraryhub.identity.enums.UserStatus;
import com.libraryhub.identity.repository.RoleRepository;
import com.libraryhub.identity.repository.UserRepository;
import com.libraryhub.identity.security.AuthUtil;
import com.libraryhub.identity.security.CustomUserDetails;
import com.libraryhub.identity.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUserEmail(),loginRequestDto.getPassword())
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();
        String accessToken = authUtil.generateAccessToken(user);
        return new LoginResponseDto(accessToken, user.getId());
    }

    public User signUpInternal (SignupRequestDto signupRequestDto,AuthProviderType authProviderType,String providerId){
        if (userRepository.findByEmail(signupRequestDto.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        Role userRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = User.builder()
                .email(signupRequestDto.getUserEmail())
                .name(signupRequestDto.getName())
                .userStatus(UserStatus.PENDING_VERIFICATION)
                .deleted(false)
                .authProviderType(authProviderType)
                .providerId(providerId)
                .build();
        if(authProviderType.equals(AuthProviderType.LOCAL)){
            user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }
        if(user.getRoles() == null){
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
        }else {
            user.getRoles().add(userRole);
        }
        return userRepository.save(user);
    }

    @Override  // calling this method AuthController
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        //sendEmailForVerification();
        User user = signUpInternal(signupRequestDto,AuthProviderType.LOCAL,null);
        return new SignupResponseDto(user.getId(),user.getEmail());
    }

    @Override
    public ResponseEntity<LoginResponseDto> handleOAuthLogin2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User,registrationId);

        // fetch  User by providerType and providerID
        User user = userRepository.findByProviderIdAndAuthProviderType(providerId,providerType).orElse(null);

        String email = oAuth2User.getAttribute("email");

        User emailUser = userRepository.findByEmail(email).orElse(null);
        if(user==null && emailUser==null){
            //signup flow
            String userName  = authUtil.determineUserNameFromOAuth2User(oAuth2User,registrationId,providerId);
            String namOfTheUser =""; // later
            SignupRequestDto signupRequestDto = new SignupRequestDto();
            signupRequestDto.setUserEmail(userName);
            signupRequestDto.setPassword(null);
            signupRequestDto.setName(null);
            user  = signUpInternal(signupRequestDto,providerType,providerId);
        } else if (user!=null) {
            if(email!=null  && !email.isBlank() && !email.equals(user.getEmail())){
                user.setEmail(email);
                userRepository.save(user);
            }
        }else {
                throw new BadCredentialsException("This email is already registered with provider : " + email );
        }
        LoginResponseDto   loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getId() );
        return ResponseEntity.ok(loginResponseDto);
    }
}
