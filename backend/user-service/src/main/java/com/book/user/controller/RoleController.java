package com.book.user.controller;

import com.book.common.result.Result;
import com.book.user.dto.RoleResponse;
import com.book.user.entity.Role;
import com.book.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Role Management Controller
 */
@Tag(name = "Role Management", description = "Role management APIs")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get all roles")
    @GetMapping("/list")
    public Result<List<RoleResponse>> getAllRoles() {
        return Result.success(roleService.getAllRoles());
    }

    @Operation(summary = "Create role")
    @PostMapping
    public Result<Long> createRole(@RequestBody Role role) {
        return Result.success(roleService.createRole(role));
    }

    @Operation(summary = "Update role")
    @PutMapping
    public Result<Void> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success();
    }

    @Operation(summary = "Delete role")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }
}
