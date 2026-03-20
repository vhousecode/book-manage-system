package com.book.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.common.constant.SystemConstants;
import com.book.common.result.PageResult;
import com.book.common.result.Result;
import com.book.user.dto.UserResponse;
import com.book.user.entity.User;
import com.book.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Management Controller
 */
@Tag(name = "User Management", description = "User management APIs")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user list with pagination")
    @GetMapping("/list")
    public Result<PageResult<UserResponse>> getUserList(
            @Parameter(description = "Username") @RequestParam(required = false) String username,
            @Parameter(description = "Phone") @RequestParam(required = false) String phone,
            @Parameter(description = "Status") @RequestParam(required = false) Integer status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<UserResponse> page = userService.getUserPage(username, phone, status, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public Result<UserResponse> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @Operation(summary = "Create user")
    @PostMapping
    public Result<Long> createUser(@RequestBody User user,
                                   @RequestParam(required = false) List<Long> roleIds) {
        return Result.success(userService.createUser(user, roleIds));
    }

    @Operation(summary = "Update user")
    @PutMapping
    public Result<Void> updateUser(@RequestBody User user,
                                   @RequestParam(required = false) List<Long> roleIds) {
        userService.updateUser(user, roleIds);
        return Result.success();
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @Operation(summary = "Update user status")
    @PutMapping("/status")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "Get user roles")
    @GetMapping("/{id}/roles")
    public Result<List<String>> getUserRoles(@PathVariable Long id) {
        return Result.success(userService.getUserRoleKeys(id));
    }
}
