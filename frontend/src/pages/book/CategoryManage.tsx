import { useQuery } from '@tanstack/react-query'
import { categoryApi } from '@/api/book'
import { Plus, Edit, Trash2 } from 'lucide-react'
import { useState } from 'react'
import clsx from 'clsx'

export default function CategoryManage() {
  const [showModal, setShowModal] = useState(false)
  const [editingCategory, setEditingCategory] = useState<number>()

  const { data: tree, isLoading, refetch } = useQuery({
    queryKey: ['categories', 'tree'],
    queryFn: categoryApi.getTree,
  })

  const renderTree = (categories: any[], level = 0) => {
    return categories.map((cat) => (
      <div key={cat.id}>
        <div 
          className={clsx(
            'flex items-center justify-between py-3 px-4 hover:bg-gray-50',
            level > 0 && 'ml-8'
          )}
        >
          <div className="flex items-center gap-3">
            <span className={clsx(
              'w-2 h-2 rounded-full',
              cat.status === 1 ? 'bg-green-500' : 'bg-gray-300'
            )} />
            <span className="font-medium text-gray-900">{cat.name}</span>
            {cat.status === 0 && (
              <span className="text-xs text-gray-400">(Disabled)</span>
            )}
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={() => {
                setEditingCategory(cat.id)
                setShowModal(true)
              }}
              className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded"
            >
              <Edit className="w-4 h-4" />
            </button>
            <button
              onClick={() => handleDelete(cat.id)}
              className="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        </div>
        {cat.children && cat.children.length > 0 && renderTree(cat.children, level + 1)}
      </div>
    ))
  }

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this category?')) {
      try {
        await categoryApi.delete(id)
        refetch()
      } catch (error) {
        console.error('Failed to delete category:', error)
      }
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Categories</h1>
          <p className="text-gray-500 mt-1">Manage book categories</p>
        </div>
        <button
          onClick={() => {
            setEditingCategory(undefined)
            setShowModal(true)
          }}
          className="btn btn-primary flex items-center gap-2"
        >
          <Plus className="w-4 h-4" />
          Add Category
        </button>
      </div>

      {/* Category Tree */}
      <div className="card">
        {isLoading ? (
          <div className="flex items-center justify-center h-32">
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-primary-600"></div>
          </div>
        ) : tree && tree.length > 0 ? (
          <div className="divide-y divide-gray-100">
            {renderTree(tree)}
          </div>
        ) : (
          <div className="text-center py-8 text-gray-500">
            No categories found. Click "Add Category" to create one.
          </div>
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <CategoryModal
          categoryId={editingCategory}
          onClose={() => {
            setShowModal(false)
            setEditingCategory(undefined)
          }}
          onSuccess={() => {
            setShowModal(false)
            setEditingCategory(undefined)
            refetch()
          }}
          categories={tree || []}
        />
      )}
    </div>
  )
}

// Simple Modal Component
function CategoryModal({ 
  categoryId, 
  onClose, 
  onSuccess, 
  categories 
}: { 
  categoryId?: number
  onClose: () => void
  onSuccess: () => void
  categories: any[]
}) {
  const [name, setName] = useState('')
  const [parentId, setParentId] = useState<number>(0)
  const [sort, setSort] = useState(0)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      if (categoryId) {
        await categoryApi.update({ id: categoryId, name, parentId, sort })
      } else {
        await categoryApi.create({ name, parentId, sort, status: 1 })
      }
      onSuccess()
    } catch (error) {
      console.error('Failed to save category:', error)
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <h2 className="text-lg font-semibold mb-4">
          {categoryId ? 'Edit Category' : 'Add Category'}
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Name *
            </label>
            <input
              type="text"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="input"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Parent Category
            </label>
            <select
              value={parentId}
              onChange={(e) => setParentId(Number(e.target.value))}
              className="input"
            >
              <option value={0}>None (Root)</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Sort Order
            </label>
            <input
              type="number"
              value={sort}
              onChange={(e) => setSort(Number(e.target.value))}
              className="input"
            />
          </div>
          <div className="flex justify-end gap-2 pt-4">
            <button type="button" onClick={onClose} className="btn btn-secondary">
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              Save
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
