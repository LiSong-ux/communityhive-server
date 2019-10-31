/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.28 : Database - communityhive_3u0
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`communityhive_3u0` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `communityhive_3u0`;

/*Table structure for table `authority` */

DROP TABLE IF EXISTS `authority`;

CREATE TABLE `authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `banAccount` tinyint(1) NOT NULL COMMENT '封禁用户账号',
  `banSubmitTopic` tinyint(1) NOT NULL COMMENT '禁止用户发帖',
  `banSubmitReply` tinyint(1) NOT NULL COMMENT '禁止用户回复',
  `banMessage` tinyint(1) NOT NULL COMMENT '禁止用户私信',
  `deleteTopic` tinyint(1) NOT NULL COMMENT '删除帖子',
  `deleteReply` tinyint(1) NOT NULL COMMENT '删除回复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `authority` */

/*Table structure for table `reply` */

DROP TABLE IF EXISTS `reply`;

CREATE TABLE `reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `topic_id` int(11) NOT NULL COMMENT '外键，帖子id',
  `user_id` int(11) NOT NULL COMMENT '外键，作者id',
  `submitTime` datetime(6) NOT NULL COMMENT '发布时间',
  `floor` int(11) NOT NULL COMMENT '楼层数',
  `quote` int(11) NOT NULL DEFAULT '0' COMMENT '引用的楼层',
  `content` longtext NOT NULL COMMENT '回复内容',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `topic_reply_id` (`topic_id`),
  KEY `reply_user_id` (`user_id`),
  CONSTRAINT `reply_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `topic_reply_id` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `reply` */

/*Table structure for table `topic` */

DROP TABLE IF EXISTS `topic`;

CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `label` varchar(4) NOT NULL COMMENT '帖子标签',
  `title` varchar(35) NOT NULL COMMENT '帖子标题',
  `user_id` int(11) NOT NULL COMMENT '外键，作者id',
  `submitTime` datetime(6) NOT NULL COMMENT '发布时间',
  `content` longtext NOT NULL COMMENT '帖子内容',
  `replyCount` int(11) NOT NULL DEFAULT '0' COMMENT '回复数量',
  `viewCount` int(11) NOT NULL DEFAULT '0' COMMENT '查看数量',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `topic_user_id` (`user_id`),
  CONSTRAINT `topic_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `topic` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `account` varchar(32) NOT NULL COMMENT '账号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `username` varchar(24) NOT NULL COMMENT '用户名',
  `authority_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限id',
  `bannedTopic` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止发帖',
  `bannedReply` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止回复',
  `bannedMessage` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止私信',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否封禁',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `email` varchar(48) NOT NULL COMMENT '用户邮箱',
  `gender` int(1) NOT NULL COMMENT '性别',
  `topicCount` int(11) NOT NULL DEFAULT '0' COMMENT '发帖数量',
  `replyCount` int(11) NOT NULL DEFAULT '0' COMMENT '回复数量',
  `registerTime` datetime(6) NOT NULL COMMENT '注册时间',
  `introduction` varchar(255) DEFAULT NULL COMMENT '自我介绍',
  `comeFrom` varchar(255) DEFAULT NULL COMMENT '来自于',
  `photo` varchar(255) DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`),
  KEY `user_authority_id` (`authority_id`),
  CONSTRAINT `user_authority_id` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
