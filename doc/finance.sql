-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- 主機: localhost
-- 產生日期: 2013 年 07 月 08 日 23:06
-- 伺服器版本: 5.1.69
-- PHP 版本: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 資料庫: `finance`
--
CREATE DATABASE IF NOT EXISTS `finance` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `finance`;

-- --------------------------------------------------------

--
-- 表的結構 `balancesheets`
--

DROP TABLE IF EXISTS `balancesheets`;
CREATE TABLE IF NOT EXISTS `balancesheets` (
  `season` char(6) NOT NULL DEFAULT '000000',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  `name` varchar(10) NOT NULL DEFAULT '',
  `currentassets` bigint(20) NOT NULL DEFAULT '0',
  `longterminvestments` bigint(20) NOT NULL DEFAULT '0',
  `fixedassets` bigint(20) NOT NULL DEFAULT '0',
  `intangibleassets` bigint(20) NOT NULL DEFAULT '0',
  `otherassets` bigint(20) NOT NULL DEFAULT '0',
  `totalassets` bigint(20) NOT NULL DEFAULT '0',
  `currentliabilities` bigint(20) NOT NULL DEFAULT '0',
  `longtermliabilities` bigint(20) NOT NULL DEFAULT '0',
  `otherliabilities` bigint(20) NOT NULL DEFAULT '0',
  `totalliabilities` bigint(20) NOT NULL DEFAULT '0',
  `capital` bigint(20) NOT NULL DEFAULT '0',
  `capitalsurplus` bigint(20) NOT NULL DEFAULT '0',
  `retainedearnings` bigint(20) NOT NULL DEFAULT '0',
  `equityadjustments` bigint(20) NOT NULL DEFAULT '0',
  `treasurystock` bigint(20) NOT NULL DEFAULT '0',
  `stockholdersequity` bigint(20) NOT NULL DEFAULT '0',
  `bookvaluepershare` double NOT NULL DEFAULT '0',
  `proceedsnewissue` bigint(20) NOT NULL DEFAULT '0',
  `totaltreasurystock` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`symbol`,`season`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `choosingstockresults`
--

DROP TABLE IF EXISTS `choosingstockresults`;
CREATE TABLE IF NOT EXISTS `choosingstockresults` (
  `today` char(8) NOT NULL DEFAULT '00000000',
  `cssid` smallint(6) NOT NULL DEFAULT '0',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`today`,`cssid`,`symbol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `choosingstockstrategies`
--

DROP TABLE IF EXISTS `choosingstockstrategies`;
CREATE TABLE IF NOT EXISTS `choosingstockstrategies` (
  `cssid` smallint(6) NOT NULL default '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `auto` tinyint(1) NOT NULL DEFAULT '0',
  `rfid` smallint(6) NOT NULL DEFAULT '0',
  `expression` varchar(500) NOT NULL DEFAULT '',
  PRIMARY KEY (`cssid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的結構 `incomestatements`
--

DROP TABLE IF EXISTS `incomestatements`;
CREATE TABLE IF NOT EXISTS `incomestatements` (
  `season` char(6) NOT NULL DEFAULT '000000',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  `name` varchar(10) NOT NULL DEFAULT '',
  `eps` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`symbol`,`season`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `operatingrevenues`
--

DROP TABLE IF EXISTS `operatingrevenues`;
CREATE TABLE IF NOT EXISTS `operatingrevenues` (
  `month` char(6) NOT NULL DEFAULT '000000',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  `name` varchar(10) NOT NULL DEFAULT '',
  `currentmonthrevenues` bigint(20) NOT NULL DEFAULT '0',
  `precedingmonthrevenues` bigint(20) NOT NULL DEFAULT '0',
  `precedingyearrevenues` bigint(20) NOT NULL DEFAULT '0',
  `mom` double NOT NULL DEFAULT '0',
  `yoy` double NOT NULL DEFAULT '0',
  `cumulativecurrentyearrevenues` bigint(20) NOT NULL DEFAULT '0',
  `cumulativeprecedingyearrevenues` bigint(20) NOT NULL DEFAULT '0',
  `cyoy` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`symbol`,`month`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `selfchoosingstocks`
--

DROP TABLE IF EXISTS `selfchoosingstocks`;
CREATE TABLE IF NOT EXISTS `selfchoosingstocks` (
  `today` char(8) NOT NULL DEFAULT '00000000',
  `cssid` smallint(6) NOT NULL DEFAULT '0',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`today`,`cssid`,`symbol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `stockrecords`
--

DROP TABLE IF EXISTS `stockrecords`;
CREATE TABLE IF NOT EXISTS `stockrecords` (
  `today` char(8) NOT NULL DEFAULT '00000000',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  `name` varchar(10) NOT NULL DEFAULT '',
  `volume` bigint(20) NOT NULL DEFAULT '0',
  `transaction` bigint(20) NOT NULL DEFAULT '0',
  `value` bigint(20) NOT NULL DEFAULT '0',
  `open` double NOT NULL DEFAULT '0',
  `high` double NOT NULL DEFAULT '0',
  `low` double NOT NULL DEFAULT '0',
  `close` double NOT NULL DEFAULT '0',
  `direction` varchar(2) NOT NULL DEFAULT '',
  `change` double NOT NULL DEFAULT '0',
  `lastbidprice` double NOT NULL DEFAULT '0',
  `lastbidvolume` bigint(20) NOT NULL DEFAULT '0',
  `lastaskprice` double NOT NULL DEFAULT '0',
  `lastaskvolume` bigint(20) NOT NULL DEFAULT '0',
  `per` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`symbol`,`today`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的結構 `technicalanalyses`
--

DROP TABLE IF EXISTS `technicalanalyses`;
CREATE TABLE IF NOT EXISTS `technicalanalyses` (
  `today` char(8) NOT NULL DEFAULT '00000000',
  `symbol` varchar(10) NOT NULL DEFAULT '',
  `name` varchar(10) NOT NULL DEFAULT '',
  `suma5` double NOT NULL DEFAULT '0',
  `ma5` double NOT NULL DEFAULT '0',
  `bias5` double NOT NULL DEFAULT '0',
  `sumv5` double NOT NULL DEFAULT '0',
  `mv5` double NOT NULL DEFAULT '0',
  `suma10` double NOT NULL DEFAULT '0',
  `ma10` double NOT NULL DEFAULT '0',
  `bias10` double NOT NULL DEFAULT '0',
  `sumv10` double NOT NULL DEFAULT '0',
  `mv10` double NOT NULL DEFAULT '0',
  `suma20` double NOT NULL DEFAULT '0',
  `ma20` double NOT NULL DEFAULT '0',
  `bias20` double NOT NULL DEFAULT '0',
  `sumv20` double NOT NULL DEFAULT '0',
  `mv20` double NOT NULL DEFAULT '0',
  `suma30` double NOT NULL DEFAULT '0',
  `ma30` double NOT NULL DEFAULT '0',
  `bias30` double NOT NULL DEFAULT '0',
  `sumv30` double NOT NULL DEFAULT '0',
  `mv30` double NOT NULL DEFAULT '0',
  `suma60` double NOT NULL DEFAULT '0',
  `ma60` double NOT NULL DEFAULT '0',
  `bias60` double NOT NULL DEFAULT '0',
  `sumv60` double NOT NULL DEFAULT '0',
  `mv60` double NOT NULL DEFAULT '0',
  `k9` double NOT NULL DEFAULT '0',
  `d9` double NOT NULL DEFAULT '0',
  `ema12` double NOT NULL DEFAULT '0',
  `ema26` double NOT NULL DEFAULT '0',
  `dif` double NOT NULL DEFAULT '0',
  `dem9` double NOT NULL DEFAULT '0',
  `dsm9` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`symbol`,`today`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
