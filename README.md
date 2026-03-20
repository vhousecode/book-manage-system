# 图书管理系统

一个基于微服务架构的全栈图书管理系统，支持图书管理、借阅管理、用户权限管理和统计分析等功能。

## 技术栈

### 后端
- Java 17
- Spring Boot 3.2.3
- Spring Cloud 2023.0.0
- Spring Cloud Alibaba 2023.0.0.0-RC1
- Spring Cloud Gateway（API网关）
- MyBatis-Plus 3.5.5（ORM框架）
- MySQL 8.0（数据库）
- Druid 1.2.21（连接池）
- Redis（缓存）
- Nacos 2.2.3（服务注册中心）
- JWT（身份认证）
- Knife4j 4.4.0（API文档）

### 前端
- React 18
- TypeScript
- Tailwind CSS
- Vite（构建工具）
- React Query（数据请求）
- Zustand（状态管理）
- ECharts（图表库）

## 项目结构

```
book-manage-system/
├── backend/                        # 后端项目
│   ├── common/                     # 公共模块（工具类、实体、配置）
│   ├── gateway/                    # API网关服务（端口 8080）
│   ├── user-service/               # 用户服务（端口 8081）
│   ├── book-service/               # 图书服务（端口 8082）
│   └── borrow-service/             # 借阅服务（端口 8083）
├── frontend/                       # 前端项目
├── sql/
│   ├── schema.sql                  # 数据库表结构
│   └── data.sql                    # 初始化数据
├── docker-compose.yml              # Docker编排配置
└── .env.example                    # 环境变量模板
```

## 系统架构

```
                                    ┌─────────────────────────────────────┐
                                    │           Frontend (React)          │
                                    │         http://localhost:5173       │
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
          │ - 用户认证   │  │ - 图书管理   │   │ - 借阅管理    │  │ - 服务注册    │  │ - 数据存储    │
          │ - 角色权限   │  │ - 分类管理   │   │ - 归还处理    │  │              │  │              │
          │ - 用户管理   │  │ - 库存管理   │   │ - 统计分析    │  │              │  │              │
          └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘
```

## 功能模块

- **用户权限系统**：用户注册/登录、角色管理、权限控制
- **图书管理**：图书信息CRUD、分类管理、库存管理、图书搜索
- **借阅管理**：借书申请、归还处理、续借功能、逾期管理
- **统计分析**：借阅统计、图书热度排行、用户活跃度分析

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- Node.js 18+
- Docker & Docker Compose（推荐，一键启动所有依赖服务）

### 方式一：Docker 一键部署（推荐）

```bash
# 启动所有服务（Nacos、MySQL、Redis、后端服务、前端）
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f [服务名]

# 停止所有服务
docker-compose down
```

**访问地址：**
- 前端页面：http://localhost:3000（Docker）或 http://localhost:5173（本地开发）
- Nacos 控制台：http://localhost:8848/nacos （默认无认证）
- API 网关：http://localhost:8080

### 方式二：本地开发模式

#### 1. 启动基础设施（使用 Docker）

```bash
# 仅启动 MySQL、Redis、Nacos
docker-compose up -d mysql redis nacos
```

等待 Nacos 启动完成（约 30-60 秒），访问 http://localhost:8848/nacos 确认正常。

#### 2. 初始化数据库

```bash
# 进入 MySQL 容器
docker exec -it book-mysql mysql -uroot -proot123456

# 在 MySQL 中执行
source /docker-entrypoint-initdb.d/01-schema.sql;
source /docker-entrypoint-initdb.d/02-data.sql;
```

#### 3. 启动后端服务

**IDEA 运行配置：**

在启动配置的 **Environment variables** 中添加：
```
NACOS_SERVER_ADDR=127.0.0.1:8848;MYSQL_HOST=localhost;MYSQL_PORT=3306;MYSQL_DB=book_manage;MYSQL_USER=root;MYSQL_PASSWORD=root123456
```

**命令行启动：**

```bash
cd backend

# 构建
mvn clean install -DskipTests

# 启动服务（在 separate terminals 中执行）
cd gateway && mvn spring-boot:run -Dspring-boot.run.arguments="--NACOS_SERVER_ADDR=127.0.0.1:8848"
cd user-service && mvn spring-boot:run -Dspring-boot.run.arguments="--NACOS_SERVER_ADDR=127.0.0.1:8848"
cd book-service && mvn spring-boot:run -Dspring-boot.run.arguments="--NACOS_SERVER_ADDR=127.0.0.1:8848"
cd borrow-service && mvn spring-boot:run -Dspring-boot.run.arguments="--NACOS_SERVER_ADDR=127.0.0.1:8848"
```

#### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务器启动后，访问 http://localhost:5173

## API 文档

访问 Swagger UI：`http://localhost:8080/swagger-ui.html`

