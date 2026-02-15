package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PUT /api/portfolio/mileage/{id} – 추가 설명(additional_info)만 수정.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MileageUpdateRequest {

    private String additional_info;
}
