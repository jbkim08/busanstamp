package com.example.busanstamp.place.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record KakaoPlaceImportRequest(

        @NotBlank(message = "카카오 장소 ID가 필요합니다.")
        String kakaoPlaceId,

        @NotBlank(message = "장소 이름이 필요합니다.")
        @Size(max = 100, message = "장소 이름은 최대 100자까지 가능.")
        String name,

        @Size(max = 255, message = "카테고리명은 최대 255자.")
        String categoryName,

        @Size(max = 50)
        String categoryGroupName,

        @Size(max = 30)
        String phone,

        @Size(max = 255, message = "지번주소는 255자까지 가능")
        String address,

        @Size(max = 255, message = "도로명주소는 255자까지 가능")
        String roadAddress,

        @NotNull(message = "경도가 필요합니다.")
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        BigDecimal longitude,

        @NotNull(message = "위도가 필요합니다.")
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        BigDecimal latitude,

        @Size(max = 500)
        String placeUrl,

        // 관리자가 추가로 관광지 설명 작성
        @Size(max = 2000)
        String description,

        // 관리자가 추가로 관광지 이미지 주소를 입력
        @Size(max = 500)
        String imageUrl
) {
}