# 图书管理系统 API 接口测试报告

**测试时间**: 2026-03-21  
**测试人员**: Automated Test  
**测试环境**: Docker (Gateway:8080, MySQL:3306, Nacos:8848)

---

## 一、测试概述

本次测试覆盖了图书管理系统的所有核心API接口，包括：
- User Service (用户服务)
- Book Service (图书服务)
- Category Service (分类服务)
- Borrow Service (借阅服务)
- Stats Service (统计服务)

**测试结果统计**：
- 总接口数: 20+
- 通过: 20+
- 失败: 0
- 通过率: 100%

---

## 二、问题修复记录

### 2.1 发现的问题
在测试初期发现所有接口返回500错误，错误信息如下：
```
java.lang.IllegalArgumentException: Name for argument of type [java.lang.String] not specified, 
and parameter name information not available via reflection. 
Ensure that the compiler uses the '-parameters' flag.
```

### 2.2 问题原因
Maven编译器插件未配置`-parameters`选项，导致Spring无法通过反射获取Controller方法的参数名称。

### 2.3 修复方案
在 `pom.xml` 中添加编译参数：
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>${java.version}</source>
        <target>${java.version}</target>
        <parameters>true</parameters>
        <compilerArgs>
            <arg>-parameters</arg>
        </compilerArgs>
        ...
    </configuration>
</plugin>
```

### 2.4 修复结果
重新构建并部署后，所有接口恢复正常。

---

## 三、接口测试详情（可直接执行的curl命令）

> **说明**: 请先执行"用户注册"获取TOKEN，然后将TOKEN替换到后续命令中

### 3.1 User Service 接口

#### 3.1.1 POST /api/user/auth/register - 用户注册（获取TOKEN）
```bash
# 用户注册并获取TOKEN
curl -X POST "http://localhost:8080/api/user/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123","nickname":"Test User"}'
```

**预期响应**: 返回用户信息和TOKEN，请保存TOKEN用于后续测试

---

#### 3.1.2 POST /api/user/auth/login - 用户登录
```bash
curl -X POST "http://localhost:8080/api/user/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

---

#### 3.1.3 GET /api/user/auth/info - 获取当前用户信息
```bash
# 替换YOUR_TOKEN为实际的JWT Token
curl -X GET "http://localhost:8080/api/user/auth/info" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.1.4 GET /api/user/list - 获取用户列表
```bash
curl -X GET "http://localhost:8080/api/user/list?pageNum=1&pageSize=5" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.1.5 GET /api/user/{id} - 获取用户详情
```bash
curl -X GET "http://localhost:8080/api/user/1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

### 3.2 Book Service 接口

#### 3.2.1 GET /api/book/list - 获取图书列表
```bash
curl -X GET "http://localhost:8080/api/book/list?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**带筛选条件**:
```bash
# 按标题搜索
curl -X GET "http://localhost:8080/api/book/list?title=Java&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按分类筛选
curl -X GET "http://localhost:8080/api/book/list?categoryId=11&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.2.2 GET /api/book/{id} - 获取图书详情
```bash
curl -X GET "http://localhost:8080/api/book/1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.2.3 GET /api/book/search - 搜索图书
```bash
curl -X GET "http://localhost:8080/api/book/search?keyword=Java&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.2.4 POST /api/book - 创建图书
```bash
curl -X POST "http://localhost:8080/api/book" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot实战",
    "author": "张三",
    "isbn": "978-7-111-12345-6",
    "publisher": "机械工业出版社",
    "publishDate": "2024-01-01",
    "categoryId": 11,
    "price": 89.00,
    "stock": 10,
    "availableStock": 10,
    "location": "A-1-010",
    "description": "Spring Boot实战指南"
  }'
```

---

#### 3.2.5 PUT /api/book - 更新图书
```bash
curl -X PUT "http://localhost:8080/api/book" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "title": "Effective Java (3rd Edition) - Updated",
    "stock": 15,
    "availableStock": 12
  }'
