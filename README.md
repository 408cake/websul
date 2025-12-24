# Bookstore Backend (Spring Boot)

## 0. Summary
Spring Boot 기반 서점 시스템 REST API.
Docker Compose로 Backend + Redis를 띄우고, DB는 Azure MySQL을 사용한다.
API 문서 규격에 맞춰 공통 응답 포맷(성공/실패/페이지)을 유지한다.

## 1. Tech Stack
- Java / Spring Boot
- JPA (Hibernate)
- Azure MySQL
- Redis (Cache / Token)
- Docker Compose
- (Optional) Swagger/OpenAPI
- (Auth) JWT (Access/Refresh)

## 2. Requirements
- Docker Desktop (WSL2)
- JDK 17
- Gradle

## 3. Environment Variables
datasource는 application.properties에 하드코딩하지 않고 env로만 주입한다.

### .env
프로젝트 루트에 `.env` 파일을 생성한다.

예시:
DB_HOST=your-azure-mysql-host
DB_PORT=3306
DB_NAME=bookstore
DB_USER=bookstoreadmin
DB_PASSWORD=yourpassword

REDIS_HOST=redis
REDIS_PORT=6379

JWT_SECRET=your-secret
JWT_ACCESS_EXPIRES_MIN=30
JWT_REFRESH_EXPIRES_DAYS=14

SERVER_PORT=8080

## 4. Run (Docker Compose)
```bash
docker compose up -d --build
docker compose ps
docker compose logs -f backend
