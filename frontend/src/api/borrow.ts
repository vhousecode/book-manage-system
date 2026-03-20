import request from './index'
import type { BorrowRecord, BorrowRequest, PageResult, PageParams } from '@/types'

// Borrow APIs
export const borrowApi = {
  borrow: (data: BorrowRequest): Promise<BorrowRecord> => 
    request.post('/api/borrow', data),
  
  returnBook: (recordId: number): Promise<BorrowRecord> => 
    request.post('/api/borrow/return', null, { params: { recordId } }),
  
  renew: (recordId: number, days?: number): Promise<BorrowRecord> => 
    request.post('/api/borrow/renew', null, { params: { recordId, days } }),
  
  getList: (params: {
    userId?: number
    bookId?: number
    status?: number
    startDate?: string
    endDate?: string
  } & PageParams): Promise<PageResult<BorrowRecord>> => 
    request.get('/api/borrow/list', { params }),
  
  getUserRecords: (userId: number, params?: { status?: number } & PageParams): Promise<PageResult<BorrowRecord>> => 
    request.get(`/api/borrow/user/${userId}`, { params }),
  
  getOverdue: (params?: PageParams): Promise<PageResult<BorrowRecord>> => 
    request.get('/api/borrow/overdue', { params }),
  
  getById: (id: number): Promise<BorrowRecord> => 
    request.get(`/api/borrow/${id}`),
  
  getMyRecords: (params?: { status?: number } & PageParams): Promise<PageResult<BorrowRecord>> => 
    request.get('/api/borrow/my', { params }),
}
