package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response for POST /api/portfolio/cv/build-prompt.
 * CV is created with empty html; frontend patches html_content in the next
 * step.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvBuildPromptResponse {

    /** Full prompt ready to paste into LLM. */
    private String prompt;

    /** Created CV id – use for PATCH /cv/{id} to submit html_content. */
    private Long cv_id;

    /**
     * Numeric public id for share URL (10 digits). When the CV is public, HTML is
     * served without login under
     * /api/portfolio/share/cv/ plus this token, suffix /html.
     */
    private String public_token;
}
