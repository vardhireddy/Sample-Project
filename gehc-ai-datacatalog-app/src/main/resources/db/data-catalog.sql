create table if not exists image_set ( 
 id varchar(50) NOT NULL primary key,
schemaVersion varchar(5),
seriesId varchar(100),
studyId varchar(100),
patientId varchar(100),
orgId varchar(50),
orgName varchar(200),
permissionId varchar(50),
modality varchar(50),
anatomy varchar(100),
diseaseType varchar(100),
dataFormat varchar(100),
age int,
gender varchar(6),
uri varchar(500)
);

create table if not exists annotation_set (
id varchar(50) not null primary key,
data JSON not null
);

create table if not exists data_collection (
id varchar(50) not null primary key, 
data JSON not null
);

create table if not exists patient (
 id INT auto_increment primary key,
 schema_version varchar(50),
 patient_id varchar(255) NOT NULL,
 patient_name varchar(500),
 birth_date varchar(255),
 org_id varchar(255),
 gender varchar(50),
 age varchar(50),
 upload_date date NOT NULL,
 upload_by varchar(255),
 properties JSON
);

create table if not exists study (
 id INT auto_increment primary key,
 schema_version varchar(50),
 patient_dbid INT NOT NULL,
 study_id varchar(255),
 study_instance_uid varchar(255),
 study_description varchar(100),
 study_url varchar(500),
 org_id varchar(255),
 study_date varchar(255),
 study_time varchar(255),
 referring_physician varchar(255),
 upload_date date NOT NULL,
 upload_by varchar(255),
 properties JSON
);

create table if not exists cos_notification (
 id INT auto_increment primary key,
 time_stamp datetime,
 message text,
 patient_status varchar(500),
 study_status varchar(500),
 imageset_status varchar(500),
 annotation_status varchar(500)
);

alter table cos_notification 
drop column time_stamp,
drop column patient_status,
drop column study_status,
drop column imageset_status,
drop column annotation_status;

alter table cos_notification 
modify column message json;

alter table image_set
drop column age, 
drop column gender, 
drop column diseaseType, 
drop column permissionId, 
drop column patientId, 
drop column studyId, 
drop column orgName, 
drop column seriesId,
add series_instance_uid varchar(255),
add acq_date varchar(25),
add acq_time varchar(25),
add description varchar(100),
add institution varchar(100),
add equipment varchar(100),
add instance_count INT,
add upload_by varchar(255),
add upload_date datetime default current_timestamp,
add properties JSON, 
add patient_dbid INT,
add study_dbid INT;


create table if not exists annotation (
 id INT auto_increment primary key,
 schema_version varchar(50),
 org_id varchar(255) NOT NULL,
 annotator_id varchar(255),
 annotation_tool varchar(255),
 annotation_date datetime default current_timestamp,
 type varchar(100),
 image_set varchar(50) NOT NULL,
 item JSON
);

alter table annotation 
CHANGE COLUMN `annotation_date` `annotation_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

alter table data_collection 
add properties JSON;

alter table data_collection 
add org_id varchar(255);

alter table cos_notification 
add org_id varchar(255);

alter table patient  
CHANGE COLUMN `upload_date` `upload_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

alter table study  
CHANGE COLUMN `upload_date` `upload_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

create table if not exists data_set ( 
 id INT auto_increment primary key,
 schema_version varchar(5),
 org_id varchar(255),
 name varchar(200),
 type varchar(50),
 description varchar(500),
 image_sets JSON,
 created_by varchar(200),
 created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
 properties JSON
)


create table if not exists annotation_properties ( 
 id INT auto_increment primary key,
 schema_version varchar(5),
 org_id varchar(255),
 resource_name varchar(500),
 classes JSON, 
 created_by varchar(200),
 created_date DATETIME DEFAULT CURRENT_TIMESTAMP
 )
 
create table if not exists image_series ( 
id BIGINT auto_increment primary key,
schema_version varchar(50),
org_id varchar(255),
modality varchar(50),
anatomy varchar(100),
data_format varchar(100),
uri varchar(500),
series_instance_uid varchar(255),
description varchar(100),
institution varchar(100),
equipment varchar(100),
instance_count INT,
properties JSON, 
upload_by varchar(255),
upload_date datetime default current_timestamp,
patient_dbid INT,
study_dbid INT
);

ALTER TABLE lfdb.image_set MODIFY COLUMN id BIGINT(20); 

alter table lfdb.image_set  
CHANGE COLUMN `dataFormat` `data_format` varchar(100);

alter table lfdb.image_set  
CHANGE COLUMN `orgId` `org_id` varchar(255);

alter table lfdb.image_set  
CHANGE COLUMN `schemaVersion` `schema_version` varchar(50);

Alter table lfdb.image_set modify id BIGINT(20) AUTO_INCREMENT;

alter table image_set AUTO_INCREMENT=1635500015265;

alter table annotation modify image_set bigint(20);

alter table data_set 
add filters JSON;

alter table lfdb.image_set  
CHANGE COLUMN `equipment` `manufacturer` varchar(255);

alter table lfdb.image_set 
add `equipment` varchar(255) DEFAULT "N/A";

alter table lfdb.image_set 
add `image_type` varchar(50) DEFAULT "N/A";

alter table lfdb.image_set 
add `view` varchar(50) DEFAULT "N/A";

create table if not exists upload ( 
id BIGINT auto_increment primary key,
schema_version varchar(50),
org_id varchar(255),
data_type JSON,
data_usage varchar(500),
contract_id BIGINT,
space_id varchar(50),
summary JSON,
tags JSON,
status JSON,
upload_by varchar(255),
upload_date datetime default current_timestamp
);

create table if not exists contract ( 
id BIGINT auto_increment primary key,
schema_version varchar(50),
org_id varchar(255),
uri JSON,
contract_name varchar(255),
business_case varchar(500),
deid_status varchar(255),
usage_length INT,
data_origin_country varchar(255),
usage_rights varchar(255),
usage_notes varchar(500),
contact_info varchar(255),
active varchar(50),
properties JSON, 
upload_by varchar(255),
upload_date datetime default current_timestamp
);

alter table lfdb.image_set 
add `upload_id` bigint;

alter table lfdb.annotation
add `upload_id` bigint;

alter table contract
drop column contract_name, 
drop column contact_info, 
drop column usage_length, 
drop column business_case, 
drop column usage_rights, 
drop column usage_notes, 
drop column properties, 
add agreement_name varchar(255),
add primary_contact_email varchar(50),
add agreement_begin_date datetime,
add data_usage_period varchar(50),
add data_origin_state varchar(100),
add use_cases json,
add status varchar(50);

alter table lfdb.contract
drop column data_origin_country,
drop column data_origin_state,
add data_origin_countries_states json,
add data_location_allowed varchar(100);

alter table lfdb.contract
MODIFY COLUMN `agreement_begin_date` varchar(25);