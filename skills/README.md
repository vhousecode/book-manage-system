# Agent Skills 技能库

本目录包含用于代码生成和项目脚手架的Agent技能。

## 技能分类

### 后端技能

| 技能 | 描述 | 路径 |
|------|------|------|
| Spring Boot 微服务 | 创建生产级Spring Boot微服务项目 | `backend/springboot-microservice/` |
| SQL 标准 | 数据库Schema设计和SQL脚本规范 | `backend/sql-standards/` |

### 前端技能

| 技能 | 描述 | 路径 |
|------|------|------|
| React TypeScript | 使用TypeScript创建React应用 | `frontend/react-typescript/` |
| Tailwind CSS | 使用Tailwind CSS进行UI样式设计 | `frontend/tailwind-css/` |

## 技能结构

每个技能遵循标准化结构：

```
skill-name/
├── SKILL.md              # 主技能文档
│   ├── 技能描述
│   ├── 触发场景
│   ├── 执行步骤
│   └── 输出标准
├── references/           # 参考文档
│   └── code-standards.md
├── scripts/              # 自动化脚本
│   └── generate-*.sh
└── assets/               # 模板文件
    └── templates/
```

## 使用方式

### 对于Agent
1. 阅读 `SKILL.md` 了解技能用途和工作流程
2. 按照执行步骤生成一致的输出
3. 参考 `references/` 获取代码规范
4. 使用 `scripts/` 进行自动化任务
5. 使用 `assets/templates/` 获取模板代码

### 对于开发者
1. 使用脚本搭建新项目/模块
2. 复制模板获取一致的代码模式
3. 参考文档了解编码规范

## 安装方法

将 `skills` 目录复制到你的Agent配置目录：

```bash
# 示例：iFlow CLI
cp -r skills ~/.iflow/skills/

# 示例：自定义Agent
cp -r skills /path/to/agent/skills/
```

## 版本信息

- 技能版本：1.0.0
- 创建日期：2024-03-19