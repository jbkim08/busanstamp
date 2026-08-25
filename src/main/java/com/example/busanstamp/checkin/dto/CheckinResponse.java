package com.example.busanstamp.checkin.dto;

import java.time.LocalDateTime;

public record CheckinResponse(

        Long checkinId,

        Long placeId,

        String placeName,

        String category,

        LocalDateTime checkedInAt

) {
}