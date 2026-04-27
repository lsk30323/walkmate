# WalkMate 디자인 시스템 — 결정 근거 (면접 대비)

> 면접관이 "왜 이렇게 정했어요?"라고 물을 때 30초 내 답변 가능하도록 정리.
> Stitch MCP 활용 + 수동 매핑 하이브리드 결과.

---

## 1. 왜 emerald (#16A34A)를 primary로?

**핵심 답변** (30초):
> "한국 헬스케어 도메인 톤 조사 결과 워크온/삼성헬스/캐시워크가 모두 그린 계열 사용. 그중 emerald-500이 채도가 적절해 장시간 사용에도 피로가 적고, 자연 친화·신뢰감 메시지에 정합. blue 계열은 의료기기/딱딱한 느낌이 강해 일상 헬스케어엔 부적합."

**대안 비교**:
| 색 | 대안 | 거부 이유 |
|---|---|---|
| Material Blue | #1976D2 | 의료기기 느낌, 일상감 ↓ |
| Teal | #009688 | 헬스 OK지만 채도 과함 |
| Sage Green | #84A98C | 채도 부족, 활동성 ↓ |
| **Emerald 500** ⭐ | #16A34A | 채도/명도 균형, 워크온 톤 정합 |

---

## 2. 왜 amber를 secondary로? (랭킹 강조)

**핵심 답변**:
> "랭킹 1위는 시각적 보상이 핵심. 메달의 금색 = amber-400. emerald primary와 보색 관계라 명확히 구분되고, 헬스케어 앱에서 자주 쓰이는 'streak' (연속 달성) 배지에도 정합."

---

## 3. 왜 blue tertiary? (위치/GPS)

**핵심 답변**:
> "Google Maps Blue (#4285F4) 와 정합되는 톤. 사용자가 GPS 위치 표시를 직관적으로 인식. emerald(걷기 액션) vs blue(현재 위치) 의미 분리."

---

## 4. 왜 DynamicColors를 쓰지 않았나?

**핵심 답변** (가장 빈도 높은 면접 질문):
> "Material You의 DynamicColors는 사용자 월페이퍼에 따라 primary가 바뀌는 기능. 좋은 기능이지만 WalkMate는 **브랜드 정체성을 emerald 그린으로 강하게 묶는** 결정을 함. 사용자가 '걷기 = 그린' 메시지를 일관되게 인지하길 원해서. Application.onCreate에 의도 주석 명시했고 ADR-005에 기록."

**트레이드오프 인정**:
> "사용자 개인화 vs 브랜드 일관성 트레이드오프. 우리는 후자 선택. 만약 워크온이 사내 챌린지로 다양한 부서별 컬러 적용을 요구하면 DynamicColors 재검토 가능."

---

## 5. 왜 Material 3 colorVariant TONAL_SPOT?

**핵심 답변**:
> "Stitch에서 7가지 colorVariant 중 TONAL_SPOT 선택. VIBRANT는 채도가 과해 헬스케어 앱에 부적합. MONOCHROME은 emerald 시그니처가 사라짐. TONAL_SPOT이 워크온/삼성헬스 다크모드 톤과 가장 가깝게 derive됨."

---

## 6. 왜 28 + 25 토큰? (M3 표준 + 도메인 커스텀)

**핵심 답변**:
> "M3 표준 28토큰만으론 도메인 표현 한계. 예: Polyline 색상, HC 배지 색상, 랭킹 1/2/3위 색상은 표준 토큰에 매핑 어색함. 명시적 walkmate_ 접두사 25개로 도메인 의미 분리. 표준은 표준대로, 도메인은 도메인대로."

---

## 7. 왜 Pretendard? (폰트)

**핵심 답변**:
> "한국어 가독성이 헬스케어 앱 핵심. Roboto/Inter는 영문은 OK지만 한국어 자모 결합이 미려하지 못함. Pretendard Variable은 SIL OFL 라이선스 + 1개 ttf로 모든 weight 커버 → APK 크기 절약 + 한국어 디자인 표준."

