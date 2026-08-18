package com.example.busanstamp.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String role;
    private LocalDateTime createdAt;

}