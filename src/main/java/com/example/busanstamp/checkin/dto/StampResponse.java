package com.example.busanstamp.checkin.dto;

import com.example.busanstamp.checkin.StampStatus;

import java.time.LocalDateTime;

public record StampResponse(

        Long placeId,

        String name,

        String category,

        String address,

        String imageUrl,

        boolean acquired,

        LocalDateTime checkedInAt

) {

    public static StampResponse from(
            StampStatus stamp
    ) {

        return new StampResponse(
                stamp.getPlaceId(),
                stamp.getName(),
                stamp.getCategory(),
                stamp.getAddress(),
                stamp.getImageUrl(),
                stamp.isAcquired(),
                stamp.getCheckedInAt()
        );
    }
}