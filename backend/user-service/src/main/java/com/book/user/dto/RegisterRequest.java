package com.book.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * Register Request DTO
 */
@Data
@Schema(description = "Register Request")
public class RegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscores")
    @Schema(description = "Username", example = "testuser")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @Schema(description = "Password", example = "123456")
    private String password;

    @Size(max = 50, message = "Nickname cannot exceed 50 characters")
    @Schema(description = "Display name", example = "Test User")
    private String nickname;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "Invalid phone number format")
    @Schema(description = "Phone number", example = "13800138000")
    private String phone;

    @Email(message = "Invalid email format")
    @Schema(description = "Email address", example = "test@example.com")
    private String email;
}
