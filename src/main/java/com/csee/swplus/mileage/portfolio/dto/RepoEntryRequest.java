package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * One item in PUT /api/portfolio/repositories body.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepoEntryRequest {

    @NotNull
    private Long repo_id;

    private String custom_title;

    private String description;

    private Boolean is_visible;
}
