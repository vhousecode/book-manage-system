# 图书管理系统完整实现方案

## 目录
- [一、项目概述](#一项目概述)
- [二、系统架构设计](#二系统架构设计)
- [三、数据库设计](#三数据库设计)
- [四、后端接口设计](#四后端接口设计)
- [五、前端技术架构](#五前端技术架构)
- [六、部署方案](#六部署方案)
- [七、开发环境搭建](#七开发环境搭建)
- [八、模拟数据](#八模拟数据)

---

## 一、项目概述

### 1.1 项目背景
本项目是一个完整的图书管理系统，采用前后端分离架构，支持图书管理、借阅管理、用户权限管理、统计分析等核心功能。

### 1.2 技术选型

| 层级 | 技术栈 | 版本 |
|------|--------|------|
| 后端语言 | Java | 17 |
| 后端框架 | Spring Boot | 3.2.x |
| 构建工具 | Maven | 3.9.x |
| ORM框架 | MyBatis-Plus | 3.5.x |
| 数据库 | MySQL | 8.0 |
| API文档 | Swagger/Knife4j | 4.x |
| 服务网关 | Spring Cloud Gateway | 4.x |
| 服务注册 | Nacos | 2.2.x |
| 容器化 | Docker + Docker Compose | 24.x |
| 前端框架 | React | 18.x |
| CSS框架 | Tailwind CSS | 3.x |
| 状态管理 | Zustand | 4.x |
| HTTP客户端 | Axios | 1.x |
| 图表库 | ECharts | 5.x |

### 1.3 核心功能模块

```
图书管理系统
├── 用户权限系统
│   ├── 用户注册/登录
│   ├── 角色管理
│   └── 权限控制
├── 基础图书管理
│   ├── 图书信息CRUD
│   ├── 图书分类管理
│   ├── 库存管理
│   └── 图书搜索
├── 借阅管理
│   ├── 借书申请
│   ├── 归还处理
│   ├── 续借功能
│   └── 逾期管理
└── 统计分析
    ├── 借阅统计
    ├── 图书热度排行
    └── 用户活跃度分析
```

---

## 二、系统架构设计

### 2.1 微服务架构拓扑图

```
                                    ┌─────────────────────────────────────┐
                                    │           Frontend (React)          │
                                    │         http://localhost:3000       │
                                    └─────────────────┬───────────────────┘
                                                      │
                                                      ▼
                                    ┌─────────────────────────────────────┐
                                    │        Gateway Service (8080)       │
                                    │  - API路由转发                       │
                                    │  - 统一鉴权                          │
                                    │  - 限流熔断                          │
                                    └─────────────────┬───────────────────┘
                                                      │
                    ┌─────────────────┬───────────────┼───────────────┬─────────────────┐
                    │                 │               │               │                 │
                    ▼                 ▼               ▼               ▼                 ▼
          ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
          │ User Service │  │Book Service  │  │Borrow Service│  │    Nacos     │  │    MySQL     │
          │   (8081)     │  │   (8082)     │  │   (8083)     │  │   (8848)     │  │   (3306)     │
          │              │  │              │  │              │  │              │  │              │
          │ - 用户认证   │  │ - 图书管理   │  │ - 借阅管理   │  │ - 服务注册   │  │ - 数据存储   │
          │ - 角色权限   │  │ - 分类管理   │  │ - 归还处理   │  │ - 配置中心   │  │              │
          │ - 用户管理   │  │ - 库存管理   │  │ - 统计分析   │  │              │  │              │
          └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘
```

### 2.2 项目目录结构

```
book-manage-system/
├── backend/                          # 后端项目根目录
│   ├── pom.xml                       # 父工程POM
│   ├── common/                       # 公共模块
│   │   ├── pom.xml
│   │   └── src/main/java/com/book/common/
│   │       ├── config/               # 公共配置
│   │       ├── constant/             # 常量定义
│   │       ├── enums/                # 枚举类
│   │       ├── exception/            # 异常处理
│   │       ├── result/               # 统一返回结果
│   │       └── utils/                # 工具类
│   │
│   ├── gateway/                      # 网关服务
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── src/main/java/com/book/gateway/
│   │       ├── BookGatewayApplication.java
│   │       ├── config/
│   │       └── filter/
│   │
│   ├── user-service/                 # 用户服务
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── src/main/java/com/book/user/
│   │       ├── UserServiceApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       ├── mapper/
│   │       └── entity/
│   │
│   ├── book-service/                 # 图书服务
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── src/main/java/com/book/bookservice/
│   │       ├── BookServiceApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       ├── mapper/
│   │       └── entity/
│   │
│   └── borrow-service/               # 借阅服务
│       ├── pom.xml
│       ├── Dockerfile
│       └── src/main/java/com/book/borrow/
│           ├── BorrowServiceApplication.java
│           ├── controller/
│           ├── service/
│           ├── mapper/
│           └── entity/
│
├── frontend/                         # 前端项目
│   ├── package.json
│   ├── tailwind.config.js
│   ├── vite.config.ts
│   ├── Dockerfile
│   ├── public/
│   └── src/
│       ├── main.tsx
│       ├── App.tsx
│       ├── api/                      # API请求
│       ├── components/               # 公共组件
│       ├── layouts/                  # 布局组件
│       ├── pages/                    # 页面组件
│       ├── stores/                   # 状态管理
│       ├── hooks/                    # 自定义Hooks
│       ├── router/                   # 路由配置
│       └── utils/                    # 工具函数
│
├── sql/                              # SQL脚本
│   ├── schema.sql                    # 建表语句
│   └── data.sql                      # 模拟数据
│
├── docker-compose.yml                # Docker编排文件
└── README.md                         # 项目说明
```

### 2.3 服务依赖关系

```
┌─────────────────────────────────────────────────────────────────┐
│                        Service Dependencies                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  gateway-service                                                │
│       ├── nacos (服务发现)                                       │
│       └── user-service (鉴权)                                   │
│                                                                 │
│  user-service                                                   │
│       ├── nacos (服务注册/配置)                                  │
│       └── mysql (数据存储)                                       │
│                                                                 │
│  book-service                                                   │
│       ├── nacos (服务注册/配置)                                  │
│       └── mysql (数据存储)                                       │
│                                                                 │
│  borrow-service                                                 │
│       ├── nacos (服务注册/配置)                                  │
│       ├── mysql (数据存储)                                       │
│       ├── user-service (Feign调用-用户信息)                     │
│       └── book-service (Feign调用-图书信息)                     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 三、数据库设计

### 3.1 ER图

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   sys_user   │       │  sys_role    │       │sys_user_role │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │──┐    │ id (PK)      │──┐    │ user_id (FK) │──┐
│ username     │  │    │ role_name    │  │    │ role_id (FK) │  │
│ password     │  │    │ role_key     │  │    └──────────────┘  │
│ nickname     │  │    │ status       │  │           │          │
│ phone        │  │    │ sort         │  │           │          │
│ email        │  │    └──────────────┘  │           │          │
│ status       │  │           │          │           │          │
│ create_time  │  │           │          │           │          │
└──────────────┘  │           │          │           │          │
       │          └───────────┴──────────┴──────────┘          │
       │                                                     │
       │    ┌──────────────┐       ┌──────────────┐           │
       │    │borrow_record │       │  book_info   │           │
       │    ├──────────────┤       ├──────────────┤           │
       └────│ user_id (FK) │       │ id (PK)      │───────────┘
            │ book_id (FK) │───────│ title        │
            │ borrow_date  │       │ author       │
            │ return_date  │       │ isbn         │
            │ status       │       │ category_id  │──┐
            └──────────────┘       │ stock        │  │
                                   │ price        │  │
                                   └──────────────┘  │
                                          │          │
                                          │    ┌─────┴──────┐
                                          │    │book_category│
                                          │    ├──────────────┤
                                          └────│ id (PK)      │
                                               │ name         │
                                               │ parent_id    │
                                               │ sort         │
                                               └──────────────┘
```

### 3.2 建表语句

```sql
-- =====================================================
-- Database: book_manage_system
-- Charset: utf8mb4
-- Engine: InnoDB
-- =====================================================

CREATE DATABASE IF NOT EXISTS `book_manage_system` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `book_manage_system`;

-- -----------------------------------------------------
-- Table: sys_user (User table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `username` varchar(50) NOT NULL COMMENT 'Username for login',
  `password` varchar(100) NOT NULL COMMENT 'Password (encrypted)',
  `nickname` varchar(50) DEFAULT NULL COMMENT 'Display name',
  `phone` varchar(20) DEFAULT NULL COMMENT 'Phone number',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email address',
  `avatar` varchar(255) DEFAULT NULL COMMENT 'Avatar URL',
  `gender` tinyint DEFAULT 0 COMMENT 'Gender: 0-Unknown, 1-Male, 2-Female',
  `status` tinyint DEFAULT 1 COMMENT 'Status: 0-Disabled, 1-Enabled',
  `deleted` tinyint DEFAULT 0 COMMENT 'Soft delete: 0-Not deleted, 1-Deleted',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System user table';

-- -----------------------------------------------------
-- Table: sys_role (Role table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `role_name` varchar(50) NOT NULL COMMENT 'Role display name',
  `role_key` varchar(50) NOT NULL COMMENT 'Role key for permission check',
  `description` varchar(255) DEFAULT NULL COMMENT 'Role description',
  `status` tinyint DEFAULT 1 COMMENT 'Status: 0-Disabled, 1-Enabled',
  `sort` int DEFAULT 0 COMMENT 'Sort order',
  `deleted` tinyint DEFAULT 0 COMMENT 'Soft delete flag',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System role table';

-- -----------------------------------------------------
-- Table: sys_user_role (User-Role relation table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` bigint unsigned NOT NULL COMMENT 'User ID',
  `role_id` bigint unsigned NOT NULL COMMENT 'Role ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User-Role relation table';

-- -----------------------------------------------------
-- Table: book_category (Book category table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(50) NOT NULL COMMENT 'Category name',
  `parent_id` bigint unsigned DEFAULT 0 COMMENT 'Parent category ID, 0 for root',
  `sort` int DEFAULT 0 COMMENT 'Sort order',
  `icon` varchar(255) DEFAULT NULL COMMENT 'Category icon',
  `status` tinyint DEFAULT 1 COMMENT 'Status: 0-Disabled, 1-Enabled',
  `deleted` tinyint DEFAULT 0 COMMENT 'Soft delete flag',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book category table';

-- -----------------------------------------------------
-- Table: book_info (Book information table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `title` varchar(200) NOT NULL COMMENT 'Book title',
  `author` varchar(100) DEFAULT NULL COMMENT 'Book author',
  `isbn` varchar(20) DEFAULT NULL COMMENT 'ISBN number',
  `publisher` varchar(100) DEFAULT NULL COMMENT 'Publisher',
  `publish_date` date DEFAULT NULL COMMENT 'Publish date',
  `category_id` bigint unsigned DEFAULT NULL COMMENT 'Category ID',
  `price` decimal(10,2) DEFAULT NULL COMMENT 'Book price',
  `stock` int DEFAULT 0 COMMENT 'Stock quantity',
  `available_stock` int DEFAULT 0 COMMENT 'Available stock for borrowing',
  `location` varchar(50) DEFAULT NULL COMMENT 'Book shelf location',
  `cover` varchar(255) DEFAULT NULL COMMENT 'Cover image URL',
  `description` text COMMENT 'Book description',
  `status` tinyint DEFAULT 1 COMMENT 'Status: 0-Off shelf, 1-On shelf',
  `deleted` tinyint DEFAULT 0 COMMENT 'Soft delete flag',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_isbn` (`isbn`),
  KEY `idx_title` (`title`),
  KEY `idx_author` (`author`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book information table';

-- -----------------------------------------------------
-- Table: borrow_record (Borrow record table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` bigint unsigned NOT NULL COMMENT 'Borrower user ID',
  `book_id` bigint unsigned NOT NULL COMMENT 'Book ID',
  `borrow_date` datetime NOT NULL COMMENT 'Borrow date',
  `due_date` datetime NOT NULL COMMENT 'Due date for return',
  `return_date` datetime DEFAULT NULL COMMENT 'Actual return date',
  `renew_count` int DEFAULT 0 COMMENT 'Renew times count',
  `status` tinyint DEFAULT 0 COMMENT 'Status: 0-Borrowed, 1-Returned, 2-Overdue, 3-Lost',
  `remark` varchar(255) DEFAULT NULL COMMENT 'Remark',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_book_id` (`book_id`),
  KEY `idx_status` (`status`),
  KEY `idx_borrow_date` (`borrow_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Borrow record table';

-- -----------------------------------------------------
-- Table: borrow_config (Borrow configuration table)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `borrow_config`;
CREATE TABLE `borrow_config` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `config_key` varchar(50) NOT NULL COMMENT 'Config key',
  `config_value` varchar(255) NOT NULL COMMENT 'Config value',
  `description` varchar(255) DEFAULT NULL COMMENT 'Config description',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Borrow configuration table';
```

---

## 四、后端接口设计

### 4.1 统一返回结果格式

```java
/**
 * Unified API response format
 */
@Data
public class Result<T> {
    private Integer code;       // Response code
    private String message;     // Response message
    private T data;             // Response data
    private Long timestamp;     // Timestamp

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}
```

### 4.2 分页请求参数

```java
/**
 * Pagination request parameters
 */
@Data
public class PageRequest {
    @ApiModelProperty(value = "Page number", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "Page size", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "Sort field")
    private String sortField;

    @ApiModelProperty(value = "Sort order: asc/desc")
    private String sortOrder = "desc";
}
```

---

### 4.3 User Service API (Port: 8081)

#### 4.3.1 用户认证接口

##### POST /api/user/auth/register
**Description**: User registration

**Request Body**:
```json
{
  "username": "string (required, 4-20 chars)",
  "password": "string (required, 6-20 chars)",
  "nickname": "string (optional, max 50 chars)",
  "phone": "string (optional, max 20 chars)",
  "email": "string (optional, valid email format)"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "nickname": "Test User",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": 1710835200000
}
```

**Error Codes**:
- 400: Invalid parameters
- 409: Username already exists

---

##### POST /api/user/auth/login
**Description**: User login

**Request Body**:
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "Administrator",
    "avatar": "https://example.com/avatar.png",
    "roles": ["admin"],
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": 1710835200000
}
```

**Error Codes**:
- 401: Invalid username or password
- 403: Account is disabled

---

##### POST /api/user/auth/logout
**Description**: User logout

**Headers**: 
```
Authorization: Bearer {token}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1710835200000
}
```

---

##### GET /api/user/auth/info
**Description**: Get current user info

**Headers**: 
```
Authorization: Bearer {token}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "Administrator",
    "phone": "13800138000",
    "email": "admin@example.com",
    "avatar": "https://example.com/avatar.png",
    "gender": 1,
    "roles": [
      {
        "id": 1,
        "roleName": "Administrator",
        "roleKey": "admin"
      }
    ]
  },
  "timestamp": 1710835200000
}
```

---

#### 4.3.2 用户管理接口

##### GET /api/user/list
**Description**: Get user list with pagination

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pageNum | int | No | Page number, default 1 |
| pageSize | int | No | Page size, default 10 |
| username | string | No | Filter by username |
| phone | string | No | Filter by phone |
| status | int | No | Filter by status: 0-Disabled, 1-Enabled |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "Administrator",
        "phone": "13800138000",
        "email": "admin@example.com",
        "status": 1,
        "createTime": "2024-03-19 10:00:00",
        "roles": [
          {"id": 1, "roleName": "Administrator", "roleKey": "admin"}
        ]
      }
    ]
  },
  "timestamp": 1710835200000
}
```

---

##### GET /api/user/{id}
**Description**: Get user by ID

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | long | Yes | User ID |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "Administrator",
    "phone": "13800138000",
    "email": "admin@example.com",
    "avatar": "https://example.com/avatar.png",
    "gender": 1,
    "status": 1,
    "createTime": "2024-03-19 10:00:00",
    "roleIds": [1, 2]
  },
  "timestamp": 1710835200000
}
```

---

##### PUT /api/user
**Description**: Update user info

**Request Body**:
```json
{
  "id": 1,
  "nickname": "New Nickname",
  "phone": "13900139000",
  "email": "newemail@example.com",
  "gender": 1,
  "avatar": "https://example.com/new-avatar.png",
  "roleIds": [1, 2]
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1710835200000
}
```

---

##### DELETE /api/user/{id}
**Description**: Delete user (soft delete)

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | long | Yes | User ID |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1710835200000
}
```

---

##### PUT /api/user/status
**Description**: Update user status

**Request Body**:
```json
{
  "id": 1,
  "status": 0
}
```

---

##### PUT /api/user/password
**Description**: Change password

**Request Body**:
```json
{
  "oldPassword": "oldPassword123",
  "newPassword": "newPassword456"
}
```

---

#### 4.3.3 角色管理接口

##### GET /api/role/list
**Description**: Get all roles

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "roleName": "Administrator",
      "roleKey": "admin",
      "description": "System administrator",
      "status": 1,
      "sort": 1,
      "createTime": "2024-03-19 10:00:00"
    },
    {
      "id": 2,
      "roleName": "Librarian",
      "roleKey": "librarian",
      "description": "Library manager",
      "status": 1,
      "sort": 2,
      "createTime": "2024-03-19 10:00:00"
    },
    {
      "id": 3,
      "roleName": "Reader",
      "roleKey": "reader",
      "description": "Normal reader",
      "status": 1,
      "sort": 3,
      "createTime": "2024-03-19 10:00:00"
    }
  ],
  "timestamp": 1710835200000
}
```

---

##### POST /api/role
**Description**: Create role

**Request Body**:
```json
{
  "roleName": "New Role",
  "roleKey": "new_role",
  "description": "Role description",
  "sort": 4,
  "status": 1
}
```

---

##### PUT /api/role
**Description**: Update role

**Request Body**:
```json
{
  "id": 4,
  "roleName": "Updated Role Name",
  "description": "Updated description"
}
```

---

##### DELETE /api/role/{id}
**Description**: Delete role

---

---

### 4.4 Book Service API (Port: 8082)

#### 4.4.1 图书管理接口

##### GET /api/book/list
**Description**: Get book list with pagination

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pageNum | int | No | Page number, default 1 |
| pageSize | int | No | Page size, default 10 |
| title | string | No | Filter by title (fuzzy search) |
| author | string | No | Filter by author (fuzzy search) |
| isbn | string | No | Filter by ISBN |
| categoryId | long | No | Filter by category ID |
| status | int | No | Filter by status: 0-Off shelf, 1-On shelf |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 1000,
    "list": [
      {
        "id": 1,
        "title": "Java Programming",
        "author": "John Smith",
        "isbn": "978-7-111-12345-6",
        "publisher": "Mechanical Industry Press",
        "publishDate": "2023-01-01",
        "categoryId": 1,
        "categoryName": "Programming",
        "price": 89.00,
        "stock": 10,
        "availableStock": 8,
        "location": "A-1-001",
        "cover": "https://example.com/covers/java.jpg",
        "status": 1,
        "createTime": "2024-03-19 10:00:00"
      }
    ]
  },
  "timestamp": 1710835200000
}
```

---

##### GET /api/book/{id}
**Description**: Get book detail by ID

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | long | Yes | Book ID |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Java Programming",
    "author": "John Smith",
    "isbn": "978-7-111-12345-6",
    "publisher": "Mechanical Industry Press",
    "publishDate": "2023-01-01",
    "categoryId": 1,
    "categoryName": "Programming",
    "price": 89.00,
    "stock": 10,
    "availableStock": 8,
    "location": "A-1-001",
    "cover": "https://example.com/covers/java.jpg",
    "description": "This book covers advanced Java programming techniques...",
    "status": 1,
    "createTime": "2024-03-19 10:00:00",
    "updateTime": "2024-03-19 12:00:00"
  },
  "timestamp": 1710835200000
}
```

---

##### POST /api/book
**Description**: Create new book

**Request Body**:
```json
{
  "title": "Java Programming",
  "author": "John Smith",
  "isbn": "978-7-111-12345-6",
  "publisher": "Mechanical Industry Press",
  "publishDate": "2023-01-01",
  "categoryId": 1,
  "price": 89.00,
  "stock": 10,
  "location": "A-1-001",
  "cover": "https://example.com/covers/java.jpg",
  "description": "This book covers advanced Java programming techniques..."
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1
  },
  "timestamp": 1710835200000
}
```

---

##### PUT /api/book
**Description**: Update book info

**Request Body**:
```json
{
  "id": 1,
  "title": "Java Programming (3rd Edition)",
  "stock": 15,
  "availableStock": 13,
  "location": "A-1-002"
}
```

---

##### DELETE /api/book/{id}
**Description**: Delete book (soft delete)

---

##### PUT /api/book/status
**Description**: Update book status

**Request Body**:
```json
{
  "id": 1,
  "status": 0
}
```

---

##### PUT /api/book/stock
**Description**: Update book stock

**Request Body**:
```json
{
  "id": 1,
  "stock": 20,
  "availableStock": 18
}
```

---

##### GET /api/book/search
**Description**: Search books by keyword

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| pageNum | int | No | Page number |
| pageSize | int | No | Page size |

---

#### 4.4.2 分类管理接口

##### GET /api/category/tree
**Description**: Get category tree structure

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Programming",
      "parentId": 0,
      "sort": 1,
      "icon": "code",
      "children": [
        {
          "id": 11,
          "name": "Java",
          "parentId": 1,
          "sort": 1,
          "children": []
        },
        {
          "id": 12,
          "name": "Python",
          "parentId": 1,
          "sort": 2,
          "children": []
        }
      ]
    },
    {
      "id": 2,
      "name": "Literature",
      "parentId": 0,
      "sort": 2,
      "children": []
    }
  ],
  "timestamp": 1710835200000
}
```

---

##### GET /api/category/list
**Description**: Get all categories (flat list)

---

##### POST /api/category
**Description**: Create category

**Request Body**:
```json
{
  "name": "New Category",
  "parentId": 0,
  "sort": 5,
  "icon": "book"
}
```

---

##### PUT /api/category
**Description**: Update category

---

##### DELETE /api/category/{id}
**Description**: Delete category

---

---

### 4.5 Borrow Service API (Port: 8083)

#### 4.5.1 借阅管理接口

##### POST /api/borrow
**Description**: Borrow a book

**Request Body**:
```json
{
  "bookId": 1,
  "userId": 2,
  "days": 30
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "bookId": 1,
    "bookTitle": "Java Programming",
    "userId": 2,
    "username": "reader01",
    "borrowDate": "2024-03-19 10:00:00",
    "dueDate": "2024-04-18 10:00:00",
    "status": 0
  },
  "timestamp": 1710835200000
}
```

**Error Codes**:
- 400: Book not available
- 403: User has reached max borrow limit
- 404: Book not found

---

##### POST /api/borrow/return
**Description**: Return a book

**Request Body**:
```json
{
  "recordId": 1
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "returnDate": "2024-03-25 14:30:00",
    "status": 1,
    "overdueDays": 0,
    "fine": 0.00
  },
  "timestamp": 1710835200000
}
```

---

##### POST /api/borrow/renew
**Description**: Renew a book

**Request Body**:
```json
{
  "recordId": 1,
  "days": 15
}
```

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "dueDate": "2024-05-03 10:00:00",
    "renewCount": 1,
    "status": 0
  },
  "timestamp": 1710835200000
}
```

