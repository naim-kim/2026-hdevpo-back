package com.csee.swplus.mileage.subitem.mapper;

import com.csee.swplus.mileage.subitem.dto.SubitemNamesDto;
import com.csee.swplus.mileage.subitem.dto.SubitemResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SubitemMapper {
    List<SubitemResponseDto> findSubitems(
            @Param("studentId") String studentId,
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("semester") String semester,
            @Param("done") String done
    );

    SubitemNamesDto findNamesBySubitemId(@Param("subitemId") Integer subitemId);
}
