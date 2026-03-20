import { useState } from 'react'
import { useMutation } from '@tanstack/react-query'
import { useUserStore } from '@/stores/userStore'
import { authApi } from '@/api/user'
import { User, Save } from 'lucide-react'

export default function Profile() {
  const { user } = useUserStore()
  const [isEditing, setIsEditing] = useState(false)
  const [formData, setFormData] = useState({
    nickname: user?.nickname || '',
    email: user?.email || '',
    phone: user?.phone || '',
  })

  const [passwordData, setPasswordData] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  })

  const updateMutation = useMutation({
    mutationFn: () => Promise.resolve(), // Placeholder
    onSuccess: () => {
      setIsEditing(false)
    },
  })

  const passwordMutation = useMutation({
    mutationFn: () => authApi.changePassword(passwordData.oldPassword, passwordData.newPassword),
    onSuccess: () => {
      setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' })
      alert('Password changed successfully!')
    },
  })

  const handlePasswordChange = () => {
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      alert('Passwords do not match!')
      return
    }
    if (passwordData.newPassword.length < 6) {
      alert('Password must be at least 6 characters!')
      return
    }
    passwordMutation.mutate()
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Profile</h1>
        <p className="text-gray-500 mt-1">Manage your account settings</p>
      </div>

      {/* Profile Card */}
      <div className="card">
        <div className="flex items-start gap-6">
          <div className="w-20 h-20 rounded-full bg-primary-100 flex items-center justify-center">
            <User className="w-10 h-10 text-primary-600" />
          </div>
          <div className="flex-1">
            <h2 className="text-xl font-semibold text-gray-900">{user?.nickname || user?.username}</h2>
            <p className="text-gray-500">@{user?.username}</p>
            <div className="mt-2 flex gap-2">
              {user?.roles?.map((role) => (
                <span key={role.id} className="badge badge-info">
                  {role.roleName}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Edit Profile */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Edit Profile</h3>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Display Name
            </label>
            <input
              type="text"
              value={formData.nickname}
              onChange={(e) => setFormData({ ...formData, nickname: e.target.value })}
              className="input"
              disabled={!isEditing}
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Email
            </label>
            <input
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              className="input"
              disabled={!isEditing}
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Phone
            </label>
            <input
              type="tel"
              value={formData.phone}
              onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
              className="input"
              disabled={!isEditing}
            />
          </div>
          <div className="flex gap-2">
            {isEditing ? (
              <>
                <button
                  onClick={() => updateMutation.mutate()}
                  className="btn btn-primary flex items-center gap-2"
                >
                  <Save className="w-4 h-4" />
                  Save
                </button>
                <button
                  onClick={() => setIsEditing(false)}
                  className="btn btn-secondary"
                >
                  Cancel
                </button>
              </>
            ) : (
              <button
                onClick={() => setIsEditing(true)}
                className="btn btn-primary"
              >
                Edit Profile
              </button>
            )}
          </div>
        </div>
      </div>

      {/* Change Password */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Change Password</h3>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Current Password
            </label>
            <input
              type="password"
              value={passwordData.oldPassword}
              onChange={(e) => setPasswordData({ ...passwordData, oldPassword: e.target.value })}
              className="input"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              New Password
            </label>
            <input
              type="password"
              value={passwordData.newPassword}
              onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
              className="input"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Confirm New Password
            </label>
            <input
              type="password"
              value={passwordData.confirmPassword}
              onChange={(e) => setPasswordData({ ...passwordData, confirmPassword: e.target.value })}
              className="input"
            />
          </div>
          <button
            onClick={handlePasswordChange}
            disabled={passwordMutation.isPending}
            className="btn btn-primary"
          >
            {passwordMutation.isPending ? 'Changing...' : 'Change Password'}
          </button>
        </div>
      </div>
    </div>
  )
}
