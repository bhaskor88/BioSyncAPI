# ************************************************************
# Sequel Ace SQL dump
# Version 3028
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# Host: localhost (MySQL 8.0.23)
# Database: biosync
# Generation Time: 2021-04-27 12:20:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table auth_roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_roles`;

CREATE TABLE `auth_roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(300) DEFAULT NULL,
  `is_enable` tinyint(1) NOT NULL DEFAULT '1',
  `role_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `UK_37946nphvatiu5dysytsi8kv` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `auth_roles` WRITE;
/*!40000 ALTER TABLE `auth_roles` DISABLE KEYS */;

INSERT INTO `auth_roles` (`role_id`, `description`, `is_enable`, `role_name`)
VALUES
	(1,'Admin',1,'ADMIN'),
	(2,'User',1,'USER');

/*!40000 ALTER TABLE `auth_roles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table auth_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_user_role`;

CREATE TABLE `auth_user_role` (
  `user_id` bigint NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKehtjp118a6mmcj3liyac68kqx` (`role_id`),
  CONSTRAINT `FKaobnghxbq100urvkwl032wjrx` FOREIGN KEY (`user_id`) REFERENCES `auth_users` (`user_id`),
  CONSTRAINT `FKehtjp118a6mmcj3liyac68kqx` FOREIGN KEY (`role_id`) REFERENCES `auth_roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `auth_user_role` WRITE;
/*!40000 ALTER TABLE `auth_user_role` DISABLE KEYS */;

INSERT INTO `auth_user_role` (`user_id`, `role_id`)
VALUES
	(1,2),
	(2,2),
	(3,2),
	(4,2);

/*!40000 ALTER TABLE `auth_user_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table auth_users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `auth_users`;

CREATE TABLE `auth_users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `age` bigint NOT NULL,
  `blood_group` varchar(3) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `is_account_not_expired` tinyint(1) NOT NULL DEFAULT '1',
  `is_account_not_locked` tinyint(1) NOT NULL DEFAULT '1',
  `is_credentials_not_expired` tinyint(1) NOT NULL DEFAULT '1',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1',
  `mobile_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `notofication_token` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `auth_users` WRITE;
/*!40000 ALTER TABLE `auth_users` DISABLE KEYS */;

INSERT INTO `auth_users` (`user_id`, `created_by`, `created_on`, `updated_by`, `updated_on`, `age`, `blood_group`, `city`, `country`, `email`, `gender`, `is_account_not_expired`, `is_account_not_locked`, `is_credentials_not_expired`, `is_enable`, `mobile_number`, `name`, `password`, `state_province`, `username`, `zip_code`, `notofication_token`, `otp`)
VALUES
	(1,NULL,NULL,NULL,NULL,30,'O+','Dibrugarh','India','bhaskor88@gmail.com','Male',1,1,1,1,'7576847577','Bhaskor Sarmah','$2a$10$3oqbAatFP157SfQFpnX3q.UjfrDSKCnIJuLsknKKmDC8QcFO0Nqyy','Assam','bhaskor88','786001',NULL,NULL),
	(2,NULL,NULL,NULL,NULL,30,'O+','Dibrugarh','India','bhaskor@gmail.com','Male',1,1,1,1,'7576847577','Bhaskor Sarmah','$2a$10$QH2f.gzBLOeGUYJftUuxbOkiP7mNuA/uVGiI4N19oVyOTCnQ63Eo2','Assam','bhaskor','786001',NULL,NULL),
	(3,NULL,NULL,NULL,NULL,30,'O+','Dibrugarh','India','bhaskor12@gmail.com','Male',1,1,1,1,'7576847577','Bhaskor Sarmah','$2a$10$epOwJ0GdXvsKbLOOODbwROUQmWH7.gDVUjw4TGzFpMRRxJfSDp.dK','Assam','bhaskor123','786001',NULL,NULL),
	(4,NULL,NULL,NULL,NULL,30,'O+','Dibrugarh','India','bhaskor12@gmail.com','Male',1,1,1,1,'7576847577','Bhaskor Sarmah','$2a$10$oO8S.v.hIeDfqyVjORYNMO9qOeXOP.sY8/uzFkrKD60OONr1.AhLC','Assam','bhuju','786001',NULL,'821719');

/*!40000 ALTER TABLE `auth_users` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table master_qr_code
# ------------------------------------------------------------

DROP TABLE IF EXISTS `master_qr_code`;

CREATE TABLE `master_qr_code` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `batch_date` datetime DEFAULT NULL,
  `batch_id` varchar(255) DEFAULT NULL,
  `qr_code` varchar(255) DEFAULT NULL,
  `batch_details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table trans_family_member
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trans_family_member`;

CREATE TABLE `trans_family_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `age` bigint NOT NULL,
  `blood_group` varchar(3) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `master_user_id` bigint DEFAULT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FKk1anbx4jrab4e1nmlgfqeirjl` (`master_user_id`),
  CONSTRAINT `FKk1anbx4jrab4e1nmlgfqeirjl` FOREIGN KEY (`master_user_id`) REFERENCES `auth_users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `trans_family_member` WRITE;
/*!40000 ALTER TABLE `trans_family_member` DISABLE KEYS */;

INSERT INTO `trans_family_member` (`id`, `created_by`, `created_on`, `updated_by`, `updated_on`, `age`, `blood_group`, `email`, `gender`, `mobile_number`, `name`, `master_user_id`, `is_primary`)
VALUES
	(1,NULL,NULL,NULL,NULL,30,'O+','bhaskor88@gmail.com','Male','7576847577','Bhaskor Sarmah',1,1),
	(5,NULL,NULL,NULL,NULL,30,'O+','bhaskor@gmail.com','Male','7576847577','Bhaskor Sarmah',2,1),
	(6,NULL,NULL,NULL,NULL,30,'O+','bhaskor12@gmail.com','Male','7576847577','Bhaskor Sarmah',3,1),
	(7,NULL,NULL,NULL,NULL,30,'O+','bhaskor88@gmail.com','Male','7576847577','Bhaskor Sarmah',1,0),
	(8,NULL,NULL,NULL,NULL,30,'O+','bhaskor88@gmail.com','Male','7576847577','Bhaskor J Sarmah',1,0),
	(10,NULL,NULL,NULL,NULL,30,'O-','bhaskor88123@gmail.com','Female','7576847577','Bhaskor J Sarmah',4,1);

/*!40000 ALTER TABLE `trans_family_member` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table trans_test_result
# ------------------------------------------------------------

DROP TABLE IF EXISTS `trans_test_result`;

CREATE TABLE `trans_test_result` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `captured_image_path` varchar(255) DEFAULT NULL,
  `lat` varchar(255) DEFAULT NULL,
  `lng` varchar(255) DEFAULT NULL,
  `qr_code_data` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `test_result` varchar(255) DEFAULT NULL,
  `family_member_id` bigint DEFAULT NULL,
  `qr_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3ux1v4yrqi26nwhuc00mvgp1c` (`family_member_id`),
  CONSTRAINT `FK3ux1v4yrqi26nwhuc00mvgp1c` FOREIGN KEY (`family_member_id`) REFERENCES `trans_family_member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
