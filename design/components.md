# FHOS Component Library

> 모든 컴포넌트는 `DESIGN.md`의 토큰을 참조한다. 토큰 없이 임의 색상값 직접 사용 금지.

---

## 1. Buttons

### Button Primary

앱에서 가장 중요한 단일 CTA. 화면 당 1개만 허용.

```
┌─────────────────────────────┐
│        버튼 텍스트          │  bg: primary #1E8C6E
└─────────────────────────────┘  text: #FFFFFF
                                  radius: 12px
                                  padding: 14px 24px
                                  font: label-lg (15px/600)
                                  touch-target: 48px height
```

**상태:**
- `default`: primary bg
- `hover/pressed`: #166F57 (10% 어두운)
- `disabled`: #A7D7CA, 텍스트 흰색, cursor: not-allowed
- `loading`: 텍스트 숨김 + spinner (20px, 흰색)

**사용:**
- 온보딩 단계 완료 "다음"
- 챌린지 시작 "챌린지 시작하기"
- 체크인 최종 확인 "완료"

### Button Secondary (Outline)

Primary와 같은 화면의 보조 선택지.

```
┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
│        버튼 텍스트          │  bg: surface #FFFFFF
└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘  border: 1.5px primary
                                  text: primary
                                  radius: 12px
```

**사용:**
- "나중에", "건너뛰기"
- 초대 링크 공유 (Primary: 직접 초대 / Secondary: 링크 복사)

### Button Accent

긴급한 행동 유도. Primary가 없을 때만 사용.

```
┌─────────────────────────────┐
│        버튼 텍스트          │  bg: accent #F97316
└─────────────────────────────┘  text: #FFFFFF
```

**사용:**
- 마감 임박 챌린지 체크인 (시간 긴박감)
- 스트리크 연장 알림에서의 CTA

### Button Ghost

텍스트만 있는 최소 버튼.

```
   버튼 텍스트                   text: primary, no bg, no border
```

**사용:**
- "자세히 보기", "전체 보기" 등 보조 탐색
- 리스트 우측 "›" 버튼

### Button Destructive

되돌릴 수 없는 위험한 작업.

```
┌─────────────────────────────┐
│        버튼 텍스트          │  bg: #EF4444
└─────────────────────────────┘  text: #FFFFFF
```

**사용:**
- 가족 그룹 탈퇴
- 계정 삭제 (확인 다이얼로그 내부)

---

## 2. Cards

### Card (기본)

```
╔══════════════════════════════════╗
║                                  ║  bg: surface #FFFFFF
║  내용                            ║  radius: 16px
║                                  ║  padding: 16px
╚══════════════════════════════════╝  elevation-1
```

### Card Elevated (홈 히어로용)

```
╔══════════════════════════════════╗
║                                  ║  bg: surface #FFFFFF
║  강조 내용                       ║  radius: 16px
║                                  ║  padding: 20px
╚══════════════════════════════════╝  elevation-2
```

### Card Family Member

```
╔════════════════════════╗
║  [아바타]  이름        ║  radius: 24px, padding: 16px
║  역할      체크인 ✓    ║  bg: surface
╚════════════════════════╝
   완료 시: bg → primary-container
```

**상태:**
- 미완료: 흰 배경, 체크인 버튼 표시
- 완료: primary-container 배경, 체크 아이콘

### Card Challenge

```
╔══════════════════════════════════════╗
║  카테고리 칩    마감 시간            ║  radius: 24px
║                                      ║  padding: 20px
║  챌린지 제목                         ║
║  설명 텍스트                         ║
║                                      ║
║  ────────── 진행률 바 ──────────     ║
║  3/4명 완료                          ║
║                                      ║
║  ┌─────────────────────────────┐     ║
║  │        체크인하기           │     ║
║  └─────────────────────────────┘     ║
╚══════════════════════════════════════╝
```

완료 상태: 배경 → `primary-container`, 버튼 → "완료됨" (비활성)

### Card Onboarding Option

```
╔════════════════════════════════╗
║  [아이콘]  선택지 텍스트       ║  radius: 16px, padding: 16px
╚════════════════════════════════╝

선택 후:
╔════════════════════════════════╗
║  [아이콘]  선택지 텍스트  [✓]  ║  bg: primary-container
╚════════════════════════════════╝  border: 2px primary
```

---

## 3. Risk Score Components

### Score Ring (홈 히어로)

```
        ╭─────────────╮
       ╱               ╲
      │   [score-hero]  │   원형 프로그레스
      │       55        │   색상: risk-* 동적 적용
      │    [뱃지: 주의] │   내부: 점수 + 등급
       ╲               ╱
        ╰─────────────╯
         ▼ 지난주 대비 -3점 개선
```

- 점수에 따라 링 색상 자동 변경
- delta 표시: 개선 시 primary + "↓ n점 개선", 악화 시 risk-high + "↑ n점 상승"

### Risk Badge

```
기본형:
  ╭─────────────╮
  │  ● 낮음(28) │  pill shape, risk-low-bg + risk-low text
  ╰─────────────╯

4종: 낮음(초록) / 주의(노랑) / 위험(주황) / 고위험(빨강)
```

---

## 4. Gamification Components

### Streak Chip

```
╭──────────────────╮
│  🔥  7일 연속   │  bg: streak-bg #FEF9C3, text: streak #F59E0B
╰──────────────────╯  font: label-md
```

수치가 클수록 (30일 이상) 폰트 사이즈 +2px 증가 허용.

### Badge Card

