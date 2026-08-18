package com.example.busanstamp.auth;

import com.example.busanstamp.auth.dto.LoginRequest;
import com.example.busanstamp.auth.dto.LoginResponse;
import com.example.busanstamp.auth.dto.SignupRequest;
import com.example.busanstamp.auth.dto.UserResponse;
import com.example.busanstamp.common.ApiException;
import com.example.busanstamp.security.JwtProvider;
import com.example.busanstamp.user.User;
import com.example.busanstamp.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponse signup(SignupRequest request) {
        String email = normalizeEmail(request.email());

        if (userMapper.existsByEmail(email) > 0) {
            throw new ApiException(
                    HttpStatus.CONFLICT, //409
                    "이미 사용 중인 이메일입니다."
            );
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setNickname(request.nickname().trim());
        user.setRole("USER");

        int result = userMapper.save(user);

        if (result != 1) {
            throw new ApiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "회원가입에 실패했습니다."
            );
        }

        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());

        User user = userMapper.findByEmail(email);

        if (user == null
                || !passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {

            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        String accessToken = jwtProvider.createAccessToken(user);

        return new LoginResponse(
                accessToken,
                "Bearer",
                jwtProvider.getExpirationSeconds(),
                UserResponse.from(user)
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getMyInfo(Long userId) {
        User user = userMapper.findById(userId);

        if (user == null) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "사용자를 찾을 수 없습니다."
            );
        }

        return UserResponse.from(user);
    }

    //이메일 문자열을 공백삭제하고 소문자로 바꿔서 리턴
    private String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}