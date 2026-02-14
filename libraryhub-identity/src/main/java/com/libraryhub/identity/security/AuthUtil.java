package com.libraryhub.identity.security;

import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.enums.AuthProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey ;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId",user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUserNameFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }


    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId){
        return  switch (registrationId.toLowerCase()){
            case "google"->AuthProviderType.GOOGLE;
            case "github"->AuthProviderType.GITHUB;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider : " + registrationId);
        };
    }

    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId){

        String providerId = switch (registrationId.toLowerCase()){
            case "google"->oAuth2User.getAttribute("sub");
            case "github"->oAuth2User.getAttribute("id").toString();
            default -> {
                log.error("Unsupported OAuth2 provider : " + registrationId);
                throw new IllegalArgumentException("Unsupported OAuth2 provider : " + registrationId);
            }
        };
        if(providerId ==null || providerId.isBlank()){
            log.error("Unable to determine providerId for provider : {}", providerId);
            throw new IllegalArgumentException("Unable to determine providerId for provider OAuth2 Login");
        }
        return providerId;
    }

    public String determineUserNameFromOAuth2User(OAuth2User oAuth2User,String registrationId,String providerId){
        String email = oAuth2User.getAttribute("email");
        if(email!=null && !email.isBlank()){
            return email;
        }
        return switch (registrationId.toLowerCase()){
            case "google"->oAuth2User.getAttribute("sub");
            case "github"->oAuth2User.getAttribute("login");
            default -> providerId;
        };
    }
}