**Error Codes**:
- 400: Max renew times reached
- 400: Book is overdue, cannot renew

---

##### GET /api/borrow/list
**Description**: Get borrow records with pagination

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pageNum | int | No | Page number |
| pageSize | int | No | Page size |
| userId | long | No | Filter by user ID |
| bookId | long | No | Filter by book ID |
| status | int | No | Filter by status: 0-Borrowed, 1-Returned, 2-Overdue, 3-Lost |
| startDate | string | No | Filter by borrow date range start |
| endDate | string | No | Filter by borrow date range end |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 500,
    "list": [
      {
        "id": 1,
        "userId": 2,
        "username": "reader01",
        "bookId": 1,
        "bookTitle": "Java Programming",
        "bookCover": "https://example.com/covers/java.jpg",
        "borrowDate": "2024-03-19 10:00:00",
        "dueDate": "2024-04-18 10:00:00",
        "returnDate": null,
        "renewCount": 0,
        "status": 0,
        "overdueDays": 0
      }
    ]
  },
  "timestamp": 1710835200000
}
```

---

##### GET /api/borrow/user/{userId}
**Description**: Get user's borrow records

**Path Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| userId | long | Yes | User ID |

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | int | No | Filter by status |
| pageNum | int | No | Page number |
| pageSize | int | No | Page size |

---

##### GET /api/borrow/overdue
**Description**: Get overdue records

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pageNum | int | No | Page number |
| pageSize | int | No | Page size |

---

#### 4.5.2 统计分析接口

##### GET /api/stats/overview
**Description**: Get system overview statistics

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalBooks": 10000,
    "totalUsers": 500,
    "totalBorrowed": 320,
    "totalOverdue": 15,
    "todayBorrow": 25,
    "todayReturn": 18,
    "availableBooks": 8500
  },
  "timestamp": 1710835200000
}
```