```
미달성:
╔══════════╗
║  [아이콘] ║  grayscale filter: 100%, opacity: 50%
║  배지이름 ║  bg: surface-elevated
║  조건설명 ║
╚══════════╝

달성:
╔══════════╗
║  [아이콘] ║  풀 컬러, bg: surface-elevated
║  배지이름 ║  border: 2px badge-gold (gold 배지일 때)
║  달성일자 ║  elevation-1
╚══════════╝
```

**배지 종류별 컬러:**
- first_challenge: accent (주황)
- streak_7: streak (amber)
- streak_30: badge-gold (금색)
- risk_improved: primary (초록)
- family_complete: family (보라)

### Progress Bar

```
목표까지:
[████████░░░░░░░░░░░] 40%

bg: border #E2E8F0, height: 8px, radius: full
fill: primary, animated (width transition 300ms)
```

---

## 5. Navigation

### Bottom Tab Bar

```
┌────────────────────────────────────────────────────────┐
│                                                        │
│  [홈]      [챌린지]   [가족]    [리포트]   [마이]     │
│  ●활성                                                │
└────────────────────────────────────────────────────────┘
  height: 56px + safe-area-inset-bottom
  bg: surface, top border: 1px border
  active: primary text + primary icon
  inactive: on-surface-muted text + muted icon
```

### App Bar

```
┌────────────────────────────────────────────────────────┐
│  ← 뒤로   화면 제목                      [액션 버튼]  │
└────────────────────────────────────────────────────────┘
  height: 56px
  title: h3, text: on-surface
  back icon: 24px, on-surface-muted
```

### Progress Stepper (온보딩)

```
  ●───●───●───○───○───○───○───○
  1   2   3   4   5   6   7   8

완료: primary 채워진 원
현재: primary 테두리 + 흰 내부
미진행: border 색상 원
```

---

## 6. Form Elements

### Input Field

```
라벨 텍스트 (label-md, on-surface-muted)
┌─────────────────────────────────────┐
│  입력 텍스트                        │  bg: background #F1F5F9
└─────────────────────────────────────┘  radius: 12px, padding: 14px 16px

포커스:
┌─────────────────────────────────────┐
│  입력 텍스트 |                      │  border: 2px primary
└─────────────────────────────────────┘  bg: surface #FFFFFF

오류:
┌─────────────────────────────────────┐
│  잘못된 입력                        │  border: 2px #EF4444
└─────────────────────────────────────┘
  ⚠ 오류 메시지 (caption, error 컬러)
```

### Slider (온보딩 식습관/생활 리듬)

```
●────────────────────○─────────────

트랙: border 컬러, 채워진 부분: primary
핸들: 24px 원, surface + elevation-2 + primary border
```

### Time Picker (취침/기상 시간)

```
╔═══════════════════════════════╗
║  [  09  ]  :  [  30  ]  [AM] ║  Wheel-style
╚═══════════════════════════════╝
```

---

## 7. Feedback & States

### Toast Notification

```
╭──────────────────────────────────────────╮
│  ✓  챌린지가 완료되었습니다!            │  bg: on-surface, text: surface
│     7일 스트리크 달성!                  │  elevation-3, bottom: 80px
╰──────────────────────────────────────────╯
  auto-dismiss: 3초
```

### Empty State

```
     [아이콘 (48px, on-surface-subtle)]

  아직 챌린지가 없어요
  (body-md, on-surface-muted)

  첫 챌린지를 시작해볼까요?
  [Button Primary]
```

### Loading State

```
카드 스켈레톤:
╔══════════════════════════════════╗
║  ▓▓▓▓▓▓▓▓  ▓▓▓▓▓▓▓▓▓▓▓         ║  shimmer animation
║  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓         ║  bg: border 컬러
╚══════════════════════════════════╝
```

### Error State

```
╔══════════════════════════════════╗
║  ⚠  데이터를 불러오지 못했어요  ║  border: risk-warning-bg
║     [다시 시도]                  ║
╚══════════════════════════════════╝
```

---

## 8. Avatars

```
역할      컬러 조합                   크기
self      primary-container/primary   40px
parent    secondary-container/second. 40px
child     accent-container/accent     40px
spouse    family-container/family     40px

기본 (그룹 아이콘):
  bg: border 컬러, icon: on-surface-muted

아바타 그룹 (가족 멤버 겹침):
  ○○○○ +2   각 2px overlap, 테두리: surface 2px
```

---

## 9. Disclaimer Banner (필수)

AI 챌린지 추천이 있는 모든 화면에 반드시 포함.

```
╔══════════════════════════════════════════════════════════╗
║  ℹ  이 서비스는 의료 진단이 아닌 건강 관리             ║
║     서비스입니다.                                       ║
╚══════════════════════════════════════════════════════════╝
bg: #FFF7ED (amber-50), text: #92400E (amber-900)
radius: 12px, padding: 12px 16px
font: body-sm (13px/400)
icon: info-circle 16px
```

---

## 10. PIPA 동의 UI (미성년자)

```
╔══════════════════════════════════════════════════════╗
║  자녀 계정 개인정보 동의                             ║
║  ─────────────────────────────────────────────────  ║
║  ○ [개인정보 처리방침 전문 보기 →]                  ║
║                                                      ║
║  □ 개인정보 수집·이용에 동의합니다 (필수)           ║
║  □ 제3자 제공에 동의합니다 (필수)                   ║
║  □ 마케팅 활용에 동의합니다 (선택)                  ║
║                                                      ║
║  [─────────── 동의하고 계속하기 ───────────]        ║
╚══════════════════════════════════════════════════════╝

필수 체크박스 미완료 시 버튼: disabled 상태
동의 완료 시: pipa_consent_by 기록 → 서버 전송
```
