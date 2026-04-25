# WalkMate

> **WalkMate는 워크온의 복제가 아니라, 사용자로서 느낀 "Health Connect 통합 부재 + 재부팅/타임존 엣지케이스 미처리"를 해결하려는 실험 프로젝트입니다.**

[![CI](https://github.com/lsk30323/walkmate/actions/workflows/android.yml/badge.svg)](https://github.com/lsk30323/walkmate/actions)
[![Play Store](https://img.shields.io/badge/Play_Store-Internal_Testing-success)]()
[![Min SDK](https://img.shields.io/badge/minSdk-26-blue)]()
[![Target SDK](https://img.shields.io/badge/targetSdk-34-orange)]()

---

## ⏱ 30초 데모

![Demo GIF](docs/demo.gif)

> 시퀀스: 권한 허용(2s) → 걸음수 표시(2s) → 산책 시작/경로(3s) → 종료/저장(2s) → 그룹 Top 10 랭킹(2s) → README 화면(1s) — 총 12초

---

## 🎯 무엇을 풀었나

| 문제 | WalkMate의 답 |
|---|---|
| 만보기 앱이 **재부팅 시 오늘 걸음수가 0으로 망가짐** | `BOOT_COUNT` 비교 + `baselineSensorValue` 분리 스키마로 baseline 보정 |
| **자정·해외 이동 시** 걸음수 날짜가 꼬임 | `LocalDate.now(ZoneId.systemDefault())` + `tzId` 필드 명시 저장 |
| **Health Connect 통합 부재** (워크온 본 앱 미지원) | `StepSourceResolver`: HC 우선 → Sensor fallback (사용자가 다른 헬스앱 데이터 잃지 않음) |
| **Android 14 FGS Type** 누락 시 즉시 크래시 | `foregroundServiceType="health"` / `"location"` 정확 선언 |

---

## 🏗 아키텍처

```mermaid
graph TB
  subgraph Android
    UI[Presentation<br/>MVVM + StateFlow]
    UC[Domain<br/>UseCase]
    REPO[Data/Repository]
    SENSOR[Sensor/StepSourceResolver<br/>HC ⇨ TYPE_STEP_COUNTER]
    LOC[Location/FusedLocationProvider]
    ROOM[(Room SSOT<br/>+ tzId, baseline)]
    NET[Retrofit + Authenticator]
  end
  subgraph Backend["Express + MongoDB"]
    AUTH[/auth: JWT + bcrypt/]
    STEPS[/steps/sync: rate-limit + upsert/]
    GROUPS[/groups: Top 10 리더보드/]
  end
  UI --> UC --> REPO
  REPO --> ROOM
  REPO --> SENSOR
  REPO --> LOC
  REPO --> NET
  NET -. WorkManager 30분 .-> AUTH
  NET --> STEPS
  NET --> GROUPS
```

---

## 🛠 기술 스택

**Android**: Kotlin · MVVM · Coroutines/StateFlow · Hilt · Room · Retrofit/OkHttp · Health Connect · Google Maps SDK · Foreground Service · WorkManager · GitHub Actions CI

**Backend**: Node.js · Express · MongoDB · JWT · `express-rate-limit` · Render Starter ($7/월)

> 모든 핵심 결정은 [`docs/adr/`](docs/adr) 5개 ADR로 즉시 기록. 추측이 아니라 결정 시점의 컨텍스트가 보존됩니다.

---

## 📊 회고 — 무엇이 잘 되고 무엇이 무너졌나

### 잘 된 것
- (Week 끝 작성)

### 무너진 것
- (Week 끝 작성)

### 알려진 한계 (의도적으로 안 한 것 + 사유)
- **Baseline Profile/Macrobenchmark 미적용** — 신입 포트폴리오 ROI 대비 1.5일 공수 무거워 Week 10 스트레치로 내림. 향후 v1.1.
- **걸음수 위조 방지 알고리즘 미적용** — 실측 4.5일 vs 견적 2일의 갭이 커서 P/R 보장 어려움. README에서만 한계 명시.
- **Wear OS 컴패니언 미지원** — 10주 일정 흡수 불가.

---

## 🚧 Trouble Shooting

### 1. Step Counter 재부팅 후 0으로 초기화
**증상**: 폰 재부팅 후 앱 다시 띄우니 오늘 걸음수가 누적값 그대로 → 갑자기 10,000보 추가.

**원인**: `TYPE_STEP_COUNTER`는 부팅 이후 누적값이라, 재부팅 시 0부터 다시 시작. baseline을 폰 메모리에만 저장하면 부팅 후 초기화 감지 불가.

**해결**:
```kotlin
val nowBoot = Settings.Global.getInt(contentResolver, "boot_count", 0)
if (nowBoot != savedBootCount) {
    // 재부팅 감지: baselineSensorValue를 0으로 리셋, 그 시점부터 누적
    saveBaseline(sensorValue = 0, bootCount = nowBoot)
}
```

### 2. Android 14 빌드 후 startForeground 즉시 크래시
**증상**: `targetSdk=34` 변경 후 첫 실행에서 `ForegroundServiceTypeNotAllowedException`.

**원인**: Android 14부터 `foregroundServiceType` 선언 + `startForeground(id, notif, type)` 시그니처 호출 모두 필수.

**해결**: AndroidManifest.xml에 `android:foregroundServiceType="health"` 추가 + `startForeground(NOTIF_ID, notif, FOREGROUND_SERVICE_TYPE_HEALTH)` 변경.

### 3. (1건 더 추가 예정 — Week 5 Health Connect 또는 Week 7 OkHttp Authenticator 재진입 등)

---

## 🚀 실행 방법

```bash
# Android
git clone https://github.com/lsk30323/walkmate.git
cd walkmate/android
./gradlew assembleDebug

# Backend
cd ../backend
cp .env.example .env  # NAVER_CLIENT_ID 등 채우기
npm install && npm run dev
```

---

## 📚 참고

- 실제 워크온/스왈라비 채용공고 분석 시트: [`docs/jd-matching-sheet.md`](docs/jd-matching-sheet.md)
- 면접 답변 스크립트: [`docs/interview-prep.md`](docs/interview-prep.md)
- ADR 5개: [`docs/adr/`](docs/adr)
