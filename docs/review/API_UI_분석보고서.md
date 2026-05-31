# FHOS 종합 비교 분석 보고서

> 작성일: 2026-05-16  
> 분석자: Claude Sonnet 4.6  
> 대상: API 명세서 vs HTML UI 디자인 vs 기획·PRD 문서

---

## 분석 대상

| 문서 | 경로 |
|------|------|
| API 명세서 (45개 엔드포인트) | `docs/backend/FHOS_API_명세서_final.xlsx` |
| HTML UI 프로토타입 (6개 화면) | `design/html-ui/01~06.html` |
| PRD | `docs/planning/PRD.md` |
| 서비스 기획서 | `docs/planning/가족 단위 AI 헬스케어 플랫폼 기획.md` |
| 아키텍처 문서 | `docs/backend/ARCHITECTURE.md` |
| 사업계획서 | `docs/planning/1조_골골방지단.docx.md` |

---

## 1. 인수 기준(AC) 충족 현황

| AC | 요구사항 | API | HTML UI | 판정 |
|----|---------|-----|---------|------|
| AC-01 | 초대 코드·링크 공유로 가족 그룹 생성 | `POST /families`, `POST /families/{id}/invite`, `GET /families/{id}/invite-code` | 04-family: 코드+링크 동시 제공 | **충족** |
| AC-02 | 개인·가족력 포함 건강 설문 완료 | `POST /health-profiles`, `/conditions`, `/family-history`, `PATCH /lifestyle` | 01-onboarding: 8단계 위저드 | **충족** |
| AC-03 | 룰 기반 리스크 점수 계산 | `POST /risk-scores/calculate`, `GET /risk-scores` | 온보딩 완료 후 점수 표시 | **충족** |
| AC-04 | 챌린지 추천·완료 체크인 | `GET /missions/recommended`, `POST /mission-logs` | 03-challenge: 체크인 바텀시트 | **충족** |
| AC-05 | 가족 공동 마감 시간 설정 | **없음** | **없음** | **미충족** |
| AC-06 | 연속 달성 스트리크 | `GET /members/{id}/streak-calendar` (P1) | 02-home·06-my에 표시 | **부분 충족** (API P1) |
| AC-07 | 배지 자동 부여 | `POST /badges/award` (서버 내부), `GET /badges` (P1) | 06-my: 배지 컬렉션 | **부분 충족** (API P1) |
| AC-08 | 리스크 개선율 기준 가족 랭킹 | `GET /families/{id}/ranking` (P1) | 06-my: **절대점수** 기준 표시 | **불일치** |
| AC-09 | 미성년자 보호자 동의 | `PATCH /members/{member_id}` (P1), `POST /consents` | 01-onboarding: Step7 보호자 이메일 | **충족** |
| AC-10 | 구성원 제외 시 즉시 삭제 경고 | `DELETE /family-members/{id}` (P1) | 04-family: 삭제 확인 모달 | **충족** |

**AC-05 (마감 시간 설정)가 API·UI 모두에서 누락된 것이 가장 큰 공백이다.**

---

## 2. 주요 불일치 목록

### 불일치 A — 형제자매(sibling) 관계 처리

| 문서 | 내용 |
|------|------|
| PRD / API 명세서 | 관계 유형: `self / parent / spouse / child` 4종만 정의 |
| 01-onboarding.html | STEP 3에 **형제자매** 선택 버튼 추가 존재 |
| 04-family.html | 구성원 목록: 본인·부모·자녀만 표시, 형제자매 없음 |

→ 온보딩 UI가 DB 스키마보다 넓게 설계되어 있다. `sibling` enum 값이 API·DB에 없으면 해당 선택지를 선택한 사용자 데이터를 저장할 수 없다.  
**온보딩 UI에서 형제자매 제거 또는 API·DB에 sibling 추가** 중 방향 결정 필요.

---

### 불일치 B — exercise(운동) 챌린지 카테고리 누락

| 문서 | 내용 |
|------|------|
| PRD | 챌린지 카테고리 5종: `walking / diet / sleep / exercise / hydration` |
| 03-challenge.html | 필터 탭: **전체 / 걷기 / 식단 / 수면 / 수분** (exercise 탭 없음) |

→ PRD 대비 UI 스펙 불일치. exercise 카테고리 미션이 추천되더라도 필터가 없어 사용자가 분류해서 볼 수 없다.

---

### 불일치 C — 가족 랭킹 기준

| 문서 | 기준 |
|------|------|
| PRD AC-08 | **리스크 개선율** (전월 대비 점수 감소량) |
| 06-my.html | **리스크 절대점수** 기준 (28점·41점·62점 순위) |

→ 절대점수 기준이면 초기 점수가 낮은 구성원(젊고 건강한 자녀)이 항상 상위권을 독점해 가족 내 동기 부여 효과가 반감된다. PRD 의도(개선율)대로 UI 수정 필요.

---

### 불일치 D — 소셜 로그인 제공자

| 문서 | 내용 |
|------|------|
| 사업계획서 (`1조_골골방지단.docx.md`) | Google / **카카오** / **네이버** |
| API 명세서 | Google + **Apple** Only |

→ 사업계획서가 업데이트되지 않은 것으로 보인다. 카카오·네이버 로그인은 한국 시장에서 전환율에 직결되므로, MVP 범위에서 의도적으로 제외했다면 사업계획서에도 반영 필요.

---

### 불일치 E — 아키텍처 문서 vs API 명세서 엔드포인트 경로

