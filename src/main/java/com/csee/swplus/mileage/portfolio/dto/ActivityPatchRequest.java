package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * PATCH /api/portfolio/activities/{id} – partial update (only non-null fields are applied).
 * Category: 1=activity, 2=project, 3=certificate, 4=camp, etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPatchRequest {

    private String title;
    private String description;
    private LocalDate start_date;
    private LocalDate end_date;
    private Integer category;
}
