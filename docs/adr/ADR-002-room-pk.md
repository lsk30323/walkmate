# ADR-002 — Room DailyStep 기본키 선택 (date String + tzId 동반)

- **상태**: Accepted
- **작성일**: 2026-04-25 (Week 3)
- **결정자**: lsk12
- **연관**: 명세서 §6(데이터 모델), Trouble Shooting #2

## 1. 컨텍스트

`DailyStepEntity`의 PK 후보:
- (A) `date: String` 단독 (예: "2026-04-25")
- (B) 복합키 `(userId, date)` (멀티 계정 대비)
- (C) `date: Long` Unix timestamp

해외 이동 시 타임존 변경, 자정 롤오버 처리 문제.

## 2. 고려한 옵션

### 옵션 A — date: String + tzId 컬럼
- 장점: 사람이 읽기 쉬움, `LocalDate` 변환 명확
- 단점: 정렬 시 문자열 비교 (ISO 8601이라 OK)

### 옵션 B — 복합키 (userId, date)
- 장점: 멀티 계정 확장성
- 단점: 단일 사용자 앱이라 over-engineering

### 옵션 C — date: Long (Unix UTC)
- 장점: 타임존 무관 정렬
- 단점: 사용자 직관 떨어짐, 디버깅 시 변환 필요

## 3. 결정

**옵션 A** 채택. `date: String` PK + `tzId: String` 컬럼 추가.

## 4. 근거

- 단일 사용자 앱 (멀티 계정 미지원) — 옵션 B는 yagni
- `tzId` 명시 저장으로 해외 이동 시 "이 날짜는 어느 타임존 기준이었는가" 추적 가능
- Trouble Shooting #2 (자정 롤오버)에 직접 활용

## 5. 결과

### 긍정적
- 면접 답변 ④에 "타임존 명시 저장" 디테일 포함 가능

### 부정적
- 멀티 계정 확장 시 마이그레이션 필요 (단, 신입 포트폴리오에선 불필요)

### 재검토 트리거
- 멀티 계정/팀 기능 추가 시 → 복합키로 마이그레이션
