import { Routes, Route, Navigate } from 'react-router-dom'
import { useUserStore } from '@/stores/userStore'
import MainLayout from '@/components/layout/MainLayout'
import Login from '@/pages/auth/Login'
import Register from '@/pages/auth/Register'
import Dashboard from '@/pages/dashboard/Dashboard'
import BookList from '@/pages/book/BookList'
import BookDetail from '@/pages/book/BookDetail'
import CategoryManage from '@/pages/book/CategoryManage'
import BorrowList from '@/pages/borrow/BorrowList'
import MyBorrow from '@/pages/borrow/MyBorrow'
import OverdueList from '@/pages/borrow/OverdueList'
import UserList from '@/pages/user/UserList'
import Profile from '@/pages/user/Profile'
import Statistics from '@/pages/stats/Statistics'

// Protected Route Component
function ProtectedRoute({ children, adminOnly = false }: { children: React.ReactNode, adminOnly?: boolean }) {
  const { user, isAuthenticated } = useUserStore()

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  if (adminOnly && !['admin', 'librarian'].includes(user?.roles?.[0]?.roleKey || '')) {
    return <Navigate to="/" replace />
  }

  return <>{children}</>
}

function App() {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Protected Routes */}
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Dashboard />} />
        
        {/* Books */}
        <Route path="books" element={<BookList />} />
        <Route path="books/:id" element={<BookDetail />} />
        <Route path="categories" element={
          <ProtectedRoute adminOnly>
            <CategoryManage />
          </ProtectedRoute>
        } />

        {/* Borrow */}
        <Route path="borrow" element={
          <ProtectedRoute adminOnly>
            <BorrowList />
          </ProtectedRoute>
        } />
        <Route path="borrow/my" element={<MyBorrow />} />
        <Route path="borrow/overdue" element={
          <ProtectedRoute adminOnly>
            <OverdueList />
          </ProtectedRoute>
        } />

        {/* Users */}
        <Route path="users" element={
          <ProtectedRoute adminOnly>
            <UserList />
          </ProtectedRoute>
        } />
        <Route path="profile" element={<Profile />} />

        {/* Statistics */}
        <Route path="stats" element={
          <ProtectedRoute adminOnly>
            <Statistics />
          </ProtectedRoute>
        } />
      </Route>

      {/* 404 */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
