# FHOS 모바일 API 연동 핸드오프
> 작성일: 2026-05-18 | 대상: 프론트엔드 개발자

---

## 개요

현재 모바일 앱(`mobile/`)은 모든 화면이 각 `api.ts`의 **mock 함수**로 동작한다.  
이 문서는 mock을 실제 REST API(`https://api.fhos.app/v1`)로 교체하는 8단계 작업 계획을 기술한다.

---

## 공통 규격 (API 명세서 기준)

| 항목 | 값 |
|------|-----|
| Base URL | `https://api.fhos.app/v1` |
| 인증 | `Authorization: Bearer {access_token}` |
| Content-Type | `application/json` |
| 날짜 형식 | ISO 8601 UTC (`2026-05-18T09:00:00Z`) |
| Access Token 만료 | 1시간 |
| Refresh Token 만료 | 30일 |
| 토큰 저장 | iOS: Keychain / Android: Keystore (`expo-secure-store`) |
| 401 처리 | `POST /auth/refresh` 자동 갱신 → 실패 시 로그인 화면 |
| 에러 포맷 | `{ error_code: string, message: string, detail?: string }` |
| delta 규칙 | 음수 = 개선, 양수 = 악화 |

---

## Phase 1 — Foundation (API 클라이언트 + 인증 컨텍스트)

**브랜치**: `feat/phase1-api-foundation`  
**우선순위**: 최상 (모든 Phase의 선행 조건)

### 1-1. 패키지 설치

```bash
npx expo install expo-secure-store
npm install @react-native-seoul/kakao-login
```

| 패키지 | 용도 |
|--------|------|
| `expo-secure-store` | JWT 토큰을 Keychain/Keystore에 암호화 저장 |
| `@react-native-seoul/kakao-login` | 카카오 네이티브 SDK 래퍼 (iOS/Android) |

> ⚠️ `@react-native-seoul/kakao-login`은 네이티브 모듈이므로 **Expo Go에서 동작하지 않음**.  
> EAS Build(`eas build --profile development`) 또는 bare workflow 사용 필수.  
> `expo-auth-session`, `expo-web-browser`는 카카오 전환 후 불필요하므로 제거.

### 1-2. `src/api/client.ts`

```
역할: 모든 API 호출의 단일 진입점
- Base URL 설정
- Authorization 헤더 자동 주입
- 401 수신 시 /auth/refresh 호출 후 원본 요청 1회 재시도
- 재시도 실패 시 tokenStorage.clearTokens() + AuthContext.logout()
- 에러 응답 { error_code, message } 정규화
```

**핵심 인터페이스**:
```typescript
interface ApiError {
  error_code: string;
  message: string;
  detail?: string;
}

async function request<T>(
  method: 'GET' | 'POST' | 'PATCH' | 'DELETE',
  path: string,
  body?: unknown
): Promise<T>
```

**401 자동 갱신 흐름**:
```
request() → 401 수신
  → POST /auth/refresh { refresh_token }
  → 성공: 새 access_token 저장 → 원본 요청 재시도
  → 실패: clearTokens() → logout() (LoginScreen으로 이동)
```

> ⚠️ 무한루프 방지: refresh 요청 자체가 401이면 즉시 logout (재시도 없음)

### 1-3. `src/storage/tokenStorage.ts`

```typescript
// expo-secure-store 래퍼
saveTokens(access: string, refresh: string): Promise<void>
getAccessToken(): Promise<string | null>
getRefreshToken(): Promise<string | null>
clearTokens(): Promise<void>
```

### 1-4. `src/context/AuthContext.tsx`

```typescript
interface AuthState {
  userId: string | null;
  familyId: string | null;
  isAuthenticated: boolean;
  isOnboarded: boolean;    // onboarding 완료 여부
  isLoading: boolean;      // 앱 시작 시 토큰 복원 중
}

interface AuthContextValue extends AuthState {
  login(tokens: { access_token: string; refresh_token: string; user_id: string; is_new_user: boolean }): Promise<void>;
  setOnboarded(familyId: string): void;  // 온보딩 완료 후 family_id 저장
  logout(): Promise<void>;
}
```

