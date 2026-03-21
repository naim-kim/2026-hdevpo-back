package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A single tech stack entry with optional domain and proficiency level.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechStackItem {

    /** Technology name (e.g. "Java", "React") */
    private String name;

    /** Domain/category (e.g. "Frontend", "Backend", "Database", "DevOps") */
    private String domain;

    /** Proficiency level 0–100 (percentage). Null = not specified. */
    private Integer level;
}
