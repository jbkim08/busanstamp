package com.example.busanstamp.place;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceMapper {
    //검색어와 카테고리 조건으로 장소를 검색함
    List<Place> findAll(
            @Param("keyword") String keyword,
            @Param("category") String category
    );
    //placeId로 장소를 상세조회
    Place findById(
            @Param("placeId") Long placeId
    );
    //저장되어 있는 카카오장소가 있는지 확인
    int existsByKakaoPlaceId(
            @Param("kakaoPlaceId") String kakaoPlaceId
    );
    //신규 장소저장
    int save(Place place);
    //기존 장소 업데이트
    int update(Place place);
    //아이디로 삭제하기
    int deleteById(
            @Param("placeId") Long placeId
    );
}