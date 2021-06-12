# knitting-service

## Install

```bash
./scripts/install.sh
```

## Environment

- Project Owner 에게 secret_env.sh 파일을 요청하여 받습니다.
  - 전달받은 파일은 프로젝트 루트에 두고 관리합니다.
- 실행 전 secret_env.sh를 실행하여 환경변수를 설정해줍니다.
- IDE를 이용하여 실행하는 경우 해당 파일의 환경설정을 IDE에 별도로 설정해줍니다.
  - IntelliJ 기준 아래 방법으로 설정할 수 있습니다.
    - [Run] -> [Edit Configurations] -> [Environment variables]

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
