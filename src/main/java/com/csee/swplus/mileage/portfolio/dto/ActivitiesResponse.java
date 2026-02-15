package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GET /api/portfolio/activities – 활동 목록 응답.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesResponse {

    private List<ActivityResponse> activities;
}
