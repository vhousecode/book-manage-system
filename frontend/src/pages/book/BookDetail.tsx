import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation } from '@tanstack/react-query'
import { bookApi } from '@/api/book'
import type { BookRequest } from '@/types'
import { ArrowLeft, Save } from 'lucide-react'
import { useState, useEffect } from 'react'

export default function BookDetail() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isNew = id === 'new'
  
  const [formData, setFormData] = useState<BookRequest>({
    title: '',
    author: '',
    isbn: '',
    publisher: '',
    publishDate: '',
    categoryId: undefined,
    price: undefined,
    stock: 1,
    availableStock: 1,
    location: '',
    cover: '',
    description: '',
  })

  const { data: book } = useQuery({
    queryKey: ['book', id],
    queryFn: () => bookApi.getById(Number(id)),
    enabled: !isNew,
  })

  useEffect(() => {
    if (book) {
      setFormData({
        title: book.title,
        author: book.author || '',
        isbn: book.isbn || '',
        publisher: book.publisher || '',
        publishDate: book.publishDate || '',
        categoryId: book.categoryId,
        price: book.price,
        stock: book.stock,
        availableStock: book.availableStock,
        location: book.location || '',
        cover: book.cover || '',
        description: book.description || '',
      })
    }
  }, [book])

  const createMutation = useMutation({
    mutationFn: bookApi.create,
    onSuccess: () => navigate('/books'),
  })

  const updateMutation = useMutation({
    mutationFn: (data: BookRequest) => bookApi.update({ ...data, id: Number(id) }),
    onSuccess: () => navigate('/books'),
  })

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (isNew) {
      createMutation.mutate(formData)
    } else {
      updateMutation.mutate(formData)
    }
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <button
          onClick={() => navigate('/books')}
          className="p-2 hover:bg-gray-100 rounded-lg"
        >
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            {isNew ? 'Add New Book' : 'Edit Book'}
          </h1>
          <p className="text-gray-500 mt-1">
            {isNew ? 'Add a new book to the library' : 'Update book information'}
          </p>
        </div>
      </div>

      {/* Form */}
      <form onSubmit={handleSubmit} className="card space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Title *
            </label>
            <input
              type="text"
              required
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Author
            </label>
            <input
              type="text"
              value={formData.author}
              onChange={(e) => setFormData({ ...formData, author: e.target.value })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              ISBN
            </label>
            <input
              type="text"
              value={formData.isbn}
              onChange={(e) => setFormData({ ...formData, isbn: e.target.value })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Publisher
            </label>
            <input
              type="text"
              value={formData.publisher}
              onChange={(e) => setFormData({ ...formData, publisher: e.target.value })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Publish Date
            </label>
            <input
              type="date"
              value={formData.publishDate}
              onChange={(e) => setFormData({ ...formData, publishDate: e.target.value })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Price
            </label>
            <input
              type="number"
              step="0.01"
              value={formData.price || ''}
              onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) || undefined })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Total Stock *
            </label>
            <input
              type="number"
              required
              min="0"
              value={formData.stock}
              onChange={(e) => setFormData({ 
                ...formData, 
                stock: Number(e.target.value),
                availableStock: Math.min(formData.availableStock || 0, Number(e.target.value))
              })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Available Stock *
            </label>
            <input
              type="number"
              required
              min="0"
              max={formData.stock}
              value={formData.availableStock}
              onChange={(e) => setFormData({ ...formData, availableStock: Number(e.target.value) })}
              className="input"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Location
            </label>
            <input
              type="text"
              value={formData.location}
              onChange={(e) => setFormData({ ...formData, location: e.target.value })}
              className="input"
              placeholder="e.g., Shelf A-101"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Cover URL
            </label>
            <input
              type="url"
              value={formData.cover}
              onChange={(e) => setFormData({ ...formData, cover: e.target.value })}
              className="input"
              placeholder="https://..."
            />
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <textarea
            rows={4}
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            className="input"
          />
        </div>

        <div className="flex justify-end gap-4 pt-4 border-t border-gray-100">
          <button
            type="button"
            onClick={() => navigate('/books')}
            className="btn btn-secondary"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={createMutation.isPending || updateMutation.isPending}
            className="btn btn-primary flex items-center gap-2"
          >
            <Save className="w-4 h-4" />
            {createMutation.isPending || updateMutation.isPending ? 'Saving...' : 'Save'}
          </button>
        </div>
      </form>
    </div>
  )
}