---

##### GET /api/stats/borrow/trend
**Description**: Get borrow trend data

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| type | string | No | Type: day/week/month, default: day |
| startDate | string | No | Start date |
| endDate | string | No | End date |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "dates": ["2024-03-13", "2024-03-14", "2024-03-15", "2024-03-16", "2024-03-17", "2024-03-18", "2024-03-19"],
    "borrowCount": [15, 22, 18, 30, 25, 20, 25],
    "returnCount": [12, 18, 20, 25, 22, 15, 18]
  },
  "timestamp": 1710835200000
}
```

---

##### GET /api/stats/book/hot
**Description**: Get hot books ranking

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | int | No | Number of results, default: 10 |
| period | string | No | Period: week/month/year |

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "rank": 1,
      "bookId": 5,
      "title": "Python Machine Learning",
      "author": "Mike Johnson",
      "cover": "https://example.com/covers/python-ml.jpg",
      "borrowCount": 156
    },
    {
      "rank": 2,
      "bookId": 12,
      "title": "Design Patterns",
      "author": "Gang of Four",
      "cover": "https://example.com/covers/dp.jpg",
      "borrowCount": 142
    }
  ],
  "timestamp": 1710835200000
}
```

---

##### GET /api/stats/category/distribution
**Description**: Get book distribution by category

