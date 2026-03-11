package com.libraryhub.payment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionPlanResponseDTO {

    private Long planId;

    private String name;

    private String description;

    private Integer durationDays;

    private BigDecimal price;

    private String status;

    private LocalDateTime createdAt;
}
