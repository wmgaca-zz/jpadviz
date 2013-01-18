/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`pad` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `pad`;

/*Table structure for table `experiment` */

DROP TABLE IF EXISTS `experiment`;

CREATE TABLE `experiment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `experiment_session` */

DROP TABLE IF EXISTS `experiment_session`;

CREATE TABLE `experiment_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `method_id` int(11) NOT NULL,
  `experiment_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `method_id` (`method_id`),
  KEY `experiment_id` (`experiment_id`),
  CONSTRAINT `experiment_session_ibfk_1` FOREIGN KEY (`method_id`) REFERENCES `method` (`id`),
  CONSTRAINT `experiment_session_ibfk_2` FOREIGN KEY (`experiment_id`) REFERENCES `experiment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

/*Table structure for table `method` */

DROP TABLE IF EXISTS `method`;

CREATE TABLE `method` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `pad_value` */

DROP TABLE IF EXISTS `pad_value`;

CREATE TABLE `pad_value` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `p` float NOT NULL DEFAULT '0',
  `a` float NOT NULL DEFAULT '0',
  `d` float NOT NULL DEFAULT '0',
  `cp` float NOT NULL DEFAULT '0',
  `ca` float NOT NULL DEFAULT '0',
  `cd` float NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0',
  `result_set_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `result_set_id` (`result_set_id`),
  CONSTRAINT `pad_value_ibfk_1` FOREIGN KEY (`result_set_id`) REFERENCES `result_set` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=186197 DEFAULT CHARSET=utf8;

/*Table structure for table `result_set` */

DROP TABLE IF EXISTS `result_set`;

CREATE TABLE `result_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `session_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `session_id` (`session_id`),
  CONSTRAINT `result_set_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `experiment_session` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
