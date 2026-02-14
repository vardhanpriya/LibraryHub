package com.libraryhub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    private Status status = Status.SUCCESS;
    private String message;
    private String responseCd = "00";
}
