package com.libraryhub.api.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class ApiResponse<T> {

    private MetaData metaData;
    private List<T> resourceData;

    private List<ErrorDetail> errorDetails;
    @Data
    @Builder
    public static class ErrorDetail {
        private String field;
        private String errorMessage;
    }

    @Data
    @Builder
    public static class MetaData {
        private String status;
        private String message;
        private String responseCd;
    }
}
