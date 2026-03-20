# 图书管理系统

一个基于微服务架构的全栈图书管理系统，支持图书管理、借阅管理、用户权限管理和统计分析等功能。

## 技术栈

### 后端
- Java 17
- Spring Boot 3.x
- Spring Cloud Gateway（API网关）
- MyBatis-Plus（ORM框架）
- MySQL 8.0（数据库）
- Redis（缓存）
- Nacos（服务注册与配置中心）
- JWT（身份认证）

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
- MySQL 8.0+
- Redis（可选）
- Docker & Docker Compose（容器化部署）

### 1. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE book_manage CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行建表脚本
source sql/schema.sql;

-- 导入初始数据
source sql/data.sql;
```

### 2. 后端启动

```bash
# 进入后端目录
cd backend

# 构建所有模块
mvn clean install -DskipTests

# 按顺序启动服务：
# 1. 启动网关
java -jar gateway/target/gateway-1.0.0.jar

# 2. 启动用户服务
java -jar user-service/target/user-service-1.0.0.jar

# 3. 启动图书服务
java -jar book-service/target/book-service-1.0.0.jar

# 4. 启动借阅服务
java -jar borrow-service/target/borrow-service-1.0.0.jar
```

### 3. 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 生产构建
npm run build
```

### 4. Docker 部署

```bash
# 复制环境变量配置
cp .env.example .env

# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止所有服务
docker-compose down
```

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

### 后端配置
修改各服务模块的 `application.yml`：
- 数据库连接信息
- Redis 连接信息
- Nacos 服务地址
- JWT 配置

### 前端配置
创建 `.env` 文件：
```env
VITE_API_BASE_URL=http://localhost:8080
```

## 开发指南

详细的开发文档请参考 [BOOK_MANAGE_SYSTEM_PLAN.md](./BOOK_MANAGE_SYSTEM_PLAN.md)，包含：
- 完整的系统架构设计
- 数据库表结构设计
- API接口详细规范
- 前端技术架构
- 部署方案

## 许可证

MIT License