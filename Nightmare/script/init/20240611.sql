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


-- nightmare 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `nightmare` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
USE `nightmare`;

-- 테이블 nightmare.tbm_sm_prompts 구조 내보내기
CREATE TABLE IF NOT EXISTS `tbm_sm_prompts` (
  `GROUP` varchar(50) NOT NULL,
  `ID` varchar(50) NOT NULL DEFAULT '(cast(unix_timestamp(sysdate()) as unsigned))',
  `DISPLAY_TEXT` varchar(50) DEFAULT NULL,
  `PROMPT` text DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT 'Y',
  PRIMARY KEY (`GROUP`,`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 nightmare.tbm_sm_prompts:~2 rows (대략적) 내보내기
DELETE FROM `tbm_sm_prompts`;
/*!40000 ALTER TABLE `tbm_sm_prompts` DISABLE KEYS */;
INSERT INTO `tbm_sm_prompts` (`GROUP`, `ID`, `DISPLAY_TEXT`, `PROMPT`, `USE_YN`) VALUES
	('CONTEXT', '영어 문장 분석', '영어 문장 분석', '아래 문장에 중요한 영어 2개~5개 사이로 정리해주고 아래 포멧에 맞게 내용을 분석해줘\r\n\r\n## 포멧\r\n[순번].[단어] [한글 뜻][품사]([발음기호])\r\n[예제]\r\n\r\n##문장\r\n$content\r\n', 'Y'),
	('SUPPORT', '오늘의 표현', '오늘의 표현', '최신 뉴스를 로드하고 중요해보이는 기사 10개와 각 기사에 대한 영어 표현 3개씩 정리해줘\r\n\r\n## 영어 표현 \r\n1. 중요한 단어를 3~5개 적어주세요.\r\n2. 단어들에 대한 영어 표현 예시 작성해주세요.', 'Y');
/*!40000 ALTER TABLE `tbm_sm_prompts` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
