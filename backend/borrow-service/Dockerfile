# Backend Borrow Service Dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy parent and module poms first to leverage Docker layer cache for dependency download
COPY pom.xml ./pom.xml
COPY common/pom.xml ./common/pom.xml
COPY borrow-service/pom.xml ./borrow-service/pom.xml
COPY gateway/pom.xml ./gateway/pom.xml
COPY user-service/pom.xml ./user-service/pom.xml
COPY book-service/pom.xml ./book-service/pom.xml

# Copy source code
COPY common ./common
COPY borrow-service ./borrow-service

# Build borrow-service with required modules
RUN mvn -q -pl borrow-service -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Configure timezone for consistent log timestamps
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

COPY --from=builder /build/borrow-service/target/borrow-service-1.0.0.jar app.jar

EXPOSE 8083

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
