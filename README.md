# Tetris 遊戲後端 API

基於 Spring Boot 的 Tetris 遊戲後端服務，提供使用者認證、遊戲紀錄儲存等功能，使用 TiDB 雲端資料庫。

## 🚀 技術棧

- **Java 17**
- **Spring Boot 3.5.8**
- **Spring Security** + **JWT** 認證
- **Spring Data JPA** + **Hibernate**
- **TiDB Cloud** (MySQL 相容資料庫)
- **Swagger/OpenAPI** 3.0 API 文檔
- **Maven** 專案管理
- **Lombok** 簡化程式碼

## 📋 功能特性

- ✅ 使用者註冊與登入 (JWT Token 認證)
- ✅ 遊戲紀錄儲存與查詢
- ✅ 排行榜功能
- ✅ RESTful API 設計
- ✅ Swagger UI 互動式 API 文檔
- ✅ 雲端資料庫整合 (TiDB)

## 🛠️ 環境需求

- Java 17 或以上版本
- Maven 3.6+
- TiDB 資料庫（或任何 MySQL 8.0+ 資料庫）

## ⚙️ 本地開發設定

### 1. 複製專案

```bash
git clone <repository-url>
cd Tetris_backend
```

### 2. 設定環境變數

在專案根目錄建立 `.env` 檔案：

```bash
# .env
DB_URL=jdbc:mysql://<your-tidb-host>:<port>/<database>?sslMode=VERIFY_IDENTITY&useSSL=true
DB_USER=<your-database-username>
DB_PASSWORD=<your-database-password>
```

> ⚠️ **注意**：`.env` 檔案包含敏感資訊，請勿上傳至 Git！

### 3. 執行專案

使用 Maven Wrapper 執行：

```bash
./mvnw spring-boot:run
```

或者使用本地 Maven：

```bash
mvn spring-boot:run
```

應用程式將在 `http://localhost:8080` 啟動。

### 4. 訪問 API 文檔

啟動後，訪問 Swagger UI：

```
http://localhost:8080/swagger-ui.html
```

## 📁 專案結構

```
src/
├── main/
│   ├── java/tetris/demo/
│   │   ├── TetrisApplication.java          # 主程式入口
│   │   ├── controller/                     # REST API 控制器
│   │   │   ├── AuthController.java         # 認證相關 API
│   │   │   └── GameController.java         # 遊戲紀錄 API
│   │   ├── model/                          # 資料模型
│   │   │   ├── User.java                   # 使用者實體
│   │   │   └── TetrisRecord.java           # 遊戲紀錄實體
│   │   ├── repository/                     # 資料庫存取層
│   │   │   ├── UserRepository.java
│   │   │   └── RecordRepository.java
│   │   └── security/                       # 安全性配置
│   │       ├── JwtUtil.java                # JWT 工具類
│   │       └── SecurityConfig.java         # Spring Security 配置
│   └── resources/
│       └── application.properties          # 應用程式配置
└── test/                                   # 測試檔案
```

## 🔑 API 端點

### 認證相關

- `POST /api/auth/register` - 使用者註冊
- `POST /api/auth/login` - 使用者登入
- `GET /api/auth/refresh` - 刷新 Token

### 遊戲紀錄

- `POST /api/game/records` - 儲存遊戲紀錄
- `GET /api/game/records/me` - 查詢個人紀錄
- `GET /api/game/leaderboard` - 查詢排行榜

詳細 API 文檔請查看 Swagger UI。

## 🔧 配置說明

### application.properties

主要配置項：

```properties
# 資料庫連線（從環境變數讀取）
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA 配置
spring.jpa.hibernate.ddl-auto=update  # 自動更新資料表結構
spring.jpa.show-sql=true              # 顯示 SQL 語句

# Server 配置
server.port=8080
```

## 🐳 Docker 部署

（如果有 Dockerfile，可以補充 Docker 部署說明）

## 🧪 測試

執行測試：

```bash
./mvnw test
```