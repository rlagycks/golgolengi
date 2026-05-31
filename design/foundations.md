# FHOS Design Foundations

> DESIGN.md 토큰 파일의 시각적 해설판. 실제 토큰 값은 `DESIGN.md`가 정규 소스이며, 이 문서는 적용 이유와 규칙을 설명한다.

---

## 1. Color System

### 1.1 Brand Palette

```
Primary   #1E8C6E ████  포레스트 틸  — 건강·성공·핵심 CTA
          #34D399 ████  민트 라이트  — 호버, 진행 표시
          #D1FAE5 ████  민트 컨테이너 — 완료 카드 배경, 선택 상태

Secondary #3B82F6 ████  스카이 블루 — 가족 연결, 소셜 초대
          #DBEAFE ████  블루 컨테이너 — 부모 아바타 배경

Accent    #F97316 ████  웜 오렌지  — 체크인 CTA, 에너지 요소
          #FED7AA ████  피치 컨테이너 — 자녀 아바타 배경

Family    #8B5CF6 ████  라벤더    — 가족 공동 배지 전용
          #EDE9FE ████  라벤더 컨테이너 — 배우자 아바타 배경
```

**사용 규칙:**
- Primary는 앱 내 가장 중요한 1개 CTA 버튼에만 사용한다
- Accent는 Primary와 같은 화면에 동시에 버튼으로 쓰지 않는다 (주의 분산)
- Family 컬러는 가족 공동 달성 요소에만 한정한다

### 1.2 Semantic Palette (리스크 점수)

```
위험도 점수 시각화:

0─────────30────────60────────80────────100
  낮음(초록)  주의(노랑)  위험(주황)  고위험(빨강)

risk-low      #22C55E ████  점수 0–30
risk-warning  #F59E0B ████  점수 31–60
risk-high     #F97316 ████  점수 61–80
risk-critical #EF4444 ████  점수 81–100
```

리스크 점수가 낮을수록(건강이 좋을수록) 초록이 되는 역방향 직관을 UI에서 반드시 강조해야 한다. "55점 → 52점이 좋아진 것"임을 텍스트와 delta 화살표로 명확히 한다.

### 1.3 Surface Hierarchy

```
Layer 0 — Background   #F1F5F9  앱 배경 (슬레이트)
Layer 1 — Surface      #FFFFFF  카드, 모달, 시트
Layer 2 — Surface-El.  #F8FAFC  서브 배경, 배지 카드
```

배경이 카드보다 어두운 구조로 카드 내용이 "떠오르는" 느낌을 준다. 이것이 Soft UI Evolution 핵심.

### 1.4 Text Palette

```
on-surface         #1A1F36  본문 텍스트 (contrast 14.5:1 on white)
on-surface-muted   #6B7280  보조 텍스트 (contrast 5.9:1 — WCAG AA ✓)
on-surface-subtle  #9CA3AF  플레이스홀더, 비활성 (contrast 3.0:1 — 장식용만)
```

`on-surface-subtle`은 WCAG AA를 충족하지 못하므로, 정보 전달 텍스트(오류, 상태, 레이블)에 절대 사용하지 않는다. 장식적 플레이스홀더에만 허용.

---

## 2. Typography Scale

```
역할            토큰           크기     두께   용도
─────────────────────────────────────────────────────────────────
score-hero     score-hero     64px     800   홈 리스크 점수 (1곳만)
Title XL       h1             32px     700   화면 제목
Title L        h2             24px     700   섹션 제목
Title M        h3             20px     600   카드 제목
Title S        h4             17px     600   서브 타이틀
Body L         body-lg        17px     400   중요 본문
Body M         body-md        15px     400   기본 본문  ← 앱 기본값
Body S         body-sm        13px     400   설명, 메타
Label L        label-lg       15px     600   버튼 텍스트
Label M        label-md       13px     600   배지, 칩
Label S        label-sm       11px     600   상태 뱃지, 태그
Caption        caption        11px     400   타임스탬프, 면책문구 보조
```

**폰트 로딩 전략:**
```
font-family: 'Pretendard Variable', 'Pretendard', -apple-system,
             BlinkMacSystemFont, 'Noto Sans KR', sans-serif;
```
- Pretendard Variable: 가변 폰트 (최적화)
- -apple-system: iOS 시스템 폰트 폴백
- Noto Sans KR: Android 한국어 폴백

**행간 기준:**
- 헤딩류: line-height 1.2–1.4 (타이트)
- 본문류: line-height 1.5–1.6 (읽기 편함)
- 레이블/배지: line-height 1.4

