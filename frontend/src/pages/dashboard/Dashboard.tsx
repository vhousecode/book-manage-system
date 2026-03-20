import { useQuery } from '@tanstack/react-query'
import { statsApi } from '@/api/stats'
import { useUserStore } from '@/stores/userStore'
import { 
  BookOpen, 
  Users, 
  Clock, 
  TrendingUp,
  AlertTriangle,
  ArrowUpRight,
  ArrowDownRight
} from 'lucide-react'
import clsx from 'clsx'

export default function Dashboard() {
  const { user } = useUserStore()
  const isAdmin = ['admin', 'librarian'].includes(user?.roles?.[0]?.roleKey || '')

  const { data: overview, isLoading } = useQuery({
    queryKey: ['stats', 'overview'],
    queryFn: statsApi.getOverview,
    enabled: isAdmin,
  })

  const { data: hotBooks } = useQuery({
    queryKey: ['stats', 'hotBooks'],
    queryFn: () => statsApi.getHotBooks({ limit: 5 }),
    enabled: isAdmin,
  })

  if (!isAdmin) {
    return <UserDashboard />
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  const stats = [
    { name: 'Total Books', value: overview?.totalBooks || 0, icon: BookOpen, color: 'bg-blue-500' },
    { name: 'Total Users', value: overview?.totalUsers || 0, icon: Users, color: 'bg-green-500' },
    { name: 'Borrowed', value: overview?.totalBorrowed || 0, icon: Clock, color: 'bg-yellow-500' },
    { name: 'Overdue', value: overview?.totalOverdue || 0, icon: AlertTriangle, color: 'bg-red-500' },
  ]

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-500 mt-1">Welcome back, {user?.nickname || user?.username}!</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat) => (
          <div key={stat.name} className="card">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-500">{stat.name}</p>
                <p className="text-2xl font-bold text-gray-900 mt-1">{stat.value.toLocaleString()}</p>
              </div>
              <div className={clsx('p-3 rounded-lg', stat.color)}>
                <stat.icon className="w-6 h-6 text-white" />
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Today Stats */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Today's Activity</h3>
          <div className="grid grid-cols-3 gap-4">
            <div className="text-center">
              <div className="flex items-center justify-center w-12 h-12 mx-auto bg-green-100 rounded-full mb-2">
                <ArrowUpRight className="w-6 h-6 text-green-600" />
              </div>
              <p className="text-sm text-gray-500">Borrowed</p>
              <p className="text-xl font-bold text-gray-900">{overview?.todayBorrow || 0}</p>
            </div>
            <div className="text-center">
              <div className="flex items-center justify-center w-12 h-12 mx-auto bg-blue-100 rounded-full mb-2">
                <ArrowDownRight className="w-6 h-6 text-blue-600" />
              </div>
              <p className="text-sm text-gray-500">Returned</p>
              <p className="text-xl font-bold text-gray-900">{overview?.todayReturn || 0}</p>
            </div>
            <div className="text-center">
              <div className="flex items-center justify-center w-12 h-12 mx-auto bg-purple-100 rounded-full mb-2">
                <BookOpen className="w-6 h-6 text-purple-600" />
              </div>
              <p className="text-sm text-gray-500">Available</p>
              <p className="text-xl font-bold text-gray-900">{overview?.availableBooks || 0}</p>
            </div>
          </div>
        </div>

        {/* Hot Books */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Popular Books</h3>
          <div className="space-y-3">
            {hotBooks?.map((book) => (
              <div key={book.bookId} className="flex items-center justify-between py-2 border-b border-gray-100 last:border-0">
                <div className="flex items-center gap-3">
                  <span className="text-sm font-medium text-gray-400">#{book.rank}</span>
                  <div>
                    <p className="text-sm font-medium text-gray-900">{book.title}</p>
                    <p className="text-xs text-gray-500">{book.author}</p>
                  </div>
                </div>
                <span className="text-sm text-primary-600 font-medium">{book.borrowCount} borrows</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

// User Dashboard for non-admin users
function UserDashboard() {
  const { user } = useUserStore()
  
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Welcome, {user?.nickname || user?.username}!</h1>
        <p className="text-gray-500 mt-1">Manage your library activities</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="card text-center">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-primary-100 rounded-full mb-4">
            <BookOpen className="w-8 h-8 text-primary-600" />
          </div>
          <h3 className="text-lg font-semibold text-gray-900">Browse Books</h3>
          <p className="text-sm text-gray-500 mt-2">Search and explore our collection</p>
        </div>
        <div className="card text-center">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-green-100 rounded-full mb-4">
            <Clock className="w-8 h-8 text-green-600" />
          </div>
          <h3 className="text-lg font-semibold text-gray-900">My Borrowings</h3>
          <p className="text-sm text-gray-500 mt-2">View your borrow history</p>
        </div>
        <div className="card text-center">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-blue-100 rounded-full mb-4">
            <TrendingUp className="w-8 h-8 text-blue-600" />
          </div>
          <h3 className="text-lg font-semibold text-gray-900">Statistics</h3>
          <p className="text-sm text-gray-500 mt-2">Track your reading progress</p>
        </div>
      </div>
    </div>
  )
}
