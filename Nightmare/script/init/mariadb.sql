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

-- 테이블 nightmare.chat_history 구조 내보내기
CREATE TABLE IF NOT EXISTS `chat_history` (
  `ID` double NOT NULL,
  `SYSTEM` text DEFAULT NULL,
  `QUESTION` text DEFAULT NULL,
  `ANSWER` text DEFAULT NULL,
  `FIRST_REGER_ID` enum('USER','AI') DEFAULT NULL,
  `FST_REG_DT` timestamp NULL DEFAULT sysdate(),
  `AI_ID` double DEFAULT NULL,
  `SPEECH_ID` double DEFAULT NULL,
  `PROMPT_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 nightmare.speech_history 구조 내보내기
CREATE TABLE IF NOT EXISTS `speech_history` (
  `ID` double NOT NULL,
  `SYSTEM` text DEFAULT NULL,
  `QUESTION` mediumblob DEFAULT NULL,
  `ANSWER` text DEFAULT NULL,
  `CONTENT_TYPE` varchar(50) DEFAULT NULL,
  `FIRST_REGER_ID` enum('USER','AI') DEFAULT NULL,
  `FST_REG_DT` timestamp NULL DEFAULT sysdate(),
  `AI_ID` double DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 nightmare.tbm_sm_cnf 구조 내보내기
CREATE TABLE IF NOT EXISTS `tbm_sm_cnf` (
  `ID` double NOT NULL DEFAULT sysdate(),
  `GROUP` varchar(50) NOT NULL,
  `KEY` varchar(50) NOT NULL,
  `VALUE` varchar(500) DEFAULT NULL,
  `FST_REG_DT` datetime DEFAULT sysdate(),
  `FNL_UPD_DT` datetime DEFAULT NULL,
  `CNF_CMF_1` varchar(100) DEFAULT NULL COMMENT 'model',
  `CNF_CMF_2` varchar(100) DEFAULT NULL COMMENT 'api key',
  `CNF_CMF_3` varchar(100) DEFAULT NULL COMMENT 'language',
  `CNF_CMF_4` varchar(100) DEFAULT NULL COMMENT 'prompt',
  `CNF_CMF_5` varchar(100) DEFAULT NULL COMMENT 'response_format',
  `CNF_CMF_6` varchar(100) DEFAULT NULL COMMENT 'voice',
  `CNF_CMF_7` varchar(100) DEFAULT NULL COMMENT 'voice speed',
  `CNF_CMF_8` varchar(100) DEFAULT NULL,
  `CNF_CMF_9` varchar(100) DEFAULT NULL,
  `CNF_CMF_10` varchar(100) DEFAULT NULL,
  `USE_YN` varchar(50) DEFAULT 'Y',
  PRIMARY KEY (`ID`,`GROUP`,`KEY`),
  UNIQUE KEY `ID` (`ID`),
  UNIQUE KEY `KEY` (`KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='시스템 설정 관련 테이블';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 nightmare.tbm_sm_prompts 구조 내보내기
CREATE TABLE IF NOT EXISTS `tbm_sm_prompts` (
  `GROUP` varchar(50) NOT NULL COMMENT '시스템 역할',
  `ID` varchar(50) NOT NULL DEFAULT '(cast(unix_timestamp(sysdate()) as unsigned))' COMMENT '고유 ID',
  `DISPLAY_TEXT` varchar(50) DEFAULT NULL COMMENT '메뉴에 보이는 텍스트',
  `PROMPT` text DEFAULT NULL COMMENT '수행 명령어',
  `USE_YN` varchar(1) DEFAULT 'Y' COMMENT '사용여부',
  `DESCRIPTION` text DEFAULT NULL,
  `GRAPHIC_CLASS` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`GROUP`,`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='시스템 옵션으로 추가될 프롬프트 정보 입력';

-- 내보낼 데이터가 선택되어 있지 않습니다.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
