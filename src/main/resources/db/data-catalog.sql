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
 schema_version varchar(5),
 patient_id varchar(100) NOT NULL,
 patient_name varchar(100),
 birth_date varchar(100),
 org varchar(50),
 gender varchar(10),
 age varchar(10),
 upload_date datetime default current_timestamp,
 upload_by varchar(100),
 properties JSON
);

create table if not exists study (
 id INT auto_increment primary key,
 schema_version varchar(5),
 patient_id varchar(100) NOT NULL,
 study_id varchar(100),
 study_instance_uid varchar(100),
 study_url varchar(200),
 org varchar(50),
 study_date varchar(50),
 study_time varchar(50),
 referring_physician varchar(50),
 upload_date datetime default current_timestamp,
 upload_by varchar(50),
 properties JSON
);
