# Spring Boot 后端代码规范参考

## 项目结构规范

### 包命名
```
com.{公司}.{项目}
├── common/           # 共享工具和配置
├── config/           # 配置类
├── controller/       # REST控制器
├── service/          # 业务逻辑层
│   └── impl/         # 服务实现
├── mapper/           # MyBatis映射器
├── entity/           # JPA/MyBatis实体
├── dto/              # 数据传输对象
│   ├── request/      # 请求DTO
│   └── response/     # 响应DTO
├── enums/            # 枚举类
├── exception/        # 自定义异常
└── util/             # 工具类
```

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类 | PascalCase | `UserService` |
| 接口 | PascalCase | `UserService` |
| 实现类 | PascalCase + Impl | `UserServiceImpl` |
| 方法 | camelCase | `getUserById()` |
| 变量 | camelCase | `userName` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT` |
| 包 | 全小写 | `com.example.user` |

## 实体类规范

### 必需字段
```java
public class BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
}
```

### 字段注解
```java
@TableName("sys_user")
public class User extends BaseEntity {
    @TableField("username")
    private String username;
    
    @TableField("status")
    private Integer status;
    
    // 使用英文注释避免编码问题
    @ApiModelProperty("用户状态：0-禁用，1-启用")
    private Integer status;
}
```

## 服务层规范

### 接口定义
```java
public interface UserService extends IService<User> {
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户实体
     */
    User getByUsername(String username);
    
    /**
     * 登录验证
     * @param request 登录请求
     * @return 登录响应（含token）
     */
    LoginResponse login(LoginRequest request);
}
```

### 实现模式
```java
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Override
    public User getByUsername(String username) {
        return lambdaQuery()
            .eq(User::getUsername, username)
            .one();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        // 1. 校验输入
        // 2. 查询用户
        // 3. 验证密码
        // 4. 生成token
        // 5. 返回响应
    }
}
```

## 控制器规范

### 基本结构
```java
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取用户")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }
    
    @PostMapping
    @ApiOperation("创建用户")
    public Result<Long> create(@Valid @RequestBody UserRequest request) {
        Long id = userService.create(request);
        return Result.success(id);
    }
}
```

### Swagger注解
```java
@ApiModel("用户登录请求")
public class LoginRequest {
    
    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;
}
```

## 异常处理

### 自定义异常
```java
@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;
    
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
```

### 全局异常处理器
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), message);
    }
}
```

## 配置规范

### application.yml模板
```yaml
server:
  port: 8080

spring:
  application:
    name: service-name
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:db_name}
    username: ${MYSQL_USER:root}
    password: ${MYSQL_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}

mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true

jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: ${JWT_EXPIRATION:86400000}
```