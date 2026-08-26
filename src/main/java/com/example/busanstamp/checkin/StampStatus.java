package com.example.busanstamp.checkin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StampStatus {

    private Long placeId;

    private String name;

    private String category;

    private String address;

    private String imageUrl;

    private boolean acquired;

    private LocalDateTime checkedInAt;
}