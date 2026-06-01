# Family Health OS (FHOS) — Architecture Design
> Phase 1 MVP | Version 1.0.0 | 2026-04-19

---

## 1. 아키텍처 개요

```
┌─────────────────────────────────────────────────────┐
│                   Mobile Client                      │
│            React Native (iOS + Android)              │
│  HealthKit (iOS)        Health Connect (Android)     │
└──────────────────────┬──────────────────────────────┘
                       │ HTTPS / REST
                       ▼
┌─────────────────────────────────────────────────────┐
│                   AWS API Gateway                    │
└──────────────────────┬──────────────────────────────┘
                       │
              ┌────────┴────────┐
              ▼                 ▼
┌─────────────────┐    ┌─────────────────┐
│  Auth Service   │    │  Core API       │
│  (Spring Boot)  │    │  (Spring Boot)  │
│                 │    │                 │
│  - JWT 발급     │    │  - 가족 그룹    │
│  - 소셜 로그인  │    │  - 리스크 점수  │
│  - PIPA 동의    │    │  - 챌린지       │
└────────┬────────┘    │  - 배지         │
         │             │  - 알림         │
         │             └────────┬────────┘
         │                      │
         └──────────┬───────────┘
                    ▼
         ┌──────────────────┐
         │   PostgreSQL     │
         │   (AWS RDS)      │
         └──────────────────┘
                    │
         ┌──────────┴──────────┐
         ▼                     ▼
┌─────────────────┐   ┌─────────────────┐
│   LLM API       │   │  Push Notify    │
│  (챌린지 추천)  │   │  (FCM / APNs)   │
│  + 가드레일     │   │                 │
└─────────────────┘   └─────────────────┘
```

---

## 2. 기술 스택

| 레이어 | 기술 | 버전/메모 |
|--------|------|----------|
| 모바일 | React Native | iOS + Android 동시 지원 |
| 웨어러블 (Android) | Google Health Connect | 권한 거부 시 이전 데이터 유지 |
| 웨어러블 (iOS) | Apple HealthKit | 권한 거부 시 이전 데이터 유지 |
| 백엔드 | Kotlin + Spring Boot | 멀티 모듈 구조 |
| 데이터베이스 | PostgreSQL | AWS RDS |
| 인프라 | AWS | 월 50만원 이하 |
| 인증 | JWT + 소셜 로그인 | Kakao OAuth |
| 푸시 알림 | FCM (Android) + APNs (iOS) | |
| LLM API | 외부 LLM (챌린지 추천 전용) | 리스크 점수 계산 제외 |

---

## 3. 백엔드 모듈 구조

```
fhos-backend/
├── auth-service/           # 인증, 소셜 로그인, PIPA 동의
├── family-service/         # 가족 그룹 생성, 초대, 구성원 관리
├── health-service/         # 건강 프로파일, 설문, 웨어러블 동기화
├── risk-service/           # 룰 기반 리스크 점수 계산
├── challenge-service/      # 챌린지 CRUD, 체크인, AI 추천
├── gamification-service/   # 배지, 스트리크, 랭킹
├── notification-service/   # 푸시 알림 (FCM / APNs)
└── common/                 # 공통 도메인, 예외, 유틸
```

### 3.1 Risk Service 상세 (룰 기반, LLM 미사용)

```kotlin
data class RiskScore(
    val total: Float,       // 0~100
    val genetic: Float,     // 가족력 × 0.30
    val lifestyle: Float,   // 생활습관 × 0.25
    val behavior: Float,    // 챌린지 달성율 × 0.20
    val environment: Float, // 수면 패턴 × 0.15
    val clinical: Float,    // BMI/질환 × 0.10
    val updatedAt: Instant
)

// 점수 갱신 트리거: 설문 재응답 | 웨어러블 동기화 | 챌린지 완료
```

### 3.2 Challenge Service 상세 (LLM 사용)

```
입력: RiskScore + 달성 이력 + 선호도
→ LLM Prompt + 가드레일 (A안: 허용 카테고리 + B안: 금지 목록)
→ 출력: 챌린지 제안 1개 이상 (walking/diet/sleep/exercise/hydration)
```

**가드레일 구현 원칙**:
- A안: 허용 카테고리 화이트리스트 검증 (출력 파싱 후 카테고리 검증)
- B안: 금지 키워드 블랙리스트 필터 (의료 진단, 약물 추천 등)
- 두 검증 모두 통과해야 최종 추천 노출

