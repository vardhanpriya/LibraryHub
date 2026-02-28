package com.libraryhub.common.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LibraryBranchResp {

    private Long branchId;
    private String name;
    private String branchCode;
    private String address;
    private String phone;
    private String email;
    private String openingHours;
    private String status;
    private Integer maxCapacity;
    private String cityName;
    private String cityCode;
    private String stateName;
}
