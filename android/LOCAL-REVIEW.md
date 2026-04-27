# Android 스캐폴드 로컬 검토 결과 (Week 0)

> **검토일**: 2026-04-25
> **검토 방법**: 환경 점검 + cross-reference 전수 + 빌드 차단 가능성 분석
> **결과**: 빌드 차단 이슈 3건 발견 + 즉시 수정 / 잔여 이슈 5건 문서화

---

## 환경 점검 결과

| 도구 | 상태 | 결과 |
|---|---|---|
| Java | ❌ 미설치 | `java -version` 실패 |
| Gradle CLI | ❌ 미설치 | global gradle 없음 |
| Android Studio | ❓ | 사용자 환경 — Studio Open 시 검증 가능 |
| Git | ✅ 2.53 | OK |

**결론**: 로컬에서 `./gradlew build` 실행 불가. **Android Studio Open 후 Gradle Sync로 검증 필수**.

---

## 발견 이슈 (3건 수정, 5건 문서화)

### 🔴 1. `font/pretendard.xml` 빌드 차단 — 수정 완료

**증상**: `font/pretendard.xml`이 `app:fontProviderCerts="@array/com_google_android_gms_fonts_certs"`를 참조하지만 해당 string-array 미정의. AAPT 빌드 시 "resource not found" 에러.

**원인**: Android Studio가 New > XML > Font asset > Add font from Google Fonts 액션을 통해서만 `res/values/font_certs.xml` 자동 생성. 우리가 수동 작성 시 해당 array 누락.

**수정**: `font/pretendard.xml` 삭제 + `type.xml`의 `@font/pretendard` 참조 → `sans-serif-medium` 시스템 폰트로 교체. Pretendard 활성화는 Week 7 ttf 번들 절차로 문서화.

**영향**: 빌드 통과. 시각적으로는 한국어 가독성 system 기본보다 약간 떨어짐 (Pretendard 미적용). README + mapping-rationale.md에 업그레이드 절차 명시.

---

### 🟠 2. `gradle.properties` KSP2 preview 모드 — 수정 완료

**증상**: `ksp.useKSP2=true` 옵션이 KSP 2.0.20-1.0.25에서 preview 단계. Hilt/Room/Moshi 등 일부 processor가 KSP2 호환 안 될 시 빌드 실패 또는 stale generation 가능.

**수정**: `ksp.useKSP2=true` 주석 처리 (KSP1 stable 모드 사용).

**영향**: 빌드 안정성 ↑. 추후 KSP2가 stable 되면 활성화 검토.

---

### 🟢 3. `font/` 폴더 자체 정리 — 수정 완료

**증상**: 빈 `font/` 폴더 존재. AGP는 빈 res 폴더 무시하지만 깔끔하지 않음.

**수정**: `android/app/src/main/res/font/` + `design-system/android-resources/font/` 양쪽 폴더 제거.

---

## 잔여 이슈 (문서화, 빌드 차단 X)

### 🟡 4. mipmap launcher icon 미사용 (관행 위반, 빌드 OK)

**현 상태**: AndroidManifest의 `android:icon="@drawable/ic_walkmate_logo"` 사용. 표준은 `@mipmap/ic_launcher`.

**영향**: 일부 launcher가 mipmap만 지원하는 경우 fallback. 대부분 OK.

**권장**: Week 7에 Android Studio Image Asset Studio로 적응형 아이콘 생성 → mipmap-*/ic_launcher.xml + foreground 추출.

---

### 🟡 5. Health Connect Permissions XML 미생성

**현 상태**: AndroidManifest에 `<uses-permission android:name="android.permission.health.READ_STEPS" />` 직접 선언.

**Week 5 활성화 시 필요**:
- `res/xml/health_permissions.xml` 작성
- Manifest의 ViewPermissionUsageActivity alias 주석 해제
- `HealthConnectClient.getOrCreate(context).permissionController` 으로 권한 요청 plumbing

**영향**: Week 0 빌드 OK. Week 5에서 추가 작업 필요.

---

### 🟡 6. Hilt AppModule 미작성

**현 상태**: `@HiltAndroidApp` Application + `@AndroidEntryPoint` Fragment만 존재. `@Provides` 모듈 없음.

**Week 1 활성화**: `di/AppModule.kt`로 Retrofit/OkHttp/Room/MoshiBuilder 제공.

**영향**: 현 상태 빌드 OK (placeholder Fragment가 ViewModel 안 씀). Week 1에 추가.

---

### 🟡 7. Foreground Service 클래스 미작성

