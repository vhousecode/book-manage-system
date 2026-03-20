import request from './index'
import type { StatsOverview, BorrowTrend, HotBook, CategoryStats } from '@/types'

// Statistics APIs
export const statsApi = {
  getOverview: (): Promise<StatsOverview> => 
    request.get('/api/stats/overview'),
  
  getBorrowTrend: (params?: { type?: string; startDate?: string; endDate?: string }): Promise<BorrowTrend> => 
    request.get('/api/stats/borrow/trend', { params }),
  
  getHotBooks: (params?: { limit?: number; period?: string }): Promise<HotBook[]> => 
    request.get('/api/stats/book/hot', { params }),
  
  getCategoryDistribution: (): Promise<CategoryStats[]> => 
    request.get('/api/stats/category/distribution'),
  
  getActiveUsers: (params?: { limit?: number }): Promise<{ id: number; username: string; nickname: string; borrow_count: number }[]> => 
    request.get('/api/stats/user/active', { params }),
}
