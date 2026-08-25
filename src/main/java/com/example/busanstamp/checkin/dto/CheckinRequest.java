package com.example.busanstamp.checkin.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckinRequest(

        @NotBlank
        String token

) {
}