**앱 시작 시 토큰 복원 순서**:
```
앱 마운트 → getAccessToken()
  → 있음: /auth/me 호출하여 user_id, family_id 복원
  → 없음: isAuthenticated = false → LoginScreen
```

### 1-5. `App.tsx` 수정

```
기존: onboarded: boolean 단순 플래그
변경: AuthContext 구독

isLoading → SplashScreen (로딩 스피너)
!isAuthenticated → LoginScreen
isAuthenticated && !isOnboarded → OnboardingScreen
isAuthenticated && isOnboarded → MainApp
```

---

## Phase 2 — 로그인 화면

**브랜치**: `feat/phase2-auth-login`  
**의존**: Phase 1 완료

### 신규 파일

- `src/features/auth/LoginScreen.tsx`
- `src/features/auth/api.ts`

### API 매핑

| 버튼 | 흐름 |
|------|------|
| 카카오 로그인 | `@react-native-seoul/kakao-login`의 `login()` 호출 → `kakao_access_token` 수신 → `POST /oauth/kakao/callback` |

### 콜백 처리

```
@react-native-seoul/kakao-login login() 호출
  → KakaoOAuthToken { accessToken, refreshToken, ... } 수신
  → POST /oauth/kakao/callback { kakao_access_token: accessToken }
  → 응답: { access_token, refresh_token, user_id, is_new_user }
  → saveTokens() 저장
  → AuthContext.login() 호출
  → is_new_user=true → TermsModal → OnboardingScreen
  → is_new_user=false → MainApp
```

### 약관 동의 (로그인 직후 1회)

```
POST /auth/terms
Body: { member_id, terms_agreed: true, privacy_agreed: true }
```

> 미동의 시 서비스 진입 차단 — 로그인 직후 모달로 표시

---

## Phase 3 — 온보딩 API 연동

**브랜치**: `feat/phase3-onboarding-api`  
**의존**: Phase 2 완료

### 단계별 API 호출 순서

```
STEP 1 (동의)
  → POST /consents
    body: { member_id, health_data: true, family_share: true, marketing: bool }
    → consent_id 저장

STEP 2 (가족 구성원 추가)
  → 가족 생성: POST /families
    body: { family_name }
    → family_id, invite_code 저장
  → 관계 설정: POST /family-members
    body: { family_id, member_id, relation_type: 'self'|'parent'|'spouse'|'child' }

STEP 3 (신체 정보)
  → POST /health-profiles
    body: { member_id, height, weight, birth_date, gender }
    → health_profile_id 저장 (이후 단계에서 사용)

STEP 4 (현재 질환)
  → POST /health-profiles/{health_profile_id}/conditions
    body: [{ condition_code: 'DM2' }, { condition_code: 'HTN' }, ...]

STEP 5 (가족력)
  → POST /health-profiles/{health_profile_id}/family-history
    body: [{ disease_code: 'DM2' }, ...]

STEP 5 (식습관) + STEP 6 (생활 리듬)
  → PATCH /health-profiles/{health_profile_id}/lifestyle
    body: { shared_meals_per_week, takeout_freq, sleep_time, wake_time, weekend_sleep_diff }
    ※ 두 단계에서 각 1회씩 호출 (부분 업데이트 허용)

STEP 7 (목표 설정)
  → POST /goals
    body: { member_id, family_id, goal_type: 'walk'|'diet'|'sleep'|'water' }

STEP 8 (완료 — 리스크 점수 계산)
  → POST /risk-scores/calculate
    body: { member_id }
    → score, risk_level, breakdown 수신
  → AuthContext.setOnboarded(family_id) → MainApp 진입
```

### 상태 관리

온보딩 중 수집한 ID(`health_profile_id`, `family_id` 등)는  
`OnboardingScreen` 내부 state로 단계 간 전달. 완료 후에는 AuthContext로 이관.

---

## Phase 4 — 홈 화면 API 연동

**브랜치**: `feat/phase4-home-api`  
**의존**: Phase 1

### 파일 수정: `src/features/home/api.ts`

