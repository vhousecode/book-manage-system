package com.book.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * Role Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
