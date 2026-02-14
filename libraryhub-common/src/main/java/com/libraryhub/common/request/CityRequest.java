package com.libraryhub.common.request;


import lombok.Data;

@Data
public class CityRequest {
    private String name;
    private Integer stateId;
}