```typescript
// 기존 mock → 실제 API 호출로 교체
export async function fetchHomeData(familyId: string, memberId: string): Promise<HomeData>
```

### API 매핑

| 데이터 | 엔드포인트 | 응답 필드 |
|--------|-----------|----------|
| 가족 리스크 요약 | `GET /families/{family_id}/risk-summary` | `avg_score, grade, delta_weekly, members_scores` |
| 오늘 현황 | `GET /families/{family_id}/today-status` | `streak, done_count, total_count, members[].challenge_done` |
| 추천 미션 | `GET /missions/recommended?family_id=` | `mission_id, title, urgent, remaining_time` |
| 개인 리스크 | `GET /risk-scores?member_id=` | `score, risk_level, calculated_at` |

### hooks.ts 수정

```typescript
// AuthContext에서 familyId, userId 가져와 api 호출
const { familyId, userId } = useAuth();
```

### `HomeData` 타입 변경 (mock → 실제 응답 형태 반영)

```typescript
export interface HomeData {
  familyRisk: { avgScore: number; grade: RiskLevel; deltaWeekly: number; membersScores: MemberScore[] };
  todayStatus: { streak: number; doneCount: number; totalCount: number };
  recommendedMission: { missionId: string; title: string; urgent: boolean; remainingTime: string | null } | null;
  myRisk: { score: number; level: RiskLevel };
}
```

---

## Phase 5 — 챌린지 화면 API 연동

**브랜치**: `feat/phase5-challenge-api`  
**의존**: Phase 1

### API 매핑

| 기능 | 엔드포인트 |
|------|-----------|
| 미션 목록 | `GET /missions?family_id=&category=all\|walk\|diet\|sleep\|water&status=active` |
| 체크인 | `POST /mission-logs` |

### 체크인 body 매핑

```typescript
// 웨어러블
{ mission_id, member_id, method: 'wearable', value: 8234, source: 'healthkit', photo_url: null }

// 수동
{ mission_id, member_id, method: 'manual', value: 8000, source: null, photo_url: null }

// 사진
{ mission_id, member_id, method: 'photo', value: null, source: null, photo_url: 'https://...' }
```

> 체크인 성공 응답에 `behavior_score_delta` 포함 → 홈 점수 갱신 트리거 가능

---

## Phase 6 — 가족 화면 API 연동

**브랜치**: `feat/phase6-family-api`  
**의존**: Phase 1

### API 매핑

| 기능 | 엔드포인트 |
|------|-----------|
| 가족 개요 | `GET /families/{family_id}/overview` → `streak_days, avg_score, habit_pct, grade` |
| 구성원 목록 | `GET /families/{family_id}/members` → `member_id, name, relation, score, challenge_done, share_status` |
| 초대 코드 | `GET /families/{family_id}/invite-code` → `invite_code, invite_url, expires_at` |
| 공유 설정 | `PATCH /privacy-settings/{member_id}` → `share_risk_score, share_challenge_status, share_health_detail` |
| 구성원 제외 | `DELETE /family-members/{member_id}` → 204 No Content |

### 주의사항

- 초대 코드 복사: `Clipboard.setString(invite_code)` (RN 내장)
- 구성원 제외: 되돌릴 수 없음 → `Alert.alert` 확인 후 호출

---

## Phase 7 — 리포트 화면 API 연동

**브랜치**: `feat/phase7-report-api`  
**의존**: Phase 1

### API 매핑

| 기능 | 엔드포인트 |
|------|-----------|
| 가족 탭 리스크 | `GET /families/{family_id}/risk-summary` (Phase 4와 공유) |
| 개인 탭 리스크 | `GET /members/{member_id}/risk-summary` → `score, grade, delta_monthly` |
| 점수 추이 차트 | `GET /risk-scores/history?family_id=&period=weekly` → `[{ week, score }]` |
| 요인별 기여도 | `GET /risk-scores/breakdown?member_id=` → `factors[], negative_factors[], positive_factors[]` |

### 차트 데이터 → SVG 매핑

