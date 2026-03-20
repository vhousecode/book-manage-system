import request from './index'
import type { Book, BookRequest, BookCategory, PageResult, PageParams } from '@/types'

// Book APIs
export const bookApi = {
  getList: (params: { 
    title?: string
    author?: string
    isbn?: string
    categoryId?: number
    status?: number
  } & PageParams): Promise<PageResult<Book>> => 
    request.get('/api/book/list', { params }),
  
  getById: (id: number): Promise<Book> => 
    request.get(`/api/book/${id}`),
  
  create: (data: BookRequest): Promise<number> => 
    request.post('/api/book', data),
  
  update: (data: BookRequest): Promise<void> => 
    request.put('/api/book', data),
  
  delete: (id: number): Promise<void> => 
    request.delete(`/api/book/${id}`),
  
  updateStatus: (id: number, status: number): Promise<void> => 
    request.put('/api/book/status', null, { params: { id, status } }),
  
  updateStock: (id: number, stock: number, availableStock?: number): Promise<void> => 
    request.put('/api/book/stock', null, { params: { id, stock, availableStock } }),
  
  search: (keyword: string, params?: PageParams): Promise<PageResult<Book>> => 
    request.get('/api/book/search', { params: { keyword, ...params } }),
}

// Category APIs
export const categoryApi = {
  getTree: (): Promise<BookCategory[]> => 
    request.get('/api/category/tree'),
  
  getAll: (): Promise<BookCategory[]> => 
    request.get('/api/category/list'),
  
  create: (data: Partial<BookCategory>): Promise<number> => 
    request.post('/api/category', data),
  
  update: (data: Partial<BookCategory>): Promise<void> => 
    request.put('/api/category', data),
  
  delete: (id: number): Promise<void> => 
    request.delete(`/api/category/${id}`),
}
