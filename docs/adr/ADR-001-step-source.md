# ADR-001 — 걸음수 데이터 소스 선택 (Health Connect 우선)

- **상태**: Accepted
- **작성일**: 2026-04-25 (Week 5 결정 시점)
- **결정자**: lsk12
- **연관**: 명세서 §2(기술 스택), §6(데이터 모델), interview-prep.md #1

## 1. 컨텍스트

걸음수 측정 소스가 2개 이상 존재:
- `SensorManager.TYPE_STEP_COUNTER` (기기 센서, 부팅 후 누적)
- Health Connect (Samsung Health, Google Fit, Garmin 등 다양한 헬스앱이 통합 제공)

워크온 본 앱은 Health Connect 미통합 → **차별화 각도** 가능.
단, Health Connect 권한 거부/미설치 시 fallback 필요.

## 2. 고려한 옵션

### 옵션 A — Sensor Only
- 장점: 단순, 권한 1개
- 단점: 다른 헬스앱 데이터 단절, 차별화 ★

### 옵션 B — Health Connect Only
- 장점: 데이터 통합
- 단점: HC 미설치 기기 / 권한 거부 시 측정 불가

### 옵션 C — StepSourceResolver (HC 우선 → Sensor fallback) ⭐
- 장점: HC 사용자 데이터 통합 + Sensor 사용자도 동작 + Health Connect 부재 환경 대응
- 단점: 코드 복잡도 ↑, 두 소스 동시 매칭 시 데이터 중복 처리 필요

## 3. 결정

**옵션 C** 채택.

## 4. 근거

- 워크온 복제 리스크 방어의 핵심 차별화 각도
- 사용자가 어느 헬스앱을 메인으로 쓰든 데이터 단절 없음
- 면접 답변 #1에 직접 활용

## 5. 결과

### 긍정적
- 차별화 각도 #2 (가장 강력)
- 면접 답변 풀이 자연스러움

### 부정적
- 두 소스 데이터 중복 시 우선순위 룰 필요 (HC > Sensor 단순 채택)
- HC API 학습 비용 (1.5일, Week 5 예산 흡수)

### 재검토 트리거
- 워크온 본 앱이 HC 통합하면 차별화 약화 → 다른 각도(재부팅/타임존)로 무게 이동
- HC API가 deprecated 되면 재검토
