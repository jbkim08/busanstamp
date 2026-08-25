package com.example.busanstamp.qr;

import com.example.busanstamp.common.ApiException;
import com.example.busanstamp.place.Place;
import com.example.busanstamp.place.PlaceMapper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;

import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    private final QrTokenProvider qrTokenProvider;
    private final PlaceMapper placeMapper;

    @Value("${app.frontend-url}")
    private String frontendBaseUrl;


    public byte[] createPlaceQr(Long placeId, int size) {

        Place place = placeMapper.findById(placeId);
        //관광 장소가 있는지 체크
        if (place == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "관광 장소를 찾을 수 없습니다.");
        }


        /*
         * QR 체크인 Token
         */
        String token = qrTokenProvider.createToken(placeId);


        /*
         * React 체크인 주소
         */
        String checkinUrl = frontendBaseUrl + "/checkin?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);


        try {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();


            Map<EncodeHintType, Object> hints = new HashMap<>();


            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            hints.put(EncodeHintType.MARGIN, 1);

            //리액트 체크인주소를 QR로 만듬
            BitMatrix bitMatrix = qrCodeWriter.encode(checkinUrl, BarcodeFormat.QR_CODE, size, size, hints);


            ByteArrayOutputStream output = new ByteArrayOutputStream();


            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", output);


            return output.toByteArray();


        } catch (WriterException | IOException exception) {

            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "QR 코드 생성에 실패했습니다.");
        }
    }
}