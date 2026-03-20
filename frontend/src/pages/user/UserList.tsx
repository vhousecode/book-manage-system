import { useState } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'
import { userApi } from '@/api/user'
import type { PageParams } from '@/types'
import { UserCheck, UserX } from 'lucide-react'
import clsx from 'clsx'

export default function UserList() {
  const [params] = useState<PageParams>({ pageNum: 1, pageSize: 10 })
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState<number>()

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['users', params, statusFilter],
    queryFn: () => userApi.getList({
      username: searchTerm,
      status: statusFilter,
      ...params,
    }),
  })

  const statusMutation = useMutation({
    mutationFn: ({ id, status }: { id: number; status: number }) => 
      userApi.updateStatus(id, status),
    onSuccess: () => refetch(),
  })

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Users</h1>
          <p className="text-gray-500 mt-1">Manage system users</p>
        </div>
      </div>

      {/* Filters */}
      <div className="card">
        <div className="flex flex-wrap gap-4">
          <input
            type="text"
            placeholder="Search username..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="input flex-1 min-w-64"
          />
          <select
            value={statusFilter ?? ''}
            onChange={(e) => setStatusFilter(Number(e.target.value) || undefined)}
            className="input w-32"
          >
            <option value="">All Status</option>
            <option value="1">Active</option>
            <option value="0">Disabled</option>
          </select>
          <button onClick={() => refetch()} className="btn btn-primary">
            Search
          </button>
        </div>
      </div>

      {/* Table */}
      <div className="card overflow-hidden">
        {isLoading ? (
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Username</th>
                  <th>Nickname</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {data?.list?.map((user) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td className="font-medium">{user.username}</td>
                    <td>{user.nickname || '-'}</td>
                    <td>{user.email || '-'}</td>
                    <td>{user.phone || '-'}</td>
                    <td>
                      {user.roles?.map((role) => (
                        <span key={role.id} className="badge badge-info mr-1">
                          {role.roleName}
                        </span>
                      ))}
                    </td>
                    <td>
                      <span className={clsx(
                        'badge',
                        user.status === 1 ? 'badge-success' : 'badge-danger'
                      )}>
                        {user.status === 1 ? 'Active' : 'Disabled'}
                      </span>
                    </td>
                    <td>
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => statusMutation.mutate({ 
                            id: user.id, 
                            status: user.status === 1 ? 0 : 1 
                          })}
                          className={clsx(
                            'p-1.5 rounded',
                            user.status === 1
                              ? 'text-gray-400 hover:text-red-600 hover:bg-red-50'
                              : 'text-gray-400 hover:text-green-600 hover:bg-green-50'
                          )}
                        >
                          {user.status === 1 ? (
                            <UserX className="w-4 h-4" />
                          ) : (
                            <UserCheck className="w-4 h-4" />
                          )}
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
