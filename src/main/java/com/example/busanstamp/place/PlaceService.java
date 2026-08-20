package com.example.busanstamp.place;

import com.example.busanstamp.common.ApiException;
import com.example.busanstamp.place.dto.KakaoPlaceImportRequest;
import com.example.busanstamp.place.dto.PlaceCreateRequest;
import com.example.busanstamp.place.dto.PlaceResponse;
import com.example.busanstamp.place.dto.PlaceUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceMapper placeMapper;

    @Transactional(readOnly = true)
    public List<PlaceResponse> findAll(String keyword, String category) {
        //널값이나 공백만 넣으면 => 널값처리 , 검색어와 공백이 있을때 => 공백만 제거
        String normalizedKeyword = normalize(keyword);
        String normalizedCategory = normalize(category);

        return placeMapper.findAll(normalizedKeyword, normalizedCategory).stream().map(PlaceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PlaceResponse findById(Long placeId) {
        Place place = getPlace(placeId);

        return PlaceResponse.from(place);
    }

    @Transactional
    public PlaceResponse create(PlaceCreateRequest request, Long adminUserId) {
        Place place = new Place();

        place.setName(request.name().trim());
        place.setDescription(trimToNull(request.description()));
        place.setAddress(request.address().trim());

        place.setLatitude(request.latitude());
        place.setLongitude(request.longitude());

        place.setCategory(request.category().trim());
        place.setImageUrl(trimToNull(request.imageUrl()));

        place.setCreatedBy(adminUserId);

        int result = placeMapper.save(place);

        if (result != 1) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "관광 장소 등록에 실패했습니다.");
        }

        return PlaceResponse.from(getPlace(place.getPlaceId()));
    }

    @Transactional
    public PlaceResponse createFromKakao(KakaoPlaceImportRequest request, Long adminUserId) {
        if (placeMapper.existsByKakaoPlaceId(request.kakaoPlaceId()) > 0) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 등록된 카카오 장소입니다.");
        }

        String address = selectAddress(request.roadAddress(), request.address());

        String category = resolveCategory(request.categoryGroupName(), request.categoryName());

        Place place = new Place();

        place.setName(request.name().trim());
        place.setDescription(trimToNull(request.description()));
        place.setAddress(address);

        place.setLongitude(request.longitude());
        place.setLatitude(request.latitude());

        place.setCategory(category);
        place.setImageUrl(trimToNull(request.imageUrl()));

        place.setKakaoPlaceId(request.kakaoPlaceId().trim());
        place.setPhone(trimToNull(request.phone()));
        place.setKakaoPlaceUrl(trimToNull(request.placeUrl()));
        place.setKakaoCategoryName(trimToNull(request.categoryName()));

        place.setCreatedBy(adminUserId);

        int result = placeMapper.save(place);

        if (result != 1) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 관광 장소 저장에 실패했습니다.");
        }

        return PlaceResponse.from(getPlace(place.getPlaceId()));
    }

    private String selectAddress(String roadAddress, String address) {
        //도로명 주소가 있으면 가져옴
        if (roadAddress != null && !roadAddress.isBlank()) {
            return roadAddress.trim();
        }
        //지번주소가 있으면 가져옴
        if (address != null && !address.isBlank()) {
            return address.trim();
        }
        //둘 다 없을 경우 예외 발생
        throw new ApiException(HttpStatus.BAD_REQUEST, "장소 주소가 필요합니다.");
    }

    //카테고리 문자열을 만든다.
    private String resolveCategory(String categoryGroupName, String categoryName) {
        //카테고리그룹명이 있으면 가져옴
        if (categoryGroupName != null && !categoryGroupName.isBlank()) {
            return categoryGroupName.trim();
        }
        //카테고리명이 없을경우 "기타"
        if (categoryName == null || categoryName.isBlank()) {
            return "기타";
        }
        // 카테고리명을 구분해서 배열에 저장
        String[] categories = categoryName.split(">");
        // 제일 하위 카테고리명
        return categories[categories.length - 1].trim();
    }

    @Transactional
    public PlaceResponse update(Long placeId, PlaceUpdateRequest request) {
        // 존재하는 장소인지 먼저 확인 없으면 예외처리됨
        getPlace(placeId);

        Place place = new Place();

        place.setPlaceId(placeId);
        place.setName(request.name().trim());
        place.setDescription(trimToNull(request.description()));
        place.setAddress(request.address().trim());

        place.setLatitude(request.latitude());
        place.setLongitude(request.longitude());

        place.setCategory(request.category().trim());
        place.setImageUrl(trimToNull(request.imageUrl()));

        int result = placeMapper.update(place);

        if (result != 1) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "관광 장소 수정에 실패했습니다.");
        }

        return PlaceResponse.from(getPlace(placeId));
    }

    @Transactional
    public void delete(Long placeId) {
        getPlace(placeId); // 삭제전 있는지 확인

        int result = placeMapper.deleteById(placeId);

        if (result != 1) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "관광 장소 삭제에 실패했습니다.");
        }
    }

    //placeId로 장소를 찾고 없으면 예외처리
    private Place getPlace(Long placeId) {
        Place place = placeMapper.findById(placeId);

        if (place == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "관광 장소를 찾을 수 없습니다.");
        }

        return place;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null; //빈 값이나 null 일경우 null로 리턴
        }
        // 값이 있을경우 공백을 없애줍니다.
        return value.trim();
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}