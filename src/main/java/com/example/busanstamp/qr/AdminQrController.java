package com.example.busanstamp.qr;

import com.example.busanstamp.common.ApiException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminQrController {

    private final QrCodeService qrCodeService;

    @GetMapping("/api/admin/places/{placeId}/qr")
    public ResponseEntity<byte[]> getPlaceQr(@PathVariable Long placeId,
                                             @RequestParam(defaultValue = "320") int size) {

        if (size < 160 || size > 1000) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "QR 크기는 160부터 1000까지 가능합니다.");
        }

        byte[] image = qrCodeService.createPlaceQr(placeId, size);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"place-" + placeId + "-qr.png\"")
                .body(image);
    }
}