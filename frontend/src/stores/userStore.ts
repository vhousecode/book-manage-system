import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { User, LoginResponse } from '@/types'

interface UserState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  setUser: (user: User | null) => void
  setToken: (token: string | null) => void
  login: (response: LoginResponse) => void
  logout: () => void
}

export const useUserStore = create<UserState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      setUser: (user) => set({ user, isAuthenticated: !!user }),
      setToken: (token) => set({ token }),
      login: (response) => set({
        user: {
          id: response.id,
          username: response.username,
          nickname: response.nickname,
          avatar: response.avatar,
          roles: response.roles.map(r => ({ id: 0, roleName: r, roleKey: r, status: 1, sort: 0 })),
          status: 1,
        },
        token: response.token,
        isAuthenticated: true,
      }),
      logout: () => set({ user: null, token: null, isAuthenticated: false }),
    }),
    {
      name: 'user-storage',
      partialize: (state) => ({ user: state.user, token: state.token, isAuthenticated: state.isAuthenticated }),
    }
  )
)
