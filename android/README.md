# WalkMate Android — Week 0 Scaffold

> **상태**: Week 1 시작 직전 스캐폴드. 빌드는 Android Studio에서 wrapper 자동 생성 후 가능.
> **타겟**: minSdk 26 / targetSdk 34 / Java 17 / Kotlin 2.0.20 / AGP 8.5.2
> **시그널**: 풀스택 실행력 (옵션 C) — `docs/SIGNAL.md` 참조

---

## 빠른 시작

### 1. Android Studio에서 열기 (권장)
```
File > Open > C:\Users\lsk12\walkmate-prep\android
```
- Studio가 Gradle Sync 시 wrapper jar 자동 생성
- 처음 sync는 Gradle 8.9 + AGP 8.5.2 + Kotlin 2.0.20 다운로드로 5-10분 소요
- 완료 후 ▶ Run 'app' 클릭 → 에뮬레이터/기기에 빈 BottomNav 3탭 화면 띄움

### 2. CLI에서 빌드 (gradle 글로벌 설치 시)
```bash
cd android
gradle wrapper --gradle-version 8.9   # gradlew jar 생성 (1회)
./gradlew assembleDebug                # APK 빌드
./gradlew installDebug                 # 연결된 기기에 설치
```

### 3. 디자인 시스템 시각 검증
```bash
adb shell am start -n com.walkmate.debug/com.walkmate.ui.design.ColorTokenPreviewActivity
```
→ Compose Preview로 28 M3 토큰 light/dark 시각 확인 (mapping-rationale.md §검증).

---

## 프로젝트 구조

```
android/
├── settings.gradle.kts                 # 모듈 + 저장소 (jitpack 포함)
├── build.gradle.kts                    # 루트 (plugin alias만)
├── gradle.properties                   # JVM args + AndroidX + KSP2
├── gradle/
│   ├── libs.versions.toml              # 버전 카탈로그 (단일 진실)
│   └── wrapper/gradle-wrapper.properties
├── .gitignore                          # build/, local.properties, .idea/, *.jks 등
└── app/
    ├── build.gradle.kts                # AGP/Compose/Hilt/KSP/Room 의존성
    ├── proguard-rules.pro              # Moshi DTO 보존 등
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml     # 권한 + Application + MainActivity + (services 주석)
        │   ├── java/com/walkmate/
        │   │   ├── WalkMateApplication.kt    # @HiltAndroidApp + DynamicColors X 주석
        │   │   ├── MainActivity.kt           # Single Activity + NavHostFragment
        │   │   └── ui/
        │   │       ├── home/HomeFragment.kt           (placeholder)
        │   │       ├── walk/WalkFragment.kt           (placeholder)
        │   │       ├── ranking/RankingFragment.kt     (placeholder)
        │   │       ├── design/ColorTokenPreviewActivity.kt  (M3 28토큰 검증)
        │   │       └── theme/{Color,Theme,Type}.kt    (Compose 평행 정의)
        │   └── res/
        │       ├── values/{colors,themes,shapes,type,strings}.xml
        │       ├── values-night/{colors,themes}.xml
        │       ├── font/pretendard.xml      (Downloadable)
        │       ├── layout/{activity_main, fragment_*}.xml
        │       ├── menu/bottom_nav.xml
        │       ├── navigation/nav_graph.xml
        │       ├── drawable/{ic_home,ic_walk,ic_ranking,ic_walkmate_logo}.xml
        │       └── xml/{backup_rules,data_extraction_rules}.xml
        ├── test/java/com/walkmate/                # ExampleUnitTest (Week 2-7 확장)
        └── androidTest/java/com/walkmate/         (placeholder)
```

---

## 적용된 디자인 시스템

`design-system/android-resources/` 의 8개 리소스 파일을 그대로 복사 (source of truth는 design-system/).

| 토큰 종류 | 위치 | 개수 |
|---|---|---|
| Material 3 표준 (light/dark) | `values/colors.xml`, `values-night/colors.xml` | 28 × 2 |
| WalkMate 도메인 커스텀 (light/dark) | 동일 파일 | 23 × 2 |
| 컴포넌트 스타일 (Button/Card/Hero/BottomNav/Sheet/Pill) | `values/themes.xml` | 7 |
| ShapeAppearance (12/16/24dp + Pill + BottomSheet) | `values/shapes.xml` | 5 |
| Typography (Pretendard 12레벨) | `values/type.xml` | 12 |

WCAG 검증 결과 + 결정 근거: `../design-system/audit-report.md` + `../design-system/mapping-rationale.md`.

---

