package com.example.busanstamp.place;

import com.example.busanstamp.common.ApiException;
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
    public List<PlaceResponse> findAll(
            String keyword,
            String category
    ) {
        //널값이나 공백만 넣으면 => 널값처리 , 검색어와 공백이 있을때 => 공백만 제거
        String normalizedKeyword = normalize(keyword);
        String normalizedCategory = normalize(category);

        return placeMapper
                .findAll(normalizedKeyword, normalizedCategory)
                .stream()
                .map(PlaceResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlaceResponse findById(Long placeId) {
        Place place = getPlace(placeId);

        return PlaceResponse.from(place);
    }

    @Transactional
    public PlaceResponse create(
            PlaceCreateRequest request,
            Long adminUserId
    ) {
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
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "관광 장소 등록에 실패했습니다."
            );
        }

        return PlaceResponse.from(getPlace(place.getPlaceId()));
    }

    @Transactional
    public PlaceResponse update(
            Long placeId,
            PlaceUpdateRequest request
    ) {
        // 존재하는 장소인지 먼저 확인
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
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "관광 장소 수정에 실패했습니다."
            );
        }

        return PlaceResponse.from(getPlace(placeId));
    }

    @Transactional
    public void delete(Long placeId) {
        getPlace(placeId);

        int result = placeMapper.deleteById(placeId);

        if (result != 1) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "관광 장소 삭제에 실패했습니다."
            );
        }
    }

    private Place getPlace(Long placeId) {
        Place place = placeMapper.findById(placeId);

        if (place == null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "관광 장소를 찾을 수 없습니다."
            );
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