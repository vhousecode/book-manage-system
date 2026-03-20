# Tailwind CSS 样式技能

## 技能描述
本技能确保使用Tailwind CSS进行一致的UI样式设计，包含自定义设计令牌、组件模式和响应式设计最佳实践。

## 触发场景
- 使用Tailwind CSS创建UI组件
- 设置Tailwind配置
- 实现响应式设计
- 创建可复用的CSS工具类模式

## 执行步骤

### 1. 配置设置

#### tailwind.config.js
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // 主色调色板
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
          800: '#1e40af',
          900: '#1e3a8a',
        },
        // 中性色
        gray: {
          50: '#f9fafb',
          100: '#f3f4f6',
          200: '#e5e7eb',
          300: '#d1d5db',
          400: '#9ca3af',
          500: '#6b7280',
          600: '#4b5563',
          700: '#374151',
          800: '#1f2937',
          900: '#111827',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
      boxShadow: {
        'soft': '0 2px 15px -3px rgba(0, 0, 0, 0.07), 0 10px 20px -2px rgba(0, 0, 0, 0.04)',
        'card': '0 0 0 1px rgba(0, 0, 0, 0.05), 0 1px 2px 0 rgba(0, 0, 0, 0.05)',
      },
      animation: {
        'fade-in': 'fadeIn 0.2s ease-in-out',
        'slide-up': 'slideUp 0.3s ease-out',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
  ],
}
```

#### postcss.config.js
```javascript
export default {
  plugins: {
    tailwindcss: {},
    autoprefixer: {},
  },
}
```

### 2. 全局样式（globals.css）
```css
@tailwind base;
@tailwind components;
@tailwind utilities;

/* 基础样式 */
:root {
  font-family: 'Inter', system-ui, sans-serif;
}

body {
  @apply min-h-screen bg-gray-50 text-gray-900 antialiased;
}

/* 自定义组件类 */
@layer components {
  /* 卡片 */
  .card {
    @apply bg-white rounded-xl shadow-soft border border-gray-100 p-6;
  }

  /* 按钮 */
  .btn {
    @apply inline-flex items-center justify-center px-4 py-2 rounded-lg font-medium 
           transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed;
  }
  
  .btn-primary {
    @apply btn bg-primary-600 text-white hover:bg-primary-700 active:bg-primary-800;
  }
  
  .btn-secondary {
    @apply btn bg-gray-100 text-gray-700 hover:bg-gray-200 active:bg-gray-300;
  }
  
  .btn-danger {
    @apply btn bg-red-500 text-white hover:bg-red-600 active:bg-red-700;
  }

  /* 输入框 */
  .input {
    @apply w-full px-4 py-2 border border-gray-300 rounded-lg 
           focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent
           transition-all duration-200;
  }

  /* 标签 */
  .badge {
    @apply inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium;
  }
  
  .badge-success { @apply badge bg-green-100 text-green-800; }
  .badge-warning { @apply badge bg-yellow-100 text-yellow-800; }
  .badge-danger { @apply badge bg-red-100 text-red-800; }
  .badge-info { @apply badge bg-blue-100 text-blue-800; }

  /* 表格 */
  .table {
    @apply w-full border-collapse;
  }
  
  .table th {
    @apply px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider bg-gray-50;
  }
  
  .table td {
    @apply px-4 py-3 text-sm text-gray-900 border-t border-gray-100;
  }
}
```

### 3. 组件模式

#### 卡片组件
```tsx
export function Card({ children, className }: { children: React.ReactNode; className?: string }) {
  return (
    <div className={clsx('bg-white rounded-xl shadow-soft border border-gray-100 p-6', className)}>
      {children}
    </div>
  )
}
```

#### 按钮组件
```tsx
import clsx from 'clsx'

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger'
  size?: 'sm' | 'md' | 'lg'
}

export function Button({ 
  variant = 'primary', 
  size = 'md', 
  className, 
  children, 
  ...props 
}: ButtonProps) {
  return (
    <button
      className={clsx(
        'inline-flex items-center justify-center rounded-lg font-medium transition-all',
        {
          'bg-primary-600 text-white hover:bg-primary-700': variant === 'primary',
          'bg-gray-100 text-gray-700 hover:bg-gray-200': variant === 'secondary',
          'bg-red-500 text-white hover:bg-red-600': variant === 'danger',
        },
        {
          'px-3 py-1.5 text-sm': size === 'sm',
          'px-4 py-2 text-sm': size === 'md',
          'px-6 py-3 text-base': size === 'lg',
        },
        className
      )}
      {...props}
    >
      {children}
    </button>
  )
}
```

#### 输入框组件
```tsx
import clsx from 'clsx'

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
}

export function Input({ label, error, className, ...props }: InputProps) {
  return (
    <div>
      {label && (
        <label className="block text-sm font-medium text-gray-700 mb-1">
          {label}
        </label>
      )}
      <input
        className={clsx(
          'w-full px-4 py-2 border rounded-lg transition-all',
          'focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent',
          error ? 'border-red-300' : 'border-gray-300',
          className
        )}
        {...props}
      />
      {error && <p className="mt-1 text-sm text-red-600">{error}</p>}
    </div>
  )
}
```

### 4. 响应式设计模式

#### 响应式网格
```tsx
// 卡片网格 - 移动端1列，平板2列，桌面4列
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
  {items.map(item => <Card key={item.id} {...item} />)}
</div>
```

#### 响应式侧边栏
```tsx
// 侧边栏在移动端隐藏，lg及以上显示
<div className="hidden lg:block lg:w-64">
  <Sidebar />
</div>
```

#### 响应式文本
```tsx
// 文本随视口缩放
<h1 className="text-2xl md:text-3xl lg:text-4xl font-bold">
  响应式标题
</h1>
```

### 5. 布局模式

#### 页面布局
```tsx
export default function PageLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
    </div>
  )
}
```

#### 区块布局
```tsx
// 标准区块间距
<div className="space-y-6">
  <div className="flex items-center justify-between">
    <h1 className="text-2xl font-bold text-gray-900">区块标题</h1>
    <Button>操作按钮</Button>
  </div>
  <Card>
    {/* 内容 */}
  </Card>
</div>
```

## 输出标准

### 颜色使用
- 使用语义化颜色：primary、success、warning、danger
- 使用灰度色阶作为中性元素
- 避免使用任意颜色值

### 间距
- 使用Tailwind间距比例：p-4、m-2、gap-6
- 使用space-y-*实现垂直韵律
- 使用gap-*实现网格/flex间距

### 排版
- 使用text-*设置字号
- 使用font-*设置字重
- 使用text-*设置颜色

### 状态
- 始终包含hover状态
- 为交互元素包含focus状态
- 包含disabled状态

## 版本信息
- 版本：1.0.0
- 最后更新：2024-03-19