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