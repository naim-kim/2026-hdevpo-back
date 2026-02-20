package com.csee.swplus.mileage.subitem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Minimal DTO for subitem/category names lookup.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubitemNamesDto {
    private String subitemName;
    private String categoryName;
}

