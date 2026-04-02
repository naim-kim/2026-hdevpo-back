package com.csee.swplus.mileage.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * PATCH /api/portfolio/user-info (application/json) — bio, profile image fields, profile_links.
 * 이미지 파일 업로드는 PUT /api/portfolio/user-info/image (upload wins; clears external URL).
 * <p>
 * Image fields: {@code null} = omit (no change). Empty string = clear that field.
 * Non-empty values for both {@code profile_image_external_url} and {@code profile_image_upload_key} in one request → 400.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoPatchRequest {

    @Size(max = 5000)
    private String bio;

    @Size(max = 255)
    private String profile_image_upload_key;

    @Size(max = 2048)
    private String profile_image_external_url;

    /**
     * Optional profile links. {@code null} = leave unchanged; {@code []} = clear all.
     */
    private List<ProfileLinkDto> profile_links;
}
