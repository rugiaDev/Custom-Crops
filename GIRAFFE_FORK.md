# 기린월드 포크 변경점

CustomCrops 원본에 **게임 규칙을 넣지 않는다.** 포크에는 다른 플러그인(GLife)이 끼어들 수 있는 이벤트와 기본 API 만 더하고,
실제 판단(비료를 넣을지·덮어쓸지)은 GLife 가 한다.

| 날짜 | 무엇 | 파일 | 쓰는 곳 |
|---|---|---|---|
| 2026-09-15 | 새 이벤트 `SprinklerWaterPotEvent` — 스프링클러가 화분통에 물을 주기 직전, 화분마다 한 번. 취소하면 그 화분은 안 적심 | `api/.../event/SprinklerWaterPotEvent.java`, `SprinklerBlock.tickSprinkler` | GLife `farm/FarmFertilizers` — 백금 스프링클러가 적시는 화분에 품질 비료 |
| 2026-09-15 | 새 API `PotBlock.removeFertilizer(state, id)` | `PotBlock.java` | GLife — 칸이 찬 화분에서 비료 덮어쓰기 |
| 2026-10-02 | 새 이벤트 `WateringCanClickSprinklerEvent` — 물뿌리개로 스프링클러를 눌렀을 때 스프링클러 처리 **전**. 취소하면 스프링클러 클릭으로 보지 않고 물뿌리개 원래 처리(물 담기 · 시선 끝 물 찾기)로 넘어간다 | `api/.../event/WateringCanClickSprinklerEvent.java`, `WateringCanItem.interactAt` | GLife `farm/FarmSprinklerCanFill` — 빈 물뿌리개 · 가득 찬 스프링클러면 스프링클러 밑 물(물 채운 나뭇잎 등)을 담게 |
| 2026-09-15 | `FertilizerUseEvent` 를 칸 수 검사 **앞**으로 옮김 — 리스너가 먼저 자리를 비울 수 있게 | `FertilizerItem.interactAt` | GLife — 손으로 넣는 비료도 덮어쓰기 |

- 버전: 로컬 `project_version=3.6.56` (서버에서 돌던 jar 는 3.6.55). GLife 는 이 포크로 빌드한 jar 에 맞춰 컴파일해야 새 이벤트·API 가 보인다

## 빌드

```
JAVA_HOME="C:/Program Files/Zulu/zulu-21" ./gradlew :plugin:shadowJar
```

- 결과: `target/CustomCrops-3.6.56.jar`
- ★ItemsAdder API 저장소(maven.devs.beer)가 죽어 있으면(09-15 522 오류) 받기에서 멈춘다. 그때는 저장소 파일을 안 건드리는 보조 스크립트로:
  `./gradlew -I C:/Users/kdcfv/Downloads/claude/2026-09-14_1815_농사인수인계_검토/itemsadder_local.init.gradle :plugin:shadowJar`
  (루나 서버의 ItemsAdder jar 를 컴파일 전용으로 대신 붙인다. 생활 서버는 Nexo 라 동작 영향 없음)
- 서버에 넣기: 서버가 켜져 있으면 옛 jar 가 잠겨 있으므로 `plugins/update/<옛 jar 와 같은 이름>` 에 넣고 재시작 → 교체된다
- ★GLife 는 IntelliJ 라이브러리 `ServerPlugins`(= `giraffe_returns/plugins` 폴더의 jar) 로 컴파일한다 → **재시작으로 jar 가 바뀐 뒤에** GLife 를 빌드해야 새 이벤트가 보인다