```

---

#### 3.2.6 PUT /api/book/stock - 更新图书库存
```bash
curl -X PUT "http://localhost:8080/api/book/stock?id=1&stock=20&availableStock=18" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.2.7 DELETE /api/book/{id} - 删除图书
```bash
curl -X DELETE "http://localhost:8080/api/book/26" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

### 3.3 Category 接口

#### 3.3.1 GET /api/category/tree - 获取分类树
```bash
curl -X GET "http://localhost:8080/api/category/tree" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.3.2 GET /api/category/list - 获取分类列表
```bash
curl -X GET "http://localhost:8080/api/category/list" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.3.3 POST /api/category - 创建分类
```bash
curl -X POST "http://localhost:8080/api/category" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Category",
    "parentId": 1,
    "sort": 10,
    "status": 1
  }'
```

---

### 3.4 Borrow Service 接口

#### 3.4.1 POST /api/borrow - 借书
```bash
# userId: 用户ID, bookId: 图书ID
curl -X POST "http://localhost:8080/api/borrow" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"bookId":1}'
```

**带自定义借阅天数**:
```bash
curl -X POST "http://localhost:8080/api/borrow" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"bookId":1,"days":14}'
```

---

#### 3.4.2 POST /api/borrow/return - 还书
```bash
# recordId: 借阅记录ID
curl -X POST "http://localhost:8080/api/borrow/return?recordId=1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.4.3 POST /api/borrow/renew - 续借
```bash
# recordId: 借阅记录ID, days: 续借天数(可选)
curl -X POST "http://localhost:8080/api/borrow/renew?recordId=1" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 指定续借天数
curl -X POST "http://localhost:8080/api/borrow/renew?recordId=1&days=7" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.4.4 GET /api/borrow/list - 获取借阅记录列表
```bash
# 基础查询
curl -X GET "http://localhost:8080/api/borrow/list?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按用户筛选
curl -X GET "http://localhost:8080/api/borrow/list?userId=1&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按状态筛选 (0-借阅中, 1-已归还, 2-逾期)
curl -X GET "http://localhost:8080/api/borrow/list?status=0&pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.4.5 GET /api/borrow/my - 获取当前用户借阅记录
```bash
curl -X GET "http://localhost:8080/api/borrow/my?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.4.6 GET /api/borrow/overdue - 获取逾期记录
```bash
curl -X GET "http://localhost:8080/api/borrow/overdue?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.4.7 GET /api/borrow/user/{userId} - 获取指定用户的借阅记录
```bash
curl -X GET "http://localhost:8080/api/borrow/user/1?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

### 3.5 Stats 接口

#### 3.5.1 GET /api/stats/overview - 获取统计概览
```bash
curl -X GET "http://localhost:8080/api/stats/overview" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.5.2 GET /api/stats/borrow/trend - 获取借阅趋势
```bash
# 按周统计
curl -X GET "http://localhost:8080/api/stats/borrow/trend?type=week" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按月统计
curl -X GET "http://localhost:8080/api/stats/borrow/trend?type=month" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按天统计
curl -X GET "http://localhost:8080/api/stats/borrow/trend?type=day" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.5.3 GET /api/stats/book/hot - 获取热门图书
```bash
# 默认按月统计
curl -X GET "http://localhost:8080/api/stats/book/hot?limit=10" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按周统计
curl -X GET "http://localhost:8080/api/stats/book/hot?limit=5&period=week" \
  -H "Authorization: Bearer YOUR_TOKEN"

# 按年统计
curl -X GET "http://localhost:8080/api/stats/book/hot?limit=10&period=year" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.5.4 GET /api/stats/category/distribution - 获取分类分布
```bash
curl -X GET "http://localhost:8080/api/stats/category/distribution" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

#### 3.5.5 GET /api/stats/user/active - 获取活跃用户
```bash
curl -X GET "http://localhost:8080/api/stats/user/active?limit=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 四、快速测试脚本

### 4.1 一键获取TOKEN并测试
```bash
#!/bin/bash

# 基础URL
BASE_URL="http://localhost:8080"

# 1. 注册用户获取TOKEN
echo "=== 1. 注册用户获取TOKEN ==="
RESPONSE=$(curl -s -X POST "$BASE_URL/api/user/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"username":"testapi","password":"test123","nickname":"API Test"}')

echo "$RESPONSE"

# 提取TOKEN
TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo ""
echo "TOKEN: $TOKEN"
echo ""

# 2. 获取用户信息
echo "=== 2. 获取用户信息 ==="
curl -s -X GET "$BASE_URL/api/user/auth/info" \
  -H "Authorization: Bearer $TOKEN" | jq .
echo ""

# 3. 获取图书列表
echo "=== 3. 获取图书列表 ==="
curl -s -X GET "$BASE_URL/api/book/list?pageNum=1&pageSize=3" \
  -H "Authorization: Bearer $TOKEN" | jq .
echo ""

# 4. 获取统计概览
echo "=== 4. 获取统计概览 ==="
curl -s -X GET "$BASE_URL/api/stats/overview" \
  -H "Authorization: Bearer $TOKEN" | jq .
echo ""

# 5. 借书测试
echo "=== 5. 借书测试 ==="
curl -s -X POST "$BASE_URL/api/borrow" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"bookId":1}' | jq .
echo ""

echo "测试完成!"
```

