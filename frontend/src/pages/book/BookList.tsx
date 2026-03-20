import { useState } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { bookApi, categoryApi } from '@/api/book'
import type { PageParams } from '@/types'
import { Search, Plus, Edit, Trash2, Eye } from 'lucide-react'
import clsx from 'clsx'

export default function BookList() {
  const [params, setParams] = useState<PageParams>({ pageNum: 1, pageSize: 10 })
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedCategory, setSelectedCategory] = useState<number>()
  const [statusFilter, setStatusFilter] = useState<number>()

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['books', params, selectedCategory, statusFilter],
    queryFn: () => bookApi.getList({
      title: searchTerm,
      categoryId: selectedCategory,
      status: statusFilter,
      ...params,
    }),
  })

  const { data: categories } = useQuery({
    queryKey: ['categories'],
    queryFn: categoryApi.getAll,
  })

  const deleteMutation = useMutation({
    mutationFn: (id: number) => bookApi.delete(id),
    onSuccess: () => refetch(),
  })

  const handleDelete = (id: number) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      deleteMutation.mutate(id)
    }
  }

  const handleSearch = () => {
    setParams({ ...params, pageNum: 1 })
    refetch()
  }

  const handlePageChange = (page: number) => {
    setParams({ ...params, pageNum: page })
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Books</h1>
          <p className="text-gray-500 mt-1">Manage your library collection</p>
        </div>
        <Link to="/books/new" className="btn btn-primary flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Add Book
        </Link>
      </div>

      {/* Search & Filters */}
      <div className="card">
        <div className="flex flex-wrap gap-4">
          <div className="flex-1 min-w-64">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search by title or author..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                className="input pl-10"
              />
            </div>
          </div>
          <select
            value={selectedCategory || ''}
            onChange={(e) => setSelectedCategory(Number(e.target.value) || undefined)}
            className="input w-48"
          >
            <option value="">All Categories</option>
            {categories?.map((cat) => (
              <option key={cat.id} value={cat.id}>{cat.name}</option>
            ))}
          </select>
          <select
            value={statusFilter ?? ''}
            onChange={(e) => setStatusFilter(Number(e.target.value) || undefined)}
            className="input w-32"
          >
            <option value="">All Status</option>
            <option value="1">Available</option>
            <option value="0">Disabled</option>
          </select>
          <button onClick={handleSearch} className="btn btn-primary">
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
          <>
            <div className="overflow-x-auto">
              <table className="table">
                <thead>
                  <tr>
                    <th>Cover</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Category</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {data?.list?.map((book) => (
                    <tr key={book.id}>
                      <td>
                        <img
                          src={book.cover || `https://via.placeholder.com/40x60?text=${book.title.charAt(0)}`}
                          alt={book.title}
                          className="w-10 h-14 object-cover rounded"
                        />
                      </td>
                      <td className="font-medium">{book.title}</td>
                      <td>{book.author || '-'}</td>
                      <td className="text-gray-500 text-xs">{book.isbn || '-'}</td>
                      <td>{book.categoryName || '-'}</td>
                      <td>
                        <span className={clsx(
                          'font-medium',
                          book.availableStock > 0 ? 'text-green-600' : 'text-red-600'
                        )}>
                          {book.availableStock}/{book.stock}
                        </span>
                      </td>
                      <td>
                        <span className={clsx(
                          'badge',
                          book.status === 1 ? 'badge-success' : 'badge-danger'
                        )}>
                          {book.status === 1 ? 'Available' : 'Disabled'}
                        </span>
                      </td>
                      <td>
                        <div className="flex items-center gap-2">
                          <Link
                            to={`/books/${book.id}`}
                            className="p-1.5 text-gray-400 hover:text-primary-600 hover:bg-primary-50 rounded"
                          >
                            <Eye className="w-4 h-4" />
                          </Link>
                          <Link
                            to={`/books/${book.id}/edit`}
                            className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded"
                          >
                            <Edit className="w-4 h-4" />
                          </Link>
                          <button
                            onClick={() => handleDelete(book.id)}
                            className="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            {data && data.pages > 1 && (
              <div className="flex items-center justify-between px-4 py-3 border-t border-gray-100">
                <p className="text-sm text-gray-500">
                  Showing {((data.pageNum - 1) * data.pageSize) + 1} to{' '}
                  {Math.min(data.pageNum * data.pageSize, data.total)} of {data.total} results
                </p>
                <div className="flex gap-1">
                  {Array.from({ length: data.pages }, (_, i) => i + 1).map((page) => (
                    <button
                      key={page}
                      onClick={() => handlePageChange(page)}
                      className={clsx(
                        'px-3 py-1 rounded text-sm',
                        page === data.pageNum
                          ? 'bg-primary-600 text-white'
                          : 'text-gray-600 hover:bg-gray-100'
                      )}
                    >
                      {page}
                    </button>
                  ))}
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
}