**Response**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"category": "Programming", "count": 2500},
    {"category": "Literature", "count": 1800},
    {"category": "Science", "count": 1500},
    {"category": "History", "count": 1200},
    {"category": "Art", "count": 800}
  ],
  "timestamp": 1710835200000
}
```

---

##### GET /api/stats/user/active
**Description**: Get active users ranking

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | int | No | Number of results, default: 10 |

---

## 五、前端技术架构

### 5.1 技术栈详情

| 技术 | 版本 | 用途 |
|------|------|------|
| React | 18.2 | 核心框架 |
| TypeScript | 5.x | 类型支持 |
| Vite | 5.x | 构建工具 |
| Tailwind CSS | 3.4 | CSS框架 |
| React Router | 6.x | 路由管理 |
| Zustand | 4.x | 状态管理 |
| Axios | 1.x | HTTP请求 |
| React Query | 5.x | 数据请求缓存 |
| ECharts | 5.x | 图表库 |
| React Hook Form | 7.x | 表单处理 |
| Zod | 3.x | 数据校验 |
| Lucide React | latest | 图标库 |
| Day.js | 1.x | 日期处理 |

### 5.2 项目目录结构

```
frontend/
├── public/
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── api/                          # API请求模块
│   │   ├── index.ts                  # Axios实例配置
│   │   ├── user.ts                   # 用户相关API
│   │   ├── book.ts                   # 图书相关API
│   │   ├── borrow.ts                 # 借阅相关API
│   │   └── stats.ts                  # 统计相关API
│   │
│   ├── components/                   # 公共组件
│   │   ├── common/                   # 通用组件
│   │   │   ├── Button.tsx
│   │   │   ├── Input.tsx
│   │   │   ├── Modal.tsx
│   │   │   ├── Table.tsx
│   │   │   ├── Pagination.tsx
│   │   │   ├── Loading.tsx
│   │   │   └── Empty.tsx
│   │   ├── layout/                   # 布局组件
│   │   │   ├── MainLayout.tsx
│   │   │   ├── Sidebar.tsx
│   │   │   ├── Header.tsx
│   │   │   └── Footer.tsx
│   │   └── business/                 # 业务组件
│   │       ├── BookCard.tsx
│   │       ├── BorrowStatusBadge.tsx
│   │       ├── UserAvatar.tsx
│   │       └── StatsCard.tsx
│   │
│   ├── pages/                        # 页面组件
│   │   ├── auth/                     # 认证页面
│   │   │   ├── Login.tsx
│   │   │   └── Register.tsx
│   │   ├── dashboard/                # 仪表盘
│   │   │   └── Dashboard.tsx
│   │   ├── book/                     # 图书管理
│   │   │   ├── BookList.tsx
│   │   │   ├── BookDetail.tsx
│   │   │   ├── BookForm.tsx
│   │   │   └── CategoryManage.tsx
│   │   ├── borrow/                   # 借阅管理
│   │   │   ├── BorrowList.tsx
│   │   │   ├── BorrowRecord.tsx
│   │   │   └── OverdueList.tsx
│   │   ├── user/                     # 用户管理
│   │   │   ├── UserList.tsx
│   │   │   ├── UserForm.tsx
│   │   │   ├── Profile.tsx
│   │   │   └── RoleManage.tsx
│   │   └── stats/                    # 统计分析
│   │       └── Statistics.tsx
│   │
│   ├── stores/                       # 状态管理
│   │   ├── userStore.ts              # 用户状态
│   │   ├── bookStore.ts              # 图书状态
│   │   └── uiStore.ts                # UI状态
│   │
│   ├── hooks/                        # 自定义Hooks
│   │   ├── useAuth.ts
│   │   ├── usePagination.ts
│   │   ├── useTable.ts
│   │   └── useModal.ts
│   │
│   ├── router/                       # 路由配置
│   │   ├── index.tsx
│   │   └── routes.ts
│   │
│   ├── utils/                        # 工具函数
│   │   ├── request.ts                # 请求封装
│   │   ├── storage.ts                # 本地存储
│   │   ├── format.ts                 # 格式化工具
│   │   └── constants.ts              # 常量定义
│   │
│   ├── types/                        # TypeScript类型
│   │   ├── user.ts
│   │   ├── book.ts
│   │   ├── borrow.ts
│   │   └── common.ts
│   │
│   ├── styles/                       # 全局样式
│   │   └── globals.css
│   │
│   ├── App.tsx                       # 根组件
│   ├── main.tsx                      # 入口文件
│   └── vite-env.d.ts
│
├── index.html
├── package.json
├── tsconfig.json
├── tailwind.config.js
├── postcss.config.js
├── vite.config.ts
├── Dockerfile
└── .env
```

### 5.3 页面路由规划

| 路径 | 页面 | 权限 | 说明 |
|------|------|------|------|
| `/login` | Login | Public | 登录页 |
| `/register` | Register | Public | 注册页 |
| `/` | Dashboard | User | 仪表盘首页 |
| `/books` | BookList | User | 图书列表 |
| `/books/:id` | BookDetail | User | 图书详情 |
| `/books/add` | BookForm | Admin/Librarian | 新增图书 |
| `/books/edit/:id` | BookForm | Admin/Librarian | 编辑图书 |
| `/categories` | CategoryManage | Admin/Librarian | 分类管理 |
| `/borrow` | BorrowList | User | 借阅记录 |
| `/borrow/my` | MyBorrow | User | 我的借阅 |
| `/borrow/overdue` | OverdueList | Admin/Librarian | 逾期列表 |
| `/users` | UserList | Admin | 用户列表 |
| `/users/:id` | UserForm | Admin | 用户编辑 |
| `/roles` | RoleManage | Admin | 角色管理 |
| `/profile` | Profile | User | 个人中心 |
| `/stats` | Statistics | Admin/Librarian | 统计分析 |
| `*` | NotFound | Public | 404页面 |

### 5.4 核心页面设计

#### 5.4.1 登录页面
```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│                    [Logo] Book Manage System                │
│                                                             │
│              ┌─────────────────────────────┐                │
│              │     ┌───────────────────┐   │                │
│              │     │ Username          │   │                │
│              │     └───────────────────┘   │                │
│              │     ┌───────────────────┐   │                │
│              │     │ Password          │   │                │
│              │     └───────────────────┘   │                │
│              │                             │                │
│              │     [Remember Me]           │                │
│              │                             │                │
│              │     ┌───────────────────┐   │                │
│              │     │      Login        │   │                │
│              │     └───────────────────┘   │                │
│              │                             │                │
│              │     No account? Register    │                │
│              └─────────────────────────────┘                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

