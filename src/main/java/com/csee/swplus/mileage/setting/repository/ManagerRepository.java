package com.csee.swplus.mileage.setting.repository;

import com.csee.swplus.mileage.setting.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long>{
    Optional<Manager> findById(Long id);

    /**
     * Fetches only mypage_announcement so /announcement does not depend on
     * other columns (contact_info, reg_start type, etc.). Returns empty if
     * column or row is missing.
     */
    @Query(value = "SELECT mypage_announcement FROM _sw_manager_setting WHERE id = :id", nativeQuery = true)
    Optional<String> findMypageAnnouncementById(@Param("id") long id);

    /**
     * Fetches only contact_info so /contact does not depend on full entity.
     * Returns empty if column or row is missing (table may not have contact_info yet).
     */
    @Query(value = "SELECT contact_info FROM _sw_manager_setting WHERE id = :id", nativeQuery = true)
    Optional<String> findContactInfoById(@Param("id") long id);
}
