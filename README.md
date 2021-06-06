# knitting-service

## Install

```bash
./scripts/install.sh
```


## Format

```bash
./gradlew ktlintFormat
```


## Structure

```
^ - pure
| - domain
| - usecase
| - controller
| - infra
dependency flow
```
- pure: 코틀린 / java 순수 utils, **외부 라이브러리에 의존하면 안 됨**
- domain: 도메인 로직 담당
- usecase: 응용 계층, 도메인 레이어에서 제공하는 기능을 통해 비즈니스 로직 구현
- controller: HTTP 요청 처리
- infra: 디테일한 구현체 (ex. R2DBC ...)
