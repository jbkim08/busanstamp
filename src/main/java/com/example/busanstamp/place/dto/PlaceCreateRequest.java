package com.example.busanstamp.place.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

//레코드는 자동으로 생성자, get메소드, hashcode, equals, toString
public record PlaceCreateRequest(

        @NotBlank(message = "장소 이름을 입력해주세요.")
        @Size(
                max = 100,
                message = "장소 이름은 100자 이하로 입력해주세요."
        )
        String name,

        @Size(
                max = 2000,
                message = "장소 설명은 2000자 이하로 입력해주세요."
        )
        String description,

        @NotBlank(message = "주소를 입력해주세요.")
        @Size(
                max = 255,
                message = "주소는 255자 이하로 입력해주세요."
        )
        String address,

        @NotNull(message = "위도를 입력해주세요.")
        @DecimalMin(
                value = "-90.0",
                message = "위도는 -90 이상이어야 합니다."
        )
        @DecimalMax(
                value = "90.0",
                message = "위도는 90 이하여야 합니다."
        )
        BigDecimal latitude,

        @NotNull(message = "경도를 입력해주세요.")
        @DecimalMin(
                value = "-180.0",
                message = "경도는 -180 이상이어야 합니다."
        )
        @DecimalMax(
                value = "180.0",
                message = "경도는 180 이하여야 합니다."
        )
        BigDecimal longitude,

        @NotBlank(message = "카테고리를 입력해주세요.")
        @Size(
                max = 50,
                message = "카테고리는 50자 이하로 입력해주세요."
        )
        String category,

        @Size(
                max = 500,
                message = "이미지 주소는 500자 이하로 입력해주세요."
        )
        String imageUrl
) {
}