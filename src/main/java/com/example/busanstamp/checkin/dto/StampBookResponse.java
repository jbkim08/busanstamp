package com.example.busanstamp.checkin.dto;

import java.util.List;

public record StampBookResponse(

        int totalCount,

        int acquiredCount,

        int remainingCount,

        int progressPercent,

        List<StampResponse> stamps

) {
}