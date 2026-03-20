import { Outlet, Link, useLocation, useNavigate } from 'react-router-dom'
import { 
  LayoutDashboard, 
  BookOpen, 
  BookMarked, 
  Users, 
  BarChart3, 
  Menu, 
  X, 
  LogOut, 
  User,
  FolderTree
} from 'lucide-react'
import { useUserStore } from '@/stores/userStore'
import { useUIStore } from '@/stores/uiStore'
import clsx from 'clsx'

const navigation = [
  { name: 'Dashboard', href: '/', icon: LayoutDashboard },
  { name: 'Books', href: '/books', icon: BookOpen },
  { name: 'Categories', href: '/categories', icon: FolderTree, adminOnly: true },
  { name: 'Borrow Records', href: '/borrow', icon: BookMarked, adminOnly: true },
  { name: 'My Borrow', href: '/borrow/my', icon: BookMarked },
  { name: 'Overdue', href: '/borrow/overdue', icon: BookMarked, adminOnly: true },
  { name: 'Users', href: '/users', icon: Users, adminOnly: true },
  { name: 'Statistics', href: '/stats', icon: BarChart3, adminOnly: true },
]

export default function MainLayout() {
  const location = useLocation()
  const navigate = useNavigate()
  const { user, logout, isAuthenticated } = useUserStore()
  const { sidebarCollapsed, toggleSidebar } = useUIStore()

  const isAdmin = ['admin', 'librarian'].includes(user?.roles?.[0]?.roleKey || '')

  const filteredNavigation = navigation.filter(item => 
    !item.adminOnly || isAdmin
  )

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  if (!isAuthenticated) {
    return null
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Sidebar */}
      <aside className={clsx(
        'fixed left-0 top-0 z-40 h-screen transition-transform bg-white border-r border-gray-200',
        sidebarCollapsed ? '-translate-x-full' : 'translate-x-0',
        'w-64'
      )}>
        <div className="h-full px-3 py-4 overflow-y-auto">
          {/* Logo */}
          <Link to="/" className="flex items-center px-3 mb-5">
            <BookOpen className="w-8 h-8 text-primary-600" />
            <span className="ml-3 text-xl font-semibold text-gray-900">Book System</span>
          </Link>

          {/* Navigation */}
          <nav className="space-y-1">
            {filteredNavigation.map((item) => {
              const isActive = location.pathname === item.href
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={clsx(
                    'flex items-center px-3 py-2 rounded-lg text-sm font-medium transition-colors',
                    isActive
                      ? 'bg-primary-50 text-primary-700'
                      : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                  )}
                >
                  <item.icon className={clsx('w-5 h-5 mr-3', isActive ? 'text-primary-600' : 'text-gray-400')} />
                  {item.name}
                </Link>
              )
            })}
          </nav>
        </div>
      </aside>

      {/* Main Content */}
      <div className={clsx('transition-all', sidebarCollapsed ? 'ml-0' : 'ml-64')}>
        {/* Header */}
        <header className="sticky top-0 z-30 bg-white border-b border-gray-200">
          <div className="flex items-center justify-between px-4 py-3">
            <button
              onClick={toggleSidebar}
              className="p-2 text-gray-500 hover:text-gray-700 rounded-lg hover:bg-gray-100"
            >
              {sidebarCollapsed ? <Menu className="w-5 h-5" /> : <X className="w-5 h-5" />}
            </button>

            <div className="flex items-center gap-4">
              <Link to="/profile" className="flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900">
                <div className="w-8 h-8 rounded-full bg-primary-100 flex items-center justify-center">
                  <User className="w-4 h-4 text-primary-600" />
                </div>
                <span>{user?.nickname || user?.username}</span>
              </Link>
              <button
                onClick={handleLogout}
                className="p-2 text-gray-500 hover:text-red-600 rounded-lg hover:bg-red-50"
                title="Logout"
              >
                <LogOut className="w-5 h-5" />
              </button>
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="p-6">
          <Outlet />
        </main>
      </div>

      {/* Overlay for mobile */}
      {!sidebarCollapsed && (
        <div
          className="fixed inset-0 z-30 bg-black bg-opacity-50 lg:hidden"
          onClick={toggleSidebar}
        />
      )}
    </div>
  )
}