#### 5.4.2 仪表盘页面
```
┌──────────────────────────────────────────────────────────────────────┐
│ Header                                              [User] [Logout] │
├──────────────┬───────────────────────────────────────────────────────┤
│              │                                                       │
│  Sidebar     │   Dashboard                                          │
│              │   ─────────────────────────────────────────────────  │
│  ├ Dashboard │                                                       │
│  ├ Books     │   ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  ├ Borrow    │   │ Total   │ │ Borrowed│ │ Overdue │ │ Users   │   │
│  ├ Users     │   │ 10,000  │ │   320   │ │   15    │ │   500   │   │
│  └ Stats     │   └─────────┘ └─────────┘ └─────────┘ └─────────┘   │
│              │                                                       │
│              │   ┌───────────────────────────────────────────────┐  │
│              │   │              Borrow Trend Chart               │  │
│              │   │                                               │  │
│              │   │    [Line Chart showing borrow/return trend]   │  │
│              │   │                                               │  │
│              │   └───────────────────────────────────────────────┘  │
│              │                                                       │
│              │   ┌────────────────────┐ ┌────────────────────┐      │
│              │   │   Hot Books        │ │  Category Dist.    │      │
│              │   │   [Pie Chart]      │ │   [Bar Chart]      │      │
│              │   └────────────────────┘ └────────────────────┘      │
│              │                                                       │
└──────────────┴───────────────────────────────────────────────────────┘
```

#### 5.4.3 图书列表页面
```
┌──────────────────────────────────────────────────────────────────────┐
│ Header                                              [User] [Logout] │
├──────────────┬───────────────────────────────────────────────────────┤
│              │                                                       │
│  Sidebar     │   Book Management                                    │
│              │   ─────────────────────────────────────────────────  │
│  ├ Dashboard │                                                       │
│  ├ Books  >  │   [Search...] [Category ▼] [Status ▼]  [+ Add Book] │
│  ├ Borrow    │                                                       │
│  ├ Users     │   ┌─────────────────────────────────────────────────┐│
│  └ Stats     │   │ ID │ Title │ Author │ Category │ Stock │ Actions││
│              │   ├────┼───────┼────────┼──────────┼───────┼────────││
│              │   │ 1  │ Java  │ John   │ Prog.    │ 10    │ E D V  ││
│              │   │ 2  │ Python│ Mike   │ Prog.    │ 5     │ E D V  ││
│              │   │ 3  │ Design│ GoF    │ Prog.    │ 8     │ E D V  ││
│              │   │ ...│ ...   │ ...    │ ...      │ ...   │ ...    ││
│              │   └─────────────────────────────────────────────────┘│
│              │                                                       │
│              │   [<] 1 2 3 4 5 ... 100 [>]    Total: 1000           │
│              │                                                       │
└──────────────┴───────────────────────────────────────────────────────┘
```

### 5.5 Tailwind CSS 配置

```javascript
// tailwind.config.js
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
          800: '#1e40af',
          900: '#1e3a8a',
        },
        success: '#10b981',
        warning: '#f59e0b',
        danger: '#ef4444',
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
```

### 5.6 Axios 请求封装

```typescript
// src/api/index.ts
import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { storage } from '@/utils/storage';

const instance: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
instance.interceptors.request.use(
  (config) => {
    const token = storage.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
instance.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response;
    if (data.code === 200) {
      return data.data;
    }
    // Handle business error
    return Promise.reject(new Error(data.message || 'Request failed'));
  },
  (error) => {
    if (error.response?.status === 401) {
      // Token expired, redirect to login
      storage.clearToken();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default instance;
```

---

## 六、部署方案

### 6.1 Docker Compose 完整配置

```yaml
# docker-compose.yml
version: '3.8'

services:
  # MySQL Database
  mysql:
    image: mysql:8.0
    container_name: book-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root123456
      MYSQL_DATABASE: book_manage_system
      MYSQL_USER: bookuser
      MYSQL_PASSWORD: book123456
      TZ: Asia/Shanghai
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/schema.sql:/docker-entrypoint-initdb.d/1-schema.sql
      - ./sql/data.sql:/docker-entrypoint-initdb.d/2-data.sql
    command: 
      --default-authentication-plugin=mysql_native_password
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
    networks:
      - book-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Nacos Registry Center
  nacos:
    image: nacos/nacos-server:v2.2.3
    container_name: book-nacos
    restart: always
    environment:
      MODE: standalone
      SPRING_DATASOURCE_PLATFORM: mysql
      MYSQL_SERVICE_HOST: mysql
      MYSQL_SERVICE_PORT: 3306
      MYSQL_SERVICE_DB_NAME: nacos_config
      MYSQL_SERVICE_USER: root
      MYSQL_SERVICE_PASSWORD: root123456
      JVM_XMS: 256m
      JVM_XMX: 512m
    ports:
      - "8848:8848"
      - "9848:9848"
    depends_on:
      mysql:
        condition: service_healthy
    networks:
      - book-network

  # Gateway Service
  gateway-service:
    build:
      context: ./backend/gateway
      dockerfile: Dockerfile
    container_name: book-gateway
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: docker
      NACOS_SERVER_ADDR: nacos:8848
    ports:
      - "8080:8080"
    depends_on:
      - nacos
    networks:
      - book-network

  # User Service
  user-service:
    build:
      context: ./backend/user-service
      dockerfile: Dockerfile
    container_name: book-user-service
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: docker
      NACOS_SERVER_ADDR: nacos:8848
      MYSQL_HOST: mysql
      MYSQL_PORT: 3306
      MYSQL_DB: book_manage_system
      MYSQL_USER: bookuser
      MYSQL_PASSWORD: book123456
    ports:
      - "8081:8081"
    depends_on:
      - nacos
      - mysql
    networks:
      - book-network

  # Book Service
  book-service:
    build:
      context: ./backend/book-service
      dockerfile: Dockerfile
    container_name: book-book-service
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: docker
      NACOS_SERVER_ADDR: nacos:8848
      MYSQL_HOST: mysql
      MYSQL_PORT: 3306
      MYSQL_DB: book_manage_system
      MYSQL_USER: bookuser
      MYSQL_PASSWORD: book123456
    ports:
      - "8082:8082"
    depends_on:
      - nacos
      - mysql
    networks:
      - book-network

  # Borrow Service
  borrow-service:
    build:
      context: ./backend/borrow-service
      dockerfile: Dockerfile
    container_name: book-borrow-service
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: docker
      NACOS_SERVER_ADDR: nacos:8848
      MYSQL_HOST: mysql
      MYSQL_PORT: 3306
      MYSQL_DB: book_manage_system
      MYSQL_USER: bookuser
      MYSQL_PASSWORD: book123456
    ports:
      - "8083:8083"
    depends_on:
      - nacos
      - mysql
      - user-service
      - book-service
    networks:
      - book-network

  # Frontend (React)
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: book-frontend
    restart: always
    ports:
      - "80:80"
    depends_on:
      - gateway-service
    networks:
      - book-network

volumes:
  mysql_data:
    driver: local

networks:
  book-network:
    driver: bridge
```

