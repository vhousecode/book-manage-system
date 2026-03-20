-- =============================================
-- Table Template
-- Replace {{table_name}} and {{table_description}}
-- =============================================

DROP TABLE IF EXISTS `{{table_name}}`;
CREATE TABLE `{{table_name}}` (
  -- Primary Key
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  
  -- Business Fields (customize as needed)
  `name` VARCHAR(100) NOT NULL COMMENT 'Name',
  `code` VARCHAR(50) COMMENT 'Code',
  `description` VARCHAR(500) COMMENT 'Description',
  
  -- Status Fields
  `status` TINYINT DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `sort` INT DEFAULT 0 COMMENT 'Sort order',
  
  -- Audit Fields
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  `deleted` TINYINT DEFAULT 0 COMMENT 'Delete flag: 0-normal, 1-deleted',
  
  -- Indexes
  KEY `idx_name` (`name`) USING BTREE,
  KEY `idx_code` (`code`) USING BTREE,
  
  -- Primary Key
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='{{table_description}}';
