package com.libraryhub.circulation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayRequest {

    private Long branchId; // nullable
    private LocalDate date;
    private String reason;
    private String status;
}
