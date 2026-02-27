package com.libraryhub.common.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class StateResponse {
    private Integer stateId;
    private String name;
    private String code;
    private String status;

    private List<CityResponse> cities;
}