### 4.2 Windows PowerShell 测试脚本
```powershell
# 基础URL
$BASE_URL = "http://localhost:8080"

# 1. 注册用户获取TOKEN
Write-Host "=== 1. 注册用户获取TOKEN ===" -ForegroundColor Green
$response = Invoke-RestMethod -Uri "$BASE_URL/api/user/auth/register" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"username":"testpwsh","password":"test123","nickname":"PowerShell Test"}'

$TOKEN = $response.data.token
Write-Host "TOKEN: $TOKEN"
Write-Host ""

# 2. 获取用户信息
Write-Host "=== 2. 获取用户信息 ===" -ForegroundColor Green
Invoke-RestMethod -Uri "$BASE_URL/api/user/auth/info" `
  -Headers @{ Authorization = "Bearer $TOKEN" } | ConvertTo-Json -Depth 5
Write-Host ""

# 3. 获取图书列表
Write-Host "=== 3. 获取图书列表 ===" -ForegroundColor Green
Invoke-RestMethod -Uri "$BASE_URL/api/book/list?pageNum=1&pageSize=3" `
  -Headers @{ Authorization = "Bearer $TOKEN" } | ConvertTo-Json -Depth 5
Write-Host ""

# 4. 获取统计概览
Write-Host "=== 4. 获取统计概览 ===" -ForegroundColor Green
Invoke-RestMethod -Uri "$BASE_URL/api/stats/overview" `
  -Headers @{ Authorization = "Bearer $TOKEN" } | ConvertTo-Json
Write-Host ""

Write-Host "测试完成!" -ForegroundColor Green
```

---

## 五、测试总结

### 5.1 测试结论
所有核心接口测试通过，系统功能正常运行。

### 5.2 修复内容
1. 修复了Maven编译器缺少`-parameters`配置导致的接口500错误
2. 修改文件: `pom.xml`

### 5.3 接口清单

| 服务 | 接口 | 方法 | 状态 |
|------|------|------|------|
| User | /api/user/auth/login | POST | ✅ |
| User | /api/user/auth/register | POST | ✅ |
| User | /api/user/auth/info | GET | ✅ |
| User | /api/user/auth/logout | POST | ✅ |
| User | /api/user/list | GET | ✅ |
| User | /api/user/{id} | GET | ✅ |
| Book | /api/book/list | GET | ✅ |
| Book | /api/book/{id} | GET | ✅ |
| Book | /api/book/search | GET | ✅ |
| Book | /api/book | POST | ✅ |
| Book | /api/book | PUT | ✅ |
| Book | /api/book/{id} | DELETE | ✅ |
| Book | /api/book/stock | PUT | ✅ |
| Category | /api/category/tree | GET | ✅ |
| Category | /api/category/list | GET | ✅ |
| Category | /api/category | POST | ✅ |
| Borrow | /api/borrow | POST | ✅ |
| Borrow | /api/borrow/return | POST | ✅ |
| Borrow | /api/borrow/renew | POST | ✅ |
| Borrow | /api/borrow/list | GET | ✅ |
| Borrow | /api/borrow/my | GET | ✅ |
| Borrow | /api/borrow/overdue | GET | ✅ |
| Borrow | /api/borrow/user/{userId} | GET | ✅ |
| Stats | /api/stats/overview | GET | ✅ |
| Stats | /api/stats/borrow/trend | GET | ✅ |
| Stats | /api/stats/book/hot | GET | ✅ |
| Stats | /api/stats/category/distribution | GET | ✅ |
| Stats | /api/stats/user/active | GET | ✅ |

---

**报告更新时间**: 2026-03-21 18:45