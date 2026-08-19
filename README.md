# Cineverse

> 영화 콘텐츠 탐색부터 리뷰, 찜, 구독 및 결제까지 제공하는 영화 OTT 플랫폼

<!-- 프로젝트 대표 이미지 또는 로고 -->
<p align="center">
  <img width="800" height="495" alt="Image" src="https://github.com/user-attachments/assets/f3bfe235-25f4-47e3-87d6-623cbc56e12b" />
</p>

<br>

## 프로젝트 소개

Cineverse는 사용자가 다양한 영화 콘텐츠를 탐색하고 이용할 수 있는 웹 기반 OTT 플랫폼입니다.

사용자는 회원가입과 로그인을 통해 콘텐츠를 검색하고, 리뷰와 평점을 작성하거나 관심 있는 콘텐츠를 찜 목록에 추가할 수 있습니다. 구독 상품 결제와 이벤트 참여 등의 기능도 제공합니다.

관리자는 콘텐츠, 회원, 결제, 구독 상품, 이벤트 및 공지사항을 관리할 수 있습니다.

<br>

## 개발 기간

```text
2026.07.03 ~ 2026.08.17
```

<br>

## 팀 구성

- 개인 프로젝트
- 기획, UI 구현, 백엔드 API 및 데이터베이스 설계

<br>

## 주요 기능

### 사용자 기능

- 회원가입 및 로그인
- 이메일 인증
- JWT 기반 인증과 토큰 재발급
- 콘텐츠 목록 및 상세 조회
- 장르별 콘텐츠 검색
- 콘텐츠 평점 및 리뷰
- 찜 목록 관리
- 구독 상품 조회 및 결제
- 영화 뉴스 조회
- 공지사항 조회
- 이벤트 및 당첨 결과 조회
- 마이페이지 및 회원정보 관리

### 관리자 기능

- 회원 조회 및 상태 관리
- 콘텐츠 등록, 수정 및 삭제
- 이미지와 영상 파일 관리
- 리뷰 관리
- 공지사항 관리
- 이벤트 및 당첨 결과 관리
- 대시보드 통계 조회

<br>

## 화면 구성

| 메인 화면 | 콘텐츠 상세 |
|---|---|
| <img width="400" alt="Cineverse" src="https://github.com/user-attachments/assets/9e9b8837-fa31-46ad-9970-6de3b60bac06" /> | <img width="400" alt="Image" src="https://github.com/user-attachments/assets/7acb2986-1464-456d-a8ed-8269ba4f9ca4" /> |

| 구독 및 결제 | 관리자 대시보드 |
|---|---|
| <img width="400" alt="subscription" src="https://github.com/user-attachments/assets/fbdffd9d-e90a-4307-9c07-290aa8223a6f" /> | <img width="400" alt="dashboard" src="https://github.com/user-attachments/assets/0b02de61-831a-4a17-9978-41fb389a504b" /> |

<!-- 이미지 예시
| 메인 화면 | 콘텐츠 상세 |
|---|---|
| <img src="./docs/images/main.png" width="400"> | <img src="./docs/images/content-detail.png" width="400"> |
-->

<br>

## 기술 스택

### Backend

- JAVA 17
- Spring Boot 3.5.16
- Spring Security
- Spring Data JPA
- QueryDsl

### Frontend

- React
- Vite
- JavaScript
- Tailwind CSS
- React Router
- Axios
- Zustand

### Database

- MariaDB
- Redis

### Tools

- GitHub
- Postman
- Swagger

### 주요 프론트엔드 라이브러리

- React Router: 클라이언트 라우팅
- Axios: HTTP API 통신
- Zustand: 전역 상태 관리 및 Persist를 활용한 상태 유지
- Tailwind CSS: UI 스타일링
- React Icons: 아이콘
- Sonner: 토스트 알람
- React Daum Postcode: 주소 검색
- Chart.js: 데이터 차트 시각화

<br>

## 시스템 아키텍처

<!-- 시스템 구조 이미지를 추가하세요. -->

```text
React Client
     │
     │ REST API
     ▼
Spring Boot Server
     │
     ├── MariaDB
     ├── Redis
     ├── 외부 결제 API
     ├── 이메일 SMTP
     └── 외부 뉴스 API
```

<br>

## ERD

<!-- ERDCloud 또는 이미지 링크 -->

<p align="center">
  <img width="2620" height="1702" alt="Image" src="https://github.com/user-attachments/assets/ca200071-548f-45bc-8099-1f44e36f1cb4" />
</p>

<br>

## 프로젝트 구조

### Backend

```text
src/main/java/com/cineverse/cineverse_backend
├── domain
│   ├── auth
│   ├── content
│   ├── dashboard
│   ├── event
│   ├── image
│   ├── mail
│   ├── news
│   ├── notice
│   ├── subscription
│   ├── terms
│   ├── user
│   └── video
└── global
    ├── config
    ├── entity
    ├── exception
    ├── scheduler
    ├── security
    └── util
```

### Frontend

```text
src
├── api           # 서버 API 요청 및 응답 처리
├── assets        # 이미지 로고 등 정적 리소스
├── components    # 재사용 가능한 공통 UI 컴포넌트
├── constants     # 어플리케이션 전역 상수
├── hooks         # 재사용 가능한 커스텀 훅
├── layouts       # 페이지 공통 레이아웃
├── pages         # 라우트별 페이지 컴포넌트
├── router        # 라우팅 경로 및 설정
├── store         # 전역 상태 관리
└── utils         # 공용 유틸리티 함수
```
<br>

