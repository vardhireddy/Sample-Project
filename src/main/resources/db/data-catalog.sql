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