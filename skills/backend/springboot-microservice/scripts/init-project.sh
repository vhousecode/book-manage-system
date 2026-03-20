#!/bin/bash
# Project Initialization Script
# Usage: ./init-project.sh <project-name> <company-name>

set -e

PROJECT_NAME=${1:-"my-project"}
COMPANY_NAME=${2:-"company"}
PROJECT_DIR=$(pwd)

echo "🚀 Initializing project: $PROJECT_NAME"
echo "📁 Company: $COMPANY_NAME"

# Create backend directory structure
echo "Creating backend structure..."
mkdir -p backend/{common,gateway,user-service,book-service,borrow-service}/src/main/{java/com/${COMPANY_NAME}/${PROJECT_NAME}/{result,exception,enums,constant,utils,config},resources}

# Create frontend directory structure
echo "Creating frontend structure..."
mkdir -p frontend/src/{api,components/{layout,common,ui},pages/{auth,dashboard,book,borrow,user,stats},stores,types,hooks,utils,styles}

# Create sql directory
echo "Creating sql directory..."
mkdir -p sql

echo "✅ Project structure created successfully!"
echo ""
echo "Next steps:"
echo "1. Run 'generate-parent-pom.sh' to create parent pom.xml"
echo "2. Run 'generate-common-module.sh' to create common module"
echo "3. Run 'generate-service.sh <service-name>' to create services"
