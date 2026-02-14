package com.libraryhub.common.response;

import java.util.List;

public class ApiResponse <T>{
    private MetaData metaData;
    private List<T> resourceData;
    private ErrorDetails error;
}
