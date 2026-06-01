---
version: "1.0"
name: "Family Health OS (FHOS)"
description: "가족 단위 헬스케어 플랫폼을 위한 따뜻하고 신뢰감 있는 디자인 시스템. 다세대 가족이 함께 사용하는 앱으로, 의료적 신뢰감과 게임화 요소를 균형 있게 결합한다."

colors:
  # Brand Core
  primary: "#1E8C6E"
  primary-light: "#34D399"
  primary-container: "#D1FAE5"
  on-primary: "#FFFFFF"
  on-primary-container: "#064E3B"

  secondary: "#3B82F6"
  secondary-container: "#DBEAFE"
  on-secondary: "#FFFFFF"
  on-secondary-container: "#1E3A8A"

  accent: "#F97316"
  accent-container: "#FED7AA"
  on-accent: "#FFFFFF"
  on-accent-container: "#7C2D12"

  family: "#8B5CF6"
  family-container: "#EDE9FE"
  on-family: "#FFFFFF"
  on-family-container: "#4C1D95"

  # Surfaces & Backgrounds
  background: "#F1F5F9"
  surface: "#FFFFFF"
  surface-elevated: "#F8FAFC"
  surface-overlay: "rgba(26, 31, 54, 0.5)"

  # Text
  on-surface: "#1A1F36"
  on-surface-muted: "#6B7280"
  on-surface-subtle: "#9CA3AF"
  on-background: "#1A1F36"

  # Borders
  border: "#E2E8F0"
  border-strong: "#CBD5E1"

  # Risk Score Spectrum (리스크 점수 시각화용)
  risk-low: "#22C55E"
  risk-low-bg: "#F0FDF4"
  risk-warning: "#F59E0B"
  risk-warning-bg: "#FFFBEB"
  risk-high: "#F97316"
  risk-high-bg: "#FFF7ED"
  risk-critical: "#EF4444"
  risk-critical-bg: "#FEF2F2"

  # Gamification
  streak: "#F59E0B"
  streak-bg: "#FEF9C3"
  badge-gold: "#F59E0B"
  badge-silver: "#94A3B8"
  badge-bronze: "#D97706"

  # Semantic
  success: "#22C55E"
  warning: "#F59E0B"
  error: "#EF4444"
  info: "#3B82F6"

typography:
  # Display — 홈 리스크 점수 히어로 숫자
  score-hero:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "4rem"
    fontWeight: "800"
    lineHeight: "1.0"
    letterSpacing: "-0.04em"

  # Headings
  h1:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "2rem"
    fontWeight: "700"
    lineHeight: "1.25"
    letterSpacing: "-0.02em"
  h2:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "1.5rem"
    fontWeight: "700"
    lineHeight: "1.3"
    letterSpacing: "-0.01em"
  h3:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "1.25rem"
    fontWeight: "600"
    lineHeight: "1.4"
    letterSpacing: "-0.01em"
  h4:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "1.0625rem"
    fontWeight: "600"
    lineHeight: "1.4"
    letterSpacing: "0"

  # Body
  body-lg:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "1.0625rem"
    fontWeight: "400"
    lineHeight: "1.6"
    letterSpacing: "0"
  body-md:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "0.9375rem"
    fontWeight: "400"
    lineHeight: "1.6"
    letterSpacing: "0"
  body-sm:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "0.8125rem"
    fontWeight: "400"
    lineHeight: "1.5"
    letterSpacing: "0"

  # Labels & UI
  label-lg:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "0.9375rem"
    fontWeight: "600"
    lineHeight: "1.4"
    letterSpacing: "0"
  label-md:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "0.8125rem"
    fontWeight: "600"
    lineHeight: "1.4"
    letterSpacing: "0.01em"
  label-sm:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "0.6875rem"
    fontWeight: "600"
    lineHeight: "1.4"
    letterSpacing: "0.02em"
  caption:
    fontFamily: "Pretendard, -apple-system, BlinkMacSystemFont, 'Noto Sans KR', sans-serif"
    fontSize: "0.6875rem"
    fontWeight: "400"
    lineHeight: "1.4"
    letterSpacing: "0"

