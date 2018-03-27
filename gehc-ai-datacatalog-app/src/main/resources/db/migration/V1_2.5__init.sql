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
