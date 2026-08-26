package com.example.busanstamp.checkin;

import com.example.busanstamp.checkin.dto.CheckinRequest;
import com.example.busanstamp.checkin.dto.CheckinResponse;
import com.example.busanstamp.security.AuthenticatedUser;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    @PostMapping
    public ResponseEntity<CheckinResponse> checkin(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CheckinRequest request) {

        CheckinResponse response = checkinService.checkin(
                request.token(),
                authenticatedUser.userId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}