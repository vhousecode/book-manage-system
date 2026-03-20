#!/bin/bash
# Generate Service Module Script
# Usage: ./generate-service.sh <service-name> <port> <group-id>

SERVICE_NAME=${1:-"user"}
PORT=${2:-8081}
GROUP_ID=${3:-"com.book"}
SERVICE_DIR="backend/${SERVICE_NAME}-service"
PACKAGE_PATH=$(echo $GROUP_ID | tr '.' '/')
CLASS_NAME=$(echo $SERVICE_NAME | sed 's/-\(.\)/\u\1/g' | sed 's/^\(.\)/\u\1/')

echo "🔧 Generating service: ${SERVICE_NAME}-service"
echo "📁 Port: ${PORT}"
echo "📦 Package: ${GROUP_ID}.${SERVICE_NAME}"

# Create directory structure
mkdir -p ${SERVICE_DIR}/src/main/java/${PACKAGE_PATH}/${SERVICE_NAME}/{controller,service/impl,mapper,entity,dto}
mkdir -p ${SERVICE_DIR}/src/main/resources

# Generate pom.xml
cat > ${SERVICE_DIR}/pom.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>${GROUP_ID}</groupId>
        <artifactId>book-manage</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>${SERVICE_NAME}-service</artifactId>
    <name>${CLASS_NAME} Service</name>
    <description>${CLASS_NAME} microservice</description>

    <dependencies>
        <!-- Common Module -->
        <dependency>
            <groupId>${GROUP_ID}</groupId>
            <artifactId>common</artifactId>
        </dependency>

        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- MyBatis Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </dependency>

        <!-- Knife4j -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Generate application.yml
cat > ${SERVICE_DIR}/src/main/resources/application.yml << EOF
server:
  port: ${PORT}

spring:
  application:
    name: ${SERVICE_NAME}-service
  datasource:
    url: jdbc:mysql://\${MYSQL_HOST:localhost}:3306/\${MYSQL_DATABASE:book_manage}
    username: \${MYSQL_USER:root}
    password: \${MYSQL_PASSWORD:root}
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0

knife4j:
  enable: true
EOF

# Generate Application class
cat > ${SERVICE_DIR}/src/main/java/${PACKAGE_PATH}/${SERVICE_NAME}/${CLASS_NAME}ServiceApplication.java << EOF
package ${GROUP_ID}.${SERVICE_NAME};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("${GROUP_ID}.${SERVICE_NAME}.mapper")
public class ${CLASS_NAME}ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(${CLASS_NAME}ServiceApplication.class, args);
    }
}
EOF

# Generate Dockerfile
cat > ${SERVICE_DIR}/Dockerfile << EOF
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache tzdata && \\
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
COPY target/${SERVICE_NAME}-service-1.0.0.jar app.jar
EXPOSE ${PORT}
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"
ENTRYPOINT ["sh", "-c", "java \$JAVA_OPTS -jar app.jar"]
EOF

echo "✅ Service generated: ${SERVICE_DIR}"
echo ""
echo "Created files:"
echo "  - ${SERVICE_DIR}/pom.xml"
echo "  - ${SERVICE_DIR}/src/main/resources/application.yml"
echo "  - ${SERVICE_DIR}/src/main/java/.../${CLASS_NAME}ServiceApplication.java"
echo "  - ${SERVICE_DIR}/Dockerfile"
