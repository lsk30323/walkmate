# ADR-004 — Coroutine Scope 정책 (UseCase는 viewModelScope, Sensor는 ApplicationScope)

- **상태**: Accepted
- **작성일**: 2026-04-25 (Week 7)
- **결정자**: lsk12
- **연관**: 명세서 §2(아키텍처), §3 Week 7

## 1. 컨텍스트

Coroutine Scope 선택 기준:
- ViewModel 안 — `viewModelScope` (LifecycleAware)
- Service 안 — `LifecycleService` + `lifecycleScope`
- Sensor/Background 작업 — Application 레벨 필요?

`OkHttp Authenticator` 401 자동 갱신 시 재진입 방지가 까다로움.

## 2. 고려한 옵션

### 옵션 A — 모든 비동기는 viewModelScope
- 장점: 단순
- 단점: ViewModel 종료 시 Sensor 데이터 누락

### 옵션 B — Sensor는 Application Scope, UI는 viewModelScope ⭐
- 장점: Sensor 누락 없음, UI는 빠른 정리
- 단점: Application Scope 메모리 누수 위험 → Hilt `@ApplicationScope` 강제

### 옵션 C — 모든 곳 GlobalScope
- 장점: 신경 안 써도 됨
- 단점: 안티 패턴, 면접 감점

## 3. 결정

**옵션 B** 채택.

## 4. 근거

- Sensor 데이터는 Activity 종료 후에도 수집 필요 (Foreground Service 동작)
- UI 작업은 viewModelScope가 정석
- `Authenticator` 재진입 방지는 별도 mutex로 처리 (이 ADR 외)

## 5. 결과

### 긍정적
- 면접 답변에 "Scope 정책" 디테일 포함 가능
- LeakCanary 검증 통과

### 부정적
- Application Scope 잘못 쓰면 메모리 누수 — 코드 리뷰 시 주의

### 재검토 트리거
- KMP 도입 시 Coroutine Scope 정책 재설계 필요