도메인 중심의 패키지 구조를 적용했습니다. 각 도메인은 독립적으로
`Controller`, `Service`, `Repository`, `Entity` 및 `DTO` 계층을 가지며,
프로젝트 전반에서 사용하는 설정과 보안, 예외 처리 기능은
`global` 패키지로 분리했습니다.

<br>

## 인증 흐름

```text
로그인 요청
   ↓
사용자 정보 검증
   ↓
Access Token 및 Refresh Token 발급
   ↓
Access Token으로 API 요청
   ↓
Access Token 만료 시 토큰 재발급
```

- Spring Security 기반 인증
- JWT Access Token 및 Refresh Token 사용
- Refresh Token을 Redis에 저장하고 만료 시간 설정
- HttpOnly Cookie를 통한 Refresh Token 전달
- RTR(Refresh Token Rotation) 방식을 적용하여 토큰 재발급 시 Refresh Token도 함께 교체
- UUID 기반의 임의 문자열로 Refresh Token 생성

<br>

## 결제 흐름

```text
구독 상품 선택
   ↓
결제 요청 및 주문 생성
   ↓
토스페이먼츠 결제창 호출
   ↓
서버 결제 승인 요청
   ↓
결제 및 구독 정보 저장
```

<br>

## 주요 API

| 기능 | Method | Endpoint |
|---|---:|---|
| 로그인 | `POST` | `/api/auth/login` |
| 회원가입 | `POST` | `/api/auth/signup` |
| 콘텐츠 상세 | `GET` | `/api/contents/{contentId}` |
| 결제 승인 | `POST` | `/api/payments/confirm` |
| 이벤트 목록 | `GET` | `/api/event` |
| 공지사항 목록 | `GET` | `/api/notice` |

<br>

### Swagger API Documentation

전체 API 명세는 Swagger UI에서 확인할 수 있습니다.

<p align="center">
  <img width="3510" height="2488" alt="Image" src="https://github.com/user-attachments/assets/a89097a6-320d-44ac-9424-8599b01f4121" />
</p>

로컬에서 백엔드 서버를 실행한 다음 아래 주소로 접속합니다.

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger에서는 다음 내용을 확인하고 테스트할 수 있습니다.

- API 엔드포인트와 HTTP Method
- 요청 파라미터 및 Request Body
- 응답 데이터 구조
- HTTP 상태 코드
- JWT 인증이 필요한 API
- API 직접 실행 및 응답 확인

<br>

## 실행 방법

### 개발 및 테스트 환경

```text
Java 17
Node.js 24
MariaDB 11.8
Redis 8.4
```

### Backend

```bash
git clone https://github.com/sjrnfl1746/cineverse_backend.git
cd cineverse_backend
cp src/main/resources/application.yml.example src/main/resources/application.yml
./gradlew bootRun
```

`application.yml`에 필요한 환경 설정을 입력해야 합니다.

### Frontend

```bash
git clone https://github.com/sjrnfl1746/cineverse_frontend.git
cd cineverse_frontend
cp .env.example .env
npm install
npm run dev
```

<br>

## 환경 설정

### Backend

```yaml
spring:
  datasource:
    username: DB username
    password: DB password

  mail:
    username: mail username
    password: mail password

jwt:
  secret: jwt secret

file:
  upload:
    root-path: file root path

toss:
  payments:
    secret-key: toss payments secret key

naver:
  news:
    client-id: naver client id
    client-secret: naver client secret key
```

### Frontend

```dotenv
VITE_API_SERVER= backend api server
VITE_TOSS_CLIENT_KEY= toss payments client key
```

실제 비밀번호와 Secret Key는 GitHub에 올리지 않습니다.

<br>

## 트러블슈팅

### 문제 제목

**문제**

문제가 발생한 상황과 원인을 작성합니다.

**해결**

어떤 방식으로 분석하고 해결했는지 작성합니다.

**결과**

성능, 안정성 또는 코드 품질이 어떻게 개선됐는지 작성합니다.

<br>

## 프로젝트 회고

- Spring Security와 JWT를 활용한 인증 과정에서 단순히 토큰을 발급하는 것뿐만 아니라, Access Token과 Refresh Token의 역할을 분리하고 Redis를 이용해 Refresh Token을 관리하는 방법을 배웠습니다.
- RTR 방식을 적용하며 기존 Refresh Token을 폐기하고 새로운 토큰으로 교체하는 과정을 통해 토큰 탈취와 재사용을 고려한 인증 설계의 중요성을 이해할 수 있었습니다.
- 데이터 조회 기능에서는 Spring Data JPA의 메서드 기반 쿼리만으로 여러 검색 조건을 처리할 경우 코드가 복잡해지는 문제가 이었습니다. 이를 해결하기 위해 QueryDsl을 적용하고 검색 조건을 동적으로 조합하도록 구현하면서,
  요구사항 변경에 유연하게 대응할 수 있는 조회 구조를 설계하는 방법을 배웠습니다.

<br>

## Repository

- [Cineverse Backend](https://github.com/sjrnfl1746/cineverse_backend)
- [Cineverse Frontend](https://github.com/sjrnfl1746/cineverse_frontend)
