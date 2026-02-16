package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * POST /api/portfolio/mileage – 기존 마일리지 연결.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MileageLinkRequest {

    @NotNull
    private Long mileage_id;

    private String additional_info;
}