rounded:
  xs: "4px"
  sm: "8px"
  md: "12px"
  lg: "16px"
  xl: "24px"
  "2xl": "32px"
  full: "9999px"

spacing:
  "1": "4px"
  "2": "8px"
  "3": "12px"
  "4": "16px"
  "5": "20px"
  "6": "24px"
  "8": "32px"
  "10": "40px"
  "12": "48px"
  "16": "64px"
  page-horizontal: "20px"
  page-horizontal-lg: "24px"

components:
  # ─── Buttons ────────────────────────────────────
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
    padding: "14px 24px"
    typography: "{typography.label-lg}"
  button-primary-hover:
    backgroundColor: "#166F57"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.md}"
  button-primary-disabled:
    backgroundColor: "#A7D7CA"
    textColor: "#FFFFFF"
    rounded: "{rounded.md}"

  button-secondary:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.primary}"
    rounded: "{rounded.md}"
    padding: "14px 24px"
    typography: "{typography.label-lg}"

  button-accent:
    backgroundColor: "{colors.accent}"
    textColor: "{colors.on-accent}"
    rounded: "{rounded.md}"
    padding: "14px 24px"
    typography: "{typography.label-lg}"

  button-ghost:
    backgroundColor: "transparent"
    textColor: "{colors.primary}"
    rounded: "{rounded.md}"
    padding: "14px 24px"
    typography: "{typography.label-lg}"

  button-destructive:
    backgroundColor: "{colors.error}"
    textColor: "#FFFFFF"
    rounded: "{rounded.md}"
    padding: "14px 24px"
    typography: "{typography.label-lg}"

  # ─── Cards ────────────────────────────────────
  card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.lg}"
    padding: "16px"

  card-elevated:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.lg}"
    padding: "20px"

  card-family-member:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.xl}"
    padding: "16px"

  card-challenge:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.xl}"
    padding: "20px"

  card-challenge-completed:
    backgroundColor: "{colors.primary-container}"
    rounded: "{rounded.xl}"
    padding: "20px"

  card-onboarding-option:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    rounded: "{rounded.lg}"
    padding: "16px"

  card-onboarding-option-selected:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.primary}"
    rounded: "{rounded.lg}"
    padding: "16px"

  # ─── Risk Badges ────────────────────────────────
  risk-badge-low:
    backgroundColor: "{colors.risk-low-bg}"
    textColor: "{colors.risk-low}"
    rounded: "{rounded.full}"
    padding: "4px 12px"
    typography: "{typography.label-sm}"

  risk-badge-warning:
    backgroundColor: "{colors.risk-warning-bg}"
    textColor: "{colors.risk-warning}"
    rounded: "{rounded.full}"
    padding: "4px 12px"
    typography: "{typography.label-sm}"

  risk-badge-high:
    backgroundColor: "{colors.risk-high-bg}"
    textColor: "{colors.risk-high}"
    rounded: "{rounded.full}"
    padding: "4px 12px"
    typography: "{typography.label-sm}"

  risk-badge-critical:
    backgroundColor: "{colors.risk-critical-bg}"
    textColor: "{colors.risk-critical}"
    rounded: "{rounded.full}"
    padding: "4px 12px"
    typography: "{typography.label-sm}"

  # ─── Gamification ────────────────────────────────
  streak-chip:
    backgroundColor: "{colors.streak-bg}"
    textColor: "{colors.streak}"
    rounded: "{rounded.full}"
    padding: "4px 12px"
    typography: "{typography.label-md}"

  badge-card:
    backgroundColor: "{colors.surface-elevated}"
    rounded: "{rounded.xl}"
    padding: "16px"

  progress-bar:
    backgroundColor: "{colors.border}"
    height: "8px"
    rounded: "{rounded.full}"

  progress-bar-fill:
    backgroundColor: "{colors.primary}"
    height: "8px"
    rounded: "{rounded.full}"

  # ─── Avatars ────────────────────────────────────
  avatar-self:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.primary}"
    rounded: "{rounded.full}"
    size: "40px"

  avatar-parent:
    backgroundColor: "{colors.secondary-container}"
    textColor: "{colors.secondary}"
    rounded: "{rounded.full}"
    size: "40px"

  avatar-child:
    backgroundColor: "{colors.accent-container}"
    textColor: "{colors.accent}"
    rounded: "{rounded.full}"
    size: "40px"

  avatar-spouse:
    backgroundColor: "{colors.family-container}"
    textColor: "{colors.family}"
    rounded: "{rounded.full}"
    size: "40px"

  # ─── Navigation ────────────────────────────────
  bottom-tab:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface-muted}"
    height: "56px"

  bottom-tab-active:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.primary}"
    height: "56px"

  # ─── Form Elements ────────────────────────────────
  input-field:
    backgroundColor: "{colors.background}"
    textColor: "{colors.on-surface}"
    rounded: "{rounded.md}"
    padding: "14px 16px"
    typography: "{typography.body-md}"

  input-field-focused:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    rounded: "{rounded.md}"
    padding: "14px 16px"

  # ─── Disclaimer (의료 면책 문구) ────────────────
  disclaimer-banner:
    backgroundColor: "#FFF7ED"
    textColor: "#92400E"
    rounded: "{rounded.md}"
    padding: "12px 16px"
    typography: "{typography.body-sm}"
