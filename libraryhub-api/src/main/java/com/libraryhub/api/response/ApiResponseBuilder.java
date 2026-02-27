package com.libraryhub.api.response;

import com.libraryhub.api.response.ApiResponse;

import java.util.Collections;
import java.util.List;

public class ApiResponseBuilder {

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .metaData(
                        ApiResponse.MetaData.builder()
                                .status("SUCCESS")
                                .message("Request processed successfully")
                                .responseCd("00")
                                .build()
                )
                .resourceData(Collections.singletonList(data))
                .build();
    }

    public static ApiResponse<Object> error(String message,
                                            String responseCd,
                                            List<ApiResponse.ErrorDetail> errors) {
        return ApiResponse.builder()
                .metaData(
                        ApiResponse.MetaData.builder()
                                .status("ERROR")
                                .message(message)
                                .responseCd(responseCd)
                                .build()
                )
                .errorDetails(errors)
                .build();
    }
}
