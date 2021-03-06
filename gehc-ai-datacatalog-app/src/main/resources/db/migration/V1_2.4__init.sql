create index org_id_index on image_set(org_id) using BTREE;
create index modality_index on image_set(modality) using BTREE;
create index anatomy_index on image_set(anatomy) using BTREE;
create index data_format_index on image_set(data_format) using BTREE;
create index institution_index on image_set(institution) using BTREE;
create index manufacturer_index on image_set(manufacturer) using BTREE;
create index equipment_index on image_set(equipment) using BTREE;
create index image_type_index on image_set(image_type) using BTREE;
create index view_index on image_set(view) using BTREE;
create index series_instance_uid_index on image_set(series_instance_uid) using BTREE;
create index patient_dbid_index on image_set(patient_dbid) using BTREE;
create index org_id_index on patient(org_id) using BTREE;
create index org_id_index on annotation(org_id) using BTREE;
create index image_set_index on annotation(image_set) using BTREE;
create index type_index on annotation(type) using BTREE;