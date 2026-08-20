package com.example.busanstamp.kakao.dto;

import java.util.List;

// 카카오에서 응답받은 전체 데이터를 프론트로 전달하는 객체
public record KakaoPlaceSearchResult(
        int totalCount,
        int pageableCount,
        boolean end,
        List<KakaoPlaceResponse> places
) {
}