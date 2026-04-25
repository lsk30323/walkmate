# Agent 팀 운영 — 반복 Task & 반복 Issue 분류

> 5명 팀(walkmate-review) + 7명 팀(walkmate-naver-v3) 운영 결과 데이터 기반
> 작성일: 2026-04-25
> 목적: 다음 팀 운영 시 사전 방어로 토큰·시간 절약

---

## A. 모든 팀원이 반복적으로 수행한 TASK (Step by Step)

| Step | Task | 누가 | 비고 |
|---|---|---|---|
| 1 | TaskList 호출 → 자기 task 찾기 | 전원 | claim 직전 |
| 2 | TaskUpdate (`owner=name`, `status=in_progress`) | 전원 | 시작 신호 |
| 3 | 명세서 원본 Read (`WalkMate_프로젝트_명세서_v2.md` 또는 v1) | 전원 | 입력 컨텍스트 |
| 4 | 이전 리뷰 파일 Read (참고용) | 80% | portfolio/android/backend 리뷰 등 |
| 5 | ultrathink로 깊이 분석 | 전원 | 코어 작업 |
| 6 | Markdown 결과 파일 Write | 전원 | 산출물 (`walkmate-*.md`) |
| 7 | TaskUpdate (`status=completed`) | 전원 | 완료 신호 |
| 8 | SendMessage to team-lead (3줄 요약) | 전원 | 보고 |
| 9 | idle 자동 전환 | 전원 | 시스템 처리 |

**최적화 지점**:
- Step 3 (원본 Read)는 모든 팀원이 동일하게 수행 → **공유 컨텍스트 페이지 1개**로 줄일 수 있으면 토큰 절약 (현재 MCP 구조상 불가능, 미래 개선 영역)
- Step 4 (이전 리뷰 Read)는 종합 담당자만 필수, 다른 팀원은 자기 영역에 직결되는 1-2개만 → 프롬프트로 명시 권장

---

## B. 종합 담당 팀원이 추가로 수행한 TASK

| Step | Task | 비고 |
|---|---|---|
| 10 | 다른 팀원 산출물 N개 Read | N=4 (5명팀) 또는 N=6 (7명팀) |
| 11 | 교차검증 (모순/과장/약점 식별) | ultrathink 추가 사이클 |
| 12 | SendMessage 후속 질문 (선택) | 1-2개 팀원에 |
| 13 | 답변 수신 후 추가 분석 | 비동기 |
| 14 | 최종 종합 리포트 Write | `*-FINAL.md` |
| 15 | (선택) 명세서 v3 작성 | `WalkMate_프로젝트_명세서_v3.md` |
| 16 | Executive Summary로 team-lead 보고 | 최종 |

---

## C. 반복 발생한 ISSUE (4가지 유형, 빈도 순)

### Issue #1 — **중복 task_assignment** (가장 빈번, 6+ 사례)

**증상**:
- 팀원이 이미 `completed` 마크한 task에 대해 task_assignment 메시지를 또 받음
- 팀원이 "이미 완료했습니다" 응답 → 토큰 낭비 (응답마다 50-200토큰)

**원인 (추정)**:
- 시스템이 자동 echo로 task_assignment 재전달
- 또는 SendMessage 라우팅 버그 (sender=`risk-assessor`로 자기 자신에게 도착한 사례 1건)

**대응 패턴**:
- 팀원: "이미 completed, 추가 작업 필요 시 구체 지시 주세요" 응답 후 idle
- team-lead: 무시 (수동 개입 불필요)

**예방**:
- 프롬프트에 "task_assignment를 받았는데 이미 completed라면 무시하고 idle" 명시
- 더 강한 예방: SendMessage 발송 전 TaskList로 상태 재확인

---

### Issue #2 — **Stale Read** (1 사례, 임팩트 큼)

**증상**:
- `devils-advocate`가 `walkmate-review-portfolio.md`를 읽었을 때 348줄로 보임 (실제는 814줄로 확장된 직후)
- 결과: FINAL.md에 portfolio 확장본 내용 누락 → v3 재작업 필요 → 토큰 ×2 소비

**원인**:
- portfolio-strategist가 Write 직후, devils-advocate가 거의 동시에 Read
- 파일 시스템 캐시 또는 MCP 시점 차이

**예방 패턴**:
- 종합 담당 팀원에게 "Write 완료 보고 받은 후에만 Read 시작" 명시
- 또는 Read 시 파일 줄 수/타임스탬프 검증 (`offset`/`limit`로 끝부분 확인)

---

### Issue #3 — **Race Condition (Pause vs Auto-claim)** (1 사례)

**증상**:
- team-lead가 "잠시 일시중단" SendMessage 보내기 전, devils-advocate가 이미 Task #5를 자율 claim하고 작업 진행
- 결과: 중단 시점이 어색해서 작업물 일부만 작성된 상태로 일시 멈춤 → v3 재시작 필요

**원인**:
- 팀원의 자율 claim 정책 (TaskList에서 unblocked 보이면 즉시 claim)
- team-lead의 메시지 발송 ↔ 팀원의 claim 사이 race condition

**예방 패턴**:
- 종합 담당 팀원 프롬프트에 "team-lead의 명시적 시작 신호 받기 전엔 claim 금지" 추가
- 또는 task 자체에 "manual_start: true" 메타데이터 추가 (현재 시스템 미지원)

---

### Issue #4 — **모호한 사용자 지시 해석** (1-2 사례)

**증상**:
- 사용자: "android 시간 단축, portfolio 상세화, schedule 5분만에 완료"
- 모순(상이한 길이 요구) → team-lead가 명확화 질문 → 사용자 추가 답변 필요

**대응 패턴**:
- 모호 시 즉시 (a)(b)(c) 3옵션 제시 → 사용자 1글자 답변으로 해결

**예방**:
- 사용자 프롬프트가 모호할 때 **추측 진행 금지** → 옵션 제시 후 단답 받기

---

## D. 토큰·시간 효율 최적화 권장사항 (다음 팀 운영 시)

1. **팀원 인풋 파일 명시 단축**:
   - "다음 5개 모두 읽어" → "Step별 1차/2차 인풋 명시" (1차만 필수)
2. **출력 길이 사전 지정**:
   - "원본의 30%/100%/200%" 명시. 첫 라운드부터 적정 길이 → 개정 round 절약
3. **종합 담당 팀원 자동 시작 금지**:
   - 프롬프트에 "team-lead 신호 대기 후 시작" 명시
4. **task_assignment 중복 무시 룰**:
   - 프롬프트에 "이미 completed면 응답 없이 idle" 명시
5. **Stale Read 방어**:
   - 종합 담당자 프롬프트에 "Write 완료 보고 받은 후에만 Read" 명시

---

## E. 정량 데이터 (참고)

| 팀 | 팀원 수 | 산출물 수 | 개정 라운드 | 임팩트 큰 이슈 |
|---|---|---|---|---|
| walkmate-review (5명) | 5 | 5개 + FINAL | 1회 (portfolio 확장 race) | Stale Read |
| walkmate-naver-v3 (7명) | 7 | 6개 + FINAL (진행 중) | 0회 (현재) | 6:1 비대칭 토론 |

---

**다음 팀 운영 시**: 본 문서 D섹션 5개 권장사항을 spawn 프롬프트에 미리 반영.
