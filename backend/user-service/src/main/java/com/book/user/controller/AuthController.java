package com.book.user.controller;

import com.book.common.constant.SystemConstants;
import com.book.common.result.Result;
import com.book.user.dto.*;
import com.book.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 */
@Tag(name = "Authentication", description = "User authentication APIs")
@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "User login")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @Operation(summary = "User registration")
    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(userService.register(request));
    }

    @Operation(summary = "User logout")
    @PostMapping("/logout")
    public Result<Void> logout() {
        // In JWT, logout is handled on the client side by removing the token
        return Result.success();
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/info")
    public Result<UserResponse> getCurrentUser(
            @RequestHeader(value = SystemConstants.USER_ID_HEADER, required = false) Long userId) {
        return Result.success(userService.getCurrentUser(userId));
    }

    @Operation(summary = "Change password")
    @PutMapping("/password")
    public Result<Void> changePassword(
            @RequestHeader(SystemConstants.USER_ID_HEADER) Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success();
    }
}
