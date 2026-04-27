# WalkMate 면접 답변 스크립트 (Top 6)

> 워크온/스왈라비 신입 안드로이드 채용 대비
> **핵심 시그널: 풀스택 실행력 (옵션 C, 2026-04-25 확정)**
> v2 portfolio 확장본 §3 + walkmate-review-FINAL.md 기반

## 사용 원칙

1. **30초 답변 우선**, 면접관이 "더 자세히"라고 하면 60초 버전
2. **어필 전환** — 약점 답변에서 강점으로 자연스럽게 넘어가기
3. **유도 질문** — 답변 끝에 면접관이 받아칠 후속 질문을 준비
4. **금지 어휘**: "위조 방지 프로토타입", "Closed Testing 20명 피드백", "공유 URL/OG 이미지" — 명세서에서 기각된 항목, 언급 시 공수·완성도 의심받음

---

## ① 워크온 복제 리스크 (가장 파괴력 큰 공격)

**예상 질문 (3버전)**:
- 부드러운: "워크온 앱 써보셨어요? 비슷한데 왜 만드셨어요?"
- 날카로운: "포트폴리오가 저희 본 서비스를 그대로 따라한 것처럼 보이는데, 실무 들어와서 차별화된 가치를 어떻게 만드실 건가요?"
- 트릭: "본인이 워크온 PM이라면 이 프로젝트를 사내 사이드프로젝트로 통과시키시겠어요?"

**30초 답변** (풀스택 시그널 강조):
> "워크온 복제가 아니라 **혼자서 풀스택을 production-ready 수준까지 만들어보고 싶어 헬스케어 도메인을 빌렸습니다**. Android만 만들면 안 보이는 것 — 백엔드 `/steps/sync` 위변조 방어, OkHttp Authenticator 재진입 race, Render 배포 비용 검토 — 이런 풀스택 함정을 직접 부딪혀 해결했습니다. 워크온이 만약 안드로이드 신입이 백엔드 컨텍스트도 이해하길 원한다면 즉시 도움이 될 거라고 봤습니다."

**60초 답변** (어필 전환 + 디테일):
> "30초 버전 + 'Android 측에서는 Health Connect 우선 → Sensor fallback (StepSourceResolver), 백엔드 측에서는 보안 결함 3개를 직접 식별해서 (rate-limit, 위변조 검증, Authenticator race) ADR로 기록했습니다. 즉 클라이언트만 짠 신입 포트폴리오와는 다른 시그널을 의도했어요. 워크온 본 서비스를 따라했다는 인상을 받으실 수 있지만, 도메인보다 풀스택 실행 깊이를 보여드리는 게 목적이었습니다.'"

**유도 질문 (자연스럽게 끌어내기)**:
> "사내에서 안드로이드 개발자가 백엔드 컨텍스트를 어디까지 들여다보시나요?"

---

## ⚠️ 새 ① 보강 — "왜 안드로이드 신입이 풀스택?" (가장 흔한 후속 공격)

**예상 질문**:
- "안드로이드 채용 지원하면서 왜 풀스택이에요? 백엔드 회사 가시지."
- "Android, Backend 둘 다 깊이 부족한 거 아닌가요?"
- "차라리 안드로이드 디테일에 시간을 더 쓰지 그랬어요?"

**30초 답변** (방어 + 어필 전환):
> "안드로이드 신입에 지원합니다. 백엔드는 **부산물이 아니라 안드로이드 결정의 컨텍스트를 이해하기 위한 학습** 이었어요. 예: Retrofit Authenticator 재진입 race를 백엔드 refresh 흐름까지 직접 짜보지 않았으면 못 봤을 거예요. ADR-006에서 Naver Geocoding을 백엔드 프록시로 둔 이유 (OWASP M3) 도 마찬가지. 안드로이드 깊이는 별도 — Step Counter baseline, Android 14 FGS Type, Doze/Standby 모두 부딪혔습니다."

**60초 답변** (백업 디테일):
> "30초 + '풀스택 신입의 가장 흔한 약점이 둘 다 어설픔이라는 걸 알았어요. 그래서 백엔드는 production 수준 (rate-limit, helmet, JWT refresh rotation, Render 배포)까지 가되, **범위는 의도적으로 축소** — Naver Maps SDK는 빼고, 그룹 랭킹은 Top 10 단순화로 끝냈습니다. ADR-006/skeduler-recalc.md에 이 트레이드오프를 정량으로 기록했어요. 입사하면 안드로이드 본업에 집중하면서 백엔드 PR도 능숙하게 리뷰할 수 있는 신입을 노렸습니다.'"

**어필 전환 테크닉**: "둘 다 어설픔" → "의도적 범위 축소 + ADR 기록"으로 전환

**유도 질문**: "사내에서 안드로이드 개발자가 백엔드 PR 리뷰까지 하시나요?"

---

## ② Compose 미사용 / 기술 폭 부족

**예상 질문**:
- "왜 Compose 안 쓰셨어요? 요즘 다 Compose인데."
- "XML Views로 한 이유가 있나요?"
- "Compose 경험 있으세요?"

**30초 답변**:
> "메인은 안정성을 위해 XML로 두되, **Week 8에 홈 통계 카드 1개를 Compose로 작성**해 양쪽을 비교했어요. `MPAndroidChart`를 `AndroidView`로 감싸는 방식으로 점진 전환 가능성도 검증했고, ADR-005에 도입 범위와 트레이드오프를 기록했습니다."

**어필 전환**: "10주 일정에서 풀 Compose는 학습 비용과 안정성 리스크가 컸지만, 1화면 도입으로 '양쪽 다 만져봤다'는 시그널은 확보했어요."

