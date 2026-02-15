package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GET /api/portfolio/mileage – 연결된 마일리지 목록 응답.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MileageListResponse {

    private List<MileageEntryResponse> mileage;
}