## Week별 활성화 계획

| Week | 추가 활성화 | 신규 클래스/리소스 |
|---|---|---|
| **Week 0 (현재)** | 빈 스캐폴드 | 위 구조 그대로 |
| Week 1 | Hilt 모듈 + ViewModel 베이스 | `di/AppModule.kt`, `presentation/common/BaseFragment.kt` |
| Week 2 | StepCounterService **활성화** | `service/StepCounterService.kt`, manifest service 주석 해제, Notification Channel |
| Week 3 | Room + 통계 | `data/local/{DailyStepEntity,DailyStepDao,WalkMateDatabase}.kt`, MPAndroidChart binding |
| Week 4 | LocationService + Maps | `service/LocationService.kt`, Maps Fragment, Polyline |
| Week 5 | Health Connect 통합 | `data/sensor/StepSourceResolver.kt`, `xml/health_permissions.xml`, activity-alias 주석 해제 |
| Week 6 | Retrofit + 서버 동기화 | `data/remote/{ApiService,AuthInterceptor,Authenticator}.kt`, WorkManager `SyncStepsWorker.kt` |
| Week 7 | 그룹 + Top 10 랭킹 | `presentation/ranking/`, `data/remote/dto/Ranking*.kt` |
| Week 8 | Material 마감 + Compose 1화면 | `presentation/compose/HomeStatsCard.kt`, ProGuard 검증 |
| Week 9 | README/스크린샷/면접 prep | `docs/` 콘텐츠 |
| Week 10 | 버퍼 + Play Store | (스트레치) Baseline Profile |

---

## 핵심 결정 (이 스캐폴드에 박제됨)

1. **DynamicColors 미사용** — `WalkMateApplication.onCreate`에 의도 주석. 트리거 시 재검토 조건 명시.
2. **Single Activity + Navigation Component** — `MainActivity` + `nav_graph.xml`. BottomNav는 `setupWithNavController`로 자동 동기화.
3. **Hilt + KSP** — kapt 대신 KSP (빌드 ~30% 빠름).
4. **ViewBinding + Compose 공존** — XML 메인 (Week 1-7) + Compose 1화면 (Week 8). `buildFeatures { viewBinding = true; compose = true }`.
5. **Foreground Service Type 사전 선언** — manifest에 `FOREGROUND_SERVICE_HEALTH/LOCATION` 권한만 추가 (Service 클래스는 Week 2+).
6. **Backup 비활성화** — `allowBackup=false` + 두 xml 규칙으로 헬스 데이터 외부 노출 차단.
7. **Debug 빌드 패키지명 분리** — `com.walkmate.debug` (NCP Maps 콘솔에 별도 등록 필요 — `design-system/android-resources/values/themes.xml` 참조).

---

## 알려진 한계 / TODO

- **Pretendard 폰트**: `font/pretendard.xml` Downloadable 설정. Google Fonts 미등재일 경우 ttf 번들 옵션으로 전환 (font/pretendard.xml 주석 참조).
- **gradle-wrapper.jar**: 버전 관리 X (Android Studio 또는 `gradle wrapper` 명령으로 생성).
- **mipmap launcher icon**: `ic_walkmate_logo.xml` placeholder 1개. Image Asset Studio로 적응형 아이콘 생성 권장.
- **health_permissions.xml**: Week 5 Health Connect 활성화 시 추가.
- **Hilt AppModule**: Week 1에 `@Provides` 메서드 작성 (Retrofit/OkHttp/Room 등).
- **GitHub Actions CI**: `.github/workflows/android.yml` 별도 셋업 필요 (P2 Next Steps).

---

## Week 1 시작 체크리스트

- [ ] Android Studio Hedgehog (2023.1) 또는 Iguana (2023.2) 이상 설치
- [ ] JDK 17 설정 확인 (File > Project Structure > SDK Location)
- [ ] Open 후 Gradle Sync 성공 (5-10분)
- [ ] 에뮬레이터 (API 34, Pixel 7) 또는 실기기 (Android 8+) 연결
- [ ] ▶ Run 'app' → 빈 화면 + BottomNav 3탭 표시 확인
- [ ] Light/Dark 모드 토글 → emerald primary 시각 확인
- [ ] ColorTokenPreviewActivity 진입 → 20+ 토큰 시각 검증
- [ ] GitHub Actions CI 셋업 (별도)
- [ ] 첫 ADR 갱신 — Week 1 결정 사항 추가

---

*v0.1.0-prep · Week 0 종료 시점 스냅샷. Week 1 진행 시 이 README의 체크리스트와 Week 1 행을 갱신.*
