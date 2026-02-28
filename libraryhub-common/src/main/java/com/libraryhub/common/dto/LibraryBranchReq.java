package com.libraryhub.common.dto;

import com.libraryhub.common.entity.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBranchReq {

    private String name;
    private String branchCode;
    private String address;
    private String phone;
    private String email;
    private String openingHours;
    private String status;           // ACTIVE, INACTIVE, UNDER_MAINTENANCE
    private Integer maxCapacity;
    private String cityCode;
}
