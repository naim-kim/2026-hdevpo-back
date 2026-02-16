package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * One linked mileage in GET list or POST/PUT response (id = portfolio_mileage link id).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MileageEntryResponse {

    private Long id;
    private Long mileage_id;
    private String additional_info;
    private Integer display_order;
}
