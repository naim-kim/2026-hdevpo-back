package com.csee.swplus.mileage.portfolio.controller;

import com.csee.swplus.mileage.auth.service.AuthService;
import com.csee.swplus.mileage.portfolio.dto.ActivitiesResponse;
import com.csee.swplus.mileage.portfolio.dto.ActivityRequest;
import com.csee.swplus.mileage.portfolio.dto.ActivityResponse;
import com.csee.swplus.mileage.portfolio.dto.RepoEntryRequest;
import com.csee.swplus.mileage.portfolio.dto.RepositoriesResponse;
import com.csee.swplus.mileage.portfolio.dto.TechStackPutRequest;
import com.csee.swplus.mileage.portfolio.dto.TechStackResponse;
import com.csee.swplus.mileage.portfolio.dto.UserInfoPatchRequest;
import com.csee.swplus.mileage.portfolio.dto.UserInfoResponse;
import com.csee.swplus.mileage.portfolio.service.PortfolioService;
import com.csee.swplus.mileage.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Portfolio "내 정보 모아보기" API.
 * Base path: /api/portfolio
 */
@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final AuthService authService;
    private final PortfolioService portfolioService;

    /**
     * GET /api/portfolio/user-info – 기본 정보 (이름, 학교, 전공, 학년, 학기, 소개글).
     */
    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        Users user = getCurrentUser();
        UserInfoResponse body = portfolioService.getUserInfo(user);
        return ResponseEntity.ok(body);
    }

    /**
     * PATCH /api/portfolio/user-info – 소개글(bio) 수정.
     * Body: { "bio": "안녕하세요 ..." }
     */
    @PatchMapping("/user-info")
    public ResponseEntity<UserInfoResponse> patchUserInfo(@Valid @RequestBody UserInfoPatchRequest request) {
        Users user = getCurrentUser();
        UserInfoResponse body = portfolioService.updateBio(user, request.getBio());
        return ResponseEntity.ok(body);
    }

    /**
     * GET /api/portfolio/tech-stack – 기술 스택 목록.
     */
    @GetMapping("/tech-stack")
    public ResponseEntity<TechStackResponse> getTechStack() {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.getTechStack(user));
    }

    /**
     * PUT /api/portfolio/tech-stack – 기술 스택 전체 교체.
     * Body: { "tech_stack": ["Java", "Spring Boot", "Docker"] }
     */
    @PutMapping("/tech-stack")
    public ResponseEntity<TechStackResponse> putTechStack(@Valid @RequestBody TechStackPutRequest request) {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.putTechStack(user, request.getTech_stack()));
    }

    /**
     * GET /api/portfolio/repositories – 노출 토글 + 커스텀 제목 목록.
     */
    @GetMapping("/repositories")
    public ResponseEntity<RepositoriesResponse> getRepositories() {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.getRepositories(user));
    }

    /**
     * PUT /api/portfolio/repositories – 전체 목록 교체 (batch sync).
     * Body: [ { "repo_id": 123, "custom_title": "Project A", "is_visible": true }, ... ]
     */
    @PutMapping("/repositories")
    public ResponseEntity<RepositoriesResponse> putRepositories(@Valid @RequestBody List<RepoEntryRequest> request) {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.putRepositories(user, request));
    }

    /**
     * GET /api/portfolio/activities – 활동 목록.
     */
    @GetMapping("/activities")
    public ResponseEntity<ActivitiesResponse> getActivities() {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.getActivities(user));
    }

    /**
     * POST /api/portfolio/activities – 활동 추가 (반환 id로 이후 PUT).
     * Body: { "title": "...", "description": "...", "start_date": "2024-01-01", "end_date": "2024-06-30" }
     */
    @PostMapping("/activities")
    public ResponseEntity<ActivityResponse> postActivity(@Valid @RequestBody ActivityRequest request) {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.createActivity(user, request));
    }

    /**
     * PUT /api/portfolio/activities/{id} – 활동 수정.
     */
    @PutMapping("/activities/{id}")
    public ResponseEntity<ActivityResponse> putActivity(@PathVariable Long id, @Valid @RequestBody ActivityRequest request) {
        Users user = getCurrentUser();
        return ResponseEntity.ok(portfolioService.updateActivity(user, id, request));
    }

    /**
     * DELETE /api/portfolio/activities/{id} – 활동 삭제.
     */
    @DeleteMapping("/activities/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        Users user = getCurrentUser();
        portfolioService.deleteActivity(user, id);
        return ResponseEntity.noContent().build();
    }

    private Users getCurrentUser() {
        String uniqueId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.getLoginUser(uniqueId);
    }
}
