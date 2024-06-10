
# 설정

## 기본환경
1. java21 javafx
2. Mariadb

## 실행명령어
%JAVA21_HOME%\java -jar Nightmare.jar


## 기본 테이블은 아래 스크립트를 참조
Nightmare\script\init\mariadb.sql

## 데이터베이스 설정 UserConf.properties 파일
```
db.dev.driver=org.mariadb.jdbc.Driver
db.dev.url=jdbc:mariadb://localhost:3306/nightmare
db.dev.userid=tester1
db.dev.password=tester1
```

## 기준정보 입력하고 OPEN_AI_API_KEY를 사용자의 KEY로 맞춰야함.
```
INSERT INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`) VALUES (1, 'AUDIO', 'MIC', '마이크(2- H710)', '2024-06-07 00:14:06', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`) VALUES (2, 'OPEN_AI', 'GTP_3_5', 'https://api.openai.com/v1/chat/completions', '2024-05-26 17:10:39', '2024-05-26 17:14:19', 'gpt-3.5-turbo', 'OPEN_AI_API_KEY', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`) VALUES (3, 'OPEN_AI', 'GTP_4_O', 'https://api.openai.com/v1/chat/completions', '2024-05-26 17:17:30', NULL, 'gpt-4o', 'OPEN_AI_API_KEY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`) VALUES (4, 'OPEN_AI', 'TEXT_TO_SPEECH', 'https://api.openai.com/v1/audio/speech', '2024-06-07 17:19:56', NULL, 'tts-1', 'OPEN_AI_API_KEY', NULL, NULL, 'wav', 'alloy', '', '1', NULL, NULL);
INSERT INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`) VALUES (5, 'OPEN_AI', 'TRANSCRIPTIONS', 'https://api.openai.com/v1/audio/transcriptions', '2024-06-07 00:28:21', NULL, 'whisper-1', 'OPEN_AI_API_KEY', NULL, 'write to english', 'text', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`) VALUES (6, 'OPEN_AI', 'TRANSLATE', 'https://api.openai.com/v1/audio/translations', '2024-06-07 00:28:21', NULL, 'whisper-1', 'OPEN_AI_API_KEY', NULL, 'write to english', 'text', NULL, NULL, NULL, NULL, NULL);
```
