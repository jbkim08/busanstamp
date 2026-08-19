package com.example.busanstamp.place;

import com.example.busanstamp.place.dto.PlaceCreateRequest;
import com.example.busanstamp.place.dto.PlaceResponse;
import com.example.busanstamp.place.dto.PlaceUpdateRequest;
import com.example.busanstamp.security.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/places")
@RequiredArgsConstructor
public class AdminPlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<PlaceResponse> createPlace(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody PlaceCreateRequest request
    ) {
        PlaceResponse response = placeService.create(
                request,
                authenticatedUser.userId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{placeId}")
    public PlaceResponse updatePlace(
            @PathVariable Long placeId,
            @Valid @RequestBody PlaceUpdateRequest request
    ) {
        return placeService.update(placeId, request);
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> deletePlace(
            @PathVariable Long placeId
    ) {
        placeService.delete(placeId);

        return ResponseEntity.noContent().build();
    }
}