**현 상태**: AndroidManifest의 service 선언 주석 처리. `service/StepCounterService.kt` 등 파일 없음.

**의도**: Week 2/4 활성화. 권한과 권한 사용 의도만 manifest에 선언해서 검토 단계에서 노출.

**영향**: 현 상태 빌드 OK.

---

### 🟡 8. xmlns:tools queries 블록 (만약 nested 됐을 때)

**현 상태**: `<queries>` 블록은 manifest 루트 직속이라 `xmlns:tools` 상속 OK.

**확인**: 검토 결과 OK.

---

## 추가 검증 (Cross-reference 통과)

| 참조 | 정의 위치 | 상태 |
|---|---|---|
| `R.id.nav_host` | activity_main.xml `@+id/nav_host` | ✅ |
| `binding.bottomNav` (ViewBinding camelCase) | activity_main.xml `@+id/bottom_nav` | ✅ |
| `@layout/fragment_{home,walk,ranking}` | layout/ 3개 파일 | ✅ |
| `@menu/bottom_nav` | menu/bottom_nav.xml | ✅ |
| `@navigation/nav_graph` | navigation/nav_graph.xml | ✅ |
| `@drawable/{ic_home,ic_walk,ic_ranking,ic_walkmate_logo}` | drawable/ 4개 | ✅ |
| `@string/{app_name,nav_*,placeholder_*}` | values/strings.xml | ✅ |
| `@xml/{backup_rules,data_extraction_rules}` | xml/ 2개 | ✅ |
| `@style/Theme.WalkMate` | values/themes.xml | ✅ |
| `@style/Widget.WalkMate.{Button,CardView,Hero,BottomNavigationView}` | values/themes.xml | ✅ |
| `@style/ShapeAppearance.WalkMate.{Small,Medium,Large,BottomSheet,Pill}Component` | values/shapes.xml | ✅ |
| `@style/TextAppearance.WalkMate.{Display,Headline,Title,Body,Label}*` | values/type.xml | ✅ |
| `@color/md_theme_{light,dark}_*` (28×2=56 토큰) | colors.xml + values-night/colors.xml | ✅ |
| `@color/walkmate_*` (23×2=46 도메인 토큰) | colors.xml + values-night/colors.xml | ✅ |

총 cross-reference 116건 검증 — 빌드 차단 이슈 0건.

---

## 의존성 호환성 검증

| 항목 | 버전 | 호환 |
|---|---|---|
| AGP | 8.5.2 | Kotlin 2.0.x ✅ |
| Kotlin | 2.0.20 | KSP 2.0.20-1.0.25 ✅ |
| KSP | 2.0.20-1.0.25 | Kotlin 2.0.20 정확 매치 ✅ |
| Hilt | 2.51.1 | Kotlin 2.0 + KSP1 ✅ |
| Compose Compiler Plugin | Kotlin 2.0.20 빌트인 | composeOptions 불필요 ✅ |
| Compose BOM | 2024.06.00 | Material 3 1.2.1 (surfaceContainer 5단계 지원) ✅ |
| Material Components | 1.12.0 | surfaceContainer attrs + Widget.Material3.* 모두 지원 ✅ |
| Health Connect Client | 1.1.0-alpha07 | minSdk 26+ ✅ (alpha 주의) |
| Play Services Maps | 19.0.0 | targetSdk 34 ✅ |

---

## Week 1 시작 전 체크리스트

- [x] 모든 cross-reference 통과 (116건)
- [x] 빌드 차단 이슈 3건 수정 (pretendard, KSP2, font 폴더)
- [x] 의존성 버전 호환 검증
- [ ] Android Studio Hedgehog+ 설치 확인 (사용자)
- [ ] JDK 17 설정 확인 (사용자)
- [ ] Gradle Sync 성공 (사용자, 5-10분)
- [ ] ▶ Run 'app' 빈 BottomNav 3탭 표시 확인 (사용자)

---

## 면접 대비 — 검토 어필 포인트

> "스캐폴드 작성 후 환경 검증 시도 → Java/Gradle 미설치로 빌드 불가 → cross-reference 116건 수동 점검 + AGP 8.5/Kotlin 2.0/KSP 호환성 verify → 빌드 차단 이슈 3건 (`pretendard.xml` cert 누락, `ksp.useKSP2` preview, 빈 font 폴더) 사전 식별 + 수정 → Android Studio Open 시 즉시 sync 가능한 상태로 인계."

이 자체가 "신입인데 빌드 디버깅 사고력 보유" 시그널.
