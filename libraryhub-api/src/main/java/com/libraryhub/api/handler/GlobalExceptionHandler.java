package com.libraryhub.api.handler;

import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.api.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<ApiResponse.ErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapToErrorDetail)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ApiResponseBuilder.error(
                        "Validation failed",
                        "01",
                        errors
                ));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNameNotFoundException(
            UsernameNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseBuilder.error(
                        "Username not found",
                        "04",
                        List.of(
                                ApiResponse.ErrorDetail.builder()
                                        .field("username")
                                        .errorMessage(ex.getMessage())
                                        .build()
                        )
                ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            AuthenticationException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseBuilder.error(
                        "Authentication failed",
                        "02",
                        List.of(
                                ApiResponse.ErrorDetail.builder()
                                        .field("authentication")
                                        .errorMessage(ex.getMessage())
                                        .build()
                        )
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponseBuilder.error(
                        "Access denied",
                        "03",
                        List.of(
                                ApiResponse.ErrorDetail.builder()
                                        .field("authorization")
                                        .errorMessage("Insufficient permissions")
                                        .build()
                        )
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseBuilder.error(
                        "Internal server error",
                        "99",
                        List.of(
                                ApiResponse.ErrorDetail.builder()
                                        .field("system")
                                        .errorMessage(ex.getMessage())
                                        .build()
                        )
                ));
    }

    private ApiResponse.ErrorDetail mapToErrorDetail(FieldError fieldError) {
        return ApiResponse.ErrorDetail.builder()
                .field(fieldError.getField())
                .errorMessage(fieldError.getDefaultMessage())
                .build();
    }
}
