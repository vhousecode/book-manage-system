#!/bin/bash
# Generate React Component Script
# Usage: ./generate-component.sh <component-name> [page|common|ui]

COMPONENT_NAME=${1:-"Example"}
TYPE=${2:-"common"}
COMPONENT_DIR="src/components/${TYPE}"

# Ensure PascalCase
CLASS_NAME=$(echo $COMPONENT_NAME | sed 's/\([A-Z]\)/\u\1/g' | sed 's/^\(.\)/\u\1/')

mkdir -p ${COMPONENT_DIR}

case $TYPE in
  "page")
    cat > ${COMPONENT_DIR}/${CLASS_NAME}.tsx << EOF
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function ${CLASS_NAME}() {
  const [searchTerm, setSearchTerm] = useState('')

  const { data, isLoading } = useQuery({
    queryKey: ['${COMPONENT_NAME}', searchTerm],
    queryFn: () => {/* API call */},
  })

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900">${CLASS_NAME}</h1>
      </div>

      <div className="card">
        {/* Content */}
      </div>
    </div>
  )
}
EOF
    ;;
  "common")
    cat > ${COMPONENT_DIR}/${CLASS_NAME}.tsx << EOF
import clsx from 'clsx'

interface ${CLASS_NAME}Props {
  className?: string
  children?: React.ReactNode
}

export function ${CLASS_NAME}({ className, children }: ${CLASS_NAME}Props) {
  return (
    <div className={clsx('', className)}>
      {children}
    </div>
  )
}
EOF
    ;;
  "ui")
    cat > ${COMPONENT_DIR}/${CLASS_NAME}.tsx << EOF
import clsx from 'clsx'

interface ${CLASS_NAME}Props extends React.HTMLAttributes<HTMLDivElement> {
  variant?: 'primary' | 'secondary'
  size?: 'sm' | 'md' | 'lg'
}

export function ${CLASS_NAME}({
  variant = 'primary',
  size = 'md',
  className,
  ...props
}: ${CLASS_NAME}Props) {
  return (
    <div
      className={clsx(
        'rounded-lg transition-all',
        {
          'bg-primary-600 text-white': variant === 'primary',
          'bg-gray-100 text-gray-700': variant === 'secondary',
        },
        {
          'px-3 py-1.5 text-sm': size === 'sm',
          'px-4 py-2 text-sm': size === 'md',
          'px-6 py-3 text-base': size === 'lg',
        },
        className
      )}
      {...props}
    />
  )
}
EOF
    ;;
esac

echo "✅ Component generated: ${COMPONENT_DIR}/${CLASS_NAME}.tsx"