---

## Overview

**Family Health OS (FHOS)** — 가족을 하나의 건강 운영 단위로 연결하는 예방 헬스케어 플랫폼.

스타일 방향: **Soft UI Evolution** + **Inclusive Design**의 결합.

헬스케어 앱의 신뢰감과 명확성을 기반으로, 가족 단위 게임화(배지·스트리크·랭킹)의 따뜻한 에너지를 더한다. 청소년부터 중장년까지 다세대가 함께 쓰는 앱이므로 가독성·접근성을 최우선에 둔다.

**브랜드 퍼소나**
- 신뢰할 수 있는 가족 주치의
- 함께 뛰는 팀 코치
- 차갑지 않고, 너무 캐주얼하지도 않은 균형

---

## Colors

팔레트는 "건강의 초록"을 중심으로 설계되었다.

- **Primary (#1E8C6E):** 포레스트 틸. 건강·성장·신뢰의 컬러. 핵심 CTA, 완료 상태, 리스크 개선 표시에 사용한다.
- **Secondary (#3B82F6):** 스카이 블루. 가족 연결·소통의 컬러. 초대, 가족 공유 기능 등 소셜 맥락에 쓴다.
- **Accent (#F97316):** 웜 오렌지. 즉각적인 행동 유도. 체크인 완료, 챌린지 시작 버튼 등 에너지가 필요한 곳에만 제한적으로 사용한다.
- **Family (#8B5CF6):** 라벤더 퍼플. 가족 그룹·공동 배지 전용 컬러. 개인 컬러와 명확히 구분한다.
- **Background (#F1F5F9):** 슬레이트 화이트. 차갑지 않은 앱 배경.
- **Surface (#FFFFFF):** 카드·모달 표면. 배경과 대비로 깊이를 만든다.

**리스크 점수 스펙트럼**
0–30 낮음 → `risk-low` #22C55E  
31–60 주의 → `risk-warning` #F59E0B  
61–80 위험 → `risk-high` #F97316  
81–100 고위험 → `risk-critical` #EF4444

점수가 낮을수록(건강 개선) 초록, 높을수록 빨강. 리스크 개선 = 점수 하락이라는 서비스 로직을 색상으로 명확히 전달한다.

---

## Typography

**Pretendard**를 주력 서체로 채택. 한국어와 영문 모두 최적의 가독성을 제공하며, 모든 기기에서 가변 폰트로 동작한다. 시스템 폰트(-apple-system, BlinkMacSystemFont)를 폴백으로 두어 로딩 전에도 레이아웃이 유지된다.

**폰트 계층**
- `score-hero` (4rem/800): 홈 화면 리스크 점수 단독 사용. 숫자 하나가 가족의 건강 상태를 즉시 전달한다.
- `h1–h4`: 화면 제목, 섹션 타이틀. 항상 굵고 좁은 자간으로 임팩트를 준다.
- `body-md` (0.9375rem): 앱 내 기본 텍스트. 15px는 모바일에서 높은 가독성의 기준선이다.
- `label-*`: 버튼·배지·탭 레이블. 항상 semi-bold 이상.
- `caption`: 날짜·메타 정보·면책 문구 보조.

---

## Layout

**4px 기반 8pt 그리드 시스템**을 사용한다. 모든 컴포넌트의 크기와 여백은 4의 배수로 정의된다.

```
페이지 수평 패딩: 20px (phone) / 24px (tablet)
카드 간 수직 갭: 12px
섹션 간 갭: 24px
```

**화면 우선순위 (홈 화면)**
1. 가족 리스크 점수 (최상단, 가장 큰 시각 요소)
2. 오늘의 챌린지 진행 상황
3. 가족 구성원 체크인 현황
4. 배지·스트리크 요약

**모바일 레이아웃**
- Bottom Navigation: 탭 5개 이하 유지 (홈·챌린지·리포트·알림·마이)
- 스크롤: 세로 단방향. 수평 스크롤은 가족 멤버 목록에만 허용
- Safe Area: iOS notch / Android edge-to-edge 고려하여 bottom-tab 높이 56px + safe area inset

---

## Elevation & Depth

그림자를 통해 카드 계층을 명확히 한다. Soft UI Evolution 스타일답게 뚜렷한 드롭섀도보다 subtle한 방향으로.

```
elevation-0: 없음 (배경, 입력 필드)
elevation-1: box-shadow: 0 1px 3px rgba(0,0,0,0.08)  — 기본 카드
elevation-2: box-shadow: 0 4px 16px rgba(0,0,0,0.10) — 강조 카드, 홈 리스크 카드
elevation-3: box-shadow: 0 8px 32px rgba(0,0,0,0.12) — 모달, Bottom Sheet
```

---

## Shapes

둥근 모서리가 따뜻하고 접근하기 쉬운 느낌을 만든다.

- **`rounded.full` (pill):** 배지, 스트리크 칩, 리스크 뱃지, 아바타
- **`rounded.xl` (24px):** 가족 멤버 카드, 챌린지 카드, 홈 히어로 카드 — 앱의 핵심 컨텐츠 컨테이너
- **`rounded.lg` (16px):** 일반 카드, 모달 상단
- **`rounded.md` (12px):** 버튼, 입력 필드, 태그
- **`rounded.sm` (8px):** 소형 UI 요소, 체크박스

---

## Components

### 리스크 점수 카드 (Home Hero)

홈 화면 최상단에 위치. 가족 평균 점수를 `score-hero` 타이포로 표시하고, 리스크 등급에 따라 `risk-*` 컬러를 동적으로 적용한다.

```
[점수: 55] → risk-warning 색상 → "주의" 뱃지
[점수: 28] → risk-low 색상 → "낮음" 뱃지
[점수: 73] → risk-high 색상 → "위험" 뱃지
```

점수 링(원형 프로그레스)의 색상은 리스크 등급을 따른다. 점수 숫자 아래에는 "지난 주 대비 -3점 개선" 같은 delta 표시를 `label-sm` + `primary` 컬러로.

### 가족 멤버 카드

역할별 아바타 컬러를 사용해 한눈에 구분:
- self → primary (녹색 계열)
- parent → secondary (파란 계열)
- child → accent (주황 계열)
- spouse → family (보라 계열)

체크인 완료 시 카드 배경이 `primary-container`로 변경되며 체크마크 아이콘 표시.

### 챌린지 카드

체크인 방법 3가지(웨어러블·수동·사진)를 탭 또는 칩으로 선택. 완료 상태에서 카드는 `card-challenge-completed` 컴포넌트로 전환된다.

### 스트리크 & 배지

`streak-chip`: "🔥 7일 연속" 형태. amber 계열. 숫자가 증가할수록 이모지 크기 애니메이션.  
배지 카드: 미달성 시 grayscale + 50% opacity, 달성 시 풀컬러 + subtle glow.

### 온보딩 선택 카드

`card-onboarding-option` → 선택 시 `card-onboarding-option-selected`로 전환. 테두리 `primary` 색상 2px + 배경 `primary-container`. 체크 아이콘 우상단 배치.

### 면책 문구 배너

`disclaimer-banner`: 모든 AI 챌린지 추천 화면 하단에 필수 배치.  
텍스트: "이 서비스는 의료 진단이 아닌 건강 관리 서비스입니다."  
amber-100 배경 + amber-800 텍스트로 눈에 띄되 위협적이지 않게.

---

## Do's and Don'ts

### Do

- **리스크 점수는 숫자 하나로 즉시 이해되어야 한다.** 복잡한 차트보다 점수 + 등급 + delta가 먼저.
- **가족 구성원은 항상 역할 컬러로 구분한다.** 글자 없이도 누가 누군지 알 수 있게.
- **완료·성공 상태는 `primary` (녹색)으로 강하게 보상한다.** 건강 개선 = 초록은 직관적이다.
- **터치 타겟은 최소 44×44px.** 중장년 사용자를 고려한 넉넉한 터치 영역.
- **한국어 텍스트에 Pretendard를 사용한다.** 폴백으로 Noto Sans KR 지정.
- **모든 텍스트 대비는 WCAG AA 기준 4.5:1 이상.** 배경/텍스트 조합 사전 검증 필수.
- **면책 문구는 항상 AI 추천 UI에 함께 표시한다.** 위치: 카드 하단 또는 화면 최하단 fixed.
- **스트리크/배지 등 게임화 요소는 `streak`/`badge-*` 전용 컬러만 사용한다.** 핵심 UI 컬러와 혼용 금지.
- **`prefers-reduced-motion`을 반드시 지원한다.** 모션 민감 사용자를 위해 애니메이션 대신 즉시 상태 전환.

### Don't

- **의료·임상적 색상 조합 (흰 배경 + 파란 글자 + 빨간 경고) 금지.** 병원 UI처럼 보이면 앱의 브랜드가 무너진다.
- **네온·형광 컬러 사용 금지.** 건강 데이터에 신뢰감이 떨어진다.
- **리스크 점수 위에 광고·프로모션 배치 금지.** 홈 히어로 영역은 점수 전용.
- **LLM 추천 결과를 "진단"처럼 표현 금지.** "추천합니다" O / "당신의 질병은 X" X.
- **챌린지 완료 판정에 불확실한 상태를 시각화 금지.** 웨어러블 실패 시 이전 데이터 유지 (PRD AC-09).
- **미성년자 데이터 UI에 부모 동의 없이 개인 건강 데이터 노출 금지.**
- **다크 모드 우선 설계 금지.** 가족·건강 앱은 라이트 모드가 기본. 다크 모드는 Phase 2 이후.
- **AI purple/pink 그라디언트 남용 금지.** 헬스케어 앱에서 AI 느낌 과장은 신뢰를 낮춘다.
- **텍스트 입력을 온보딩 기본 UI로 사용 금지.** 카드 선택 > 슬라이더 > 텍스트 입력 순서 준수.
- **아이콘으로 이모지 사용 금지.** SVG 아이콘 라이브러리(Lucide, Heroicons) 사용.
