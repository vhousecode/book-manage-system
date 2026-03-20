package com.book.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.user.dto.*;
import com.book.user.entity.User;

import java.util.List;

/**
 * User Service Interface
 */
public interface UserService extends IService<User> {

    /**
     * User login
     */
    LoginResponse login(LoginRequest request);

    /**
     * User registration
     */
    LoginResponse register(RegisterRequest request);

    /**
     * Get user by ID
     */
    UserResponse getUserById(Long id);

    /**
     * Get user by username
     */
    User getByUsername(String username);

    /**
     * Get user page list
     */
    Page<UserResponse> getUserPage(String username, String phone, Integer status, int pageNum, int pageSize);

    /**
     * Create user
     */
    Long createUser(User user, List<Long> roleIds);

    /**
     * Update user
     */
    void updateUser(User user, List<Long> roleIds);

    /**
     * Delete user
     */
    void deleteUser(Long id);

    /**
     * Update user status
     */
    void updateStatus(Long id, Integer status);

    /**
     * Change password
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * Get user roles
     */
    List<String> getUserRoleKeys(Long userId);

    /**
     * Get current user info
     */
    UserResponse getCurrentUser(Long userId);
}