---

## 4. 데이터베이스 스키마

### 4.1 핵심 테이블

```sql
-- 가족 그룹
CREATE TABLE families (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    risk_score  DECIMAL(5,2),
    streak_days INTEGER DEFAULT 0,
    created_at  TIMESTAMP DEFAULT now()
);

-- 구성원
CREATE TABLE members (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    family_id        UUID REFERENCES families(id),
    relationship     VARCHAR(20) CHECK (relationship IN ('self','parent','spouse','child')),
    is_minor         BOOLEAN DEFAULT false,
    pipa_consent_by  UUID REFERENCES members(id),  -- 미성년자의 부모 ID
    created_at       TIMESTAMP DEFAULT now(),
    deleted_at       TIMESTAMP  -- 소프트 딜리트, 즉시 삭제 처리
);

-- 건강 프로파일
CREATE TABLE health_profiles (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id        UUID REFERENCES members(id),
    height_cm        DECIMAL(5,2),
    weight_kg        DECIMAL(5,2),
    current_diseases TEXT[],
    medications      BOOLEAN DEFAULT false,
    family_history   TEXT[],
    updated_at       TIMESTAMP DEFAULT now()
);

-- 생활습관
CREATE TABLE lifestyles (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    health_profile_id       UUID REFERENCES health_profiles(id),
    meals_together_per_week INTEGER,
    dining_out_frequency    VARCHAR(20) CHECK (dining_out_frequency IN ('rarely','sometimes','often')),
    sleep_start             TIME,
    sleep_end               TIME,
    activity_level          VARCHAR(20) CHECK (activity_level IN ('low','moderate','high')),
    smoking                 BOOLEAN,
    alcohol_frequency       VARCHAR(20) CHECK (alcohol_frequency IN ('none','occasional','regular'))
);

-- 리스크 점수
CREATE TABLE risk_scores (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id   UUID REFERENCES members(id),
    total       DECIMAL(5,2),
    genetic     DECIMAL(5,2),
    lifestyle   DECIMAL(5,2),
    behavior    DECIMAL(5,2),
    environment DECIMAL(5,2),
    clinical    DECIMAL(5,2),
    updated_at  TIMESTAMP DEFAULT now()
);

-- 챌린지
CREATE TABLE challenges (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    family_id    UUID REFERENCES families(id),
    title        VARCHAR(200) NOT NULL,
    category     VARCHAR(20) CHECK (category IN ('walking','diet','sleep','exercise','hydration')),
    deadline     TIMESTAMP,
    ai_generated BOOLEAN DEFAULT false,
    created_at   TIMESTAMP DEFAULT now()
);

-- 챌린지 완료
CREATE TABLE challenge_completions (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    challenge_id UUID REFERENCES challenges(id),
    member_id    UUID REFERENCES members(id),
    completed    BOOLEAN DEFAULT false,
    source       VARCHAR(20) CHECK (source IN ('wearable','manual_input','photo_upload')),
    completed_at TIMESTAMP
);

-- 배지
CREATE TABLE badges (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    target_id   UUID NOT NULL,  -- member_id 또는 family_id
    target_type VARCHAR(10) CHECK (target_type IN ('member','family')),
    badge_type  VARCHAR(30) CHECK (badge_type IN (
        'first_challenge','streak_7','streak_30','risk_improved','family_complete'
    )),
    earned_at   TIMESTAMP DEFAULT now()
);

-- PIPA 동의 로그
CREATE TABLE pipa_consents (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    minor_member_id  UUID REFERENCES members(id),
    parent_member_id UUID REFERENCES members(id),
    consented_at     TIMESTAMP DEFAULT now()
);
```

### 4.2 데이터 삭제 정책
- 구성원 제외: `members.deleted_at` 즉시 설정 → 관련 데이터 cascade 처리
- 계정 탈퇴: `deleted_at` 설정 후 90일 후 배치 파기

---

## 5. API 설계

### 5.1 인증
```
GET  /oauth/kakao/login      # 카카오 로그인 페이지 리디렉트 URL 반환
POST /oauth/kakao/callback   # 카카오 access_token → FHOS JWT 발급
POST /auth/pipa/consent      # 미성년자 PIPA 동의
GET  /auth/me                # 내 정보
POST /auth/refresh           # Access Token 갱신
POST /auth/terms             # 약관 동의
```

