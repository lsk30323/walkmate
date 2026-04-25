# ADR-006 — Map Provider 선택 (Google Maps SDK 메인 + Naver Cloud Geocoding/Reverse Geocoding API 절충)

- **상태**: Accepted (조건부 — Day 0 JD 게이트 결과에 따라 b 옵션 업그레이드 가능)
- **작성일**: 2026-04-25 (Week 4 결정 시점)
- **결정자**: lsk12
- **연관**: 명세서 v3 §2(기술 스택), §3 Week 0/4-6, walkmate-naver-FINAL.md, interview-prep.md #6
- **상위 토론**: 7명 팀(walkmate-naver-v3) 6:1 비대칭 토론

## 1. 컨텍스트

WalkMate는 한국 사용자 대상 헬스케어 앱으로 GPS 산책 경로 표시 + 거리 계산 + 주소 라벨 기능이 필요. 지도 SDK 선택지는 Google Maps / Naver Maps / Kakao Map.

워크온/스왈라비 본 앱과의 차별화 + 한국 도로/POI 정확도 + 신입 포트폴리오의 학습 비용/완주 확률 모두 고려해야 함.

## 2. 고려한 옵션

### 옵션 (a) — Naver Maps SDK 풀 채택
- 장점: 한국 데이터 정확도, 워크온 도메인 정합도, NCP 무료 할당량
- 단점: SDK 자료 1/30~1/300 (SO 답변 30~80건 vs Google 10,000+), 학습 페널티 1.3-2.0x, 면접 후속 질문 응답률 20-30%
- 공수: 331h / **완주확률 50%** (10주 내 위험)

### 옵션 (b) — Naver Maps SDK + Naver Geocoding/Reverse Geocoding
- 장점: 풀 통합, Direction15 도보 길찾기 활용 가능
- 단점: 옵션 (a)와 동일한 학습 비용 + 백엔드 프록시 추가 부담
- 공수: 311h / **완주확률 70%**
- **조건**: JD에 "Naver Maps SDK" 명시 + 4조건(워크온 본 앱 Naver 사용/Geocoding API 5/15 둘 다 활용/외국계 지원 안 함/Day 0 NCP 가입 완료) 충족 시에만

### 옵션 (c+) — Google Maps SDK 메인 + Naver Cloud Geocoding/Reverse Geocoding API ⭐
- 장점:
  - Google Maps SDK는 자료 풍부, 학습 비용 최소
  - Naver Cloud Geocoding/Reverse Geocoding API는 한국 도로명 주소 정확도 ↑ → 산책 시작/종료 라벨에 활용
  - 백엔드 프록시 패턴으로 Client Secret 안전 보호
  - portfolio 차별화 각도 #2 (한국 도메인 사고력) 유지
- 단점: 두 회사 API 모두 학습 (단, 핵심은 Google SDK 1개)
- 공수: 297h / **완주확률 80%**

### 옵션 (d) — Google Maps SDK + Android 내장 Geocoder
- 장점: 가장 단순, 학습 비용 최저
- 단점: 한국 도로명 정확도 부족, 워크온 도메인 차별화 0

## 3. 결정

**옵션 (c+) 채택 (디폴트)** — Google Maps SDK 메인 + Naver Cloud Geocoding/Reverse Geocoding API + 백엔드 프록시.

**Day 0 JD 게이트** — 워크온/스왈라비 채용공고 전수 조사 후 다음 4조건 모두 충족 시 옵션 (b)로 업그레이드 가능:
1. JD에 "Naver Maps SDK" 명시
2. 본 앱이 실제 Naver Maps 사용 확인
3. Naver Geocoding API 5/15 둘 다 활용 시나리오 명확
4. NCP 가입 + Application 등록 완료 (Week 0)

4조건 미충족 시 (c+) 유지.

## 4. 근거

7명 팀 6:1 토론에서 risk-assessor의 5개 반대 근거 중 3개 인정:
- ① 자료 1/30~1/300 (사실)
- ③ 면접 후속 질문 응답률 20-30% (사실)
- ⑤ Google + Geocoder도 한국 정확도 충분 (부분 인정 — 단 Naver API가 더 정확)

나머지 2개는 헷지 가능:
- ② 비용 폭증 → 활성 1만 미만 0원 (api-integrator §4.2)
- ④ 외국 면접관 역효과 → portfolio §4 영문 키워드로 헷지

schedule-recalc 정량: (a) 50% / (b) 70% / **(c+) 80%** / (d) 85% — 차별화 ↑ vs 완주 확률 균형점.

## 5. 결과 (Consequences)

### 긍정적
- Google Maps SDK 학습 비용 최소 (자료 풍부)
- Naver Cloud Geocoding/Reverse Geocoding API로 한국 도메인 차별화 유지
- 백엔드 프록시로 Client Secret 안전 보호 (OWASP M3 회피)
- 산책 종료 시 한국 도로명 라벨 자동 표시 → UX ↑
- 면접 답변 #6에 "한국 정확도 우선 의사결정" 어필 가능

### 부정적 (수용한 트레이드오프)
- 두 회사 API 학습 (단 Naver는 단순 HTTP 호출이라 부담 작음)
- Direction15 도보 길찾기 미활용 (api-integrator의 신규 기능 4종 기각)
- 워크온이 Naver SDK 사용한다면 SDK까지 통일했어야 한다는 답변 압박 (옵션 b 게이트로 대응)

### 향후 재검토 트리거
- Day 0 JD 게이트 4조건 충족 → 옵션 (b)로 업그레이드
- v1.1에서 Direction15 도보 길찾기 추가 검토 (산책 코스 추천 기능)
- Google Maps Billing 부담 발생 시 Naver SDK 풀 전환 재검토

## 6. 면접 대응

면접관 질문: "왜 Google Maps SDK + Naver API 혼합?" → interview-prep.md #6 (30초/60초/외국 면접관 3버전) 답변 활용.

핵심 메시지: "한국 사용자 대상 헬스케어 앱이라 한국 정확도 필요. 단 SDK 학습 비용/면접 깊이 우려로 풀 Naver는 거부. 백엔드에서 Naver Geocoding API만 호출하는 절충안. ADR-006에 트레이드오프 기록."
