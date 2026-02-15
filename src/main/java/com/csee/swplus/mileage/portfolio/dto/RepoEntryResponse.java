package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * One item in GET /api/portfolio/repositories response.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoEntryResponse {

    private Long id;
    private Long repo_id;
    private String custom_title;
    private Boolean is_visible;
    private Integer display_order;
}
