package com.example.busanstamp.place;

import com.example.busanstamp.place.dto.PlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public List<PlaceResponse> getPlaces(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {
        return placeService.findAll(keyword, category);
    }

    @GetMapping("/{placeId}")
    public PlaceResponse getPlace(
            @PathVariable Long placeId
    ) {
        return placeService.findById(placeId);
    }
}