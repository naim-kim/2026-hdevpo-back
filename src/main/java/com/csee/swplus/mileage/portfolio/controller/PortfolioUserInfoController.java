package com.csee.swplus.mileage.portfolio.controller;

import com.csee.swplus.mileage.auth.service.AuthService;
import com.csee.swplus.mileage.portfolio.dto.UserInfoPatchRequest;
import com.csee.swplus.mileage.portfolio.dto.UserInfoResponse;
import com.csee.swplus.mileage.portfolio.entity.Portfolio;
import com.csee.swplus.mileage.portfolio.service.PortfolioService;
import com.csee.swplus.mileage.user.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * User info / profile API — grouped in Swagger separately from the rest of portfolio
 * (same pattern as {@link PortfolioActivitiesController}).
 * Base path: /api/portfolio
 */
@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio — User info", description = "기본 프로필 (조회, JSON 수정, 프로필 이미지 업로드·조회)")
public class PortfolioUserInfoController {

    private final AuthService authService;
    private final PortfolioService portfolioService;

    @Value("${file.portfolio-profile-upload-dir:${file.profile-upload-dir:./uploads/profile}}")
    private String profileUploadDir;

    /**
     * GET /api/portfolio/user-info – 기본 정보 (이름, 학교, 전공, 학년, 학기, 소개글, 프로필 이미지).
     */
    @GetMapping("/user-info")
    @Operation(summary = "기본 프로필 조회")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        Users user = getCurrentUser();
        UserInfoResponse body = portfolioService.getUserInfo(user);
        return ResponseEntity.ok(body);
    }

    /**
     * PATCH /api/portfolio/user-info — JSON: bio, profile_image_url (filename), profile_links.
     * {@code profile_links}: null = 유지, [] = 전체 삭제. 파일 업로드는 PUT /user-info/image 사용.
     */
    @PatchMapping(value = "/user-info", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 메타데이터 수정",
            description = "bio, profile_image_url (로컬 파일명), profile_links. 이미지 파일은 PUT /user-info/image")
    public ResponseEntity<UserInfoResponse> patchUserInfo(@Valid @RequestBody UserInfoPatchRequest request) {
        Users user = getCurrentUser();
        UserInfoResponse body = portfolioService.updateBio(user, request.getBio(), request.getProfile_image_url(),
                request.getProfile_links());
        return ResponseEntity.ok(body);
    }

    /**
     * PUT /api/portfolio/user-info/image — multipart only: 새 프로필 이미지 파일 업로드 (기존 파일 교체).
     * Part: profile_image (required).
     */
    @PutMapping(value = "/user-info/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 업로드", description = "multipart: profile_image 파일만")
    public ResponseEntity<UserInfoResponse> putUserInfoImage(@RequestPart("profile_image") MultipartFile profileImage) {
        Users user = getCurrentUser();
        if (profileImage == null || profileImage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "profile_image is required");
        }
        String newUploadFilename;
        try {
            Portfolio currentPortfolio = portfolioService.getOrCreatePortfolio(user);
            String oldFilename = currentPortfolio.getProfileImageUrl();
            if (oldFilename != null && !oldFilename.isEmpty()) {
                try {
                    Path oldImagePath = Paths.get(profileUploadDir).resolve(oldFilename);
                    Files.deleteIfExists(oldImagePath);
                    log.info("Deleted old profile image: {}", oldFilename);
                } catch (IOException e) {
                    log.warn("Failed to delete old profile image: {}", oldFilename, e);
                }
            }
            Path uploadPath = Paths.get(profileUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = profileImage.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path targetLocation = uploadPath.resolve(uniqueFilename);
            Files.copy(profileImage.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            newUploadFilename = uniqueFilename;
            log.info("Profile image saved: {}", uniqueFilename);
        } catch (IOException e) {
            log.error("Failed to save profile image", e);
            throw new RuntimeException("프로필 이미지 저장 중 오류가 발생했습니다.");
        }
        UserInfoResponse body = portfolioService.updateBio(user, null, newUploadFilename, null);
        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/portfolio/user-info/image/{filename} – 프로필 이미지 조회.
     */
    @GetMapping("/user-info/image/{filename}")
    @Operation(summary = "프로필 이미지 파일 조회")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(profileUploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            log.error("Failed to load profile image: {}", filename, e);
            return ResponseEntity.badRequest().build();
        }
    }

    private Users getCurrentUser() {
        String uniqueId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.getLoginUser(uniqueId);
    }
}
