# Branch Protection Rules — main

설정 경로: https://github.com/lsk30323/walkmate/settings/branches → "Add branch protection rule" → Branch name pattern: `main`

## 권장 설정

| 항목 | 값 |
|---|---|
| Require a pull request before merging | ON |
| — Required approvals | 1 |
| Require status checks to pass before merging | ON |
| — Require branches to be up to date | ON |
| Required status checks | `Lint` / `Unit Tests` / `Build Debug APK` (android) + `TypeScript Type Check` / `Build & Test` (backend) |
| Restrict who can push to matching branches | ON — `lsk30323` only |
| Allow force pushes | OFF |
| Allow deletions | OFF |

## Status Check 이름 (워크플로우 job name과 일치)

Android:
- `Lint`
- `Unit Tests`
- `Build Debug APK`

Backend:
- `TypeScript Type Check`
- `Build & Test`

## 적용 권장 시점

- **즉시 적용 가능**: `TypeScript Type Check` + `Build & Test` — backend npm ci + tsc --noEmit은 외부 서비스 없이 통과 가능.
- **android는 Week 1 Gradle Sync 완료 후**: Studio에서 Gradle Sync가 끝나 `gradlew` wrapper jar가 생성된 뒤 CI 첫 통과를 확인하고 status check 등록.
- **PR 리뷰 규칙**: 혼자 개발 중에는 "Required approvals: 1"이 self-merge를 막을 수 있음 → 혼자인 경우 0으로 낮추거나 "Allow specified actors to bypass" 설정 고려.

## 참고

- Gradle wrapper jar가 없는 경우 CI는 `actions/setup-gradle@v3`로 Gradle 8.9를 직접 설치해 `gradle lint` 등을 실행합니다.
- APK artifact 업로드는 `main` 브랜치 push 시에만 실행됩니다 (PR에서는 skip).
