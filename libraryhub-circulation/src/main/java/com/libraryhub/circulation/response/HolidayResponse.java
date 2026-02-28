package com.libraryhub.circulation.response;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayResponse {

    private Long holidayId;
    private Long branchId;
    private String branchName;
    private LocalDate date;
    private String reason;
    private String status;
}
