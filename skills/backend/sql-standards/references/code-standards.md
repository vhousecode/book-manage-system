# SQL 代码规范参考

## 数据库设计原则

### 字符集
- 始终使用 `utf8mb4` 字符集
- 使用 `utf8mb4_unicode_ci` 排序规则进行不区分大小写的比较

### 存储引擎
- 事务表使用 `InnoDB` 引擎
- 仅在读多写少、无事务需求的场景使用 `MyISAM`

### 主键
- 始终使用 `BIGINT UNSIGNED AUTO_INCREMENT` 作为主键
- 主键列命名为 `id`

### 时间戳
- 始终包含 `created_at` 并设置 `DEFAULT CURRENT_TIMESTAMP`
- 始终包含 `updated_at` 并设置 `ON UPDATE CURRENT_TIMESTAMP`

## 表模板

### 主表模板
```sql
CREATE TABLE `table_name` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '名称',
  `code` VARCHAR(50) COMMENT '编码',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `sort` INT DEFAULT 0 COMMENT '排序号',
  `remark` VARCHAR(500) COMMENT '备注',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标志：0-正常，1-已删除',
  KEY `idx_name` (`name`) USING BTREE,
  KEY `idx_code` (`code`) USING BTREE,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表描述';
```

### 交易表模板
```sql
CREATE TABLE `transaction_table` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(18, 2) NOT NULL COMMENT '金额',
  `type` TINYINT NOT NULL COMMENT '类型：1-类型1，2-类型2',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-成功，2-失败',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_created_at` (`created_at`) USING BTREE,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易表';
```

### 关联表模板
```sql
CREATE TABLE `relation_table` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `left_id` BIGINT UNSIGNED NOT NULL COMMENT '左实体ID',
  `right_id` BIGINT UNSIGNED NOT NULL COMMENT '右实体ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `uk_relation` (`left_id`, `right_id`),
  KEY `idx_left_id` (`left_id`) USING BTREE,
  KEY `idx_right_id` (`right_id`) USING BTREE,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关联表';
```

## 数据类型参考

| 用途 | 推荐类型 | 说明 |
|------|----------|------|
| 主键 | `BIGINT UNSIGNED` | 自增 |
| 状态/标志 | `TINYINT` | 0-255范围 |
| 布尔值 | `TINYINT(1)` | 0=false, 1=true |
| 短字符串 | `VARCHAR(50)` | 代码、用户名 |
| 中等字符串 | `VARCHAR(100)` | 名称、标题 |
| 长字符串 | `VARCHAR(255)` | URL、路径 |
| 文本内容 | `TEXT` | 描述 |
| 金额/价格 | `DECIMAL(18, 2)` | 精确小数 |
| 百分比 | `DECIMAL(5, 2)` | 0.00-100.00 |
| 日期 | `DATE` | YYYY-MM-DD |
| 日期时间 | `DATETIME` | YYYY-MM-DD HH:MM:SS |
| JSON | `JSON` | MySQL 5.7+ |

## 索引规范

### 何时添加索引
- 外键列
- WHERE条件中使用的列
- ORDER BY使用的列
- JOIN条件使用的列

### 索引命名规范
- 单列索引：`idx_{列名}`
- 多列索引：`idx_{表名}_{列名}`
- 唯一约束：`uk_{列名}`

### 示例
```sql
KEY `idx_user_id` (`user_id`) USING BTREE,
KEY `idx_status_created_at` (`status`, `created_at`) USING BTREE,
UNIQUE KEY `uk_email` (`email`)
```

## 常用模式

### 用户表
```sql
CREATE TABLE `sys_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码哈希',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `email` VARCHAR(100) COMMENT '邮箱',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 角色表
```sql
CREATE TABLE `sys_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(50) NOT NULL COMMENT '角色标识',
  `sort` INT DEFAULT 0 COMMENT '排序号',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` VARCHAR(500) COMMENT '备注',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
  UNIQUE KEY `uk_role_key` (`role_key`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

## 数据插入模式

### 基本插入
```sql
INSERT INTO `table_name` (`col1`, `col2`, `status`)
VALUES ('value1', 'value2', 1);
```

### 批量插入
```sql
INSERT INTO `sys_role` (`role_name`, `role_key`, `sort`, `status`) VALUES
('管理员', 'admin', 1, 1),
('图书管理员', 'librarian', 2, 1),
('读者', 'reader', 3, 1);
```

### 带密码的插入
```sql
-- 密码：123456（BCrypt哈希）
INSERT INTO `sys_user` (`username`, `password`, `nickname`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员', 1);
```