### 主要接口

#### 用户认证
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/auth/login | 用户登录 |
| POST | /api/user/auth/register | 用户注册 |
| GET | /api/user/auth/info | 获取当前用户信息 |

#### 图书管理
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/book/list | 获取图书列表（分页） |
| GET | /api/book/{id} | 获取图书详情 |
| POST | /api/book | 新增图书（管理员） |
| PUT | /api/book | 更新图书（管理员） |
| DELETE | /api/book/{id} | 删除图书（管理员） |

#### 借阅管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/borrow | 借阅图书 |
| POST | /api/borrow/return | 归还图书 |
| POST | /api/borrow/renew | 续借图书 |
| GET | /api/borrow/my | 获取我的借阅记录 |

#### 统计分析
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/stats/overview | 获取概览统计 |
| GET | /api/stats/borrow/trend | 获取借阅趋势 |
| GET | /api/stats/book/hot | 获取热门图书 |

## 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| librarian01 | 123456 | 图书管理员 |
| reader01 | 123456 | 读者 |

## 配置说明

### 1. Nacos 配置

项目使用 Nacos 作为**服务注册中心**（不使用配置中心功能）。

**docker-compose.yml 中的 Nacos 配置：**
```yaml
nacos:
  image: nacos/nacos-server:v2.2.3
  environment:
    MODE: standalone              # 单机模式
    PREFER_HOST_MODE: hostname
    NACOS_AUTH_ENABLE: "false"    # 禁用认证（开发环境）
    JVM_XMS: 256m
    JVM_XMX: 512m
  ports:
    - "8848:8848"   # HTTP 端口
    - "9848:9848"   # gRPC 端口
```

**说明：**
- Nacos 使用内置 Derby 数据库，无需外部 MySQL
- 开发环境已禁用认证，可直接访问
- 如需启用认证，将 `NACOS_AUTH_ENABLE` 改为 `"true"`
- 仅使用 Nacos 服务注册发现功能，不使用配置中心

### 2. 后端服务配置

各服务的 `application.yml` 默认配置：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_SERVER_ADDR:127.0.0.1:8848}  # Nacos 地址
        namespace: ${NACOS_NAMESPACE:public}
        group: ${NACOS_GROUP:DEFAULT_GROUP}
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB:book_manage}?
        useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: ${MYSQL_USER:root}
    password: ${MYSQL_PASSWORD:root123456}
```

**环境变量说明：**

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `NACOS_SERVER_ADDR` | 127.0.0.1:8848 | Nacos 服务地址 |
| `NACOS_NAMESPACE` | public | Nacos 命名空间 |
| `NACOS_GROUP` | DEFAULT_GROUP | Nacos 服务分组 |
| `MYSQL_HOST` | localhost | MySQL 主机地址 |
| `MYSQL_PORT` | 3306 | MySQL 端口 |
| `MYSQL_DB` | book_manage | 数据库名 |
| `MYSQL_USER` | root | 数据库用户名 |
| `MYSQL_PASSWORD` | root123456 | 数据库密码 |

### 3. 前端配置

创建 `frontend/.env` 文件：
```env
# 开发环境 API 地址
VITE_API_BASE_URL=http://localhost:8080
```

生产环境构建时：
```bash
# 使用生产环境配置
VITE_API_BASE_URL=http://your-production-domain:8080 npm run build
```

### 4. 密码安全说明

**密码传输与存储机制：**

1. **前端传输**：密码以明文形式通过 HTTPS 传输到后端
2. **后端存储**：后端使用 BCrypt 算法对密码进行哈希处理后存储到数据库
3. **密码验证**：登录时，后端使用 `PasswordUtils.matches()` 对比明文密码和数据库中的哈希值

**安全建议：**
- 生产环境务必启用 HTTPS，确保密码传输安全
- 数据库中存储的是 BCrypt 哈希值，无法反解出原始密码
- 建议定期更新 JWT Secret 和数据库密码

## 开发指南

详细的开发文档请参考 [BOOK_MANAGE_SYSTEM_PLAN.md](./BOOK_MANAGE_SYSTEM_PLAN.md)，包含：
- 完整的系统架构设计
- 数据库表结构设计
- API接口详细规范
- 前端技术架构
- 部署方案

## 更新日志

### 2026-03-20
- 优化 Nacos 配置，使用内置 Derby 数据库，无需外部 MySQL
- 禁用 Nacos 认证（开发环境），访问无需登录
- 移除后端服务 Nacos 配置中心依赖，仅使用服务发现功能
- 前端开发端口改为 5173（避免与 Docker 前端容器 3000 端口冲突）
- 统一数据库名称为 `book_manage`
- 添加密码安全说明文档
- 完善 README 文档结构

## 许可证

MIT License