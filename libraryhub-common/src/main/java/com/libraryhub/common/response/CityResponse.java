package com.libraryhub.common.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {
    private Integer cityId;
    private String name;
    private String cityCode;
    private Integer stateId;
    private String stateName;
}
