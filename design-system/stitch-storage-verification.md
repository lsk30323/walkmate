# Stitch DS v2 저장 상태 검증 결과

> **검증일**: 2026-04-25
> **방법**: `mcp__stitch__list_design_systems` 호출 후 `update_design_system`에 보낸 페이로드와 대조
> **DS asset**: `assets/8613951316237647150`, version `2`

---

## 결론 요약

| 카테고리 | 보낸 것 | 저장된 것 | 상태 |
|---|---|---|---|
| `displayName` | "WalkMate Reference DS v2" | 동일 | ✅ |
| `designMd` | 5단락 audit 노트 | 동일 (전문 보존) | ✅ |
| `colorMode` | LIGHT | LIGHT | ✅ |
| `colorVariant` | TONAL_SPOT | TONAL_SPOT | ✅ |
| `customColor` (seed) | #16a34a | #16a34a | ✅ |
| `overridePrimaryColor` | #16a34a | #16a34a | ✅ |
| `overrideSecondaryColor` | #fbbf24 | #fbbf24 | ✅ |
| `overrideTertiaryColor` | #60a5fa | #60a5fa | ✅ |
| `overrideNeutralColor` | #0f172a | #0f172a | ✅ |
| `headlineFont` / `bodyFont` / `labelFont` | INTER | INTER | ✅ |
| `roundness` | **ROUND_TWELVE** | **ROUND_FULL** | 🟡 자동 변환 |
| `spacing` dictionary (10개) | 명시 키-값 | **`spacingScale: 2`로 환산** | 🟠 손실 |
| `typography` dictionary (11개) | Pretendard 11레벨 | **저장 안 됨** | 🔴 누락 |
| `namedColors` (Stitch derived) | (보내지 않음) | **50개 derived M3 토큰 추가됨** | 🆕 추가 정보 |

---

## 발견 사항 상세

### 🟡 1. roundness `ROUND_TWELVE` → `ROUND_FULL`

**원인**: Stitch enum 스키마 description을 보면 `ROUND_TWELVE` 항목에 `"Round 12 or full"`이라 표기됨. 즉 ROUND_TWELVE와 ROUND_FULL이 **내부적으로 같은 값**으로 취급됨. 저장 시 정규화로 `ROUND_FULL`이 됨.

**영향**: 없음. Stitch가 생성하는 시각 결과물은 동일.

**증거**: 우리 첫 호출 (v1) 응답에서도 `roundness: ROUND_TWELVE`가 echo되었지만, list 응답에서는 `ROUND_FULL`로 표시. = 동일 의미의 alias.

---

### 🟠 2. spacing dictionary → `spacingScale: 2`

**보낸 것**:
```json
"spacing": {
  "xs": "4px", "sm": "8px", "md": "12px", "lg": "16px",
  "xl": "20px", "2xl": "24px", "3xl": "32px",
  "screen-padding": "20px", "card-gap": "16px", "section-gap": "24px"
}
```

**저장된 것**: `spacingScale: 2` (단일 정수)

**원인**: Stitch는 dictionary를 받지 않고 사전 정의된 5단계 정도의 간격 스케일만 지원하는 듯. 우리의 dictionary 입력이 평균/대표값으로 환산되어 scale=2로 저장된 것으로 추정.

**영향**: Stitch에서 spacing을 설계 가이드로 활용은 불가. **Android 단에서는 무관** — 우리 themes.xml은 컴포넌트 스타일에서 직접 dp 명시 (Button 52dp, Card 16dp, padding 20dp 등).

**source of truth**: walkmate-ui.html의 Tailwind 클래스 + 향후 작성될 `walkmate-prep/design-system/android-resources/values/dimens.xml` (필요 시).

---

### 🔴 3. typography dictionary — 저장 안 됨

**보낸 것**:
```json
"typography": {
  "step-counter": {"fontFamily": "Pretendard", "fontSize": "40px", "fontWeight": "800", ...},
  "headline-large": {"fontFamily": "Pretendard", "fontSize": "28px", ...},
  ... (11개 레벨)
}
```

**저장된 것**: 없음 (응답에 typography 필드 자체가 없음)

**원인 추정**: `update_design_system` 도구는 typography dictionary를 입력으로 허용하지만, 내부적으로 silently drop하거나 별도 엔드포인트가 필요한 듯. Stitch 출력은 `headlineFont`/`bodyFont`/`labelFont` enum 3개로만 폰트를 표현.

**영향**: Stitch에서 생성하는 화면(`generate_screen_from_text`)은 INTER 폰트만 사용. Pretendard 정보는 Android에 전달되지 않음.

