package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * One item in PATCH /api/portfolio/activities (full list).
 * id is required; other fields are optional (only non-null are applied).
 * Category: 1=activity, 2=project, 3=certificate, 4=camp, etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPatchItemRequest {

    @javax.validation.constraints.NotNull
    private Long id;

    private String title;
    private String description;
    private LocalDate start_date;
    private LocalDate end_date;
    private Integer category;
}