```typescript
// 응답 예시
[{ week: '2026-W18', score: 58 }, { week: '2026-W19', score: 55 }]

// SVG polyline points 변환
const points = data.map((d, i) => `${i * 40},${100 - d.score}`).join(' ');
```

---

## Phase 8 — 마이 화면 API 연동

**브랜치**: `feat/phase8-my-api`  
**의존**: Phase 1

### API 매핑

| 기능 | 엔드포인트 |
|------|-----------|
| 개인 리스크 | `GET /members/{member_id}/risk-summary` |
| 게임화 정보 | `GET /members/{member_id}/gamification` → `current_streak, longest_streak, badges_this_month_count` |
| 배지 컬렉션 | `GET /badges?member_id=` → `badge_id, name, acquired, acquired_at, unlock_condition` |
| 랭킹 | `GET /families/{family_id}/ranking?period=monthly` → `rank, member_name, score, delta_monthly` |
| 스트리크 캘린더 | `GET /members/{member_id}/streak-calendar?month=YYYY-MM` → `[{ date, done, today }]` |
| 알림 설정 조회 | `GET /notification-settings/{member_id}` |
| 알림 설정 수정 | `PATCH /notification-settings/{member_id}` |
| 계정 탈퇴 | `PATCH /users/me/status { confirm_text: '탈퇴하겠습니다', status: 'inactive' }` |

### 배지 타입 변환 (API → 현재 Badge 타입)

```typescript
// API 응답
{ badge_id: 'streak_7', name: '7일 연속', acquired: true, acquired_at: '...', unlock_condition: '...' }

// 현재 Badge 타입으로 변환
{ id: badge_id, key: badge_id, label: name, icon: BADGE_ICONS[badge_id], locked: !acquired, earnedAt: acquired_at ?? '', description: name, condition: unlock_condition }
```

> `BADGE_ICONS` 매핑 상수 필요: `{ first_challenge: '🏅', streak_7: '🔥', streak_30: '⚡', risk_improved: '📈', family_complete: '👨‍👩‍👧' }`

---

## Mock 전환 전략 (`USE_MOCK` 플래그)

백엔드 미완성 기간 동안 각 `api.ts`에 플래그를 두어 언제든 전환 가능하게 유지:

```typescript
// src/api/config.ts
export const USE_MOCK = __DEV__ && process.env.EXPO_PUBLIC_USE_MOCK === 'true';

// 각 features/{domain}/api.ts
export async function fetchHomeData(...) {
  if (USE_MOCK) return MOCK_HOME;
  return apiClient.request('GET', `/families/${familyId}/risk-summary`);
}
```

`.env` 파일:
```
EXPO_PUBLIC_USE_MOCK=true   # 개발 중 mock 모드
EXPO_PUBLIC_API_URL=https://api.fhos.app/v1
```

---

## 작업 체크리스트

### Phase 1 ✅ 진행 중
- [ ] `expo-secure-store`, `expo-auth-session`, `expo-web-browser` 설치
- [ ] `src/api/client.ts` 구현
- [ ] `src/storage/tokenStorage.ts` 구현
- [ ] `src/context/AuthContext.tsx` 구현
- [ ] `App.tsx` 분기 로직 수정

### Phase 2
- [ ] `src/features/login/LoginScreen.tsx` 수정 (카카오 단일 버튼)
- [ ] `src/features/login/api.ts` 수정 (`postKakaoCallback` 구현)
- [ ] 약관 동의 모달 구현

### Phase 3
- [ ] 온보딩 각 스텝 → API 연결
- [ ] 단계간 ID 전달 (health_profile_id 등) state 설계

### Phase 4~8 (각 화면)
- [ ] Home API 연동
- [ ] Challenge API 연동
- [ ] Family API 연동
- [ ] Report API 연동
- [ ] My API 연동

---

## 참고 문서

- API 명세서: `docs/backend/FHOS_API_명세서_final.xlsx`
- 아키텍처: `docs/backend/ARCHITECTURE.md`
- 현재 타입 정의: `mobile/src/types/index.ts`
- 디자인 토큰: `mobile/src/theme/index.ts`
