package com.example.busanstamp.place.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PlaceUpdateRequest(

        @NotBlank(message = "장소 이름을 입력해주세요.")
        @Size(max = 100, message = "장소 이름은 100자 이내로 적어주세요.")
        String name,

        @Size(max = 2000, message = "장소 설명은 2000자 이내로 적어주세요.")
        String description,

        @NotBlank(message = "주소를 입력해주세요.")
        @Size(max = 255, message = "최대 255자 이내로 적어주세요.")
        String address,

        @NotNull(message = "위도를 입력해주세요.")
        @DecimalMin( value = "-90.0", message = "-90.0 이상만 입력")
        @DecimalMax(value = "90.0", message = "90.0 이하로 입력")
        BigDecimal latitude,

        @NotNull(message = "경도를 입력해주세요.")
        @DecimalMin(value = "-180.0", message = "최소 -180.0 이상으로 입력")
        @DecimalMax(value = "180.0", message = "최대 180 이내로 입력")
        BigDecimal longitude,

        @NotBlank(message = "카테고리를 입력해주세요.")
        @Size(max = 50, message = "카테고리는 50자 이내로 적어주세요")
        String category,

        @Size(max = 500, message = "이미지 주소는 500자 이내로 적어주세요")
        String imageUrl
) {
}