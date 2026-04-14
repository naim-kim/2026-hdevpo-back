package com.csee.swplus.mileage.github.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubOrgsResponse {
    @Builder.Default
    private List<GitHubOrgDto> organizations = new ArrayList<>();

    @Builder.Default
    private List<String> warnings = new ArrayList<>();
}

