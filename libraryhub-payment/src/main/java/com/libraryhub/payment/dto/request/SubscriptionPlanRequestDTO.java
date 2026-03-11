package com.libraryhub.payment.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanRequestDTO {

    private String name;

    private String description;

    private Integer durationDays;

    private BigDecimal price;

    private String status;
}
