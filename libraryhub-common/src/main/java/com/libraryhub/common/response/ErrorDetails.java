package com.libraryhub.common.response;


import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String errorCode;
    private String errorMessage;
    private String errorType;
}
