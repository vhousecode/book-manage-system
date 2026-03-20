package com.book.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.user.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * User-Role Relation Mapper
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
