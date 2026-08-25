package com.example.busanstamp.checkin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Checkin {

    private Long checkinId;

    private Long userId;

    private Long placeId;

    private LocalDateTime checkedInAt;
}