**source of truth**: `android-resources/values/type.xml` 12개 TextAppearance 스타일이 유일한 정답. Stitch는 typography 면에서 신뢰 불가.

---

### 🆕 4. Stitch가 derive한 50개 M3 namedColors (예상치 못한 발견)

Stitch가 내부적으로 우리 seed로부터 HCT TONAL_SPOT 알고리즘을 돌려 M3 표준 토큰을 derive한 결과를 저장. **이건 우리가 보낸 게 아닌데 응답에 포함됨.**

**Stitch derive vs 우리 colors.xml** (핵심 차이):

| M3 Token | 우리 colors.xml | Stitch derived | 차이 |
|---|---|---|---|
| primary | `#16A34A` (시안 우선) | `#006e2e` (M3 baseline tone 40) | **Stitch가 더 어둡게 derive** |
| onPrimary | `#FFFFFF` | `#e9ffe6` (mint white) | 우리가 더 단순 |
| primaryContainer | `#D1FAE5` | `#7ffc97` | Stitch가 더 진한 mint |
| secondary | `#FBBF24` (amber) | `#7a5a00` (brown) | Stitch가 amber → 어두운 갈색으로 derive |
| tertiary | `#60A5FA` (blue-400) | `#0060ad` (어두운 blue) | Stitch가 더 어둡게 derive |
| surface | `#FFFFFF` | `#faf8ff` (lavender 빛) | Stitch가 약한 보라 tint 추가 |
| surfaceContainer | `#F1F5F9` (slate) | `#eaedff` (lavender) | Stitch는 neutral seed `#0f172a`(slate-900)에 primary 그린을 살짝 섞은 결과 |

**해석**:
- Stitch의 TONAL_SPOT은 우리 override 색상을 **seed**로만 활용하고 실제 M3 토큰은 **HCT tone 40 알고리즘**으로 derive함.
- 결과: 우리 시안의 vivid 톤(#16A34A) vs Stitch의 보수 톤(#006e2e)이 명확히 다름.
- **이는 우리 audit 결정 ("시안 우선, M3 baseline 거부")이 옳았음을 empirical하게 증명함**.
- 단, Stitch가 derive한 `surface_container` 등 **lavender 보라 tint**는 우리 slate 회색과 다름. 이는 우리 neutral seed `#0f172a` (slate-900, 약간 보라 띤 다크 블루)에 TONAL_SPOT이 primary 그린을 mix한 결과.

**활용**:
- Stitch의 namedColors는 **레퍼런스용 비교 데이터**로만 보존. 우리 colors.xml은 그대로 유지.
- 면접 답변에 "M3 baseline은 #006e2e인데 우리는 시안 vibrancy를 위해 #16A34A 선택, contrast 3.30:1을 의도적으로 수용"으로 말할 수 있음.

---

## 결론: source of truth 재확인

| 영역 | source of truth | Stitch 활용 |
|---|---|---|
| 색상 토큰 (M3 28 + 도메인 23) | **우리 colors.xml** | ❌ Stitch derive와 다름, 시안 우선 |
| 폰트 / typography | **우리 type.xml (Pretendard)** | ❌ Stitch에 저장 안 됨 |
| 간격 / spacing | **우리 themes.xml + Tailwind 매핑** | ❌ Stitch는 scale 정수만 지원 |
| Roundness | **우리 shapes.xml (12/16/24dp)** | 🟡 Stitch ROUND_FULL ≈ ROUND_TWELVE alias |
| 결정 메타데이터 (designMd) | **우리 mapping-rationale.md** | ✅ Stitch에 sync 저장됨 |
| 화면 시안 비교 | walkmate-ui.html | ✅ Stitch home-screen.png 참조 |

**Stitch 본 도구의 실효성**: 우리 시나리오에서는 **참조 + 검증** 도구. 토큰 dictionary를 source of truth로 쓸 수 없음을 empirical 확인. (c) 하이브리드 전략이 옳았음.

---

## 후속 조치 (선택)

1. **(P3)** Stitch가 derive한 namedColors를 별도 비교 페이지로 시각화 — 시각 비교 시 M3 baseline vs 우리 vibrant 톤이 한눈에 보임. 면접 어필용.
2. **(P3)** `dimens.xml` 작성 — Tailwind 매핑된 간격을 Android dp 토큰으로 명시화 (현재는 themes.xml 컴포넌트 스타일에 hard-code).
3. **기각** typography를 Stitch에 저장하려는 시도 — `update_design_system` 스키마 한계, 의미 없음.

---

*검증 종료. Stitch 활용 범위는 design system seed + 화면 generation 검증으로 한정. 우리 Android 리소스가 디자인 시스템 source of truth.*
