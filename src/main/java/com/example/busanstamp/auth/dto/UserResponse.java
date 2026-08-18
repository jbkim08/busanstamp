package com.example.busanstamp.auth.dto;

import com.example.busanstamp.user.User;

public record UserResponse(
        Long userId,
        String email,
        String nickname,
        String role
) {
    //DB에서 가져온 유저객체의 값을 복사하는 from 메소드
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole()
        );
    }
}