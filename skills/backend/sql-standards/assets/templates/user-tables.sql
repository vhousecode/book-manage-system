-- =============================================
-- User Table Template
-- =============================================

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `username` VARCHAR(50) NOT NULL COMMENT 'Username',
  `password` VARCHAR(255) NOT NULL COMMENT 'Password hash',
  `nickname` VARCHAR(50) COMMENT 'Display name',
  `email` VARCHAR(100) COMMENT 'Email address',
  `phone` VARCHAR(20) COMMENT 'Phone number',
  `avatar` VARCHAR(255) COMMENT 'Avatar URL',
  `gender` TINYINT DEFAULT 0 COMMENT 'Gender: 0-unknown, 1-male, 2-female',
  `status` TINYINT DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  `deleted` TINYINT DEFAULT 0 COMMENT 'Delete flag',
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_email` (`email`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System user table';

-- =============================================
-- Role Table Template
-- =============================================

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `role_name` VARCHAR(50) NOT NULL COMMENT 'Role name',
  `role_key` VARCHAR(50) NOT NULL COMMENT 'Role key for permission check',
  `sort` INT DEFAULT 0 COMMENT 'Sort order',
  `status` TINYINT DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `remark` VARCHAR(500) COMMENT 'Remark',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  `deleted` TINYINT DEFAULT 0 COMMENT 'Delete flag',
  UNIQUE KEY `uk_role_key` (`role_key`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System role table';

-- =============================================
-- User Role Association Table Template
-- =============================================

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT 'User ID',
  `role_id` BIGINT UNSIGNED NOT NULL COMMENT 'Role ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User role association table';