**라이선스 답변** (옵션):
> "SIL Open Font License 1.1, 상용/재배포 OK. 단 폰트 자체를 별도 판매 금지. 라이브러리/앱 번들은 자유."

---

## 8. 왜 한국어 lineSpacingMultiplier 1.4~1.5?

**핵심 답변**:
> "Material 3 typography 기본값은 영문 기준. 한국어는 자모 결합으로 글자 높이가 영문 대비 1.2배 → 행간 좁으면 답답함. WCAG 2.1 SC 1.4.12 기준 한국어 본문은 line-height 1.5 권장. type.xml에서 BodyLarge/Medium 1.5 적용."

---

## 9. 왜 다크모드를 수동 정의?

**핵심 답변**:
> "Material 3 자동 톤 곡선이 다크 primary를 어떻게 derive할지 모델 의존적. 워크온/삼성헬스 다크 모드는 emerald-400 (#4ADE80) 근처가 표준이라 직접 정의가 안전. Stitch에 같은 seed로 dark colorMode 호출해 검증한 결과 약간 푸른 쪽으로 derive되어 수동 우선."

---

## 10. 왜 cornerRadius 12/16/24dp 3단계?

**핵심 답변**:
> "Tailwind 매핑 — rounded-xl=12dp (Button, Chip), rounded-2xl=16dp (Card 기본), rounded-3xl=24dp (Hero Card, Bottom Sheet). M3 ShapeAppearance Small/Medium/Large 토큰 그대로. 신입 포트폴리오에서 임의 hard-coded 값 대신 토큰 사용으로 일관성 시그널."

---

## 11. Stitch MCP를 어떻게 활용했나?

**핵심 답변** (면접 어필 가능):
> "Stitch MCP로 동일 seed `#16a34a` 입력 → Material 3 자동 derive 결과를 sanity check로 받음. 그러나 출력을 그대로 쓰지 않고 (한국어 폰트 미지원, override 검증 필요) 시안의 HEX를 source of truth로 유지. AI 도구 활용 + 자체 의사결정 균형."

---

## 12. 왜 27개 색이 아닌 28개? (surfaceTint 포함)

**핵심 답변**:
> "M3 2024 spec에 surfaceTint가 elevation overlay 컴포넌트 (Card 그림자)에 사용됨. = primary로 동일 색이지만 명시적 정의 필요. surfaceContainer 5단계 (Lowest/Low/Medium/High/Highest)도 M3 2024부터 정식 추가."

---

## 면접 시뮬레이션 — Quick Reference

| 질문 | 한 줄 답변 |
|---|---|
| 왜 emerald? | 워크온 톤 정합 + 채도 균형 |
| 왜 amber 랭킹? | 메달 골드, primary와 보색 |
| 왜 DynamicColors X? | 브랜드 일관성 우선, 의도 주석 명시 |
| 왜 TONAL_SPOT? | VIBRANT 과채도, MONOCHROME은 시그니처 사라짐 |
| 왜 28+25 토큰? | M3 표준 + 도메인 분리 |
| 왜 Pretendard? | 한국어 가독성 + 1ttf로 APK 절약 |
| 왜 line-height 1.5? | WCAG 한국어 권장 |
| 왜 다크 수동? | 자동 derive 비결정적 |
| 왜 12/16/24dp? | Tailwind 매핑 + M3 ShapeAppearance |
| Stitch 활용? | Sanity check, 시안 우선 |

---

## 디자인 시스템 변경 이력

- **2026-04-25**: 최초 생성. walkmate-ui.html → Android 리소스 변환. Stitch DS `assets/8613951316237647150`.
- **향후 v2 트리거**: 워크온 본 앱이 Naver Maps SDK로 변경 시 (ADR-006), Pretendard 가용성 변경 시.
