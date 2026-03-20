import { useState } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'
import { borrowApi } from '@/api/borrow'
import { useUserStore } from '@/stores/userStore'
import type { PageParams } from '@/types'
import clsx from 'clsx'

export default function MyBorrow() {
  const { user } = useUserStore()
  const [params] = useState<PageParams>({ pageNum: 1, pageSize: 10 })
  const [statusFilter, setStatusFilter] = useState<number>()

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['myBorrows', params, statusFilter],
    queryFn: () => borrowApi.getMyRecords({ status: statusFilter, ...params }),
    enabled: !!user,
  })

  const renewMutation = useMutation({
    mutationFn: (recordId: number) => borrowApi.renew(recordId),
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
        <h1 className="text-2xl font-bold text-gray-900">My Borrowings</h1>
        <p className="text-gray-500 mt-1">View your borrow history and manage active loans</p>
      </div>

      {/* Filters */}
      <div className="card">
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
      </div>

      {/* Cards */}
      <div className="grid gap-4">
        {isLoading ? (
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
          </div>
        ) : data?.list?.length === 0 ? (
          <div className="card text-center py-8 text-gray-500">
            No borrow records found.
          </div>
        ) : (
          data?.list?.map((record) => (
            <div key={record.id} className="card flex items-center gap-6">
              <img
                src={record.bookCover || `https://via.placeholder.com/80x120?text=Book`}
                alt={record.bookTitle}
                className="w-16 h-24 object-cover rounded"
              />
              <div className="flex-1">
                <h3 className="font-semibold text-gray-900">{record.bookTitle}</h3>
                <div className="text-sm text-gray-500 mt-1 space-y-1">
                  <p>Borrowed: {record.borrowDate?.split('T')[0]}</p>
                  <p>Due: {record.dueDate?.split('T')[0]}</p>
                  {record.returnDate && (
                    <p>Returned: {record.returnDate?.split('T')[0]}</p>
                  )}
                  {record.overdueDays && record.overdueDays > 0 && (
                    <p className="text-red-600">Overdue: {record.overdueDays} days</p>
                  )}
                </div>
              </div>
              <div className="flex flex-col items-end gap-2">
                {getStatusBadge(record.status)}
                {record.status === 0 && (
                  <button
                    onClick={() => renewMutation.mutate(record.id)}
                    disabled={renewMutation.isPending || record.renewCount >= 2}
                    className={clsx(
                      'btn btn-sm',
                      record.renewCount >= 2 ? 'btn-secondary' : 'btn-primary'
                    )}
                  >
                    {record.renewCount >= 2 ? 'Max Renewals' : 'Renew'}
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  )
}
