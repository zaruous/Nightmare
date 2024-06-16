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

-- 테이블 데이터 nightmare.tbm_sm_prompts:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `tbm_sm_prompts` DISABLE KEYS */;
REPLACE INTO `tbm_sm_prompts` (`GROUP`, `ID`, `DISPLAY_TEXT`, `PROMPT`, `USE_YN`, `DESCRIPTION`, `GRAPHIC_CLASS`) VALUES
	('CONTEXT', '영어 문장 분석', '영어 문장 분석', '아래 포멧에 맞춰 중요해보이는 영어 2개~5개 사이로 정리해줘\r\n\r\n## 정리해주는 포멧은 아래와 같아.\r\n[순번].[단어] [한글 뜻][품사]([발음기호])\r\n#examples)\r\n [단어 사용 영문 예제, 한글뜻풀이]\r\n\r\n## 물어보려는 문장은 아래 문장이야.\r\n$content', 'Y', NULL, NULL),
	('SUPPORT', '오늘의 표현 ', '오늘의 표현 보여줘', 'Phrase of the Day\r\n        Create a sentence dialog for daily contextualized English learning in the following rule format\r\n        #rules \r\n        	1. 2 people talking to each other.\r\n        	2. the conversation centers on the latest Korean news.\r\n        	3. the characters are Young-hee and Chul-su.\r\n        	4. the dialog consists of 6 to 8 sentences of dialog content\r\n        	5. the dialog is organized in the following form\r\n        		[English]\r\n        		[Korean]\r\n        		example)\r\n        		[English]\r\n        		Chulsoo : BLABLA\r\n        		Younghee : BLABLA\r\n        		...\r\n        		[Korean].\r\n        		Cheolsu : BLABLA\r\n        		Younghee: Blabla\r\n        	6. important words, focusing on the content of the conversation\r\n        		Display format\r\n        			[order].[english word] [Korean meaning] [품사][발음기호]\r\n', 'Y', '오늘의 표현', NULL),
	('SUPPORT', '퀴즈', '영어 문제', '##영어문제\n\n영어 작문을 위한 문제를 제시해줘.  전체 문장은 영어로 번역해서 보여줘\n어떤  상황에 대해 question에 대해 적어주0 question에 대한 정답을 맞추는 문제야.\n맞는 정답 1개, 정답 절대 될 수 없는 문장 4개로 구성되며, 1번~5번사이로 정답번호는 랜덤으로 처리.\n기로그 reason에는 정답에 대한 설명을 기술해줘.\n답변 구성은 example을 참고해서 json포멧으로 알려줘\nexample)\n{\n	"question" : "철수 오늘 하루 기분이 어떄?",\n	"questionList": [\n		{"no": 1, "text" : "모기가 많다.",  "correct" : false }\n		{"no": 2, "text" : "밥이 맜었어",  "correct" : false }\n		{"no": 3, "text" : "날씨가 더워"",  "correct" : false }\n		{"no": 4, "text" : "그는 거짓말을 했다.",  "correct" : false }\n		{"no": 5, "text" : "오늘 하루 기분이 좋지않았어.",  "correct" : true }\n	]\n	"reason" : "정답은 5번으로 철수의 하루에 기분이 어떤지 물어보는 질문이고, 기분에 대한 응답을 한 문장은 5번이다."\n}', 'Y', NULL, 'com.kyj.fx.nightmare.actions.ai.QuestionComposite'),
	('WEB_SYSTEM', 'WEB', NULL, '다른 출력 포멧으로 변경할 경우 아래와 같은 텍스트를 응답 가장 마지막에 추가\r\n```OUT:{포멧이름}```\r\n', 'Y', NULL, NULL);
/*!40000 ALTER TABLE `tbm_sm_prompts` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
