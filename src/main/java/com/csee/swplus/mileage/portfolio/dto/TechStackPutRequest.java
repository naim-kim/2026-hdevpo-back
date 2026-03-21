package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * PUT /api/portfolio/tech-stack – 기술 스택 전체 교체 (batch sync).
 * Each item: name, domain (optional), level (0–100, optional).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechStackPutRequest {

    private List<TechStackItem> tech_stack;
}
