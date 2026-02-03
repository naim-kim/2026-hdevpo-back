package com.csee.swplus.mileage.setting.controller;

import com.csee.swplus.mileage.setting.dto.AnnouncementResponse;
import com.csee.swplus.mileage.setting.dto.ContactResponse;
import com.csee.swplus.mileage.setting.dto.MaintenanceResponse;
import com.csee.swplus.mileage.setting.dto.ManagerResponse;
import com.csee.swplus.mileage.setting.entity.Manager;
import com.csee.swplus.mileage.setting.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mileage")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/apply")
    public ManagerResponse getMileageSetting() {
        Manager manager = managerService.getRegisterDate();

        if (manager == null) {
            log.error("Manager 정보가 없습니다.");
            return null;
        }

        return new ManagerResponse(manager.getRegStart(), manager.getRegEnd());
    }

    @GetMapping("/contact")
    public ResponseEntity<ContactResponse> getContact() {
        String contactInfo = managerService.getContactInfo();
        return ResponseEntity.ok(new ContactResponse(contactInfo));
    }

    /**
     * MyPage announcement (e.g. "학부와 전공 정보를 확인한 후, 장학금 신청 대상인 경우에만 신청하세요.").
     * Stored in _sw_manager_setting.mypage_announcement.
     */
    @GetMapping("/announcement")
    public ResponseEntity<AnnouncementResponse> getAnnouncement() {
        String announcement = managerService.getMypageAnnouncement();
        return ResponseEntity.ok(new AnnouncementResponse(announcement));
    }

    /**
     * Global maintenance flag endpoint.
     * Returns { "maintenance": true } when maintenance_mode = 1 in
     * _sw_manager_setting (id=2).
     * Frontend can use this to decide whether to show a maintenance page.
     */
    @GetMapping("/maintenance")
    public ResponseEntity<MaintenanceResponse> getMaintenance() {
        boolean maintenance = managerService.isMaintenanceMode();
        return ResponseEntity.ok(new MaintenanceResponse(maintenance));
    }
}
