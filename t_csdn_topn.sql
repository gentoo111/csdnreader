/*
 Navicat Premium Data Transfer

 Source Server         : any
 Source Server Type    : MySQL
 Source Server Version : 50553
 Source Host           : localhost:3306
 Source Schema         : csdnreader

 Target Server Type    : MySQL
 Target Server Version : 50553
 File Encoding         : 65001

 Date: 25/01/2020 19:09:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_csdn_topn
-- ----------------------------
DROP TABLE IF EXISTS `t_csdn_topn`;
CREATE TABLE `t_csdn_topn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ranking` varchar(20) DEFAULT NULL COMMENT '排名',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `nowVotes` varchar(20) DEFAULT NULL COMMENT '票数',
  `createDate` datetime DEFAULT NULL COMMENT '采集时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=555967 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
