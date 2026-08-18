package com.example.busanstamp.auth;

import com.example.busanstamp.auth.dto.LoginRequest;
import com.example.busanstamp.auth.dto.LoginResponse;
import com.example.busanstamp.auth.dto.SignupRequest;
import com.example.busanstamp.auth.dto.UserResponse;
import com.example.busanstamp.security.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        UserResponse response =
                authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse getMyInfo(
            @AuthenticationPrincipal
            AuthenticatedUser authenticatedUser
    ) {
        return authService.getMyInfo(
                authenticatedUser.userId()
        );
    }
}