import request from './index'
import type { LoginRequest, RegisterRequest, LoginResponse, User, Role, PageResult, PageParams } from '@/types'

// Auth APIs
export const authApi = {
  login: (data: LoginRequest): Promise<LoginResponse> => 
    request.post('/api/user/auth/login', data),
  
  register: (data: RegisterRequest): Promise<LoginResponse> => 
    request.post('/api/user/auth/register', data),
  
  logout: (): Promise<void> => 
    request.post('/api/user/auth/logout'),
  
  getCurrentUser: (): Promise<User> => 
    request.get('/api/user/auth/info'),
  
  changePassword: (oldPassword: string, newPassword: string): Promise<void> => 
    request.put('/api/user/auth/password', null, { params: { oldPassword, newPassword } }),
}

// User APIs
export const userApi = {
  getList: (params: { username?: string; phone?: string; status?: number } & PageParams): Promise<PageResult<User>> => 
    request.get('/api/user/list', { params }),
  
  getById: (id: number): Promise<User> => 
    request.get(`/api/user/${id}`),
  
  create: (data: Partial<User> & { password: string }, roleIds?: number[]): Promise<number> => 
    request.post('/api/user', data, { params: { roleIds: roleIds?.join(',') } }),
  
  update: (data: Partial<User>, roleIds?: number[]): Promise<void> => 
    request.put('/api/user', data, { params: { roleIds: roleIds?.join(',') } }),
  
  delete: (id: number): Promise<void> => 
    request.delete(`/api/user/${id}`),
  
  updateStatus: (id: number, status: number): Promise<void> => 
    request.put('/api/user/status', null, { params: { id, status } }),
}

// Role APIs
export const roleApi = {
  getAll: (): Promise<Role[]> => 
    request.get('/api/role/list'),
  
  create: (data: Partial<Role>): Promise<number> => 
    request.post('/api/role', data),
  
  update: (data: Partial<Role>): Promise<void> => 
    request.put('/api/role', data),
  
  delete: (id: number): Promise<void> => 
    request.delete(`/api/role/${id}`),
}
