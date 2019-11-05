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

/*Table structure for table `access` */

DROP TABLE IF EXISTS `access`;

CREATE TABLE `access` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `total` int(11) NOT NULL COMMENT '总访问量',
  `PC` int(11) NOT NULL COMMENT 'PC端',
  `Android` int(11) NOT NULL COMMENT '安卓端',
  `ios` int(11) NOT NULL COMMENT 'ios端',
  `others` int(11) NOT NULL COMMENT '其他',
  `time` datetime(6) NOT NULL COMMENT '保存时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `access` */

/*Table structure for table `authority` */

DROP TABLE IF EXISTS `authority`;

CREATE TABLE `authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `banAccount` tinyint(1) NOT NULL COMMENT '封禁账号',
  `banSubmitTopic` tinyint(1) NOT NULL COMMENT '禁止发帖',
  `banSubmitReply` tinyint(1) NOT NULL COMMENT '禁止回复',
  `banMessage` tinyint(1) NOT NULL COMMENT '禁止私信',
  `deleteTopic` tinyint(1) NOT NULL COMMENT '删除帖子',
  `deleteReply` tinyint(1) NOT NULL COMMENT '删除回复',
  `lockTopic` tinyint(1) NOT NULL COMMENT '锁定帖子',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `authority` */

insert  into `authority`(`id`,`banAccount`,`banSubmitTopic`,`banSubmitReply`,`banMessage`,`deleteTopic`,`deleteReply`,`lockTopic`) values (1,0,0,0,0,0,0,0);

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `ip` varchar(255) NOT NULL COMMENT 'IP地址',
  `user_id` int(11) NOT NULL COMMENT '用户账号',
  `time` datetime(6) NOT NULL COMMENT '登录时间',
  `terminal` varchar(255) NOT NULL COMMENT '登录终端',
  PRIMARY KEY (`id`),
  KEY `login_user_id` (`user_id`),
  CONSTRAINT `login_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `login` */

/*Table structure for table `reply` */

DROP TABLE IF EXISTS `reply`;

CREATE TABLE `reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `topic_id` int(11) NOT NULL COMMENT '外键，帖子id',
  `user_id` int(11) NOT NULL COMMENT '外键，作者id',
  `submitTime` datetime(6) NOT NULL COMMENT '发布时间',
  `floor` int(11) NOT NULL COMMENT '楼层数',
  `quote` int(11) NOT NULL DEFAULT '0' COMMENT '引用楼层',
  `content` longtext NOT NULL COMMENT '回复内容',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `reply_topic_id` (`topic_id`),
  KEY `reply_user_id` (`user_id`),
  CONSTRAINT `reply_topic_id` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`),
  CONSTRAINT `reply_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `reply` */

/*Table structure for table `topic` */

DROP TABLE IF EXISTS `topic`;

CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `user_id` int(11) NOT NULL COMMENT '外键，作者id',
  `submitTime` datetime(6) NOT NULL COMMENT '发布时间',
  `label` varchar(4) NOT NULL COMMENT '帖子标签',
  `title` varchar(36) NOT NULL COMMENT '帖子标题',
  `content` longtext NOT NULL COMMENT '帖子内容',
  `replyCount` int(11) NOT NULL DEFAULT '0' COMMENT '回复数量',
  `viewCount` int(11) NOT NULL DEFAULT '0' COMMENT '查看数量',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定',
  `hided` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏',
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
  `email` varchar(48) NOT NULL COMMENT '邮箱',
  `gender` int(1) NOT NULL COMMENT '性别',
  `authority_id` int(11) NOT NULL DEFAULT '1' COMMENT '外键，权限id',
  `bannedTopic` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止发帖',
  `bannedReply` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止回复',
  `bannedMessage` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁止私信',
  `locded` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否封禁',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `topicCount` int(11) NOT NULL DEFAULT '0' COMMENT '发帖数量',
  `replyCount` int(11) NOT NULL DEFAULT '0' COMMENT '回复数量',
  `loginCount` int(11) NOT NULL DEFAULT '1' COMMENT '登录次数',
  `registerTime` datetime(6) NOT NULL COMMENT '注册时间',
  `photo` varchar(255) DEFAULT NULL COMMENT '头像路径',
  `comeFrom` varchar(255) DEFAULT NULL COMMENT '来自',
  `introduction` varchar(255) DEFAULT NULL COMMENT '自我介绍',
  PRIMARY KEY (`id`),
  KEY `user_authority_id` (`authority_id`),
  CONSTRAINT `user_authority_id` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
