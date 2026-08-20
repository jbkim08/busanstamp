package com.example.busanstamp.kakao.dto;

import java.math.BigDecimal;

//카카오맵 API 에서 search로 받은 장소데이터를 복사해서 전달
public record KakaoPlaceResponse(
        String kakaoPlaceId,
        String name,
        String categoryName,
        String categoryGroupCode,
        String categoryGroupName,
        String phone,
        String address,
        String roadAddress,
        BigDecimal longitude,
        BigDecimal latitude,
        String placeUrl,
        String distance
) {

    public static KakaoPlaceResponse from(
            KakaoPlaceSearchResponse.Document document
    ) {
        return new KakaoPlaceResponse(
                document.id(),
                document.placeName(),
                document.categoryName(),
                document.categoryGroupCode(),
                document.categoryGroupName(),
                document.phone(),
                document.addressName(),
                document.roadAddressName(),

                // Kakao x = 경도
                new BigDecimal(document.x()),

                // Kakao y = 위도
                new BigDecimal(document.y()),

                document.placeUrl(),
                document.distance()
        );
    }
}