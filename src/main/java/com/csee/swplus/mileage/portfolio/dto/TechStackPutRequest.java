package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * PUT /api/portfolio/tech-stack – 기술 스택 전체 교체 (batch sync).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechStackPutRequest {

    private List<String> tech_stack;
}