### 6.2 后端 Dockerfile 模板

```dockerfile
# backend/user-service/Dockerfile
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Set timezone
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# Copy jar file
COPY target/user-service-1.0.0.jar app.jar

# Expose port
EXPOSE 8081

# JVM options
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Start command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### 6.3 前端 Dockerfile

```dockerfile
# frontend/Dockerfile

# Build stage
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --registry=https://registry.npmmirror.com

# Copy source code
COPY . .

# Build application
RUN npm run build

# Production stage
FROM nginx:alpine

# Copy built files
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

### 6.4 前端容器配置

前端容器内置 nginx 用于托管静态文件并代理 API 请求到网关服务。

配置文件 `frontend/nginx.conf` 已包含：
- SPA 路由支持（所有路由回退到 index.html）
- API 请求代理到 gateway:8080
- 静态资源缓存
- Gzip 压缩

### 6.5 部署步骤

#### 方式一：Docker Compose 一键部署

```bash
# 1. Clone project
git clone <repository-url>
cd book-manage-system

# 2. Create Nacos database (first time only)
# Connect to MySQL and create nacos_config database

# 3. Build and start all services
docker-compose up -d --build

# 4. Check service status
docker-compose ps

# 5. View logs
docker-compose logs -f [service-name]

# 6. Stop all services
docker-compose down

# 7. Stop and remove volumes
docker-compose down -v
```

#### 方式二：单独构建部署

```bash
# Build backend services
cd backend
mvn clean package -DskipTests

# Build Docker images
docker build -t book-gateway:latest ./gateway
docker build -t book-user-service:latest ./user-service
docker build -t book-book-service:latest ./book-service
docker build -t book-borrow-service:latest ./borrow-service

# Build frontend
cd ../frontend
npm install
npm run build
docker build -t book-frontend:latest .

# Run containers
docker run -d --name book-gateway -p 8080:8080 book-gateway:latest
# ... run other services
```

---

## 七、开发环境搭建

### 7.1 后端开发环境

#### 7.1.1 必备软件安装

```bash
# JDK 17 Installation

# Windows (using scoop)
scoop install temurin17-jdk

# Or download from:
# https://adoptium.net/temurin/releases/?version=17

# Verify installation
java -version
# Expected output: openjdk version "17.x.x"

# Maven Installation

# Windows (using scoop)
scoop install maven

# Or download from:
# https://maven.apache.org/download.cgi

# Verify installation
mvn -version
# Expected output: Apache Maven 3.9.x

# MySQL 8 Installation

# Windows (using scoop)
scoop install mysql

# Or download from:
# https://dev.mysql.com/downloads/mysql/

# Verify installation
mysql --version
# Expected output: mysql Ver 8.0.x

# Docker Installation
# Download from: https://www.docker.com/products/docker-desktop

# Verify installation
docker --version
docker-compose --version
```

#### 7.1.2 IDE 配置

**IntelliJ IDEA 配置**:
1. 安装 Lombok 插件
2. 安装 MyBatisX 插件
3. 配置 JDK 17
4. 配置 Maven settings.xml

**Maven settings.xml**:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <!-- Mirror for faster download in China -->
    <mirrors>
        <mirror>
            <id>aliyunmaven</id>
            <mirrorOf>*</mirrorOf>
            <name>Aliyun Maven Mirror</name>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>

    <!-- JDK 17 configuration -->
    <profiles>
        <profile>
            <id>jdk-17</id>
            <activation>
                <activeByDefault>true</activeByDefault>
                <jdk>17</jdk>
            </activation>
            <properties>
                <maven.compiler.source>17</maven.compiler.source>
                <maven.compiler.target>17</maven.compiler.target>
                <maven.compiler.compilerVersion>17</maven.compiler.compilerVersion>
            </properties>
        </profile>
    </profiles>
</settings>
```

#### 7.1.3 后端项目启动

```bash
# Navigate to backend directory
cd backend

# Install dependencies
mvn clean install -DskipTests

# Start Nacos (required before services)
# Download from: https://nacos.io/zh-cn/
cd nacos/bin
startup.cmd -m standalone  # Windows
# or
sh startup.sh -m standalone  # Linux/Mac

# Access Nacos console: http://localhost:8848/nacos
# Default: nacos/nacos

# Start services (in order)
# 1. Gateway Service
cd backend/gateway
mvn spring-boot:run

# 2. User Service
cd backend/user-service
mvn spring-boot:run

# 3. Book Service
cd backend/book-service
mvn spring-boot:run

# 4. Borrow Service
cd backend/borrow-service
mvn spring-boot:run

# Or start all with Maven multi-module
cd backend
mvn spring-boot:run -pl gateway,user-service,book-service,borrow-service
```

---

### 7.2 前端开发环境

#### 7.2.1 Node.js 安装

```bash
# Node.js Installation

# Windows (using scoop)
scoop install nodejs-lts

# Or using nvm-windows (recommended for version management)
# Download nvm-windows from: https://github.com/coreybutler/nvm-windows/releases

# Install Node.js 20 LTS
nvm install 20
nvm use 20

# Verify installation
node -version
# Expected output: v20.x.x

npm -version
# Expected output: 10.x.x

# Configure npm mirror (for China)
npm config set registry https://registry.npmmirror.com

# Verify configuration
npm config get registry
```

#### 7.2.2 React 项目创建

```bash
# Navigate to project root
cd book-manage-system

# Create frontend project with Vite
npm create vite@latest frontend -- --template react-ts

# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Install additional dependencies
npm install react-router-dom zustand axios @tanstack/react-query
npm install tailwindcss postcss autoprefixer -D
npm install lucide-react dayjs echarts echarts-for-react
npm install react-hook-form zod @hookform/resolvers

# Initialize Tailwind CSS
npx tailwindcss init -p

# Start development server
npm run dev

# Open browser: http://localhost:5173
```

#### 7.2.3 前端项目配置

**package.json scripts**:
```json
{
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview",
    "lint": "eslint . --ext ts,tsx --report-unused-disable-directives --max-warnings 0"
  }
}
```

**vite.config.ts**:
```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
```

---

### 7.3 数据库初始化

```bash
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE IF NOT EXISTS book_manage_system 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

# Create user
CREATE USER 'bookuser'@'%' IDENTIFIED BY 'book123456';
GRANT ALL PRIVILEGES ON book_manage_system.* TO 'bookuser'@'%';
FLUSH PRIVILEGES;

# Use database
USE book_manage_system;

# Execute schema script
SOURCE sql/schema.sql;

# Execute data script
SOURCE sql/data.sql;

# Verify tables
SHOW TABLES;
```

---

### 7.4 开发环境验证清单

| 组件 | 验证命令 | 预期结果 |
|------|----------|----------|
| JDK 17 | `java -version` | openjdk version "17.x.x" |
| Maven | `mvn -version` | Apache Maven 3.9.x |
| MySQL | `mysql --version` | mysql Ver 8.0.x |
| Docker | `docker --version` | Docker version 24.x |
| Docker Compose | `docker-compose --version` | Docker Compose version v2.x |
| Node.js | `node -v` | v20.x.x |
| npm | `npm -v` | 10.x.x |
| Nacos | 访问 http://localhost:8848/nacos | 登录页面显示 |
| Gateway | 访问 http://localhost:8080/swagger-ui.html | Swagger文档显示 |
| Frontend | 访问 http://localhost:3000 | 登录页面显示 |

---

## 八、模拟数据

### 8.1 初始化数据SQL

```sql
-- =====================================================
-- Initialize Data for Book Management System
-- =====================================================

USE `book_manage_system`;

-- -----------------------------------------------------
-- Insert Roles
-- -----------------------------------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `description`, `status`, `sort`) VALUES
(1, 'Administrator', 'admin', 'System administrator with full access', 1, 1),
(2, 'Librarian', 'librarian', 'Library staff for book and borrow management', 1, 2),
(3, 'Reader', 'reader', 'Normal reader with borrow permission', 1, 3);

