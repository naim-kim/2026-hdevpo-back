package com.csee.swplus.mileage.portfolio.service;

import com.csee.swplus.mileage.portfolio.dto.ActivityRequest;
import com.csee.swplus.mileage.portfolio.dto.ActivityResponse;
import com.csee.swplus.mileage.portfolio.dto.ActivitiesResponse;
import com.csee.swplus.mileage.portfolio.dto.RepoEntryRequest;
import com.csee.swplus.mileage.portfolio.dto.RepoEntryResponse;
import com.csee.swplus.mileage.portfolio.dto.RepositoriesResponse;
import com.csee.swplus.mileage.portfolio.dto.TechStackResponse;
import com.csee.swplus.mileage.portfolio.dto.UserInfoResponse;
import com.csee.swplus.mileage.portfolio.entity.Portfolio;
import com.csee.swplus.mileage.portfolio.entity.PortfolioActivity;
import com.csee.swplus.mileage.portfolio.entity.PortfolioRepoEntry;
import com.csee.swplus.mileage.portfolio.repository.PortfolioActivityRepository;
import com.csee.swplus.mileage.portfolio.repository.PortfolioRepository;
import com.csee.swplus.mileage.portfolio.repository.PortfolioRepoEntryRepository;
import com.csee.swplus.mileage.user.entity.Users;
import com.csee.swplus.mileage.auth.exception.DoNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioRepoEntryRepository portfolioRepoEntryRepository;
    private final PortfolioActivityRepository portfolioActivityRepository;

    /**
     * Returns the portfolio for the user, creating one if it does not exist.
     */
    public Portfolio getOrCreatePortfolio(Users user) {
        return portfolioRepository
                .findByUser_Id(user.getId())
                .orElseGet(() -> portfolioRepository.save(
                        Portfolio.builder()
                                .user(user)
                                .build()));
    }

    /**
     * GET /api/portfolio/user-info – 기본 정보 (학교 정보 + bio).
     */
    public UserInfoResponse getUserInfo(Users user) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        return UserInfoResponse.builder()
                .name(user.getName())
                .department(user.getDepartment())
                .major1(user.getMajor1())
                .major2(user.getMajor2())
                .grade(user.getGrade())
                .semester(user.getSemester())
                .bio(portfolio.getBio())
                .build();
    }

    /**
     * PATCH /api/portfolio/user-info – 소개글(bio)만 수정.
     */
    public UserInfoResponse updateBio(Users user, String bio) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        portfolio.setBio(bio);
        portfolioRepository.save(portfolio);
        return getUserInfo(user);
    }

    /**
     * GET /api/portfolio/tech-stack – 기술 스택 목록.
     */
    public TechStackResponse getTechStack(Users user) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        return TechStackResponse.builder()
                .tech_stack(portfolio.getTechStack() != null ? portfolio.getTechStack() : java.util.Collections.emptyList())
                .build();
    }

    /**
     * PUT /api/portfolio/tech-stack – 기술 스택 전체 교체 (batch sync).
     */
    public TechStackResponse putTechStack(Users user, java.util.List<String> techStack) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        portfolio.setTechStack(techStack != null ? techStack : java.util.Collections.emptyList());
        portfolioRepository.save(portfolio);
        return getTechStack(user);
    }

    /**
     * GET /api/portfolio/repositories – 노출 설정 + 커스텀 제목 목록.
     */
    public RepositoriesResponse getRepositories(Users user) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        java.util.List<PortfolioRepoEntry> entries = portfolioRepoEntryRepository.findByPortfolio_IdOrderByDisplayOrderAsc(portfolio.getId());
        java.util.List<RepoEntryResponse> list = new java.util.ArrayList<>();
        for (PortfolioRepoEntry e : entries) {
            list.add(RepoEntryResponse.builder()
                    .id(e.getId())
                    .repo_id(e.getRepoId())
                    .custom_title(e.getCustomTitle())
                    .is_visible(e.getIsVisible())
                    .display_order(e.getDisplayOrder())
                    .build());
        }
        return RepositoriesResponse.builder().repositories(list).build();
    }

    /**
     * PUT /api/portfolio/repositories – 전체 목록 교체 (batch sync).
     */
    public RepositoriesResponse putRepositories(Users user, java.util.List<RepoEntryRequest> requests) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        portfolioRepoEntryRepository.deleteByPortfolio_Id(portfolio.getId());
        if (requests != null) {
            for (int i = 0; i < requests.size(); i++) {
                RepoEntryRequest r = requests.get(i);
                Boolean visible = r.getIs_visible() != null ? r.getIs_visible() : true;
                portfolioRepoEntryRepository.save(PortfolioRepoEntry.builder()
                        .portfolio(portfolio)
                        .repoId(r.getRepo_id())
                        .customTitle(r.getCustom_title())
                        .isVisible(visible)
                        .displayOrder(i)
                        .build());
            }
        }
        return getRepositories(user);
    }

    /**
     * GET /api/portfolio/activities – 활동 목록.
     */
    public ActivitiesResponse getActivities(Users user) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        java.util.List<PortfolioActivity> list = portfolioActivityRepository.findByPortfolio_IdOrderByDisplayOrderAscStartDateDesc(portfolio.getId());
        java.util.List<ActivityResponse> responses = new java.util.ArrayList<>();
        for (PortfolioActivity a : list) {
            responses.add(toActivityResponse(a));
        }
        return ActivitiesResponse.builder().activities(responses).build();
    }

    /**
     * POST /api/portfolio/activities – 활동 추가 (반환된 id로 이후 PUT 가능).
     */
    public ActivityResponse createActivity(Users user, ActivityRequest request) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        int nextOrder = portfolioActivityRepository.findByPortfolio_IdOrderByDisplayOrderAscStartDateDesc(portfolio.getId()).size();
        PortfolioActivity activity = PortfolioActivity.builder()
                .portfolio(portfolio)
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStart_date())
                .endDate(request.getEnd_date())
                .displayOrder(nextOrder)
                .build();
        activity = portfolioActivityRepository.save(activity);
        return toActivityResponse(activity);
    }

    /**
     * PUT /api/portfolio/activities/{id} – 활동 수정.
     */
    public ActivityResponse updateActivity(Users user, Long id, ActivityRequest request) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        PortfolioActivity activity = portfolioActivityRepository.findByIdAndPortfolio_Id(id, portfolio.getId())
                .orElseThrow(() -> new DoNotExistException("해당 활동을 찾을 수 없습니다."));
        activity.setTitle(request.getTitle());
        activity.setDescription(request.getDescription());
        activity.setStartDate(request.getStart_date());
        activity.setEndDate(request.getEnd_date());
        portfolioActivityRepository.save(activity);
        return toActivityResponse(activity);
    }

    /**
     * DELETE /api/portfolio/activities/{id} – 활동 삭제.
     */
    public void deleteActivity(Users user, Long id) {
        Portfolio portfolio = getOrCreatePortfolio(user);
        PortfolioActivity activity = portfolioActivityRepository.findByIdAndPortfolio_Id(id, portfolio.getId())
                .orElseThrow(() -> new DoNotExistException("해당 활동을 찾을 수 없습니다."));
        portfolioActivityRepository.delete(activity);
    }

    private static ActivityResponse toActivityResponse(PortfolioActivity a) {
        return ActivityResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .description(a.getDescription())
                .start_date(a.getStartDate())
                .end_date(a.getEndDate())
                .display_order(a.getDisplayOrder())
                .build();
    }
}
