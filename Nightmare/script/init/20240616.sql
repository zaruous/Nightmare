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

-- 테이블 데이터 nightmare.tbm_sm_cnf:~7 rows (대략적) 내보내기
/*!40000 ALTER TABLE `tbm_sm_cnf` DISABLE KEYS */;
REPLACE INTO `tbm_sm_cnf` (`ID`, `GROUP`, `KEY`, `VALUE`, `FST_REG_DT`, `FNL_UPD_DT`, `CNF_CMF_1`, `CNF_CMF_2`, `CNF_CMF_3`, `CNF_CMF_4`, `CNF_CMF_5`, `CNF_CMF_6`, `CNF_CMF_7`, `CNF_CMF_8`, `CNF_CMF_9`, `CNF_CMF_10`, `USE_YN`) VALUES
	(1, 'AUDIO', 'MIC', '마이크(2- H710)', '2024-06-07 00:14:06', NULL, 'gpt-4o', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(2, 'OPEN_AI', 'GTP_3_5', 'https://api.openai.com/v1/chat/completions', '2024-05-26 17:10:39', '2024-05-26 17:14:19', 'gpt-3.5-turbo', '[API_KEY]', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(3, 'OPEN_AI', 'GTP_4_O', 'https://api.openai.com/v1/chat/completions', '2024-05-26 17:17:30', NULL, 'gpt-4o', '[API_KEY]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y'),
	(4, 'OPEN_AI', 'TEXT_TO_SPEECH', 'https://api.openai.com/v1/audio/speech', '2024-06-07 17:19:56', NULL, 'tts-1', '[API_KEY]', NULL, NULL, 'wav', 'alloy', '', '1', NULL, NULL, 'Y'),
	(5, 'OPEN_AI', 'TRANSCRIPTIONS', 'https://api.openai.com/v1/audio/transcriptions', '2024-06-07 00:28:21', NULL, 'whisper-1', '[API_KEY]', NULL, 'write to english', 'text', NULL, NULL, NULL, NULL, NULL, 'Y'),
	(6, 'OPEN_AI', 'TRANSLATE', 'https://api.openai.com/v1/audio/translations', '2024-06-07 00:28:21', NULL, 'whisper-1', '[API_KEY]', NULL, 'write to english', 'text', NULL, NULL, NULL, NULL, NULL, 'Y'),
	(8, 'OLLAMA', 'LLAMA3', 'http://localhost:11434/api/chat', '2024-05-26 17:17:30', NULL, 'llama3', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Y');
/*!40000 ALTER TABLE `tbm_sm_cnf` ENABLE KEYS */;

-- 테이블 데이터 nightmare.tbm_sm_prompts:~3 rows (대략적) 내보내기
/*!40000 ALTER TABLE `tbm_sm_prompts` DISABLE KEYS */;
REPLACE INTO `tbm_sm_prompts` (`GROUP`, `ID`, `DISPLAY_TEXT`, `PROMPT`, `USE_YN`, `DESCRIPTION`) VALUES
	('CONTEXT', '영어 문장 분석', '영어 문장 분석', '아래 포멧에 맞춰 중요해보이는 영어 2개~5개 사이로 정리해줘\r\n\r\n## 정리해주는 포멧은 아래와 같아.\r\n[순번].[단어] [한글 뜻][품사]([발음기호])\r\n#examples)\r\n [단어 사용 영문 예제, 한글뜻풀이]\r\n\r\n## 물어보려는 문장은 아래 문장이야.\r\n$content', 'Y', NULL),
	('SUPPORT', '오늘의 표현 ', '오늘의 표현 보여줘', 'Phrase of the Day\r\n        Create a sentence dialog for daily contextualized English learning in the following rule format\r\n        #rules \r\n        	1. 2 people talking to each other.\r\n        	2. the conversation centers on the latest Korean news.\r\n        	3. the characters are Young-hee and Chul-su.\r\n        	4. the dialog consists of 6 to 8 sentences of dialog content\r\n        	5. the dialog is organized in the following form\r\n        		[English]\r\n        		[Korean]\r\n        		example)\r\n        		[English]\r\n        		Chulsoo : BLABLA\r\n        		Younghee : BLABLA\r\n        		...\r\n        		[Korean].\r\n        		Cheolsu : BLABLA\r\n        		Younghee: Blabla\r\n        	6. important words, focusing on the content of the conversation\r\n        		Display format\r\n        			[order].[english word] [Korean meaning] [품사][발음기호]\r\n', 'Y', '오늘의 표현'),
	('WEB_SYSTEM', 'WEB', NULL, '다른 출력 포멧으로 변경할 경우 아래와 같은 텍스트를 응답 가장 마지막에 추가\r\n```OUT:{포멧이름}```\r\n', 'Y', NULL);
/*!40000 ALTER TABLE `tbm_sm_prompts` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;