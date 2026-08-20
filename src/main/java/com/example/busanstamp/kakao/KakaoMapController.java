package com.example.busanstamp.kakao;

import com.example.busanstamp.kakao.dto.KakaoPlaceSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/kakao/places")
@RequiredArgsConstructor
public class KakaoMapController {

    private final KakaoMapService kakaoMapService;

    //카카오 맵 API 로 요청해서 결과를 받아 응답
    @GetMapping("/search")
    public KakaoPlaceSearchResult searchPlaces(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        return kakaoMapService.searchPlaces(query, page, size);
    }
}