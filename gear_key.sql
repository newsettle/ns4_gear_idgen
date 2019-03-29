/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50616
 Source Host           : localhost
 Source Database       : ns4_gear

 Target Server Type    : MySQL
 Target Server Version : 50616
 File Encoding         : utf-8

 Date: 01/07/2019 16:42:53 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `gear_key`
-- ----------------------------
DROP TABLE IF EXISTS `gear_key`;
CREATE TABLE `gear_key` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(50) DEFAULT NULL,
  `key_value` decimal(50,0) DEFAULT NULL,
  `key_length` decimal(10,0) DEFAULT NULL,
  `key_cache` decimal(20,0) DEFAULT NULL,
  `key_prefix` varchar(20) DEFAULT NULL,
  `key_suffix` varchar(20) DEFAULT NULL,
  `creator` varchar(20) DEFAULT NULL COMMENT '[创建人]',
  `updator` varchar(20) DEFAULT NULL COMMENT '[修改人]',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '[创建时间]',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '[最后一次操作时间]',
  `key_digit` varchar(5) DEFAULT NULL COMMENT '对账标记号生成规则',
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_name_unique` (`key_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用来记录联机系统跟银行交互的报文信息';

-- ----------------------------
--  Records of `gear_key`
-- ----------------------------
BEGIN;
INSERT INTO `gear_key` VALUES ('1', 'TEST-KEY', '520216', '20', '1000', '${date6}', '', null, null, '2015-09-09 19:54:05', '2019-01-04 19:38:33', '10', '2725'), ('2', 'TEST-KEY1', '181325', '20', '1', '${date8}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181316'), ('3', 'TEST-KEY2', '181076', '20', '10', '${date10}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:39:59', '10', '150314'), ('4', 'TEST-KEY3', '181408', '20', '1', '${date12}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181399'), ('5', 'TEST-KEY4', '181577', '22', '1', '${date14}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181568'), ('6', 'TEST-KEY5', '181499', '24', '1', '${date15}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181490'), ('7', 'TEST-KEY6', '181303', '26', '1', '${date16}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181294'), ('8', 'TEST-KEY7', '181533', '26', '1', '${date17}', null, null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181524'), ('9', 'TEST-KEY8', '181490', '26', '1', 'T${date12}', '$', null, null, '2018-11-16 11:45:58', '2019-01-04 19:40:01', '10', '181481'), ('10', 'TEST-KEY9', '181686', '16', '1', 'TS${date6}', null, null, null, '2018-11-16 13:49:52', '2019-01-04 19:40:01', '10', '181587');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
