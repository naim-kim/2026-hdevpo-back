package com.csee.swplus.mileage.subitem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubitemResponseDto {
    private Integer subitemId;
    private String subitemName;
    private Integer categoryId;
    private String categoryName;
    private String semester;
    private Boolean done;
    private Integer recordId;
    private String description1;

    // 포트폴리오에 연결되어 있는지 여부 (새 필드)
    private Boolean inPortfolio;
}
