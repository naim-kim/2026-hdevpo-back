package com.csee.swplus.mileage.setting.service;

import com.csee.swplus.mileage.setting.entity.Manager;
import com.csee.swplus.mileage.setting.entity.SwManagerSetting;
import com.csee.swplus.mileage.setting.repository.ManagerRepository;
import com.csee.swplus.mileage.setting.repository.SwManagerSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final SwManagerSettingRepository swManagerSettingRepository;

    public Manager getRegisterDate() {
        return managerRepository.findById(2L)
                .orElse(null);
    }

    public String getCurrentSemester() {
        String currentSemester = swManagerSettingRepository.findFirstByOrderByIdDesc()
                .map(SwManagerSetting::getCurrentSemester)
                .orElse("0000-00");

        return currentSemester;
    }

    /**
     * Returns contact info from manager setting (e.g. id=2).
     * Uses native query so it works even when contact_info column is missing
     * (returns "").
     */
    public String getContactInfo() {
        try {
            return managerRepository.findContactInfoById(2L)
                    .filter(s -> s != null && !s.isEmpty())
                    .orElse("");
        } catch (Exception e) {
            log.warn("Could not load contact_info (column may not exist): {}", e.getMessage());
            return "";
        }
    }

    /**
     * Returns MyPage announcement text from manager setting (e.g. id=2).
     * Uses a native query so it does not depend on other columns (contact_info,
     * reg_start/reg_end types, etc.). Returns "" if column/row is missing or any
     * error.
     */
    public String getMypageAnnouncement() {
        try {
            return managerRepository.findMypageAnnouncementById(2L)
                    .filter(s -> s != null && !s.isEmpty())
                    .orElse("");
        } catch (Exception e) {
            log.warn("Could not load mypage_announcement (add column if needed): {}", e.getMessage());
            return "";
        }
    }

    /**
     * Returns global maintenance flag from manager setting (id=2).
     * Uses native query so it only depends on maintenance_mode column.
     * If anything fails, returns false (no maintenance).
     */
    public boolean isMaintenanceMode() {
        try {
            return managerRepository.findMaintenanceModeById(2L)
                    .map(v -> v != null && v != 0)
                    .orElse(false);
        } catch (Exception e) {
            log.warn("Could not load maintenance_mode (column may not exist): {}", e.getMessage());
            return false;
        }
    }
}
