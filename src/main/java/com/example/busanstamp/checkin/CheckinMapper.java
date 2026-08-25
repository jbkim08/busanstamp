package com.example.busanstamp.checkin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CheckinMapper {
    // 중복확인
    int existsByUserAndPlace(@Param("userId") Long userId,
                             @Param("placeId") Long placeId);

    // 새 체크인 저장
    int save(Checkin checkin);
}