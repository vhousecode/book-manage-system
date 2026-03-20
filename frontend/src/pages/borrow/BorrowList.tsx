import { useState } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'
import { borrowApi } from '@/api/borrow'
import type { PageParams } from '@/types'

export default function BorrowList() {
  const [params] = useState<PageParams>({ pageNum: 1, pageSize: 10 })
  const [statusFilter, setStatusFilter] = useState<number>()
  const [searchUserId, setSearchUserId] = useState<string>()

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['borrows', params, statusFilter, searchUserId],
    queryFn: () => borrowApi.getList({
      userId: searchUserId ? Number(searchUserId) : undefined,
      status: statusFilter,
      ...params,
    }),
  })

  const returnMutation = useMutation({
    mutationFn: borrowApi.returnBook,
    onSuccess: () => refetch(),
  })

  const getStatusBadge = (status: number) => {
    switch (status) {
      case 0:
        return <span className="badge badge-info">Borrowed</span>
      case 1:
        return <span className="badge badge-success">Returned</span>
      case 2:
        return <span className="badge badge-warning">Overdue</span>
      default:
        return <span className="badge">Unknown</span>
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Borrow Records</h1>
        <p className="text-gray-500 mt-1">Manage all borrowing activities</p>
      </div>

      {/* Filters */}
      <div className="card">
        <div className="flex flex-wrap gap-4">
          <input
            type="number"
            placeholder="User ID"
            value={searchUserId || ''}
            onChange={(e) => setSearchUserId(e.target.value || undefined)}
            className="input w-32"
          />
          <select
            value={statusFilter ?? ''}
            onChange={(e) => setStatusFilter(Number(e.target.value) || undefined)}
            className="input w-40"
          >
            <option value="">All Status</option>
            <option value="0">Borrowed</option>
            <option value="1">Returned</option>
            <option value="2">Overdue</option>
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
                  <th>User</th>
                  <th>Book</th>
                  <th>Borrow Date</th>
                  <th>Due Date</th>
                  <th>Return Date</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {data?.list?.map((record) => (
                  <tr key={record.id}>
                    <td>{record.id}</td>
                    <td>{record.username || record.userId}</td>
                    <td>{record.bookTitle || record.bookId}</td>
                    <td>{record.borrowDate?.split('T')[0]}</td>
                    <td>{record.dueDate?.split('T')[0]}</td>
                    <td>{record.returnDate?.split('T')[0] || '-'}</td>
                    <td>{getStatusBadge(record.status)}</td>
                    <td>
                      {record.status === 0 && (
                        <button
                          onClick={() => returnMutation.mutate(record.id)}
                          className="btn btn-sm btn-primary"
                        >
                          Return
                        </button>
                      )}
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
