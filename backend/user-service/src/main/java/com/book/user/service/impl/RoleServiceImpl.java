package com.book.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.common.exception.BusinessException;
import com.book.common.result.ResultCode;
import com.book.user.dto.RoleResponse;
import com.book.user.entity.Role;
import com.book.user.mapper.RoleMapper;
import com.book.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Role Service Implementation
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = list(new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, 0)
                .orderByAsc(Role::getSort));

        return roles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Long createRole(Role role) {
        // Check if role key exists
        Role existingRole = getOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleKey, role.getRoleKey())
                .eq(Role::getDeleted, 0));

        if (existingRole != null) {
            throw new BusinessException(ResultCode.CONFLICT, "Role key already exists");
        }

        role.setDeleted(0);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        save(role);

        return role.getId();
    }

    @Override
    public void updateRole(Role role) {
        Role existingRole = getById(role.getId());
        if (existingRole == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Role not found");
        }

        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = getById(id);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Role not found");
        }

        // Soft delete
        role.setDeleted(1);
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
    }

    private RoleResponse convertToResponse(Role role) {
        RoleResponse response = new RoleResponse();
        BeanUtil.copyProperties(role, response);
        if (role.getCreateTime() != null) {
            response.setCreateTime(role.getCreateTime().format(DATE_FORMATTER));
        }
        return response;
    }
}
