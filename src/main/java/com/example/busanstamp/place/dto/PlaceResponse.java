package com.example.busanstamp.place.dto;

import com.example.busanstamp.place.Place;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlaceResponse(
        Long placeId,
        String name,
        String description,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String category,
        String imageUrl,

        String kakaoPlaceId,
        String phone,
        String kakaoPlaceUrl,
        String kakaoCategoryName,

        Long createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PlaceResponse from(Place place) {
        return new PlaceResponse(
                place.getPlaceId(),
                place.getName(),
                place.getDescription(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getCategory(),
                place.getImageUrl(),

                place.getKakaoPlaceId(),
                place.getPhone(),
                place.getKakaoPlaceUrl(),
                place.getKakaoCategoryName(),

                place.getCreatedBy(),
                place.getCreatedAt(),
                place.getUpdatedAt()
        );
    }
}