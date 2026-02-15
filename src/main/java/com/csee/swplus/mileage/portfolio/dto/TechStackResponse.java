package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GET /api/portfolio/tech-stack – 기술 스택 응답.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechStackResponse {

    private List<String> tech_stack;
}
