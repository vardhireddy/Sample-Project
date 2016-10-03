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
