import { useQuery } from '@tanstack/react-query'
import { statsApi } from '@/api/stats'
import { BarChart3, BookOpen, Users, TrendingUp } from 'lucide-react'

export default function Statistics() {
  const { data: overview, isLoading: overviewLoading } = useQuery({
    queryKey: ['stats', 'overview'],
    queryFn: statsApi.getOverview,
  })

  const { data: hotBooks } = useQuery({
    queryKey: ['stats', 'hotBooks'],
    queryFn: () => statsApi.getHotBooks({ limit: 10 }),
  })

  const { data: categoryStats } = useQuery({
    queryKey: ['stats', 'category'],
    queryFn: statsApi.getCategoryDistribution,
  })

  const { data: activeUsers } = useQuery({
    queryKey: ['stats', 'activeUsers'],
    queryFn: () => statsApi.getActiveUsers({ limit: 5 }),
  })

  if (overviewLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Statistics</h1>
        <p className="text-gray-500 mt-1">Library analytics and insights</p>
      </div>

      {/* Overview Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total Books</p>
              <p className="text-3xl font-bold text-gray-900 mt-1">
                {overview?.totalBooks?.toLocaleString() || 0}
              </p>
            </div>
            <div className="p-3 bg-blue-100 rounded-lg">
              <BookOpen className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total Users</p>
              <p className="text-3xl font-bold text-gray-900 mt-1">
                {overview?.totalUsers?.toLocaleString() || 0}
              </p>
            </div>
            <div className="p-3 bg-green-100 rounded-lg">
              <Users className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Total Borrowed</p>
              <p className="text-3xl font-bold text-gray-900 mt-1">
                {overview?.totalBorrowed?.toLocaleString() || 0}
              </p>
            </div>
            <div className="p-3 bg-yellow-100 rounded-lg">
              <TrendingUp className="w-6 h-6 text-yellow-600" />
            </div>
          </div>
        </div>

        <div className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-500">Overdue</p>
              <p className="text-3xl font-bold text-red-600 mt-1">
                {overview?.totalOverdue?.toLocaleString() || 0}
              </p>
            </div>
            <div className="p-3 bg-red-100 rounded-lg">
              <BarChart3 className="w-6 h-6 text-red-600" />
            </div>
          </div>
        </div>
      </div>

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Hot Books */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Popular Books</h3>
          <div className="space-y-3">
            {hotBooks?.map((book) => (
              <div key={book.bookId} className="flex items-center justify-between py-2 border-b border-gray-100 last:border-0">
                <div className="flex items-center gap-3">
                  <span className="text-sm font-bold text-gray-400 w-6">#{book.rank}</span>
                  <div>
                    <p className="text-sm font-medium text-gray-900">{book.title}</p>
                    <p className="text-xs text-gray-500">{book.author}</p>
                  </div>
                </div>
                <span className="text-sm text-primary-600 font-medium">
                  {book.borrowCount} borrows
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Category Distribution */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Books by Category</h3>
          <div className="space-y-3">
            {categoryStats?.map((cat, index) => {
              const total = categoryStats.reduce((sum, c) => sum + c.count, 0)
              const percentage = total > 0 ? Math.round((cat.count / total) * 100) : 0
              const colors = ['bg-blue-500', 'bg-green-500', 'bg-yellow-500', 'bg-purple-500', 'bg-pink-500', 'bg-red-500']
              const color = colors[index % colors.length]

              return (
                <div key={cat.category} className="space-y-1">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">{cat.category}</span>
                    <span className="text-gray-900 font-medium">{cat.count} ({percentage}%)</span>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2">
                    <div className={`${color} h-2 rounded-full`} style={{ width: `${percentage}%` }} />
                  </div>
                </div>
              )
            })}
          </div>
        </div>
      </div>

      {/* Active Users */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Most Active Users</h3>
        <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
          {activeUsers?.map((user, index) => (
            <div key={user.id} className="text-center p-4 bg-gray-50 rounded-lg">
              <div className="text-2xl font-bold text-gray-400 mb-1">#{index + 1}</div>
              <div className="w-12 h-12 mx-auto bg-primary-100 rounded-full flex items-center justify-center mb-2">
                <Users className="w-6 h-6 text-primary-600" />
              </div>
              <p className="font-medium text-gray-900">{user.nickname || user.username}</p>
              <p className="text-sm text-primary-600">{user.borrow_count} borrows</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
