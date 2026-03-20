# 前端代码规范参考

## TypeScript 规范

### 类型定义
```typescript
// 对象类型使用interface
interface User {
  id: number
  username: string
  nickname: string
  email?: string  // 可选字段
  status: UserStatus
}

// 联合类型、基本类型使用type
type UserStatus = 'active' | 'inactive' | 'banned'

// 固定值使用enum
enum Status {
  Inactive = 0,
  Active = 1,
  Banned = 2,
}
```

### API响应类型
```typescript
interface Result<T> {
  code: number
  message: string
  data: T
  timestamp: number
}

interface PageResult<T> {
  total: number
  pageNum: number
  pageSize: number
  pages: number
  list: T[]
}

interface PageParams {
  pageNum?: number
  pageSize?: number
}
```

## 组件规范

### 文件命名
- 组件：`PascalCase.tsx`（如 `UserList.tsx`）
- Hooks：`camelCase.ts`（如 `useAuth.ts`）
- 工具函数：`camelCase.ts`（如 `formatDate.ts`）
- 类型：types文件夹中的 `index.ts`
- 样式：`globals.css`、`Component.module.css`

### 组件结构
```tsx
// 1. 导入
import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { api } from '@/api/user'
import type { User } from '@/types'

// 2. 类型定义
interface Props {
  userId: number
  onUpdate?: (user: User) => void
}

// 3. 组件
export default function UserCard({ userId, onUpdate }: Props) {
  // 3a. 状态
  const [isEditing, setIsEditing] = useState(false)
  
  // 3b. 查询/变更
  const { data: user, isLoading } = useQuery({
    queryKey: ['user', userId],
    queryFn: () => api.getById(userId),
  })
  
  // 3c. 事件处理
  const handleEdit = () => setIsEditing(true)
  
  // 3d. 副作用
  useEffect(() => {
    // 副作用逻辑
  }, [])
  
  // 3e. 渲染
  if (isLoading) return <Loading />
  
  return (
    <div className="card">
      {/* 内容 */}
    </div>
  )
}
```

### 事件处理器命名
```tsx
// 事件处理器使用 handle* 前缀
const handleClick = () => {}
const handleSubmit = (e: FormEvent) => {}
const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {}

// Props回调使用 on* 前缀
interface Props {
  onClick?: () => void
  onChange?: (value: string) => void
  onSubmit?: (data: FormData) => void
}
```

## 状态管理规范

### Zustand Store 模式
```typescript
import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface UserState {
  // 状态
  user: User | null
  token: string | null
  isAuthenticated: boolean
  
  // 操作
  setUser: (user: User | null) => void
  setToken: (token: string | null) => void
  login: (response: LoginResponse) => void
  logout: () => void
}

export const useUserStore = create<UserState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      
      setUser: (user) => set({ user, isAuthenticated: !!user }),
      setToken: (token) => set({ token }),
      login: (response) => set({
        user: response.user,
        token: response.token,
        isAuthenticated: true,
      }),
      logout: () => set({ user: null, token: null, isAuthenticated: false }),
    }),
    { name: 'user-storage' }
  )
)
```

## API层规范

### Axios实例
```typescript
import axios from 'axios'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

// 请求拦截器
instance.interceptors.request.use((config) => {
  const token = useUserStore.getState().token
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    const { data } = response
    if (data.code === 200) return data.data
    return Promise.reject(new Error(data.message))
  },
  (error) => {
    if (error.response?.status === 401) {
      useUserStore.getState().logout()
    }
    return Promise.reject(error)
  }
)

export default instance
```

### API函数
```typescript
import request from './index'
import type { User, LoginRequest, LoginResponse } from '@/types'

export const authApi = {
  login: (data: LoginRequest): Promise<LoginResponse> =>
    request.post('/api/auth/login', data),
    
  logout: (): Promise<void> =>
    request.post('/api/auth/logout'),
    
  getCurrentUser: (): Promise<User> =>
    request.get('/api/auth/me'),
}

export const userApi = {
  getList: (params: PageParams): Promise<PageResult<User>> =>
    request.get('/api/users', { params }),
    
  getById: (id: number): Promise<User> =>
    request.get(`/api/users/${id}`),
    
  create: (data: Partial<User>): Promise<number> =>
    request.post('/api/users', data),
    
  update: (id: number, data: Partial<User>): Promise<void> =>
    request.put(`/api/users/${id}`, data),
    
  delete: (id: number): Promise<void> =>
    request.delete(`/api/users/${id}`),
}
```

## React Query 规范

### 查询使用
```tsx
// 基本查询
const { data, isLoading, error } = useQuery({
  queryKey: ['users', params],
  queryFn: () => userApi.getList(params),
})

// 条件查询
const { data } = useQuery({
  queryKey: ['user', userId],
  queryFn: () => userApi.getById(userId),
  enabled: !!userId,  // 仅在userId存在时执行
})

// 带数据转换的查询
const { data } = useQuery({
  queryKey: ['users'],
  queryFn: userApi.getList,
  select: (data) => data.list.filter(u => u.status === 'active'),
})
```

### 变更使用
```tsx
const mutation = useMutation({
  mutationFn: userApi.create,
  onSuccess: (newId) => {
    queryClient.invalidateQueries({ queryKey: ['users'] })
    toast.success('创建成功！')
  },
  onError: (error) => {
    toast.error(error.message)
  },
})

// 在组件中使用
const handleSubmit = (data: UserRequest) => {
  mutation.mutate(data)
}
```

## 导入组织

```tsx
// 1. React和库
import { useState, useEffect } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

// 2. 内部组件
import { Button } from '@/components/ui/Button'
import { Card } from '@/components/ui/Card'

// 3. API和Store
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/userStore'

// 4. 类型
import type { User, UserRequest } from '@/types'

// 5. 工具函数
import { formatDate } from '@/utils/helpers'

// 6. 样式
import '@/styles/globals.css'
```