# Spring Boot 微服务开发技能

## 技能描述
本技能帮助创建生产级别的Spring Boot微服务项目，包含服务架构、数据库设计、API规范和部署配置等最佳实践。

## 触发场景
- 用户请求创建新的Spring Boot微服务项目
- 用户需要构建基于Java的后端系统
- 用户想要实现Spring Cloud微服务架构
- 用户需要设计MyBatis-Plus数据库架构
- 用户需要Docker部署配置

## 执行步骤

### 1. 需求收集
询问用户以下问题：
- 核心业务模块和功能需求
- 数据库类型和版本偏好
- 微服务拆分粒度（按业务拆分/单体先行）
- 认证方式（用户名密码/OAuth2/JWT）
- 部署目标（Docker/Kubernetes/裸机）

### 2. 架构设计
- 确定服务边界和职责
- 设计服务通信模式（REST/gRPC/消息队列）
- 规划数据库Schema和关联关系
- 定义API契约和Swagger文档

### 3. 项目结构创建
```
backend/
├── pom.xml                    # 父工程POM，依赖管理
├── common/                    # 公共模块
│   ├── src/main/java/com/{公司}/{项目}/common/
│   │   ├── result/           # 统一响应封装
│   │   ├── exception/        # 全局异常处理
│   │   ├── enums/            # 枚举定义
│   │   ├── constant/         # 系统常量
│   │   ├── utils/            # 工具类
│   │   ├── entity/           # 基础实体
│   │   └── config/           # 公共配置
│   └── pom.xml
├── gateway/                   # API网关服务
│   ├── src/main/java/com/{公司}/{项目}/gateway/
│   │   ├── filter/           # 网关过滤器
│   │   └── handler/          # 异常处理器
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── Dockerfile
│   └── pom.xml
├── {服务名}-service/          # 业务服务
│   ├── src/main/java/com/{公司}/{项目}/{服务}/
│   │   ├── controller/       # REST控制器
│   │   ├── service/          # 业务逻辑
│   │   ├── mapper/           # MyBatis映射器
│   │   ├── entity/           # JPA/MyBatis实体
│   │   └── dto/              # 数据传输对象
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── Dockerfile
│   └── pom.xml
└── sql/
    ├── schema.sql            # 数据库表结构
    └── data.sql              # 初始数据/种子数据
```

### 4. 代码实现
为每个服务生成以下内容：

#### 公共模块
- `Result<T>` - 统一API响应封装
- `ResultCode` - 响应码枚举
- `PageResult<T>` - 分页响应封装
- `BusinessException` - 自定义业务异常
- `GlobalExceptionHandler` - 全局异常处理器
- `MybatisPlusConfig` - MyBatis-Plus配置
- `SwaggerConfig` - API文档配置

#### 实体类
- 使用MyBatis-Plus注解
- 包含审计字段（create_time, update_time）
- 使用英文字段名和注释
- 适当实现逻辑删除

#### 服务层
- 定义服务接口和实现类
- 使用 `@Service` 注解
- 实现CRUD操作
- 添加业务逻辑方法

#### 控制器层
- 使用 `@RestController` 和 `@RequestMapping`
- 添加Swagger注解（`@Api`, `@ApiOperation`）
- 返回 `Result<T>` 包装
- 验证请求参数

### 5. 数据库设计
按照SQL标准技能创建SQL脚本：
- 使用 `utf8mb4` 字符集
- 添加适当的索引
- 使用英文注释避免编码问题
- 包含 created_at, updated_at 时间戳

### 6. Docker配置
- 为每个服务创建 `Dockerfile`
- 创建 `docker-compose.yml` 编排文件
- 配置环境变量
- 设置服务依赖和健康检查

### 7. 文档
创建 README.md 包含：
- 技术栈描述
- 快速启动指南
- API文档链接
- 默认账号密码
- 配置说明

## 输出标准

### 代码质量
- 遵循Java命名规范
- 使用有意义的变量和方法名
- 添加适当的JavaDoc注释
- 遵循SOLID原则

### API响应格式
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1710000000000
}
```

### 错误响应格式
```json
{
  "code": 400,
  "message": "错误描述",
  "data": null,
  "timestamp": 1710000000000
}
```

### Swagger文档规范
- 每个控制器必须有 `@Api` 注解
- 每个端点必须有 `@ApiOperation`
- 记录所有请求/响应参数
- 按标签分组API

### 配置文件规范
- 使用 `application.yml`（而非properties）
- 外部化环境相关配置
- 使用profiles区分不同环境

## 依赖关系

### 依赖的其他技能
- `sql-standards` - 用于数据库Schema设计

### 外部要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Docker（用于容器化部署）

## 示例

### 示例：创建用户服务
```
用户：创建一个包含登录和注册功能的用户服务

Agent执行步骤：
1. 询问认证方式（选择用户名密码）
2. 设计 user, role, user_role 表
3. 创建 user-service 模块包含：
   - UserController（登录、注册、个人信息端点）
   - UserService（认证逻辑）
   - UserMapper（数据库操作）
   - JWT工具类
4. 生成包含用户表的 schema.sql
5. 为 user-service 创建 Dockerfile
```

## 版本信息
- 版本：1.0.0
- 最后更新：2024-03-19