package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Language with its percentage of code (by bytes) in a repository.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoLanguageDto {

    /** Language name (e.g. "Java", "Python"). */
    private String name;

    /** Percentage of total bytes. Null when only primary language is known (from list API). */
    private Double percentage;
}
