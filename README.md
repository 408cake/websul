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
DB_NAME=test <- 추후 변경 필요 
DB_USER={YOUR_DB_USERNAME}
DB_PASSWORD={YOUR_DB_PASSWORD}

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



3. 사전 준비 (필수)
3-1. Docker 설치

    Windows / Mac:

    Docker Desktop 설치

설치 후 Docker Desktop 실행

설치 확인:

    docker --version
    docker compose version


정상 출력되면 OK.

4. 프로젝트 실행 방법 (Docker)
4-1. 저장소 클론

    git clone <REPOSITORY_URL>
    cd websul

4-2. 환경 변수 파일 생성

    .env.example을 복사하여 .env 파일을 생성한다.

    cp .env.example .env


    .env 파일에 필요한 값을 입력한다.

※ .env 파일은 절대 Git에 커밋하지 않는다.

4-3. Docker 컨테이너 실행
    docker compose up -d --build

4-4. 컨테이너 상태 확인
    docker compose ps


정상 실행 시 아래 서비스가 Up 상태로 표시된다.

    websul-backend

    mysql

    redis

4-5. 로그 확인
    docker compose logs -f websul-backend

4-6. 서버 정상 동작 확인 (Health Check)
    curl http://localhost:8080/health

    curl.exe http://localhost:8080/health ( powershell )

HTTP 200 응답이 오면 정상 실행 상태다.

5. 컨테이너 제어 명령어
중지
    docker compose down

중지 + 데이터 초기화
    
    docker compose down -v

재시작
    docker compose up -d

6. 컨테이너 내부 접근 (필요한 경우)
backend 컨테이너
    docker exec -it websul-backend sh

mysql 컨테이너

    docker exec -it mysql mysql -u <DB_USER> -p

redis 컨테이너

    docker exec -it redis redis-cli


컨테이너 이름은 docker compose ps 결과를 기준으로 한다.

7. API 테스트 예시 (Windows PowerShell)
    
    $env:BASE_URL="http://localhost:8080"

    curl.exe "$env:BASE_URL/health"

    curl.exe "$env:BASE_URL/api/public/books?page=0&size=20&sort=id,DESC"

## 8. DB Seed Data (FK 제약 때문에 필요)
리뷰/주문 등 쓰기 API는 FK 제약을 사용한다. 테스트를 위해 최소 더미 데이터가 필요하다.

주의
- `spring.jpa.hibernate.ddl-auto=update` 인 경우 데이터는 유지된다.
- AUTO_INCREMENT는 환경/이력에 따라 달라진다. 예: `book_id`가 1이 아니라 2부터 시작할 수 있다.
- 아래 쿼리로 생성된 실제 ID를 확인한 뒤 API 요청에 사용한다.

### 8.1 seller 더미 생성
```sql
INSERT INTO seller (
    business_na,
    business_nl,
    email,
    phonenumber,
    address,
    payoutbank,
    payoutaccount,
    payouthold,
    updated_at
) VALUES (
    '테스트상점',
    'TEST-BIZ-001',
    'seller@test.com',
    '010-0000-0000',
    '서울시 테스트구 테스트로 123',
    '국민은행',
    '123456-01-000000',
    'HOLD-001',
    NOW()
);

SELECT seller_id FROM seller ORDER BY seller_id DESC LIMIT 1;
```

### 8.2 book 더미 생성
`seller_id`는 위에서 생성된 실제 값을 사용한다.
```sql
INSERT INTO book (
    available_copies,
    price,
    publication_date,
    publication_year,
    total_copies,
    created_at,
    updated_at,
    seller_id,
    isbn,
    genre,
    image_url,
    title,
    description
) VALUES (
    10,
    15000,
    '2025-01-01',
    2025,
    10,
    NOW(6),
    NOW(6),
    1,
    '978-1-23456-789-0',
    'IT',
    'https://example.com/book.jpg',
    '테스트 도서',
    '리뷰 테스트용 더미 도서'
);

SELECT book_id FROM book ORDER BY book_id DESC LIMIT 5;
```

### 8.3 user 더미 생성
```sql
INSERT INTO `user` (
    active,
    birthdate,
    created_at,
    updated_at,
    phonenumber,
    name,
    password,
    address,
    email,
    gender,
    role
) VALUES (
    b'1',
    '1999-01-01',
    NOW(6),
    NOW(6),
    '010-1111-2222',
    '테스트유저',
    '$2a$10$7QZ6n5k0v8Qm8m8m8m8m8uQ8Q8Q8Q8Q8Q8Q8Q8Q8Q8Q8Q8',
    '서울시 테스트구 테스트로 123',
    'user@test.com',
    'MALE',
    'ROLE_USER'
);

SELECT user_id FROM `user` ORDER BY user_id DESC LIMIT 5;
```

### 8.4 review 더미 생성
`book_id`, `user_id`는 위에서 조회한 실제 값을 사용한다.
```sql
INSERT INTO review (
    rating,
    book_id,
    created_at,
    updated_at,
    user_id,
    comment,
    content
) VALUES (
    5,
    2,
    NOW(6),
    NOW(6),
    1,
    '리뷰 테스트 코멘트',
    '아주 좋은 책입니다.'
);

SELECT review_id, book_id, user_id, rating FROM review ORDER BY review_id DESC LIMIT 5;
```

## 9. API 검증 커맨드 (Windows PowerShell)
```powershell
$env:BASE_URL="http://localhost:8080"
```

### 9.1 Health
```powershell
curl.exe "$env:BASE_URL/health"
```

### 9.2 Books (Public)
```powershell
curl.exe "$env:BASE_URL/api/public/books?page=0&size=20&sort=id,DESC"
```

### 9.3 Authors (Public)
```powershell
curl.exe "$env:BASE_URL/api/public/authors?page=0&size=10&sort=id,DESC"
curl.exe "$env:BASE_URL/api/public/authors/1"
```

### 9.4 Categories (Public)
```powershell
curl.exe "$env:BASE_URL/api/public/categories?page=0&size=10&sort=id,DESC"
curl.exe "$env:BASE_URL/api/public/categories/1"
```

### 9.5 Reviews (Public)
주의: `sort`는 DB 컬럼명이 아니라 엔티티 필드명을 사용한다. 예: DB `review_id` → 엔티티 `id`
```powershell
Invoke-RestMethod "$env:BASE_URL/api/public/reviews?page=0&size=10&sort=id,DESC"
Invoke-RestMethod "$env:BASE_URL/api/public/reviews/1"
```

리뷰 생성 (DB에 존재하는 `bookId`/`userId` 사용)
```powershell
$payload = @{
  bookId = 2
  userId = 1
  rating = 5
  comment = "리뷰 코멘트"
  content = "짧은 내용"
}

Invoke-RestMethod -Method Post -Uri "$env:BASE_URL/api/public/reviews" -ContentType "application/json" -Body ($payload | ConvertTo-Json -Compress)
```
