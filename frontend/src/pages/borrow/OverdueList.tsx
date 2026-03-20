import { useQuery } from '@tanstack/react-query'
import { borrowApi } from '@/api/borrow'
import { AlertTriangle, Phone } from 'lucide-react'

export default function OverdueList() {
  const { data, isLoading } = useQuery({
    queryKey: ['overdue'],
    queryFn: () => borrowApi.getOverdue({ pageNum: 1, pageSize: 100 }),
  })

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Overdue Books</h1>
        <p className="text-gray-500 mt-1">Track and manage overdue borrowings</p>
      </div>

      {/* Stats */}
      <div className="card bg-red-50 border-red-200">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-red-100 rounded-lg">
            <AlertTriangle className="w-6 h-6 text-red-600" />
          </div>
          <div>
            <p className="text-sm text-red-600">Total Overdue</p>
            <p className="text-2xl font-bold text-red-700">{data?.total || 0}</p>
          </div>
        </div>
      </div>

      {/* List */}
      <div className="card">
        {isLoading ? (
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
          </div>
        ) : data?.list?.length === 0 ? (
          <div className="text-center py-8 text-gray-500">
            No overdue books. Great job!
          </div>
        ) : (
          <div className="divide-y divide-gray-100">
            {data?.list?.map((record) => (
              <div key={record.id} className="py-4 flex items-center justify-between">
                <div>
                  <p className="font-medium text-gray-900">{record.bookTitle}</p>
                  <p className="text-sm text-gray-500">Borrowed by: {record.username}</p>
                  <p className="text-sm text-red-600">
                    {record.overdueDays} days overdue (Due: {record.dueDate?.split('T')[0]})
                  </p>
                </div>
                <div className="flex items-center gap-2">
                  <button className="btn btn-sm btn-secondary flex items-center gap-1">
                    <Phone className="w-4 h-4" />
                    Contact
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
