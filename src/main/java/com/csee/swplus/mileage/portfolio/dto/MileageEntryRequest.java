package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * One item in PUT /api/portfolio/mileage body (same pattern as RepoEntryRequest).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MileageEntryRequest {

    @NotNull
    private Long mileage_id;

    private String additional_info;
}
