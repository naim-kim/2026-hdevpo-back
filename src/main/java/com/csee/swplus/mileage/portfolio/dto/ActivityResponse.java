package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Single activity in GET list or POST/PUT response.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate start_date;
    private LocalDate end_date;
    /** Category: 1=activity, 2=project, 3=certificate, 4=camp, etc. */
    private Integer category;
    private Integer display_order;
}
