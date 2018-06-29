ALTER TABLE lfdb.upload DROP COLUMN data_usage;
ALTER TABLE lfdb.upload ADD last_modified datetime;
