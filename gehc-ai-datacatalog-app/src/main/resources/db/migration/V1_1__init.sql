CREATE TABLE if not exists `annotation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schema_version` varchar(50) DEFAULT NULL,
  `org_id` varchar(255) NOT NULL,
  `annotator_id` varchar(255) DEFAULT NULL,
  `annotation_tool` varchar(255) DEFAULT NULL,
  `annotation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(100) DEFAULT NULL,
  `image_set` varchar(50) NOT NULL,
  `item` json DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `annotation_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schema_version` varchar(5) DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  `resource_name` varchar(500) DEFAULT NULL,
  `classes` json DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `cos_notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` json DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `data_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schema_version` varchar(5) DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `image_sets` json DEFAULT NULL,
  `created_by` varchar(200) DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `properties` json DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `image_set` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `schema_version` varchar(50) DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  `modality` varchar(50) DEFAULT NULL,
  `anatomy` varchar(100) DEFAULT NULL,
  `data_format` varchar(100) DEFAULT NULL,
  `uri` varchar(500) DEFAULT NULL,
  `series_instance_uid` varchar(255) DEFAULT NULL,
  `acq_date` varchar(25) DEFAULT NULL,
  `acq_time` varchar(25) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `institution` varchar(100) DEFAULT NULL,
  `equipment` varchar(100) DEFAULT NULL,
  `instance_count` int(11) DEFAULT NULL,
  `upload_by` varchar(255) DEFAULT NULL,
  `upload_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `properties` json DEFAULT NULL,
  `patient_dbid` int(11) DEFAULT NULL,
  `study_dbid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `patient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schema_version` varchar(50) DEFAULT NULL,
  `patient_id` varchar(255) NOT NULL,
  `patient_name` varchar(500) DEFAULT NULL,
  `birth_date` varchar(255) DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `age` varchar(50) DEFAULT NULL,
  `upload_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upload_by` varchar(255) DEFAULT NULL,
  `properties` json DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `study` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schema_version` varchar(50) DEFAULT NULL,
  `patient_dbid` int(11) NOT NULL,
  `study_id` varchar(255) DEFAULT NULL,
  `study_instance_uid` varchar(255) DEFAULT NULL,
  `study_description` varchar(100) DEFAULT NULL,
  `study_url` varchar(500) DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  `study_date` varchar(255) DEFAULT NULL,
  `study_time` varchar(255) DEFAULT NULL,
  `referring_physician` varchar(255) DEFAULT NULL,
  `upload_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upload_by` varchar(255) DEFAULT NULL,
  `properties` json DEFAULT NULL,
  PRIMARY KEY (`id`)
);