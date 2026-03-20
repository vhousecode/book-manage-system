#!/bin/bash
# Generate Entity from Table Script
# Usage: ./generate-entity.sh <table-name> <class-name>

TABLE_NAME=${1:-"sys_user"}
CLASS_NAME=${2:-"User"}

# Convert to camelCase for fields
to_camel() {
    echo $1 | sed 's/_\([a-z]\)/\u\1/g'
}

cat << EOF
package com.book.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity class for table: ${TABLE_NAME}
 */
@Data
@TableName("${TABLE_NAME}")
public class ${CLASS_NAME} {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("status")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
EOF

echo "✅ Entity class generated: ${CLASS_NAME}.java"
