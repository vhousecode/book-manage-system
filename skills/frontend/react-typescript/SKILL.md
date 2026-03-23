---
name: react-typescript
description: 创建生产级别的React应用，包含TypeScript类型系统、组件模式、状态管理和API集成。当用户请求创建React应用、使用React和TypeScript开发前端、实现现代UI和组件化架构、需要状态管理和API集成时使用此技能。
---

# React TypeScript 开发技能

## 技能描述
本技能帮助创建生产级别的React应用，包含TypeScript类型系统、组件模式、状态管理和API集成。

## 触发场景
- 用户请求创建新的React应用
- 用户需要使用React和TypeScript开发前端
- 用户想要实现现代UI和组件化架构
- 用户需要状态管理和API集成

## 执行步骤

### 1. 需求收集
询问用户以下问题：
- 核心功能和页面需求
- CSS框架偏好（Tailwind CSS、Ant Design、Material-UI）
- 状态管理偏好（Zustand、Redux、Jotai）
- 认证需求
- API集成需求

### 2. 项目结构创建
```
frontend/
├── package.json
├── tsconfig.json
├── vite.config.ts              # 或 next.config.js
├── tailwind.config.js          # 如使用Tailwind
├── postcss.config.js
├── index.html
├── .env.example
├── src/
│   ├── main.tsx                # 入口文件
│   ├── App.tsx                 # 根组件
│   ├── api/                    # API层
│   │   ├── index.ts            # Axios实例
│   │   ├── auth.ts             # 认证API
│   │   └── user.ts             # 用户API
│   ├── components/             # 可复用组件
│   │   ├── layout/
│   │   │   └── MainLayout.tsx
│   │   ├── common/
│   │   │   ├── Button.tsx
│   │   │   ├── Input.tsx
│   │   │   └── Modal.tsx
│   │   └── ui/                 # UI基础组件
│   ├── pages/                  # 页面组件
│   │   ├── auth/
│   │   │   ├── Login.tsx
│   │   │   └── Register.tsx
│   │   ├── dashboard/
│   │   └── user/
│   ├── stores/                 # 状态管理
│   │   ├── userStore.ts
│   │   └── uiStore.ts
│   ├── types/                  # TypeScript类型定义
│   │   └── index.ts
│   ├── hooks/                  # 自定义Hooks
│   │   ├── useAuth.ts
│   │   └── usePermission.ts
│   ├── utils/                  # 工具函数
│   │   └── helpers.ts
│   └── styles/                 # 全局样式
│       └── globals.css
├── Dockerfile
└── nginx.conf                  # 用于生产部署
```

### 3. 配置文件

#### package.json
```json
{
  "name": "app-name",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview",
    "lint": "eslint src --ext ts,tsx"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.x",
    "@tanstack/react-query": "^5.x",
    "zustand": "^4.x",
    "axios": "^1.x",
    "clsx": "^2.x",
    "lucide-react": "^0.x"
  },
  "devDependencies": {
    "@types/react": "^18.x",
    "@types/react-dom": "^18.x",
    "@vitejs/plugin-react": "^4.x",
    "typescript": "^5.x",
    "vite": "^5.x",
    "tailwindcss": "^3.x",
    "autoprefixer": "^10.x",
    "postcss": "^8.x"
  }
}
```

### 4. 组件模式

#### 页面组件模式
```tsx
import { useQuery } from '@tanstack/react-query'
import { api } from '@/api/resource'
import type { ResourceType } from '@/types'

export default function PageName() {
  const { data, isLoading, error } = useQuery({
    queryKey: ['resource'],
    queryFn: api.getList,
  })

  if (isLoading) return <Loading />
  if (error) return <Error message={error.message} />

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">页面标题</h1>
      {/* 内容 */}
    </div>
  )
}
```

#### 表单组件模式
```tsx
import { useState } from 'react'
import { useMutation } from '@tanstack/react-query'

interface FormData {
  field1: string
  field2: string
}

export default function FormComponent({ onSuccess }: { onSuccess: () => void }) {
  const [formData, setFormData] = useState<FormData>({
    field1: '',
    field2: '',
  })

  const mutation = useMutation({
    mutationFn: api.create,
    onSuccess,
  })

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    mutation.mutate(formData)
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {/* 表单字段 */}
      <button type="submit" disabled={mutation.isPending}>
        {mutation.isPending ? '提交中...' : '提交'}
      </button>
    </form>
  )
}
```

### 5. API层模式
```tsx
// api/index.ts
import axios from 'axios'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 10000,
})

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

instance.interceptors.response.use(
  (response) => response.data.data,
  (error) => {
    if (error.response?.status === 401) {
      // 处理未授权
    }
    return Promise.reject(error)
  }
)

export default instance
```

### 6. 状态管理模式（Zustand）
```tsx
import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface UserState {
  user: User | null
  token: string | null
  setUser: (user: User | null) => void
  logout: () => void
}

export const useUserStore = create<UserState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      setUser: (user) => set({ user }),
      logout: () => set({ user: null, token: null }),
    }),
    { name: 'user-storage' }
  )
)
```

### 7. 类型定义模式
```tsx
// types/index.ts

// API响应
export interface Result<T> {
  code: number
  message: string
  data: T
}

// 分页响应
export interface PageResult<T> {
  total: number
  pageNum: number
  pageSize: number
  list: T[]
}

// 实体类型
export interface User {
  id: number
  username: string
  nickname: string
  email?: string
  status: number
}

// 请求类型
export interface LoginRequest {
  username: string
  password: string
}
```

## 输出标准

### 代码质量
- 使用TypeScript严格模式
- 避免使用 `any` 类型，类型不确定时使用 `unknown`
- 使用有意义的组件和变量名
- 每个文件一个组件
- 页面组件使用默认导出

### 组件命名
- 组件使用PascalCase：`UserList`, `LoginForm`
- 工具函数使用camelCase：`formatDate`, `parseJSON`
- 常量使用UPPER_SNAKE_CASE：`API_BASE_URL`

### 文件命名
- 组件：`PascalCase.tsx`
- 工具函数：`camelCase.ts`
- 类型：types文件夹中的 `index.ts`
- 样式：`globals.css`, `ComponentName.module.css`

### 性能指南
- 为复杂组件使用React.memo
- 为长列表实现虚拟化
- 懒加载路由和重型组件
- 在hooks中使用正确的依赖数组

## 版本信息
- 版本：1.0.0
- 最后更新：2024-03-19