> **Kakao OAuth 흐름**
> 1. 모바일 SDK(`@react-native-seoul/kakao-login`)에서 카카오 로그인 → `kakao_access_token` 수신
> 2. `POST /oauth/kakao/callback { kakao_access_token }` → 서버가 Kakao API로 사용자 정보 조회
> 3. 서버가 FHOS JWT(`access_token`, `refresh_token`) 발급 후 응답
>
> **백엔드 필수 환경변수**
> - `KAKAO_REST_API_KEY` — 카카오 앱 REST API 키
> - `KAKAO_REDIRECT_URI` — 카카오 개발자 콘솔 등록 Redirect URI (서버 측 사용 시)

### 5.2 가족 그룹
```
POST /families               # 가족 그룹 생성
GET  /families/{id}          # 가족 정보 조회
POST /families/{id}/invite   # 초대 코드 생성
POST /families/{id}/join     # 초대 코드로 가입
DELETE /families/{id}/members/{memberId}  # 구성원 제외
```

### 5.3 건강 프로파일 & 설문
```
POST /members/{id}/health-profile   # 설문 제출 (최초/재설문)
GET  /members/{id}/health-profile   # 프로파일 조회
```

### 5.4 리스크 점수
```
GET  /members/{id}/risk-score       # 개인 리스크 점수
GET  /families/{id}/risk-score      # 가족 평균 리스크 점수
POST /members/{id}/risk-score/recalc  # 점수 재계산 트리거
```

### 5.5 챌린지
```
GET  /families/{id}/challenges           # 챌린지 목록
POST /families/{id}/challenges           # 챌린지 생성 (수동)
POST /families/{id}/challenges/recommend # AI 추천 챌린지 요청
POST /challenges/{id}/checkin            # 체크인
GET  /challenges/{id}/completions        # 완료 현황
```

### 5.6 게이미피케이션
```
GET /families/{id}/streak         # 스트리크 조회
GET /families/{id}/ranking        # 랭킹 조회
GET /members/{id}/badges          # 개인 배지
GET /families/{id}/badges         # 가족 배지
```

### 5.7 웨어러블
```
POST /members/{id}/wearable/sync  # 웨어러블 데이터 동기화
GET  /members/{id}/wearable/last  # 마지막 동기화 데이터 조회
```

---

## 6. 모바일 앱 구조

```
src/
├── screens/
│   ├── onboarding/     # 소셜 로그인, 설문, PIPA 동의
│   ├── home/           # 리스크 점수 홈 화면
│   ├── family/         # 가족 그룹 관리, 초대
│   ├── challenge/      # 챌린지 목록, 체크인
│   ├── gamification/   # 스트리크, 랭킹, 배지
│   └── profile/        # 개인 설정, 탈퇴
├── services/
│   ├── healthKit.ts    # Apple HealthKit 연동
│   ├── healthConnect.ts # Google Health Connect 연동
│   └── pushNotification.ts
├── store/              # 상태 관리
└── api/                # API 클라이언트
```

---

## 7. 인프라 (AWS, 월 50만원 이하)

| 서비스 | 용도 | 예상 비용 |
|--------|------|----------|
| EC2 t3.small | Spring Boot 서버 | ~$15/월 |
| RDS db.t3.micro | PostgreSQL | ~$15/월 |
| S3 | 사진 업로드 스토리지 | ~$2/월 |
| CloudFront | CDN | ~$1/월 |
| SES / SNS | 이메일 / 푸시 알림 | ~$2/월 |
| API Gateway | REST API 관리 | ~$5/월 |
| **합계** | | **~$40/월 (약 55,000원)** |

> 월 50만원 예산 내 충분히 운용 가능.

---

## 8. 장애 대응 (Fallback 전략)

| 장애 상황 | 대응 |
|-----------|------|
| LLM API 장애 | 사전 정의된 챌린지 풀에서 랜덤 추천 |
| 웨어러블 동기화 실패 | 이전 동기화 데이터 유지 표시 |
| 푸시 알림 실패 | 앱 내 배너로 대체 |
| RDS 장애 | Read replica 전환 (Phase 2에서 Multi-AZ 도입 검토) |

---

## 9. 보안 설계

- JWT 토큰: Access (15분) + Refresh (30일)
- 건강 데이터: AES-256 암호화 저장
- API: HTTPS 강제
- PIPA 준수: 개인정보 처리 목적 명시, 동의 이력 보관
- 미성년자 데이터: 부모 동의 없이 접근 불가
- 가족 간 데이터 공유: 구성원별 동의 플래그 검증
