alter table image_set  
CHANGE COLUMN `manufacturer` `manufacturer` varchar(255);

alter table image_set 
add `equipment` varchar(255);

alter table image_set 
add `image_type` varchar(50);

alter table image_set 
add `view` varchar(50);