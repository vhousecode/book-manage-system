// Common types
export interface Result<T> {
  code: number
  message: string
  data: T
  timestamp: number
}

export interface PageResult<T> {
  total: number
  pageNum: number
  pageSize: number
  pages: number
  list: T[]
}

export interface PageParams {
  pageNum?: number
  pageSize?: number
}

// User types
export interface User {
  id: number
  username: string
  nickname: string
  phone?: string
  email?: string
  avatar?: string
  gender?: number
  status: number
  createTime?: string
  roles?: Role[]
  roleIds?: number[]
}

export interface Role {
  id: number
  roleName: string
  roleKey: string
  description?: string
  status: number
  sort: number
  createTime?: string
}

export interface LoginRequest {
  username: string
  password: string
}

export interface RegisterRequest {
  username: string
  password: string
  nickname?: string
  phone?: string
  email?: string
}

export interface LoginResponse {
  id: number
  username: string
  nickname: string
  avatar?: string
  roles: string[]
  token: string
}

// Book types
export interface Book {
  id: number
  title: string
  author?: string
  isbn?: string
  publisher?: string
  publishDate?: string
  categoryId?: number
  categoryName?: string
  price?: number
  stock: number
  availableStock: number
  location?: string
  cover?: string
  description?: string
  status: number
  createTime?: string
}

export interface BookRequest {
  id?: number
  title: string
  author?: string
  isbn?: string
  publisher?: string
  publishDate?: string
  categoryId?: number
  price?: number
  stock?: number
  availableStock?: number
  location?: string
  cover?: string
  description?: string
}

export interface BookCategory {
  id: number
  name: string
  parentId: number
  sort: number
  icon?: string
  status: number
  children?: BookCategory[]
}

// Borrow types
export interface BorrowRecord {
  id: number
  userId: number
  username?: string
  bookId: number
  bookTitle?: string
  bookCover?: string
  borrowDate: string
  dueDate: string
  returnDate?: string
  renewCount: number
  status: number
  overdueDays?: number
  fine?: number
  remark?: string
}

export interface BorrowRequest {
  bookId: number
  userId?: number
  days?: number
}

// Statistics types
export interface StatsOverview {
  totalBooks: number
  totalUsers: number
  totalBorrowed: number
  totalOverdue: number
  todayBorrow: number
  todayReturn: number
  availableBooks: number
}

export interface BorrowTrend {
  dates: string[]
  borrowCount: number[]
  returnCount: number[]
}

export interface HotBook {
  rank: number
  bookId: number
  title: string
  author?: string
  cover?: string
  borrowCount: number
}

export interface CategoryStats {
  category: string
  count: number
}