---

## 3. Spacing & Grid

### 3.1 4px Base Grid

```
token  px   용도 예시
──────────────────────────────────────────
1      4px  아이콘 내부 패딩, 배지 세로 패딩
2      8px  인라인 요소 간격, 작은 갭
3      12px 배지 가로 패딩, 컴포넌트 내부 갭
4      16px 카드 패딩, 섹션 간 갭 (기본)
5      20px 페이지 수평 패딩 (모바일)
6      24px 섹션 사이 여백, 페이지 패딩 (태블릿)
8      32px 큰 섹션 간격
10     40px 화면 상단 여백
12     48px 화면 하단 safe area 여백
16     64px 히어로 영역 내부 여백
```

**화면별 수평 패딩:**
- Phone (< 428px): `spacing.5` = 20px
- Tablet (≥ 768px): `spacing.6` = 24px

### 3.2 Component Sizing

```
터치 타겟 최소값: 44 × 44px (Apple HIG 기준)
아이콘 크기: 20px (small), 24px (default), 28px (large)
아바타: 32px (list), 40px (card), 56px (profile)
Bottom Tab 높이: 56px + safe area
App Bar 높이: 56px
```

### 3.3 Column Grid

```
Mobile (360–428px): 4 columns, 20px margin, 12px gutter
Tablet (768–1024px): 8 columns, 24px margin, 16px gutter
```

---

## 4. Elevation

```
Level  값                                          용도
──────────────────────────────────────────────────────────────────
0      none                                       배경, 입력 필드
1      0 1px 3px rgba(0,0,0,0.08)                기본 카드
2      0 4px 16px rgba(0,0,0,0.10)               홈 히어로 카드, 강조
3      0 8px 32px rgba(0,0,0,0.12)               모달, Bottom Sheet
4      0 16px 48px rgba(0,0,0,0.16)              Floating Action Button
```

그림자는 항상 downward (아래로)만. upward 그림자는 사용하지 않는다.

---

## 5. Motion & Animation

```
Duration                      Easing
─────────────────────────────────────────────────────
즉시 피드백 (탭 반응): 150ms    ease-out
상태 전환 (카드 변환): 200ms    ease-in-out
화면 전환: 300ms               ease-in-out
완료 애니메이션 (배지): 400ms   spring(damping: 0.7)
```

**`prefers-reduced-motion` 대응:**
```css
@media (prefers-reduced-motion: reduce) {
  /* 모든 transition/animation duration을 0ms로 */
  *, *::before, *::after {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
```

이 규칙은 협상 불가. 건강 앱에서 모션 민감 사용자(편두통, 전정계 장애)를 고려해야 한다.

---

## 6. Icon System

**라이브러리:** Lucide Icons (기본), Heroicons (보조)  
**금지:** 이모지를 아이콘으로 사용. `🔥` → 불꽃 SVG 사용.

```
아이콘 크기:
- 탭 바: 24px
- 카드 내부: 20px
- 레이블 인라인: 16px
- 리스트 아이템: 20px

아이콘 색상:
- 활성/강조: {colors.primary}
- 비활성: {colors.on-surface-muted}
- 위험 경고: {colors.risk-critical}
- 성공: {colors.risk-low}
```

**FHOS 전용 아이콘 의미 체계:**
```
가족 그룹   → users 아이콘 (family 컬러)
리스크 점수 → shield 아이콘 (primary 컬러)
챌린지      → target / activity 아이콘 (accent 컬러)
스트리크    → flame 아이콘 (streak 컬러)
배지        → award 아이콘 (badge-gold/silver/bronze)
체크인      → check-circle 아이콘 (primary 컬러)
경고        → alert-triangle 아이콘 (risk-warning 컬러)
```

---

## 7. Accessibility Requirements

| 항목 | 기준 | 검증 방법 |
|------|------|----------|
| 텍스트 대비 | WCAG AA (4.5:1 이상) | design.md lint |
| 대형 텍스트(18px+) | WCAG AA (3:1 이상) | design.md lint |
| 터치 타겟 | 44×44px 이상 | 수동 측정 |
| 포커스 상태 | 2px outline, primary 컬러 | 키보드 탐색 테스트 |
| 색상만으로 정보 전달 금지 | 아이콘/텍스트 병용 | 설계 리뷰 |
| 동적 텍스트 크기 | iOS Dynamic Type 지원 | 기기 설정 테스트 |
| 스크린 리더 레이블 | 모든 아이콘에 accessibilityLabel | 자동화 테스트 |