-- -----------------------------------------------------
-- Insert Users (Password: 123456, encrypted with BCrypt)
-- -----------------------------------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `avatar`, `gender`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'System Admin', '13800138000', 'admin@booklib.com', '/avatars/admin.png', 1, 1),
(2, 'librarian01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Zhang San', '13800138001', 'zhangsan@booklib.com', '/avatars/librarian01.png', 1, 1),
(3, 'librarian02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Li Si', '13800138002', 'lisi@booklib.com', '/avatars/librarian02.png', 2, 1),
(4, 'reader01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Wang Wu', '13800138003', 'wangwu@example.com', '/avatars/reader01.png', 1, 1),
(5, 'reader02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Zhao Liu', '13800138004', 'zhaoliu@example.com', '/avatars/reader02.png', 2, 1),
(6, 'reader03', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Sun Qi', '13800138005', 'sunqi@example.com', '/avatars/reader03.png', 1, 1),
(7, 'reader04', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Zhou Ba', '13800138006', 'zhouba@example.com', '/avatars/reader04.png', 2, 1),
(8, 'reader05', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Wu Jiu', '13800138007', 'wujiu@example.com', '/avatars/reader05.png', 1, 1);

-- -----------------------------------------------------
-- Insert User-Role Relations
-- -----------------------------------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),  -- admin -> Administrator
(2, 2),  -- librarian01 -> Librarian
(3, 2),  -- librarian02 -> Librarian
(4, 3),  -- reader01 -> Reader
(5, 3),  -- reader02 -> Reader
(6, 3),  -- reader03 -> Reader
(7, 3),  -- reader04 -> Reader
(8, 3);  -- reader05 -> Reader

-- -----------------------------------------------------
-- Insert Book Categories
-- -----------------------------------------------------
INSERT INTO `book_category` (`id`, `name`, `parent_id`, `sort`, `icon`, `status`) VALUES
-- Level 1 Categories
(1, 'Programming', 0, 1, 'code', 1),
(2, 'Literature', 0, 2, 'book-open', 1),
(3, 'Science', 0, 3, 'flask', 1),
(4, 'History', 0, 4, 'clock', 1),
(5, 'Art', 0, 5, 'palette', 1),
(6, 'Economics', 0, 6, 'trending-up', 1),
(7, 'Philosophy', 0, 7, 'brain', 1),

-- Level 2 Categories (Programming)
(11, 'Java', 1, 1, NULL, 1),
(12, 'Python', 1, 2, NULL, 1),
(13, 'JavaScript', 1, 3, NULL, 1),
(14, 'C/C++', 1, 4, NULL, 1),
(15, 'Database', 1, 5, NULL, 1),
(16, 'Algorithm', 1, 6, NULL, 1),

-- Level 2 Categories (Literature)
(21, 'Novel', 2, 1, NULL, 1),
(22, 'Poetry', 2, 2, NULL, 1),
(23, 'Drama', 2, 3, NULL, 1),

-- Level 2 Categories (Science)
(31, 'Physics', 3, 1, NULL, 1),
(32, 'Chemistry', 3, 2, NULL, 1),
(33, 'Biology', 3, 3, NULL, 1),

-- Level 2 Categories (History)
(41, 'World History', 4, 1, NULL, 1),
(42, 'Chinese History', 4, 2, NULL, 1);

-- -----------------------------------------------------
-- Insert Books
-- -----------------------------------------------------
INSERT INTO `book_info` (`id`, `title`, `author`, `isbn`, `publisher`, `publish_date`, `category_id`, `price`, `stock`, `available_stock`, `location`, `cover`, `description`, `status`) VALUES
-- Programming - Java
(1, 'Effective Java (3rd Edition)', 'Joshua Bloch', '978-7-111-59369-5', 'Machine Press', '2018-01-01', 11, 119.00, 10, 8, 'A-1-001', '/covers/effective-java.jpg', 'Comprehensive guide to Java programming best practices and design patterns.', 1),
(2, 'Java Concurrency in Practice', 'Brian Goetz', '978-7-111-58942-1', 'Machine Press', '2016-01-01', 11, 89.00, 8, 6, 'A-1-002', '/covers/java-concurrency.jpg', 'In-depth exploration of Java concurrent programming.', 1),
(3, 'Spring Boot in Action', 'Craig Walls', '978-7-115-45432-8', 'Posts & Telecom Press', '2017-01-01', 11, 79.00, 15, 12, 'A-1-003', '/covers/spring-boot.jpg', 'Practical guide to building applications with Spring Boot.', 1),
(4, 'Head First Java (3rd Edition)', 'Kathy Sierra', '978-7-519-80234-5', 'China Youth Press', '2020-01-01', 11, 128.00, 20, 18, 'A-1-004', '/covers/head-first-java.jpg', 'Brain-friendly guide for learning Java programming.', 1),

-- Programming - Python
(5, 'Python Crash Course', 'Eric Matthes', '978-7-115-51087-1', 'Posts & Telecom Press', '2020-01-01', 12, 89.00, 12, 10, 'A-2-001', '/covers/python-crash.jpg', 'Fast-paced introduction to Python programming.', 1),
(6, 'Fluent Python (2nd Edition)', 'Luciano Ramalho', '978-7-115-58923-4', 'Posts & Telecom Press', '2022-01-01', 12, 139.00, 6, 5, 'A-2-002', '/covers/fluent-python.jpg', 'Advanced Python programming techniques and best practices.', 1),
(7, 'Python Machine Learning', 'Sebastian Raschka', '978-7-111-59370-1', 'Machine Press', '2019-01-01', 12, 99.00, 8, 6, 'A-2-003', '/covers/python-ml.jpg', 'Machine learning with Python and scikit-learn.', 1),

-- Programming - JavaScript
(8, 'JavaScript: The Good Parts', 'Douglas Crockford', '978-7-121-18616-7', 'Electronic Industry Press', '2015-01-01', 13, 49.00, 15, 13, 'A-3-001', '/covers/js-good-parts.jpg', 'Essential JavaScript patterns and best practices.', 1),
(9, 'React Up and Running', 'Stoyan Stefanov', '978-7-115-46789-2', 'Posts & Telecom Press', '2018-01-01', 13, 69.00, 10, 8, 'A-3-002', '/covers/react-up.jpg', 'Building web applications with React.', 1),
(10, 'Node.js Design Patterns', 'Mario Casciaro', '978-7-115-48123-5', 'Posts & Telecom Press', '2019-01-01', 13, 99.00, 7, 6, 'A-3-003', '/covers/node-patterns.jpg', 'Advanced Node.js patterns and practices.', 1),

-- Programming - Algorithm
(11, 'Introduction to Algorithms (4th Edition)', 'Thomas H. Cormen', '978-7-111-62345-6', 'Machine Press', '2022-01-01', 16, 168.00, 5, 4, 'A-6-001', '/covers/algo-intro.jpg', 'Comprehensive textbook on algorithms and data structures.', 1),
(12, 'Design Patterns', 'Gang of Four', '978-7-111-21054-3', 'Machine Press', '2010-01-01', 16, 79.00, 10, 7, 'A-6-002', '/covers/design-patterns.jpg', 'Classic book on software design patterns.', 1),
(13, 'Clean Code', 'Robert C. Martin', '978-7-115-21654-3', 'Posts & Telecom Press', '2011-01-01', 16, 69.00, 12, 10, 'A-6-003', '/covers/clean-code.jpg', 'A handbook of agile software craftsmanship.', 1),

-- Literature - Novel
(14, 'One Hundred Years of Solitude', 'Gabriel Garcia Marquez', '978-7-544-22678-9', 'Nanhai Publishing', '2011-01-01', 21, 55.00, 20, 15, 'B-1-001', '/covers/solitude.jpg', 'A masterpiece of magical realism literature.', 1),
(15, 'To Kill a Mockingbird', 'Harper Lee', '978-7-544-73421-5', 'Yilin Press', '2017-01-01', 21, 48.00, 18, 14, 'B-1-002', '/covers/mockingbird.jpg', 'Classic American novel about justice and morality.', 1),
(16, '1984', 'George Orwell', '978-7-530-21234-5', 'Beijing October Literature', '2010-01-01', 21, 38.00, 25, 20, 'B-1-003', '/covers/1984.jpg', 'Dystopian novel about totalitarian society.', 1),
(17, 'The Great Gatsby', 'F. Scott Fitzgerald', '978-7-544-71234-8', 'Yilin Press', '2016-01-01', 21, 35.00, 15, 12, 'B-1-004', '/covers/gatsby.jpg', 'Classic novel about the American Dream.', 1),

-- Science - Physics
(18, 'A Brief History of Time', 'Stephen Hawking', '978-7-535-78901-2', 'Hunan Science Tech', '2010-01-01', 31, 45.00, 30, 25, 'C-1-001', '/covers/brief-time.jpg', 'Introduction to cosmology and theoretical physics.', 1),
(19, 'The Elegant Universe', 'Brian Greene', '978-7-535-72345-6', 'Hunan Science Tech', '2012-01-01', 31, 52.00, 15, 12, 'C-1-002', '/covers/elegant.jpg', 'String theory and the quest for the ultimate theory.', 1),

-- History
(20, 'Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', '978-7-508-65432-1', 'CITIC Press', '2017-01-01', 41, 68.00, 25, 20, 'D-1-001', '/covers/sapiens.jpg', 'A brief history of human evolution and civilization.', 1),
(21, 'Guns, Germs, and Steel', 'Jared Diamond', '978-7-532-78901-8', 'Shanghai Translation', '2016-01-01', 41, 62.00, 18, 15, 'D-1-002', '/covers/guns-germs.jpg', 'The fates of human societies throughout history.', 1),

-- Economics
(22, 'Capital in the Twenty-First Century', 'Thomas Piketty', '978-7-508-64567-9', 'CITIC Press', '2014-01-01', 6, 98.00, 12, 10, 'E-1-001', '/covers/capital.jpg', 'Analysis of wealth inequality in the modern world.', 1),
(23, 'The Wealth of Nations', 'Adam Smith', '978-7-100-01234-5', 'Commercial Press', '2015-01-01', 6, 58.00, 20, 18, 'E-1-002', '/covers/wealth.jpg', 'Classic work on economics and free markets.', 1),

-- Philosophy
(24, 'Meditations', 'Marcus Aurelius', '978-7-201-08901-2', 'Tianjin People', '2018-01-01', 7, 38.00, 22, 18, 'F-1-001', '/covers/meditations.jpg', 'Stoic philosophy from the Roman Emperor.', 1),
(25, 'Being and Time', 'Martin Heidegger', '978-7-108-01234-5', 'SDX Joint Publishing', '2010-01-01', 7, 78.00, 8, 6, 'F-1-002', '/covers/being-time.jpg', 'Fundamental ontology and phenomenology.', 1);

-- -----------------------------------------------------
-- Insert Borrow Records
-- -----------------------------------------------------
INSERT INTO `borrow_record` (`id`, `user_id`, `book_id`, `borrow_date`, `due_date`, `return_date`, `renew_count`, `status`, `remark`) VALUES
-- Currently borrowed
(1, 4, 1, '2024-03-01 10:00:00', '2024-03-31 10:00:00', NULL, 0, 0, NULL),
(2, 4, 5, '2024-03-05 14:30:00', '2024-04-04 14:30:00', NULL, 0, 0, NULL),
(3, 5, 2, '2024-03-08 09:15:00', '2024-04-07 09:15:00', NULL, 1, 0, NULL),
(4, 5, 11, '2024-03-10 16:45:00', '2024-04-09 16:45:00', NULL, 0, 0, NULL),
(5, 6, 14, '2024-03-12 11:20:00', '2024-04-11 11:20:00', NULL, 0, 0, NULL),
(6, 6, 18, '2024-03-15 13:00:00', '2024-04-14 13:00:00', NULL, 0, 0, NULL),
(7, 7, 8, '2024-03-16 10:30:00', '2024-04-15 10:30:00', NULL, 0, 0, NULL),

-- Returned
(8, 4, 3, '2024-02-01 09:00:00', '2024-03-02 09:00:00', '2024-02-28 15:30:00', 0, 1, 'Returned on time'),
(9, 5, 6, '2024-02-10 14:00:00', '2024-03-11 14:00:00', '2024-03-08 10:00:00', 1, 1, 'Renewed once'),
(10, 6, 15, '2024-02-15 11:00:00', '2024-03-16 11:00:00', '2024-03-10 16:45:00', 0, 1, NULL),
(11, 7, 20, '2024-02-20 16:30:00', '2024-03-21 16:30:00', '2024-03-18 09:00:00', 0, 1, 'Returned early'),

-- Overdue
(12, 8, 4, '2024-02-01 10:00:00', '2024-03-02 10:00:00', NULL, 0, 2, 'Overdue for return'),
(13, 8, 9, '2024-02-05 14:00:00', '2024-03-07 14:00:00', NULL, 0, 2, 'Overdue for return');

-- -----------------------------------------------------
-- Insert Borrow Configuration
-- -----------------------------------------------------
INSERT INTO `borrow_config` (`config_key`, `config_value`, `description`) VALUES
('max_borrow_count', '5', 'Maximum books a user can borrow at once'),
('max_borrow_days', '30', 'Default borrowing period in days'),
('max_renew_times', '2', 'Maximum renewal times per borrow'),
('renew_days', '15', 'Days added per renewal'),
('overdue_fine_rate', '0.50', 'Daily fine rate for overdue books (CNY)'),
('max_overdue_days', '90', 'Maximum overdue days before marked as lost');
```

### 8.2 测试账号

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | Administrator | 系统管理员，拥有所有权限 |
| librarian01 | 123456 | Librarian | 图书管理员，可管理图书和借阅 |
| librarian02 | 123456 | Librarian | 图书管理员 |
| reader01 | 123456 | Reader | 普通读者，可借阅图书 |
| reader02 | 123456 | Reader | 普通读者 |
| reader03 | 123456 | Reader | 普通读者 |
| reader04 | 123456 | Reader | 普通读者 |
| reader05 | 123456 | Reader | 普通读者 |

> **注意**: 实际部署时请使用 BCrypt 加密后的密码，示例中的密码哈希值仅为占位符。

---

## 附录

### A. 常用命令速查

```bash
# ===== Backend Commands =====
# Build all services
mvn clean package -DskipTests

# Run single service
mvn spring-boot:run -pl user-service

# View dependency tree
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates

# ===== Frontend Commands =====
# Install dependencies
npm install

# Start dev server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Check for outdated packages
npm outdated

# ===== Docker Commands =====
# Build and start all
docker-compose up -d --build

# View logs
docker-compose logs -f [service]

# Restart service
docker-compose restart [service]

# Stop all
docker-compose down

# Clean up volumes
docker-compose down -v

# ===== Database Commands =====
# Connect to MySQL
mysql -u bookuser -p book_manage_system

# Export database
mysqldump -u bookuser -p book_manage_system > backup.sql

# Import database
mysql -u bookuser -p book_manage_system < backup.sql
```

### B. Swagger API 文档访问

| 服务 | 地址 |
|------|------|
| Gateway | http://localhost:8080/swagger-ui.html |
| User Service | http://localhost:8081/swagger-ui.html |
| Book Service | http://localhost:8082/swagger-ui.html |
| Borrow Service | http://localhost:8083/swagger-ui.html |

### C. 环境变量说明

```bash
# Backend Environment Variables
SPRING_PROFILES_ACTIVE=docker
NACOS_SERVER_ADDR=nacos:8848
MYSQL_HOST=mysql
MYSQL_PORT=3306
MYSQL_DB=book_manage_system
MYSQL_USER=bookuser
MYSQL_PASSWORD=book123456

# Frontend Environment Variables
VITE_API_BASE_URL=http://localhost:8080
```

---

**文档版本**: v1.0  
**最后更新**: 2024-03-19  
**作者**: Book Management System Team