| 위치 | 경로 |
|------|------|
| ARCHITECTURE.md | `/auth/social/google`, `/auth/social/apple` |
| API 명세서 | `/oauth/google/login`, `/oauth/apple/login`, `/oauth/google/callback`, `/oauth/apple/callback` |

→ 기능은 동일하나 경로가 다르다. API 명세서를 최신·확정 버전으로 간주하고 ARCHITECTURE.md 동기화 필요.

---

## 3. API 명세서 누락 항목

### 누락 1 — 웨어러블 동기화 전용 엔드포인트

- **체크인 우선순위** (PRD·UI): 웨어러블 > 수동 입력 > 사진 업로드
- ARCHITECTURE.md에 `/members/{id}/wearable/sync` 존재
- **API 명세서에 없음** → 웨어러블 동기화가 `POST /mission-logs`로 통합되는지 별도 API가 있는지 불명확

### 누락 2 — AC-05 가족 공동 마감 시간 설정

- PRD에 명시된 인수 기준이나 API 명세서·HTML UI 모두 미구현
- `POST /families` 또는 `PATCH /families/{id}` 확장으로 처리 가능

### 누락 3 — 리스크 점수 설명(Explainability) API

- 05-report.html: "55점인 이유" 섹션 — 점수를 높이는·낮추는 요인을 자연어로 제공
- `GET /risk-scores/breakdown`이 요인별 수치를 반환하는 것은 확인되나, 자연어 설명 필드(`reason_text`) 반환 여부 명세 불명확

### 누락 4 — 가족 구성원 개별 리포트 조회 흐름

- 05-report.html: 탭 전환으로 가족 구성원별 개인 리포트 표시
- `GET /members/{id}/risk-summary`로 처리 가능하나, 가족→구성원 ID 목록 조회 후 개별 요청해야 하는 흐름이 명세서에 명시되지 않음

### 누락 5 — 랭킹의 개선율 계산 기반 데이터

- PRD: 리스크 개선율 기준 랭킹 → 이전 달 점수와 비교 필요
- `GET /families/{id}/ranking` 응답 스펙에 `previous_score` 또는 `improvement_delta` 포함 여부 미정

---

## 4. P1 기능의 MVP UI 노출 현황

아래 P1 기능들이 HTML UI 데모에 이미 표현되어 있다. 데모 완성도를 위한 의도적 선택으로 보이나, 개발 우선순위와 UI 노출 시점을 팀 내 정렬 필요.

| 기능 | API 우선순위 | HTML UI 노출 |
|------|------------|------------|
| 스트리크 캘린더 | P1 | 06-my.html ✓ |
| 배지 컬렉션 | P1 | 02-home.html, 06-my.html ✓ |
| 가족 랭킹 | P1 | 06-my.html ✓ |
| 초대 코드 신규 생성 | P1 | 04-family.html ✓ |
| 미루기 (postpone) | P1 | 03-challenge.html에 없음 (OK) |

---

## 5. 잘 정렬된 부분

**PIPA 준수 UI 구현 완성도 높음:**
- 04-family.html: 구성원 제외 시 "건강 데이터·챌린지 기록 즉시 삭제, 되돌릴 수 없음" 경고 모달 (AC-10 충족)
- 06-my.html: 계정 탈퇴 시 "90일 후 자동 파기" 명시
- 04-family.html: 동의 기반 합류 안내 카드 (PIPA 카드)
- 01-onboarding.html: 미성년자 보호자 이메일 Step

**의료 면책 UI 일관성:**
- 03-challenge.html, 05-report.html 모두 "의료 진단이 아닌 건강 관리 서비스" 문구 표시

**데이터 공유 범위 UI 일치:**
- 04-family.html 공유 설정 3종 (리스크 점수 / 챌린지 진행 / 건강 상세)이 PRD의 동의 기반 공유 설계와 부합

---

## 6. 즉시 대응 권고 (우선순위 순)

| 순위 | 항목 | 유형 | 담당 | 작업 |
|------|------|------|------|------|
| 1 | AC-05 마감 시간 설정 | **누락** | BE+FE | API·UI 설계 및 추가 |
| 2 | 랭킹 기준 (절대점수 vs 개선율) | **불일치** | FE | 06-my.html UI 수정 or PRD 변경 |
| 3 | 형제자매 관계 처리 | **불일치** | BE+FE | API/DB에 sibling 추가 or 온보딩 UI 제거 |
| 4 | exercise 챌린지 필터 탭 | **불일치** | FE | 03-challenge.html 필터 추가 |
| 5 | 웨어러블 동기화 API 정의 | **누락** | BE | 명세서에 엔드포인트 추가 또는 통합 방식 명문화 |
| 6 | 리스크 설명(reason_text) 응답 스펙 | **불명확** | BE | breakdown API 응답 필드 추가 |
| 7 | 사업계획서 소셜 로그인 일치화 | **불일치** | 기획 | 사업계획서 업데이트 (Google+Apple) |
| 8 | ARCHITECTURE.md 경로 동기화 | **불일치** | BE | 아키텍처 문서 API 경로 최신화 |

---

## 요약

API 명세서 45개 엔드포인트는 AC 10개 중 8개를 커버하며, PRD·UI와의 전반적인 정합성은 양호한 편이다.  
**AC-05(마감 시간 설정) 누락, 랭킹 기준 불일치, 형제자매 관계 처리** 3개가 현재 가장 시급한 결함이며,  
**운동 카테고리 필터 누락, 웨어러블 API 미정의**가 그 뒤를 따른다.  
PIPA 준수와 의료 면책 UI는 문서 전반에 일관되게 잘 반영되어 있다.
