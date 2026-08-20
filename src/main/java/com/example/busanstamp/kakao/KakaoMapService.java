package com.example.busanstamp.kakao;

import com.example.busanstamp.common.ApiException;
import com.example.busanstamp.kakao.dto.KakaoPlaceResponse;
import com.example.busanstamp.kakao.dto.KakaoPlaceSearchResponse;
import com.example.busanstamp.kakao.dto.KakaoPlaceSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final RestClient kakaoRestClient;

    public KakaoPlaceSearchResult searchPlaces(String query, int page, int size) {
        validateSearchRequest(query, page, size); // 검색어 등 확인

        try {
            KakaoPlaceSearchResponse response = kakaoRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                                                    .path("/v2/local/search/keyword.json")
                                                    .queryParam("query", query.trim())
                                                    .queryParam("page", page)
                                                    .queryParam("size", size)
                                                    .build())
                    .retrieve()
                    .body(KakaoPlaceSearchResponse.class);

            if (response == null || response.meta() == null) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, "카카오 장소 검색 응답이 올바르지 않습니다.");
            }
            //카카오에서 응답을 받았으면 document => KakaoPlaceResponse 변환함
            List<KakaoPlaceResponse> places = response.documents() == null ?
                    List.of() : response.documents().stream().map(KakaoPlaceResponse::from).toList();

            return new KakaoPlaceSearchResult(response.meta().totalCount(), response.meta().pageableCount(), response.meta().isEnd(), places);

        } catch (RestClientException exception) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "카카오 장소 검색에 실패했습니다.");
        }
    }

    //검색어와 맞는 페이지와 사이즈인지 확인하는 메소드
    private void validateSearchRequest(String query, int page, int size) {
        if (query == null || query.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "검색어를 입력해주세요.");
        }

        if (page < 1 || page > 45) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "페이지는 1부터 45까지 입력할 수 있습니다.");
        }

        if (size < 1 || size > 15) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "페이지 크기는 1부터 15까지 입력할 수 있습니다.");
        }
    }
}
