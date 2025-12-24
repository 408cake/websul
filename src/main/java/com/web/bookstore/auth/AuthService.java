package com.web.bookstore.auth;

import com.web.bookstore.auth.dto.AuthResponse;
import com.web.bookstore.auth.dto.LoginRequest;
import com.web.bookstore.auth.dto.RefreshRequest;
import com.web.bookstore.auth.dto.SignupRequest;
import com.web.bookstore.common.ApiException;
import com.web.bookstore.common.ErrorCode;
import com.web.bookstore.security.JwtTokenProvider;
import com.web.bookstore.user.User;
import com.web.bookstore.user.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final long refreshTtlSeconds;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenService refreshTokenService,
            @Value("${jwt.refresh-ttl-seconds}") long refreshTtlSeconds
    ) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    public AuthResponse signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS, req.email());
        }
        String hash = encoder.encode(req.password());
        User user = userRepository.save(new User(req.email(), hash, "USER"));
        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), null, null);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CREDENTIALS, null));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS, null);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        Claims c = jwtTokenProvider.parse(refreshToken);
        refreshTokenService.save(user.getId(), c.getId(), refreshTtlSeconds);

        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshRequest req) {
        Claims c;

        try {
            c = jwtTokenProvider.parse(req.refreshToken());
        } catch (Exception e) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID, null);
        }

        if (!"refresh".equals(String.valueOf(c.get("typ")))) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID, null);
        }

        Long userId = Long.valueOf(c.getSubject());
        String jti = c.getId();

        if (!refreshTokenService.exists(userId, jti)) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REVOKED, null);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_CREDENTIALS, null));

        String newAccess = jwtTokenProvider.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(user.getId(), user.getEmail(), user.getRole(), newAccess, req.refreshToken());
    }


    public void logout(RefreshRequest req) {
        Claims c;

        try {
            c = jwtTokenProvider.parse(req.refreshToken());
        } catch (Exception e) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_INVALID, null);
        }

        Long userId = Long.valueOf(c.getSubject());
        String jti = c.getId();

        refreshTokenService.delete(userId, jti);
    }
}
