---
name: sql-standards
description: 确保SQL脚本遵循一致的数据库Schema设计、数据插入和优化标准。当用户需要创建数据库表、编写数据迁移脚本、设计数据库Schema、添加索引或约束、创建种子数据脚本时使用此技能。
---

# SQL 标准规范技能

## 技能描述
本技能确保所有SQL脚本遵循一致的数据库Schema设计、数据插入和优化标准，特别处理字符编码和注释规范。

## 触发场景
- 创建数据库表
- 编写数据迁移脚本
- 设计数据库Schema
- 添加索引或约束
- 创建种子/初始数据脚本

## 执行步骤

### 1. 数据库设计原则
- 使用 `utf8mb4` 字符集和 `utf8mb4_unicode_ci` 排序规则
- 选择合适的数据类型（避免过度分配）
- 设计合适的主键（推荐自增BIGINT）
- 为频繁查询的列添加索引

### 2. 表命名规范
- 表名使用 snake_case（下划线命名法）
- 使用复数名词（如 `users`, `books`, `borrow_records`）
- 使用有意义的前缀进行分类（如 `sys_`, `app_`）

### 3. 列标准
- 列名使用 snake_case
- 列名使用英文，注释使用中文简体
- 始终包含 `id` 作为主键（BIGINT UNSIGNED AUTO_INCREMENT）
- 包含审计列：`created_at`, `updated_at`
- 使用 `deleted` 实现软删除（TINYINT DEFAULT 0）

### 4. 注释标准
**重要**：注释统一使用中文简体，提高可读性
```sql
-- 正确
`name` VARCHAR(100) NOT NULL COMMENT '用户名',
`status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',

-- 避免（使用英文注释）
`name` VARCHAR(100) NOT NULL COMMENT 'User name',
```

### 5. 索引设计
- 为外键添加索引
- 为WHERE条件中常用的列添加索引
- 为多列查询使用复合索引
- 索引命名格式：`idx_{表名}_{列名}`

### 6. 表模板
```sql
CREATE TABLE `table_name` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  -- 业务列
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  -- 审计列
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标志：0-正常，1-已删除',
  -- 索引
  KEY `idx_name` (`name`) USING BTREE,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表描述';
```

### 7. 数据插入标准
```sql
-- 使用显式列名
INSERT INTO `table_name` (`col1`, `col2`, `col3`) VALUES
(1, 'value1', 'value2'),
(2, 'value3', 'value4');

-- 批量插入提高性能
INSERT INTO `users` (`username`, `password`, `nickname`, `status`) VALUES
('admin', '$2a$10$...', '管理员', 1),
('user01', '$2a$10$...', '用户一', 1);
```

## 输出标准

### Schema文件（schema.sql）
```sql
-- =============================================
-- Database: book_manage
-- Description: 图书管理系统数据库结构
-- Created: 2024-03-19
-- =============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table: sys_user
-- Description: 系统用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  ...
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
```

### Data文件（data.sql）
```sql
-- =============================================
-- Database: book_manage
-- Description: 初始化数据
-- Created: 2024-03-19
-- =============================================

-- ----------------------------
-- 默认角色
-- ----------------------------
INSERT INTO `sys_role` ...

-- ----------------------------
-- 默认用户（密码：123456）
-- ----------------------------
INSERT INTO `sys_user` ...
```

## 常用模式

### 用户表模式
```sql
CREATE TABLE `sys_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码哈希',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `email` VARCHAR(100) COMMENT '邮箱地址',
  `phone` VARCHAR(20) COMMENT '手机号',
  `avatar` VARCHAR(255) COMMENT '头像地址',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_email` (`email`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
```

### 关联表模式
```sql
CREATE TABLE `sys_user_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` BIGINT UNSIGNED NOT NULL COMMENT '角色ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
```

### 审计日志表模式
```sql
CREATE TABLE `operation_log` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED COMMENT '操作用户ID',
  `module` VARCHAR(50) COMMENT '模块名称',
  `action` VARCHAR(50) COMMENT '操作类型',
  `content` TEXT COMMENT '操作内容',
  `ip` VARCHAR(50) COMMENT 'IP地址',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
```

## 版本信息
- 版本：1.0.0
- 最后更新：2024-03-19
