-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.6.5-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 데이터 nightmare.datasource:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `datasource` DISABLE KEYS */;
REPLACE INTO `datasource` (`ALIAS_NAME`, `DRIVER`, `URL`, `USER_ID`, `USER_PWD`, `FST_REG_DT`) VALUES
	('investar', 'org.mariadb.jdbc.Driver', 'jdbc:mariadb://localhost:3306/investar', 'tester1', 'tester1', '0000-00-00 00:00:00'),
	('nightmare', 'org.mariadb.jdbc.Driver', 'jdbc:mariadb://localhost:3306/nightmare', 'tester1', 'tester1', '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `datasource` ENABLE KEYS */;

-- 테이블 데이터 nightmare.tbm_sm_cnf:~8 rows (대략적) 내보내기
/*!40000 ALTER TABLE `tbm_sm_cnf` DISABLE KEYS */;
REPLACE INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`, `USE_YN`) VALUES
	(1, 'AUDIO', 'MIC', '마이크(2- H710)', '2024-06-07 00:14:06', NULL, 'gpt-4o', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(2, 'OPEN_AI', 'GTP_3_5', 'https://api.openai.com/v1/chat/completions', '2024-05-26 17:10:39', '2024-05-26 17:14:19', 'gpt-3.5-turbo', '[OPEN-API-KEY]', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(3, 'OPEN_AI', 'GTP_4_O', 'https://api.openai.com/v1/chat/completions', '2024-05-26 17:17:30', NULL, 'gpt-4o', '[OPEN-API-KEY]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(4, 'OPEN_AI', 'TEXT_TO_SPEECH', 'https://api.openai.com/v1/audio/speech', '2024-06-07 17:19:56', NULL, 'tts-1', '[OPEN-API-KEY]', NULL, NULL, 'wav', 'alloy', '', '1', NULL, NULL, 'Y'),
	(5, 'OPEN_AI', 'TRANSCRIPTIONS', 'https://api.openai.com/v1/audio/transcriptions', '2024-06-07 00:28:21', NULL, 'whisper-1', '[OPEN-API-KEY]', NULL, 'write to english', 'text', NULL, NULL, NULL, NULL, NULL, 'Y'),
	(6, 'OPEN_AI', 'TRANSLATE', 'https://api.openai.com/v1/audio/translations', '2024-06-07 00:28:21', NULL, 'whisper-1', '[OPEN-API-KEY]', NULL, 'write to english', 'text', NULL, NULL, NULL, NULL, NULL, 'Y'),
	(8, 'OLLAMA', 'LLAMA3', 'http://localhost:11434/api/chat', '2024-05-26 17:17:30', NULL, 'llama3', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(9, 'whisper', 'TRANSLATE2', 'http://localhost:8000/audio/transcriptions2', '2024-06-23 01:20:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y');
/*!40000 ALTER TABLE `tbm_sm_cnf` ENABLE KEYS */;

-- 테이블 데이터 nightmare.tbm_sm_prompts:~5 rows (대략적) 내보내기
/*!40000 ALTER TABLE `tbm_sm_prompts` DISABLE KEYS */;
REPLACE INTO `tbm_sm_prompts` (`GROUP`, `ID`, `DISPLAY_TEXT`, `PROMPT`, `USE_YN`, `DESCRIPTION`, `GRAPHIC_CLASS`, `SYSTEM`) VALUES
	('CONTEXT', '영어 문장 분석', '영어 문장 분석', '아래 포멧에 맞춰 중요해보이는 영어 2개~5개 사이로 정리해줘\r\n\r\n## 정리해주는 포멧은 아래와 같아.\r\n[순번].[단어] [한글 뜻][품사]([발음기호])\r\n#examples)\r\n [단어 사용 영문 예제, 한글뜻풀이]\r\n\r\n## 물어보려는 문장은 아래 문장이야.\r\n$content\r\n\r\n#그리고 비슷한 문장이 있으면 추가로 알려주고, 보정이 필요하면 추가로 또 알려줘', 'Y', NULL, NULL, NULL),
	('SUPPORT', 'OPIC', 'OPIC', 'OPIC 영어 시험 선생님이 되어줘\r\n영어 문장을 제시해주고, 답변이 적절한지 알려줘\r\n\r\n먼저 예시 문장을 제시해줘', 'Y', 'OPIC', NULL, NULL),
	('SUPPORT', '대화 퀴즈', '영어 문제', '##영어문제\n\n영어 작문을 위한 문제를 제시해줘. 문제는 유행하는 내용을 기반으로해주고,  전체 문장은 영어로 번역해서 보여줘\n맞는 정답 1개, 정답 절대 될 수 없는 문장 4개로 구성되며, 1번~5번사이로 정답번호는 랜덤으로 처리.\n기로그 reason에는 정답에 대한 설명을 기술해줘.\n답변 구성은 example을 참고해서 json포멧으로 알려줘\nexample)\n{\n	"question" : "철수 오늘 하루 기분이 어떄?",\n	"questionList": [\n		{"no": 1, "text" : "모기가 많다.",  "correct" : false }\n		{"no": 2, "text" : "밥이 맜었어",  "correct" : false }\n		{"no": 3, "text" : "날씨가 더워"",  "correct" : false }\n		{"no": 4, "text" : "그는 거짓말을 했다.",  "correct" : false }\n		{"no": 5, "text" : "오늘 하루 기분이 좋지않았어.",  "correct" : true }\n	]\n	"reason" : "정답은 5번으로 철수의 하루에 기분이 어떤지 물어보는 질문이고, 기분에 대한 응답을 한 문장은 5번이다."\n}', 'Y', NULL, 'com.kyj.fx.nightmare.actions.ai.QuestionComposite', NULL),
	('SUPPORT', '문법 퀴즈', '영어 문법 문제', '##영어 문법 문제\r\n\r\n영어 문법을 위한 문제를 제시해줘. 문제는 최신뉴스를 기반으로해줘.\r\n맞는 정답 1개, 정답 절대 될 수 없는 문장 4개로 구성되며, 1번~5번사이로 정답번호는 랜덤으로 처리.\r\n그리고 reason에는 정답되는 이유와  정답 외에 나머지는 정답이 될 수 없는 이유에 대해 자세히 기술해줘.\r\n답변 구성은 example을 참고해서 json포멧으로 알려줘\r\nexample)\r\n{\r\n	"question" : "철수 오늘 하루 기분이 어떄?",\r\n	"questionList": [\r\n		{"no": 1, "text" : "모기가 많다.",  "correct" : false }\r\n		{"no": 2, "text" : "밥이 맜었어",  "correct" : false }\r\n		{"no": 3, "text" : "날씨가 더워"",  "correct" : false }\r\n		{"no": 4, "text" : "그는 거짓말을 했다.",  "correct" : false }\r\n		{"no": 5, "text" : "오늘 하루 기분이 좋지않았어.",  "correct" : true }\r\n	]\r\n	"reason" : "정답은 5번으로 철수의 하루에 기분이 어떤지 물어보는 질문이고, 기분에 대한 응답을 한 문장은 5번이다."\r\n}', 'Y', NULL, 'com.kyj.fx.nightmare.actions.ai.QuestionComposite', NULL),
	('SUPPORT', '오늘의 표현 ', '오늘의 표현 보여줘', 'Phrase of the Day\r\n        Create a sentence dialog for daily contextualized English learning in the following rule format\r\n        #rules \r\n        	1. 2 people talking to each other.\r\n        	2. the conversation centers on the latest Korean news.\r\n        	3. the characters are Young-hee and Chul-su.\r\n        	4. the dialog consists of 6 to 8 sentences of dialog content\r\n        	5. the dialog is organized in the following form\r\n        		[English]\r\n        		[Korean]\r\n        		example)\r\n        		[English]\r\n        		Chulsoo : BLABLA\r\n        		Younghee : BLABLA\r\n        		...\r\n        		[Korean].\r\n        		Cheolsu : BLABLA\r\n        		Younghee: Blabla\r\n        	6. important words, focusing on the content of the conversation\r\n        		Display format\r\n        			[order].[english word] [Korean meaning] [품사][발음기호]\r\n', 'Y', '오늘의 표현', NULL, NULL),
	('WEB_SYSTEM', 'WEB', NULL, '다른 출력 포멧으로 변경할 경우 아래와 같은 텍스트를 응답 가장 마지막에 추가\r\n```OUT:{포멧이름}```\r\n', 'Y', NULL, NULL, NULL);
/*!40000 ALTER TABLE `tbm_sm_prompts` ENABLE KEYS */;

-- 테이블 데이터 nightmare.web_favorite:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `web_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `web_favorite` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
