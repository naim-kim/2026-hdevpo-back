package com.csee.swplus.mileage.portfolio.service;

import com.csee.swplus.mileage.portfolio.dto.RepoEntryRequest;
import com.csee.swplus.mileage.portfolio.dto.RepoEntryResponse;
import com.csee.swplus.mileage.portfolio.dto.RepositoriesResponse;
import com.csee.swplus.mileage.portfolio.dto.TechStackResponse;
import com.csee.swplus.mileage.portfolio.dto.UserInfoResponse;
import com.csee.swplus.mileage.portfolio.entity.Portfolio;
import com.csee.swplus.mileage.portfolio.entity.PortfolioRepoEntry;
import com.csee.swplus.mileage.portfolio.repository.PortfolioRepository;
import com.csee.swplus.mileage.portfolio.repository.PortfolioRepoEntryRepository;
import com.csee.swplus.mileage.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioRepoEntryRepository portfolioRepoEntryRepository;

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
}
