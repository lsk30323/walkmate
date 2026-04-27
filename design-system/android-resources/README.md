# WalkMate Android 리소스 — Material 3 디자인 시스템

> **출처**: `walkmate-prep/decision-support/walkmate-ui.html` (시안 source of truth) + Stitch MCP 참조 (assets/8613951316237647150)
> **생성일**: 2026-04-25
> **타임박스**: 2.5h 예산 내 완성

## 폴더 구조

```
android-resources/
├── values/
│   ├── colors.xml      ← M3 28토큰 (light) + 25 WalkMate 도메인 커스텀
│   ├── themes.xml      ← Theme.WalkMate (Material3.DayNight 베이스) + 컴포넌트 스타일
│   ├── shapes.xml      ← ShapeAppearance Small/Medium/Large + Pill + BottomSheet
│   └── type.xml        ← Pretendard Variable 12단계 TextAppearance
├── values-night/
│   ├── colors.xml      ← M3 28토큰 (dark) + 25 도메인 커스텀 다크 변종 (전부 재정의)
│   └── themes.xml      ← Light 미러, dark 토큰 참조 + windowLightStatusBar=false
└── font/
    └── pretendard.xml  ← Downloadable font (Google Fonts Provider 또는 ttf 번들 옵션 명시)
```

## 실제 Android 프로젝트 통합 (Week 1)

```
app/src/main/res/
├── values/                    ← 이 폴더의 values/* 4개 복사
├── values-night/              ← 이 폴더의 values-night/* 2개 복사
└── font/                      ← 이 폴더의 font/pretendard.xml 복사 (+ 옵션 B 시 ttf 추가)
```

**복사 명령** (Android 프로젝트 루트에서):
```bash
cp -r /c/Users/lsk12/walkmate-prep/design-system/android-resources/values app/src/main/res/
cp -r /c/Users/lsk12/walkmate-prep/design-system/android-resources/values-night app/src/main/res/
cp -r /c/Users/lsk12/walkmate-prep/design-system/android-resources/font app/src/main/res/
```

## AndroidManifest.xml 적용

```xml
<application
    android:theme="@style/Theme.WalkMate"
    ... >
```

**주의**: `Application.onCreate()`에서 **DynamicColors.applyToActivitiesIfAvailable()`를 호출하지 마세요**. 의도적으로 emerald 브랜드 정체성을 유지하려는 결정 (`mapping-rationale.md` 참조).

```kotlin
class WalkMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // DynamicColors intentionally disabled to preserve emerald brand identity.
        // See docs/adr/ADR-005 and design-system/mapping-rationale.md
    }
}
```

## build.gradle 의존성

```kotlin
dependencies {
    implementation("com.google.android.material:material:1.11.0")  // surfaceContainer 5단계 필요
    implementation("androidx.core:core-ktx:1.13.0")
    // ... Compose 1화면용 (ADR-005)
    implementation("androidx.compose.material3:material3:1.2.0")
}
```

## 검증 체크리스트

- [ ] Light 미리보기에서 walkmate-ui.html 시안과 색감 5% 이내 일치
- [ ] Dark 미리보기에서 emerald-400 primary가 가독성 4.5:1 이상
- [ ] BottomNavigation 활성 indicator가 primary로 표시
- [ ] HeroCard (24dp 라운드) + Card (16dp) + Button (12dp) 시각 계층 구분
- [ ] Pretendard 폰트 다운로드 후 한국어 행간 1.4~1.5 적용
- [ ] 25개 커스텀 토큰 전부 values/ + values-night/ 양쪽에 정의됨
- [ ] `?attr/colorPrimary` / `?attr/colorOnPrimaryContainer` 등 attr 참조가 컴포넌트 레이아웃에서 작동

## 매핑 근거

상세 의사결정과 면접 답변 가이드는 `../mapping-rationale.md` 참조.

## Stitch raw 응답

`../stitch-raw/` 폴더에 보관:
- `project.json` — 프로젝트 메타
- `design-system-response.json` — DS 생성 응답 (override 적용 검증)
- `home-screen-response.json` — 홈 화면 생성 응답 (M3 컨테이너 토큰 사용 패턴 학습용)
- `home-screen.png` — Stitch가 렌더링한 홈 시안 (190×512)
