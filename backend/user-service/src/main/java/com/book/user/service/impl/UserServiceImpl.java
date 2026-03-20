package com.book.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.common.exception.BusinessException;
import com.book.common.result.ResultCode;
import com.book.common.utils.JwtUtils;
import com.book.common.utils.PasswordUtils;
import com.book.user.dto.*;
import com.book.user.entity.Role;
import com.book.user.entity.User;
import com.book.user.entity.UserRole;
import com.book.user.mapper.RoleMapper;
import com.book.user.mapper.UserMapper;
import com.book.user.mapper.UserRoleMapper;
import com.book.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Value("${jwt.secret:book-manage-system-jwt-secret-key-must-be-at-least-256-bits}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LoginResponse login(LoginRequest request) {
        // Find user by username
        User user = getByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND, "User not found");
        }

        // Check password
        if (!PasswordUtils.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // Check status
        if (user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // Get user roles
        List<String> roles = getUserRoleKeys(user.getId());

        // Generate token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername(), jwtSecret, jwtExpiration);

        // Build response
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRoles(roles);
        response.setToken(token);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse register(RegisterRequest request) {
        // Check if username exists
        User existingUser = getByUsername(request.getUsername());
        if (existingUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.encode(request.getPassword()));
        user.setNickname(StrUtil.isBlank(request.getNickname()) ? request.getUsername() : request.getNickname());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setGender(0);
        user.setStatus(1);
        user.setDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        save(user);

        // Assign default role (reader)
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(3L); // Default reader role
        userRole.setCreateTime(LocalDateTime.now());
        userRoleMapper.insert(userRole);

        // Generate token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername(), jwtSecret, jwtExpiration);

        // Build response
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setRoles(List.of("reader"));
        response.setToken(token);

        return response;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return convertToResponse(user);
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0));
    }

    @Override
    public Page<UserResponse> getUserPage(String username, String phone, Integer status, int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDeleted, 0);
        
        if (StrUtil.isNotBlank(username)) {
            wrapper.like(User::getUsername, username);
        }
        if (StrUtil.isNotBlank(phone)) {
            wrapper.like(User::getPhone, phone);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> userPage = page(page, wrapper);

        Page<UserResponse> responsePage = new Page<>();
        responsePage.setCurrent(userPage.getCurrent());
        responsePage.setSize(userPage.getSize());
        responsePage.setTotal(userPage.getTotal());
        responsePage.setPages(userPage.getPages());
        responsePage.setRecords(userPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));

        return responsePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(User user, List<Long> roleIds) {
        // Check username
        if (getByUsername(user.getUsername()) != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        user.setPassword(PasswordUtils.encode(user.getPassword()));
        user.setDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        save(user);

        // Assign roles
        if (roleIds != null && !roleIds.isEmpty()) {
            assignRoles(user.getId(), roleIds);
        }

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user, List<Long> roleIds) {
        User existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // Update user info (don't update password here)
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);

        // Update roles
        if (roleIds != null) {
            // Delete old roles
            userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                    .eq(UserRole::getUserId, user.getId()));
            // Assign new roles
            assignRoles(user.getId(), roleIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // Soft delete
        user.setDeleted(1);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);

        // Delete user roles
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        User user = getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // Verify old password
        if (!PasswordUtils.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR, "Old password is incorrect");
        }

        // Update password
        user.setPassword(PasswordUtils.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    public List<String> getUserRoleKeys(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));

        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return roles.stream()
                .filter(r -> r.getStatus() == 1)
                .map(Role::getRoleKey)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        return getUserById(userId);
    }

    /**
     * Assign roles to user
     */
    private void assignRoles(Long userId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(LocalDateTime.now());
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * Convert User entity to UserResponse DTO
     */
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtil.copyProperties(user, response);

        if (user.getCreateTime() != null) {
            response.setCreateTime(user.getCreateTime().format(DATE_FORMATTER));
        }

        // Get user roles
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));

        if (!userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream()
                    .map(UserRole::getRoleId)
                    .collect(Collectors.toList());
            response.setRoleIds(roleIds);

            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            List<RoleResponse> roleResponses = roles.stream()
                    .map(this::convertToRoleResponse)
                    .collect(Collectors.toList());
            response.setRoles(roleResponses);
        }

        return response;
    }

    /**
     * Convert Role entity to RoleResponse DTO
     */
    private RoleResponse convertToRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        BeanUtil.copyProperties(role, response);
        if (role.getCreateTime() != null) {
            response.setCreateTime(role.getCreateTime().format(DATE_FORMATTER));
        }
        return response;
    }
}
