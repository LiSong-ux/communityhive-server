/*
SQLyog Ultimate v12.08 (64 bit)
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

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `user_id` int(11) NOT NULL COMMENT '作者ID',
  `submitTime` datetime(6) NOT NULL COMMENT '发布时间',
  `label` varchar(4) NOT NULL COMMENT '公告标签',
  `title` varchar(36) NOT NULL COMMENT '公告标题',
  `content` longtext NOT NULL COMMENT '公告内容',
  `position` int(11) NOT NULL COMMENT '公告位置',
  `viewCount` int(11) NOT NULL DEFAULT '0' COMMENT '查看数量',
  `locked` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否锁定',
  `hided` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否隐藏',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `notice_user_id` (`user_id`),
  CONSTRAINT `notice_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `notice` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
