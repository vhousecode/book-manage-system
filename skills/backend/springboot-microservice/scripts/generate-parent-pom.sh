#!/bin/bash
# Generate Parent POM Script
# Usage: ./generate-parent-pom.sh <project-name> <group-id>

PROJECT_NAME=${1:-"book-manage"}
GROUP_ID=${2:-"com.book"}

cat > backend/pom.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${GROUP_ID}</groupId>
    <artifactId>${PROJECT_NAME}</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>
    <name>${PROJECT_NAME}</name>
    <description>Book Management System</description>

    <modules>
        <module>common</module>
        <module>gateway</module>
        <module>user-service</module>
        <module>book-service</module>
        <module>borrow-service</module>
    </modules>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.0</spring-boot.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
        <mybatis-plus.version>3.5.5</mybatis-plus.version>
        <mysql.version>8.0.33</mysql.version>
        <jwt.version>0.12.3</jwt.version>
        <knife4j.version>4.3.0</knife4j.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Spring Boot -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>\${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!-- Spring Cloud -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>\${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!-- MyBatis Plus -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
                <version>\${mybatis-plus.version}</version>
            </dependency>

            <!-- MySQL -->
            <dependency>
                <groupId>com.mysql</groupId>
                <artifactId>mysql-connector-j</artifactId>
                <version>\${mysql.version}</version>
            </dependency>

            <!-- JWT -->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>\${jwt.version}</version>
            </dependency>

            <!-- Knife4j (Swagger) -->
            <dependency>
                <groupId>com.github.xiaoymin</groupId>
                <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
                <version>\${knife4j.version}</version>
            </dependency>

            <!-- Common Module -->
            <dependency>
                <groupId>${GROUP_ID}</groupId>
                <artifactId>common</artifactId>
                <version>\${project.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>\${spring-boot.version}</version>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
EOF

echo "✅ Parent POM generated: backend/pom.xml"