**유도 질문**: "사내에서는 Compose 전환 단계가 어느 정도이신가요?"

---

## ③ 센서·배터리 트레이드오프 (수치 기반)

**예상 질문**:
- "GPS 켜면 배터리 빨리 닳잖아요. 어떻게 처리했어요?"
- "Step Counter랑 Activity Recognition 차이 아세요?"

**30초 답변**:
> "GPS는 1초 vs 5초 + minDistance 10m 비교 측정에서 **배터리 소모 X% → Y%** (실측). Step Counter는 기기 센서라 임팩트 무시 가능. Foreground Service Type을 `location`으로 명시하고, 사용자가 산책 종료하면 즉시 unbind하는 구조로 했어요."

> ⚠️ **실측 수치 X/Y 확보 필수** (Week 7-8에 LeakCanary + StrictMode와 함께 측정)

**유도 질문**: "Doze/Standby 환경에서 산책 데이터 누락은 어떻게 막으셨어요?"

---

## ④ Foreground Service + Android 14 대응

**예상 질문**:
- "Android 14 FGS 변경사항 아세요?"
- "Foreground Service Type을 왜 명시해야 해요?"
- "POST_NOTIFICATIONS 권한 거부되면 어떻게 되나요?"

**30초 답변**:
> "Android 14부터 `foregroundServiceType`을 manifest에 선언하고 `startForeground(id, notif, type)` 시그니처로 호출하지 않으면 **`ForegroundServiceTypeNotAllowedException`로 즉시 크래시**합니다. 저는 StepCounterService를 `health`, LocationService를 `location` 타입으로 명시했고, `POST_NOTIFICATIONS` 거부 시에도 앱이 죽지 않도록 fallback UI를 설계했어요."

**어필 전환**: "Doze + Battery Optimization Whitelist 요청 UX도 함께 처리해서, 사용자가 새벽에 폰을 꺼둬도 걸음수가 누락되지 않도록 했습니다."

---

## ⑤ 테스트 / CI 실무감

**예상 질문**:
- "테스트 커버리지 어느 정도예요?"
- "Espresso랑 Robolectric 차이 아세요?"
- "CI 직접 셋업 해보셨어요?"

**30초 답변**:
> "Unit Test를 Week 8에 몰빵하지 않고 **각 주차 산출물과 함께 작성**했습니다. ViewModel/Repository는 Turbine + MockK, Espresso는 핵심 플로우 1-2개. **GitHub Actions로 PR마다 테스트 + 린트 + APK 빌드를 자동화**하고, main 브랜치 보호 규칙으로 머지 차단 룰까지 적용했어요."

**유도 질문**: "현업에서는 테스트 커버리지 목표를 몇 %로 두시나요?"

---

## ⑥ 왜 Google Maps SDK + Naver Geocoding 혼합? (v3 채택)

> v3 결정: 옵션 (c+) — Google Maps SDK 메인 + Naver Cloud Geocoding/Reverse Geocoding API.
> ADR-006 참조.

**예상 질문 (3버전)**:
- 부드러운: "왜 Google Maps랑 Naver를 같이 쓰셨어요?"
- 날카로운: "한국 앱이면 처음부터 Naver SDK로 통일하지, 왜 굳이 두 회사 API를 섞었어요?"
- 트릭: "워크온이 Naver Maps SDK 쓴다고 하면 본인 결정 다시 하시겠어요?"

**30초 답변** (150자):
> "한국 사용자 헬스케어 앱이라 **한국 도로명 정확도가 필요**해서 산책 종료 라벨에 Naver Reverse Geocoding API를 백엔드 프록시로 호출합니다. 단 Naver Maps SDK 풀 채택은 자료가 1/30 수준이라 신입 학습 비용 vs 완주 확률 트레이드오프가 안 맞아 Google SDK 유지. ADR-006에 트레이드오프 기록했습니다."

**60초 답변** (어필 전환):
> "30초 + 'Client Secret 보호를 위해 백엔드 프록시 패턴 적용했고 LRU 캐싱으로 동일 좌표 24시간 캐시해서 NCP 일일 30,000건 한도 안에서 안전하게 동작합니다. 일일 할당량 80% 도달 시 Slack 알림 메트릭도 노출했어요. 워크온이 Naver SDK 쓴다고 하면 옵션 (b)로 업그레이드 가능하도록 게이트 4조건을 명세서에 박제했습니다.'"

**외국 면접관 영문 키워드**:
- "Hybrid map provider — Google Maps SDK for rendering, Naver Cloud Maps API for Korean street address accuracy."
- "Client Secret protected via backend proxy (OWASP M3 mitigation)."
- "Korean address geocoding has 4%p accuracy advantage over Android's built-in Geocoder for Korean roads."

**어필 전환 테크닉**:
- "혼합" 단점 → "현실적 의사결정" 장점으로 전환
- "ADR-006 + 게이트 4조건" 언급으로 단순한 답변이 아니라 계층적 의사결정 시그널

**유도 질문**:
> "혹시 워크온은 어떤 지도 SDK 쓰시는지 여쭤봐도 될까요?" (게이트 4조건 검증 + 자연스러운 면접관 정보 흡수)

---

## 면접 사전 점검 체크리스트

- [ ] 모든 질문에 30초 답변 외워서 가능
- [ ] "워크온 PM이라면" 같은 트릭 질문에 ADR 기반으로 답변 가능
- [ ] 실측 수치 (배터리 X%/Y%) 메모 보유
- [ ] 외국 면접관 영어 키워드 준비 ("step counter sensor", "foreground service type")
- [ ] README + ADR + interview-prep 한 번씩 다시 읽고 입실
