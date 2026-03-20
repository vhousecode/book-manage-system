#!/bin/bash
# Generate Table Script
# Usage: ./generate-table.sh <table-name> <table-description>

TABLE_NAME=${1:-"example_table"}
TABLE_DESC=${2:-"示例表描述"}

# Convert to snake_case if needed
TABLE_NAME=$(echo $TABLE_NAME | sed 's/\([A-Z]\)/_\L\1/g' | sed 's/^_//')

cat << EOF
-- =============================================
-- Table: ${TABLE_NAME}
-- Description: ${TABLE_DESC}
-- Created: $(date +%Y-%m-%d)
-- =============================================

DROP TABLE IF EXISTS \`${TABLE_NAME}\`;
CREATE TABLE \`${TABLE_NAME}\` (
  \`id\` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  \`name\` VARCHAR(100) NOT NULL COMMENT '名称',
  \`status\` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  \`created_at\` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  \`updated_at\` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  \`deleted\` TINYINT DEFAULT 0 COMMENT '删除标志：0-正常，1-已删除',
  KEY \`idx_name\` (\`name\`) USING BTREE,
  PRIMARY KEY (\`id\`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='${TABLE_DESC}';

EOF

echo "✅ 表SQL已生成: ${TABLE_NAME}"