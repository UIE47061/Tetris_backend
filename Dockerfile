# 第一階段：建置階段
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# 複製 Maven 配置檔案（利用 Docker 快取層）
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# 下載依賴（這層會被快取，除非 pom.xml 改變）
RUN mvn dependency:go-offline -B

# 複製源碼
COPY src ./src

# 建置應用程式（跳過測試以加快建置速度）
RUN mvn clean package -DskipTests

# 第二階段：執行階段（精簡映像）
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 建立非 root 使用者
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 從建置階段複製 JAR 檔案
COPY --from=build /app/target/*.jar app.jar

# 設定環境變數（可被外部覆蓋）
ENV SERVER_PORT=7860 \
    JAVA_OPTS="-Xms256m -Xmx512m"

# 暴露端口（Hugging Face 使用 7860）
EXPOSE 7860

# 健康檢查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:7860/actuator/health || exit 1

# 啟動應用程式
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$SERVER_PORT -jar app.jar"]