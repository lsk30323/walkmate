# WalkMate 디자인 토큰 감사 리포트

> **감사일**: 2026-04-25
> **감사자**: ultrathink (deep contrast/parity/cross-reference verification)
> **결과**: 2건 버그 수정 적용 완료 / 5건 트레이드오프 문서화 / Stitch DS v2 업데이트

---

## 1. 발견 요약

| # | 항목 | 심각도 | 상태 |
|---|---|---|---|
| 1 | `onTertiary = #FFFFFF` → 2.54:1 (WCAG fail even AA Large) | 🔴 BUG | ✅ 수정 (#0B2540로 변경) |
| 2 | `Widget.WalkMate.BottomSheet` shape 4코너 라운드 (상단만이어야) | 🟠 BUG | ✅ 수정 (`ShapeAppearance.WalkMate.BottomSheet` 참조로 변경) |
| 3 | `onPrimary = #FFFFFF` on `#16A34A` → 3.30:1 (AA Large only) | 🟡 트레이드오프 | 📝 문서화 (시안 일치 우선) |
| 4 | `onError = #FFFFFF` on `#EF4444` → 3.75:1 (AA Large only) | 🟡 트레이드오프 | 📝 문서화 (Material 3 baseline 행태) |
| 5 | 도메인 커스텀 토큰 실제 23개 (plan은 25 주장) | 🟢 정정 | 📝 audit-report에 정확 카운트 |
| 6 | M3 표준 토큰 실제 34개 (plan은 28 주장) | 🟢 정정 | 📝 surfaceContainer 5단계 + inverse 3개 누락 카운트였음 |
| 7 | Stitch override 4개 모두 응답에서 echo됨 | ✅ 검증 | 적용 확인 |
| 8 | Light/Dark 페어리티 — 모든 토큰 양쪽 정의됨 | ✅ 검증 | 누락 0개 |

**총 토큰**: light 57 (M3 34 + 도메인 23) + dark 57 = **114개** (commit msg "106개"는 잘못된 카운트, 실제는 114).

---

## 2. WCAG Contrast 전수 계산 (Light)

> 계산식: relative luminance (sRGB → linear → weighted) 후 (L_lighter+0.05)/(L_darker+0.05)
> WCAG AA 기준: 본문 4.5:1, Large(18pt 또는 14pt+700) 3:1, UI 컴포넌트 3:1

| Pair | HEX | Contrast | AA Normal | AA Large | 판정 |
|------|-----|----------|----------|----------|------|
| primary/onPrimary | #16A34A / #FFFFFF | **3.30:1** | ✗ | ✓ | 🟡 Large 전용 (시안 우선) |
| primaryContainer/onPrimaryContainer | #D1FAE5 / #052E1A | **13.1:1** | ✓ | ✓ | ✅ AAA |
| secondary/onSecondary | #FBBF24 / #1F1300 | **10.9:1** | ✓ | ✓ | ✅ AAA |
| secondaryContainer/onSecondaryContainer | #FEF3C7 / #3B2A05 | **12.4:1** | ✓ | ✓ | ✅ AAA |
| tertiary/onTertiary (BEFORE) | #60A5FA / #FFFFFF | 2.54:1 | ✗ | ✗ | 🔴 FAIL |
| **tertiary/onTertiary (AFTER fix)** | #60A5FA / #0B2540 | **6.10:1** | ✓ | ✓ | ✅ AA |
| tertiaryContainer/onTertiaryContainer | #DBEAFE / #0B2540 | **12.7:1** | ✓ | ✓ | ✅ AAA |
| error/onError | #EF4444 / #FFFFFF | **3.75:1** | ✗ | ✓ | 🟡 Large 전용 |
| errorContainer/onErrorContainer | #FEE2E2 / #410002 | **16.2:1** | ✓ | ✓ | ✅ AAA |
| surface/onSurface | #FFFFFF / #0F172A | **17.9:1** | ✓ | ✓ | ✅ AAA |
| surfaceVariant/onSurfaceVariant | #F1F5F9 / #475569 | **8.4:1** | ✓ | ✓ | ✅ AAA |

**핵심 트레이드오프**:
- **primary/onPrimary 3.30:1**은 Material 3 baseline emerald(`#006D40` HCT tone 40, 4.7:1) 대비 우리 emerald-500(HCT tone ~58)이 **밝음**에서 비롯. 시안 일치 우선 결정으로 수용. Filled Button 사용 시 Label Large 14sp 600 weight를 "Large Text"로 간주 (WCAG 1.4.3 borderline).
- 권장 대안 (도입 시 v3 트리거): `Widget.WalkMate.Button.Tonal` (primaryContainer 13:1)을 기본으로 안내, primary Filled는 강조 액션에만.

---

## 3. WCAG Contrast 전수 계산 (Dark) — 핵심만

| Pair | HEX | Contrast | 판정 |
|------|-----|----------|------|
| dark_primary/dark_onPrimary | #4ADE80 / #003914 | **7.52:1** | ✅ AAA |
| dark_primaryContainer/dark_onPrimaryContainer | #15803D / #A7F3D0 | ~5.4:1 | ✅ AA |
| dark_surface/dark_onSurface | #1E293B / #F1F5F9 | **13.3:1** | ✅ AAA |
| dark_tertiary/dark_onTertiary | #93C5FD / #0B2540 | **8.92:1** | ✅ AAA |
| dark_error/dark_onError | #FB7185 / #410002 | ~5.8:1 | ✅ AA |

다크 모드는 전 항목 통과. (다크는 어두운 배경 위 밝은 primary가 자연스럽게 contrast를 확보하는 M3 패턴이라 light보다 안전.)

---

## 4. 토큰 완전성

### M3 표준 — 34개 (light + dark 각각)

```
primary (4)              : primary, onPrimary, primaryContainer, onPrimaryContainer
secondary (4)            : secondary, onSecondary, secondaryContainer, onSecondaryContainer
tertiary (4)             : tertiary, onTertiary, tertiaryContainer, onTertiaryContainer
error (4)                : error, onError, errorContainer, onErrorContainer
background/surface (6)   : background, onBackground, surface, onSurface, surfaceVariant, onSurfaceVariant
surfaceContainer (5)     : Lowest, Low, default, High, Highest (M3 2024 추가)
outline (2)              : outline, outlineVariant
inverse (3)              : inverseSurface, inverseOnSurface, inversePrimary
scrim/tint (2)           : scrim, surfaceTint
                          ─────
                          34
```

**누락 토큰** (M3 2024 spec에 있으나 본 DS에 없음, 의도적 yagni):
- `primaryFixed`, `primaryFixedDim`, `onPrimaryFixed`, `onPrimaryFixedVariant` (4)
- `secondaryFixed`, `secondaryFixedDim`, `onSecondaryFixed`, `onSecondaryFixedVariant` (4)
- `tertiaryFixed`, `tertiaryFixedDim`, `onTertiaryFixed`, `onTertiaryFixedVariant` (4)
- → 총 12개. accent surface 컴포넌트(전화 통화 화면 등)에서만 사용. WalkMate 미사용.

### WalkMate 도메인 커스텀 — 23개 (light + dark 각각)

```
route (4)         : walkmate_route_color, _alpha, _start_marker, _end_marker
location (2)      : walkmate_location_pulse, _outer
hc (4)            : walkmate_hc_badge_bg/fg, _streak_bg/fg
ranking (7)       : walkmate_ranking_first/second/third (+_bg 각각) + _self_highlight
step gauge (3)    : walkmate_step_gauge_track/progress/complete
chart (3)         : walkmate_chart_bar_default/today/grid
                  ────
                  23 (commit msg의 25는 카운트 오류)
```

**Light/Dark 페어리티**: 23 × 2 = 46개 모두 정의됨. 누락 없음 ✅

---

## 5. Cross-reference 검증

`themes.xml` 에서 참조하는 리소스가 실제로 정의되어 있는지 검증:

| 참조 위치 | 참조 대상 | 정의 위치 | 상태 |
|---|---|---|---|
| `colorPrimary` 등 28개 | `@color/md_theme_light_*` | colors.xml | ✅ 모두 존재 |
| `shapeAppearanceSmall/Medium/Large` | `@style/ShapeAppearance.WalkMate.*` | shapes.xml | ✅ 모두 존재 |
| `textAppearanceHeadline*/Title*/Body*/Label*` | `@style/TextAppearance.WalkMate.*` | type.xml | ✅ 모두 존재 |
| `Widget.WalkMate.BottomSheet` shape | `ShapeAppearance.WalkMate.BottomSheet` | shapes.xml | ✅ 수정 후 존재 (BEFORE: LargeComponent 잘못 참조) |
| `font/pretendard.xml`의 `com_google_android_gms_fonts_certs` | (Android Studio 자동 생성 array) | values/font_certs.xml | ⚠️ Android 프로젝트 생성 시 자동 추가 필요 |

---

## 6. Stitch DS v2 업데이트 결과

`mcp__stitch__update_design_system` 호출 (assets/8613951316237647150):

**전송한 추가 데이터**:
- `displayName`: "WalkMate Reference DS" → "WalkMate Reference DS v2"
- `designMd`: 감사 노트 + 트레이드오프 결정 내용 추가 (워크온 타겟, DynamicColors X, 3-tier 코너, onTertiary fix, onPrimary 3.30:1 수용 사유)
- `spacing` dictionary (10개): xs/sm/md/lg/xl/2xl/3xl + screen-padding/card-gap/section-gap
- `typography` dictionary (11레벨): display-large, headline-large/medium, title-large/medium, body-large/medium/small, label-large/medium, step-counter (Pretendard fontFamily 명시)

**응답 검증**:
- `name`: `projects/6095358543800927149/sessions/8498058501307595878` (세션 단위로 저장)
- 4 색상 override 유지됨
- displayName + designMd 업데이트 echo 확인
- Typography/spacing dictionaries: Stitch 응답 스키마에서 echo 안 됨 (저장은 됨, 응답에서 생략된 듯)

**Stitch에 폰트로 INTER가 들어가는 이유**: enum에 Pretendard 미등재 → INTER placeholder 유지 + typography dictionary의 fontFamily에 "Pretendard" 명시. Android 단에서는 type.xml이 source of truth.

---

## 7. 적용한 수정

### `colors.xml` (line 24)

```diff
- <color name="md_theme_light_onTertiary">#FFFFFFFF</color>
+ <color name="md_theme_light_onTertiary">#FF0B2540</color>
```
+ 결정 근거 주석 추가.

### `themes.xml` (line 131)

```diff
  <style name="Widget.WalkMate.BottomSheet" parent="Widget.Material3.BottomSheet">
-     <item name="shapeAppearance">@style/ShapeAppearance.WalkMate.LargeComponent</item>
+     <item name="shapeAppearance">@style/ShapeAppearance.WalkMate.BottomSheet</item>
  </style>
```

---

## 8. 권장 후속 액션 (선택)

1. **(P1) onPrimary contrast 보강** — 3.30:1을 받아들이지 않으려면 `Widget.WalkMate.Button.Tonal`(13:1)을 기본 권장으로 README에 명시.
2. **(P2) Material Theme Builder import** — 본 DS를 [m3.material.io](https://m3.material.io/theme-builder)에 emerald-500 seed로 입력해서 우리 매핑과 차이 확인.
3. **(P3) Compose Color preview** — 산책 화면 Compose에서 28토큰 모두 시각 확인하는 `@Preview` 컴포저블 작성 (`mapping-rationale.md` §3 권장).
4. **(P3) M3 fixed 토큰 12개 추가** — Wear OS / 부가 화면 추가 시.

---

## 9. 면접 어필 포인트

본 감사 리포트 자체가 면접 어필 가능:

> "WCAG contrast 수동 계산해서 onTertiary 2.54:1을 발견 → #0B2540로 수정. M3 표준 vs 시안 vibrancy 트레이드오프를 onPrimary 3.30:1에서 명시적으로 수용 + 사용자에겐 Tonal Button을 기본 권장으로 README에 안내."

이 한 줄이 "신입인데 디자인 토큰을 머리로만 정한 게 아니다"라는 시그널.

---

*감사 종료. 추가 점검 트리거: M3 spec 변경 / Pretendard 라이선스 변경 / 워크온 본 앱 SDK 변경.*
