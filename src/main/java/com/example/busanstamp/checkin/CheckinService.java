package com.example.busanstamp.checkin;

import com.example.busanstamp.checkin.dto.CheckinResponse;
import com.example.busanstamp.common.ApiException;
import com.example.busanstamp.place.Place;
import com.example.busanstamp.place.PlaceMapper;
import com.example.busanstamp.qr.QrTokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DuplicateKeyException;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckinService {

    private final CheckinMapper checkinMapper;
    private final PlaceMapper placeMapper;
    private final QrTokenProvider qrTokenProvider;


    @Transactional
    public CheckinResponse checkin(String token, Long userId) {

        /*
         * 1. QR Token 검증
         */
        Long placeId = qrTokenProvider.getPlaceId(token);


        /*
         * 2. 장소 존재 확인
         */
        Place place = placeMapper.findById(placeId);


        if (place == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "등록되지 않은 관광 장소입니다.");
        }


        /*
         * 3. 이미 체크인했는지 검사
         */
        if (checkinMapper.existsByUserAndPlace(userId, placeId) > 0) {
            throw new ApiException(HttpStatus.CONFLICT, "이미 스탬프를 획득한 장소입니다.");
        }


        /*
         * 4. Checkin 생성
         */
        Checkin checkin = new Checkin();
        checkin.setUserId(userId);
        checkin.setPlaceId(placeId);
        checkin.setCheckedInAt(LocalDateTime.now());


        try {
            int result = checkinMapper.save(checkin);

            if (result != 1) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "체크인 처리에 실패했습니다.");
            }

        } catch (DuplicateKeyException exception) {

            /*
             * 동시에 두 요청이 들어와도
             * DB UNIQUE가 최종 방어
             */
            throw new ApiException(HttpStatus.CONFLICT, "이미 스탬프를 획득한 장소입니다.");
        }


        return new CheckinResponse(
                checkin.getCheckinId(),
                place.getPlaceId(),
                place.getName(),
                place.getCategory(),
                checkin.getCheckedInAt());
    }
}