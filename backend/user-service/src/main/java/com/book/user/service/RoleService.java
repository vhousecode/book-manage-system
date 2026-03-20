package com.book.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.book.user.dto.RoleResponse;
import com.book.user.entity.Role;

import java.util.List;

/**
 * Role Service Interface
 */
public interface RoleService extends IService<Role> {

    /**
     * Get all roles
     */
    List<RoleResponse> getAllRoles();

    /**
     * Create role
     */
    Long createRole(Role role);

    /**
     * Update role
     */
    void updateRole(Role role);

    /**
     * Delete role
     */
    void deleteRole(Long id);
}
