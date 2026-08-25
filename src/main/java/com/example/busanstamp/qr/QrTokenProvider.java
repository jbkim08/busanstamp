package com.example.busanstamp.qr;

import com.example.busanstamp.common.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class QrTokenProvider {

    private static final String ISSUER = "busan-stamp-api";
    private static final String PURPOSE = "PLACE_CHECKIN";
    private final SecretKey secretKey;

    public QrTokenProvider(@Value("${qr.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 관광 장소 QR용 Token 생성
     */
    public String createToken(Long placeId) {
        return Jwts.builder()
                .issuer(ISSUER)
                .subject(String.valueOf(placeId))
                .claim("purpose", PURPOSE)
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }


    /**
     * QR Token 검증 후
     * placeId 반환
     */
    public Long getPlaceId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            /*
             * 우리 서비스에서 만든
             * 체크인 QR인지 확인
             */
            if (!ISSUER.equals(claims.getIssuer())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "올바른 QR 코드가 아닙니다.");
            }


            String purpose = claims.get("purpose", String.class);


            if (!PURPOSE.equals(purpose)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "체크인용 QR 코드가 아닙니다.");
            }

            return Long.valueOf(claims.getSubject());


        } catch (JwtException | IllegalArgumentException exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "유효하지 않은 QR 코드입니다.");
        }
    }
}