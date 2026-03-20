# Tailwind CSS 设计令牌参考

## 颜色调色板

### 主色调
| 名称 | 十六进制 | 用途 |
|------|----------|------|
| primary-50 | #eff6ff | 浅色背景 |
| primary-100 | #dbeafe | 悬停状态 |
| primary-200 | #bfdbfe | 边框 |
| primary-300 | #93c5fd | - |
| primary-400 | #60a5fa | - |
| primary-500 | #3b82f6 | 主要元素 |
| primary-600 | #2563eb | 按钮、链接 |
| primary-700 | #1d4ed8 | 悬停状态 |
| primary-800 | #1e40af | 激活状态 |
| primary-900 | #1e3a8a | 深色强调 |

### 灰色调
| 名称 | 十六进制 | 用途 |
|------|----------|------|
| gray-50 | #f9fafb | 背景 |
| gray-100 | #f3f4f6 | 卡片背景 |
| gray-200 | #e5e7eb | 边框 |
| gray-300 | #d1d5db | 禁用边框 |
| gray-400 | #9ca3af | 占位符文本 |
| gray-500 | #6b7280 | 次要文本 |
| gray-600 | #4b5563 | 正文文本 |
| gray-700 | #374151 | 标题 |
| gray-800 | #1f2937 | 深色文本 |
| gray-900 | #111827 | 主要文本 |

### 语义色
| 颜色 | 浅色 | 主色 | 深色 | 用途 |
|------|------|------|------|------|
| 成功 | #dcfce7 | #22c55e | #166534 | 正向操作 |
| 警告 | #fef3c7 | #f59e0b | #92400e | 警告提示 |
| 危险 | #fee2e2 | #ef4444 | #991b1b | 错误、删除 |
| 信息 | #dbeafe | #3b82f6 | #1e40af | 信息提示 |

## 间距比例

| 名称 | 尺寸 | 用途 |
|------|------|------|
| 0 | 0px | 无间距 |
| 1 | 4px | 紧凑间距 |
| 2 | 8px | 小内边距 |
| 3 | 12px | - |
| 4 | 16px | 标准内边距 |
| 5 | 20px | - |
| 6 | 24px | 区块内边距 |
| 8 | 32px | 大内边距 |
| 10 | 40px | - |
| 12 | 48px | 页面内边距 |
| 16 | 64px | 区块外边距 |

## 排版比例

| 类名 | 字号 | 行高 | 用途 |
|------|------|------|------|
| text-xs | 12px | 1rem | 标签、徽章 |
| text-sm | 14px | 1.25rem | 次要文本 |
| text-base | 16px | 1.5rem | 正文文本 |
| text-lg | 18px | 1.75rem | 引导文本 |
| text-xl | 20px | 1.75rem | 小标题 |
| text-2xl | 24px | 2rem | 区块标题 |
| text-3xl | 30px | 2.25rem | 页面标题 |
| text-4xl | 36px | 2.5rem | 大标题 |

## 字重

| 类名 | 字重 | 用途 |
|------|------|------|
| font-light | 300 | 大标题 |
| font-normal | 400 | 正文 |
| font-medium | 500 | 标签、按钮 |
| font-semibold | 600 | 小标题 |
| font-bold | 700 | 标题 |

## 圆角

| 类名 | 尺寸 | 用途 |
|------|------|------|
| rounded | 4px | 小元素 |
| rounded-md | 6px | 按钮、输入框 |
| rounded-lg | 8px | 卡片 |
| rounded-xl | 12px | 大卡片 |
| rounded-2xl | 16px | 弹窗 |
| rounded-full | 50% | 头像、徽章 |

## 阴影

| 类名 | 用途 |
|------|------|
| shadow-sm | 轻微阴影 |
| shadow | 默认卡片 |
| shadow-md | 下拉菜单 |
| shadow-lg | 弹窗 |
| shadow-xl | 大元素 |

## 组件类参考

### 卡片
```css
.card {
  @apply bg-white rounded-xl shadow-soft border border-gray-100 p-6;
}
```

### 按钮
```css
.btn {
  @apply inline-flex items-center justify-center px-4 py-2 rounded-lg 
         font-medium transition-all duration-200 
         disabled:opacity-50 disabled:cursor-not-allowed;
}

.btn-primary {
  @apply btn bg-primary-600 text-white hover:bg-primary-700;
}

.btn-secondary {
  @apply btn bg-gray-100 text-gray-700 hover:bg-gray-200;
}

.btn-danger {
  @apply btn bg-red-500 text-white hover:bg-red-600;
}
```

### 输入框
```css
.input {
  @apply w-full px-4 py-2 border border-gray-300 rounded-lg 
         focus:outline-none focus:ring-2 focus:ring-primary-500 
         focus:border-transparent transition-all duration-200;
}
```

### 标签
```css
.badge {
  @apply inline-flex items-center px-2.5 py-0.5 rounded-full 
         text-xs font-medium;
}

.badge-success { @apply badge bg-green-100 text-green-800; }
.badge-warning { @apply badge bg-yellow-100 text-yellow-800; }
.badge-danger { @apply badge bg-red-100 text-red-800; }
.badge-info { @apply badge bg-blue-100 text-blue-800; }
```

### 表格
```css
.table {
  @apply w-full border-collapse;
}

.table th {
  @apply px-4 py-3 text-left text-xs font-medium text-gray-500 
         uppercase tracking-wider bg-gray-50;
}

.table td {
  @apply px-4 py-3 text-sm text-gray-900 border-t border-gray-100;
}
```

## 响应式断点

| 断点 | 前缀 | 最小宽度 |
|------|------|----------|
| sm | sm: | 640px |
| md | md: | 768px |
| lg | lg: | 1024px |
| xl | xl: | 1280px |
| 2xl | 2xl: | 1536px |

## 动画类

```css
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(10px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

.animate-fade-in { animation: fadeIn 0.2s ease-in-out; }
.animate-slide-up { animation: slideUp 0.3s ease-out; }
```