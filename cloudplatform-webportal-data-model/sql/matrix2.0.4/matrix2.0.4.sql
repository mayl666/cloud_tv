-- 为WEBPORTAL_MCLUSTER_INFO表增加CANBACKUP列，代表是否可备份，默认为可备份
ALTER TABLE `WEBPORTAL_MCLUSTER_INFO` ADD COLUMN CANBACKUP TINYINT(4) DEFAULT 1 COMMENT "是否可备份";

/*Table structure for table `WEBPORTAL_GROUP` */

DROP TABLE IF EXISTS `WEBPORTAL_GROUP`;

CREATE TABLE `WEBPORTAL_GROUP` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL COMMENT '组名',
  `DESCN` varchar(100) DEFAULT NULL COMMENT '组描述',
  `USERID` bigint(20) NOT NULL COMMENT '创建人ID',
  `DELETED` tinyint(4) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` bigint(20) DEFAULT NULL,
  `UPDATE_USER` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `WEBPORTAL_GROUP_USER` */

DROP TABLE IF EXISTS `WEBPORTAL_GROUP_USER`;

CREATE TABLE `WEBPORTAL_GROUP_USER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GROUPID` bigint(20) NOT NULL,
  `USERID` bigint(20) NOT NULL,
  `OWNERID` bigint(20) NOT NULL COMMENT '拥有者ID',
  `DELETED` tinyint(4) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` bigint(20) DEFAULT NULL,
  `UPDATE_USER` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;