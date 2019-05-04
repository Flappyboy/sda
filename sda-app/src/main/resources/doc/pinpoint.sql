/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : pinpoint

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2019-03-02 15:27:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for algorithms
-- ----------------------------
DROP TABLE IF EXISTS `algorithms`;
CREATE TABLE `algorithms` (
  `id` varchar(64) NOT NULL COMMENT '算法表id',
  `name` varchar(255) DEFAULT NULL COMMENT '算法名称',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '修改时间',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for algorithms_param
-- ----------------------------
DROP TABLE IF EXISTS `algorithms_param`;
CREATE TABLE `algorithms_param` (
  `id` varchar(64) NOT NULL COMMENT '算法参数表id',
  `algorithmsId` varchar(64) DEFAULT NULL COMMENT '算法表id',
  `param` varchar(255) DEFAULT NULL COMMENT '算法参数',
  `order` int(64) DEFAULT NULL COMMENT '参数次序',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可以，1-可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` varchar(64) NOT NULL COMMENT '项目表id',
  `name` varchar(255) NOT NULL COMMENT '项目名称',
  `path` varchar(255) DEFAULT NULL COMMENT '项目路径',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `nodeNumber` int(255) DEFAULT NULL COMMENT '结点个数',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可以，1-可用）',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `classCount` int(255) DEFAULT NULL,
  `interfaceCount` int(255) DEFAULT NULL,
  `functionCount` int(255) DEFAULT NULL,
  `interFaceFunctionCount` int(255) DEFAULT NULL,
  `status` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for class_node
-- ----------------------------
DROP TABLE IF EXISTS `class_node`;
CREATE TABLE `class_node` (
  `id` varchar(64) NOT NULL COMMENT '类结点表id',
  `name` varchar(255) DEFAULT NULL COMMENT '类名称',
  `key` int(64) DEFAULT NULL COMMENT '类结点键',
  `appId` varchar(64) DEFAULT NULL COMMENT '项目id',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `type` int(1) DEFAULT NULL COMMENT '类型（0-类，1-接口）',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dynamic_analysis_info
-- ----------------------------
DROP TABLE IF EXISTS `dynamic_analysis_info`;
CREATE TABLE `dynamic_analysis_info` (
  `id` varchar(64) NOT NULL COMMENT '动态分析表id',
  `appId` varchar(64) DEFAULT NULL COMMENT '项目id',
  `startTine` datetime DEFAULT NULL COMMENT '动态分析开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '动态分析结束时间',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-可用，1-不可用）',
  `type` int(1) DEFAULT NULL COMMENT '结点类型（0-类结点，1-方法结点）',
  `desc` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dynamic_call_info
-- ----------------------------
DROP TABLE IF EXISTS `dynamic_call_info`;
CREATE TABLE `dynamic_call_info` (
  `id` varchar(64) NOT NULL COMMENT '动态调用信息表id',
  `caller` varchar(64) DEFAULT NULL COMMENT '调用结点id',
  `callee` varchar(64) DEFAULT NULL COMMENT '被调用结点id',
  `count` int(32) DEFAULT NULL COMMENT '调用次数',
  `dynamicAnalysisInfoId` varchar(64) DEFAULT NULL COMMENT '动态分析表id',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `isInclude` int(1) DEFAULT NULL COMMENT '是否在静态调用中被包含（0-未被包含，1-被包含）',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `type` int(1) DEFAULT NULL COMMENT '结点类型（0-类结点，1-方法结点）',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for git
-- ----------------------------
DROP TABLE IF EXISTS `git`;
CREATE TABLE `git` (
  `id` varchar(64) NOT NULL COMMENT '项目表id',
  `app_id` varchar(255) NOT NULL COMMENT '项目名称',
  `path` varchar(255) DEFAULT NULL COMMENT '项目路径',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `logic_coupling_factor` int(255) DEFAULT NULL,
  `modify_frequency_factor` int(255) DEFAULT NULL,
  `reliability_factor` int(255) DEFAULT NULL,
  `status` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for method_node
-- ----------------------------
DROP TABLE IF EXISTS `method_node`;
CREATE TABLE `method_node` (
  `id` varchar(64) NOT NULL COMMENT '方法结点id',
  `name` varchar(255) DEFAULT NULL COMMENT '方法名称',
  `classId` varchar(64) DEFAULT NULL COMMENT '类结点id',
  `key` int(64) DEFAULT NULL COMMENT '方法结点键',
  `appId` varchar(64) DEFAULT NULL COMMENT '项目id',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `desc` varchar(255) DEFAULT NULL,
  `className` varchar(255) DEFAULT NULL,
  `fullName` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for partition_detail
-- ----------------------------
DROP TABLE IF EXISTS `partition_detail`;
CREATE TABLE `partition_detail` (
  `id` varchar(64) NOT NULL COMMENT '划分细节表id',
  `nodeId` varchar(64) DEFAULT NULL COMMENT '结点id',
  `patitionResultId` varchar(64) DEFAULT NULL COMMENT '划分结果表id',
  `createdAt` datetime DEFAULT NULL COMMENT '开始时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '结束时间',
  `type` int(1) DEFAULT NULL COMMENT '结点类型（0-类结点，1-方法结点）',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for partition_info
-- ----------------------------
DROP TABLE IF EXISTS `partition_info`;
CREATE TABLE `partition_info` (
  `id` varchar(64) NOT NULL,
  `appId` varchar(64) DEFAULT NULL,
  `dynamicAnalysisInfoId` varchar(64) DEFAULT NULL,
  `algorithmsId` varchar(64) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `flag` int(1) DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for partition_result
-- ----------------------------
DROP TABLE IF EXISTS `partition_result`;
CREATE TABLE `partition_result` (
  `id` varchar(64) NOT NULL COMMENT '划分结果id',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `algorithmsId` varchar(64) DEFAULT NULL COMMENT '算法id',
  `dynamicAnalysisInfoId` varchar(64) DEFAULT NULL COMMENT '动态分析id',
  `appId` varchar(64) DEFAULT NULL COMMENT '项目id',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `type` int(1) DEFAULT NULL COMMENT '结点类型（0-类结点，1-方法结点）',
  `order` int(64) DEFAULT NULL COMMENT '结果排序',
  `partitionId` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for partition_result_edge
-- ----------------------------
DROP TABLE IF EXISTS `partition_result_edge`;
CREATE TABLE `partition_result_edge` (
  `id` varchar(64) NOT NULL,
  `patitionResultAId` varchar(64) NOT NULL,
  `patitionResultBId` varchar(64) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for partition_result_edge_call
-- ----------------------------
DROP TABLE IF EXISTS `partition_result_edge_call`;
CREATE TABLE `partition_result_edge_call` (
  `id` varchar(64) NOT NULL,
  `edgeId` varchar(64) DEFAULT NULL,
  `callId` varchar(64) DEFAULT NULL,
  `callType` int(1) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for static_call_info
-- ----------------------------
DROP TABLE IF EXISTS `static_call_info`;
CREATE TABLE `static_call_info` (
  `id` varchar(64) NOT NULL COMMENT '静态信息id',
  `caller` varchar(64) DEFAULT NULL COMMENT '调用结点id',
  `callee` varchar(64) DEFAULT NULL COMMENT '被调用结点id',
  `count` int(64) DEFAULT NULL COMMENT '调用次数',
  `appId` varchar(64) DEFAULT NULL COMMENT '项目id',
  `createdAt` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedAt` datetime DEFAULT NULL COMMENT '更新时间',
  `flag` int(1) DEFAULT NULL COMMENT '是否可用（0-不可用，1-可用）',
  `type` int(1) DEFAULT NULL COMMENT '结点类型（0-类结点，1-方法结点）',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
