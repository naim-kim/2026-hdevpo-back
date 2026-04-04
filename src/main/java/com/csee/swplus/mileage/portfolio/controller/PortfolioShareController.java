package com.csee.swplus.mileage.portfolio.controller;

import com.csee.swplus.mileage.auth.service.AuthService;
import com.csee.swplus.mileage.portfolio.dto.ActivitiesResponse;
import com.csee.swplus.mileage.portfolio.dto.MileageListResponse;
import com.csee.swplus.mileage.portfolio.dto.RepositoriesResponse;
import com.csee.swplus.mileage.portfolio.dto.SettingsResponse;
import com.csee.swplus.mileage.portfolio.dto.TechStackResponse;
import com.csee.swplus.mileage.portfolio.dto.UserInfoResponse;
import com.csee.swplus.mileage.portfolio.service.PortfolioHtmlExportService;
import com.csee.swplus.mileage.portfolio.service.PortfolioService;
import com.csee.swplus.mileage.user.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public portfolio views by 학번 ({@code studentId} = {@link Users#getUniqueId()}), same idea as
 * {@code /api/mileage/share/{studentId}} — no login required.
 * Base path: {@code /api/portfolio/share}
 */
@RestController
@RequestMapping("/api/portfolio/share")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio — Public share", description = "학번으로 공개 포트폴리오 조회 (로그인 불필요). 레포는 포트폴리오에 표시(is_visible)된 항목만.")
public class PortfolioShareController {

    private final AuthService authService;
    private final PortfolioService portfolioService;
    private final PortfolioHtmlExportService htmlExportService;

    @GetMapping("/{studentId}/user-info")
    @Operation(summary = "[공개] 기본 프로필")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable String studentId) {
        return ResponseEntity.ok(portfolioService.getUserInfo(resolveUser(studentId)));
    }

    @GetMapping("/{studentId}/tech-stack")
    @Operation(summary = "[공개] 기술 스택")
    public ResponseEntity<TechStackResponse> getTechStack(@PathVariable String studentId) {
        return ResponseEntity.ok(portfolioService.getTechStack(resolveUser(studentId)));
    }

    /**
     * GitHub 레포 목록 — 공개 시 {@code visible_only=true} 고정 (비공개로 숨긴 레포는 제외).
     */
    @GetMapping("/{studentId}/repositories")
    @Operation(summary = "[공개] 레포 목록", description = "visible_only는 항상 true로 적용 (숨긴 레포 미노출)")
    public ResponseEntity<RepositoriesResponse> getRepositories(
            @PathVariable String studentId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "per_page", required = false) Integer perPage,
            @RequestParam(value = "selected_only", required = false) Boolean selectedOnly,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "visibility", required = false) String visibility,
            @RequestParam(value = "affiliation", required = false) String affiliation) {
        Users user = resolveUser(studentId);
        return ResponseEntity.ok(portfolioService.getRepositories(user, page, perPage, selectedOnly, true, sort, visibility, affiliation));
    }

    @GetMapping("/{studentId}/mileage")
    @Operation(summary = "[공개] 연결 마일리지")
    public ResponseEntity<MileageListResponse> getMileage(@PathVariable String studentId) {
        return ResponseEntity.ok(portfolioService.getMileageList(resolveUser(studentId)));
    }

    @GetMapping("/{studentId}/activities")
    @Operation(summary = "[공개] 활동 목록")
    public ResponseEntity<ActivitiesResponse> getActivities(
            @PathVariable String studentId,
            @RequestParam(value = "category", required = false) List<String> categories) {
        return ResponseEntity.ok(portfolioService.getActivities(resolveUser(studentId), categories));
    }

    @GetMapping("/{studentId}/settings")
    @Operation(summary = "[공개] 섹션 순서")
    public ResponseEntity<SettingsResponse> getSettings(@PathVariable String studentId) {
        return ResponseEntity.ok(portfolioService.getSettings(resolveUser(studentId)));
    }

    @GetMapping(value = "/{studentId}/export/html", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "[공개] HTML 내보내기")
    public ResponseEntity<String> exportHtml(@PathVariable String studentId) {
        Users user = resolveUser(studentId);
        String html = htmlExportService.generateHtml(user);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"portfolio.html\"")
                .body(html);
    }

    private Users resolveUser(String studentId) {
        return authService.getLoginUser(studentId);
    }
}
