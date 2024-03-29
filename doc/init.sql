CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- *****************************************************************************
-- ADMINISTRATION
-- *****************************************************************************
CREATE SCHEMA adm;

-- Setting
CREATE TABLE adm.setting (
    id serial NOT NULL PRIMARY KEY,
    name varchar(32) NOT NULL,
    value varchar(255)
);
ALTER TABLE adm.setting OWNER to tcm;

-- User
CREATE TABLE adm.user (
    id serial NOT NULL PRIMARY KEY,
    first_name varchar(32) NOT NULL,
    last_name varchar(32) NOT NULL,
    email varchar(64) NOT NULL UNIQUE,
    password char(40) NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE adm.user OWNER to tcm;

-- UserGroup
CREATE TABLE adm.user_group (
    id serial NOT NULL PRIMARY KEY,
    name varchar(32) NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE adm.user_group OWNER to tcm;

-- User2UserGroup 
CREATE TABLE adm.user2user_group (
    user_id integer REFERENCES adm.user(id),
	user_group_id integer REFERENCES adm.user_group(id)
);
ALTER TABLE adm.user2user_group OWNER to tcm;
	
-- *****************************************************************************
-- REPOSITORY
-- *****************************************************************************	
CREATE SCHEMA repo;	
	
-- Element
CREATE TABLE repo.element (
    id serial NOT NULL PRIMARY KEY,
    ref uuid NOT NULL UNIQUE,
    name varchar(255) NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE repo.element OWNER to tcm;

-- File
CREATE TABLE repo.file (
    id integer NOT NULL PRIMARY KEY,
	size integer NOT NULL,
	mime_type varchar(32) NOT NULL,
	preview_size integer ,
	preview_mime_type varchar(32)
);
ALTER TABLE repo.file OWNER to tcm;

-- Folder
CREATE TABLE repo.folder (
    id integer NOT NULL PRIMARY KEY,
	icon varchar(32)
);
ALTER TABLE repo.folder OWNER to tcm;

-- Association
CREATE TABLE repo.association (
    id serial NOT NULL PRIMARY KEY,
    type char(1) NOT NULL,
	parent_element_id integer REFERENCES repo.element(id) NOT NULL,
	child_element_id integer REFERENCES repo.element(id) NOT NULL,
	created_time timestamp without time zone,
    created_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE repo.association OWNER to tcm;

-- AccessRight
CREATE TABLE repo.access_right (
    id serial NOT NULL PRIMARY KEY,
    element_id int NOT NULL REFERENCES repo.element(id),
	order_no int NOT NULL,
	can_create bool NOT NULL,
	can_read bool NOT NULL,
	can_update bool NOT NULL,
	can_delete bool NOT NULL
);
ALTER TABLE repo.access_right OWNER to tcm;

-- User2AccessRight
CREATE TABLE repo.user2access_right (
	user_id int NOT NULL REFERENCES adm.user(id),
	access_right int NOT NULL REFERENCES repo.access_right(id)
);
ALTER TABLE repo.user2access_right OWNER to tcm;

-- UserGroup2AccessRight
CREATE TABLE repo.user_group2access_right (
	user_group_id int NOT NULL REFERENCES adm.user_group(id),
	access_right int NOT NULL REFERENCES repo.access_right(id)
);
ALTER TABLE repo.user_group2access_right OWNER to tcm;

-- Feature
CREATE TABLE repo.feature (
	id serial NOT NULL PRIMARY KEY,
	name varchar(32) NOT NULL,
	code varchar(32) NOT NULL,
	created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE repo.feature OWNER to tcm;

-- Element2Feature
CREATE TABLE repo.element2feature (
	element_id int NOT NULL REFERENCES repo.element(id),
	feature_id int NOT NULL REFERENCES repo.feature(id)
);
ALTER TABLE repo.element2feature OWNER to tcm;

-- FeatureAttribute
CREATE TABLE repo.feature_attribute (
	id serial NOT NULL PRIMARY KEY,
	name varchar(32) NOT NULL,
	type varchar(16) NOT NULL,
	required bool NOT NULL,
	feature_id int NOT NULL REFERENCES repo.feature(id),
	created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE repo.feature_attribute OWNER to tcm;

-- FeatureAttributeValue
CREATE TABLE repo.feature_attribute_value (
	id serial NOT NULL PRIMARY KEY,
	feature_attribute_id int NOT NULL REFERENCES repo.feature_attribute(id),
	element_id int NOT NULL REFERENCES repo.element(id),
	value_int int, 
	value_float float,
	value_dec decimal(10,2),
	value_bool boolean,
	value_string varchar(255),
	value_text text,
	value_date date,
	value_time timestamp,
	created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES adm.user(id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES adm.user(id)
);
ALTER TABLE repo.feature_attribute_value OWNER TO tcm;

-- *****************************************************************************
-- AUTHENTICATION
-- *****************************************************************************
CREATE SCHEMA auth;

CREATE TABLE auth.access_token ( 
	token_id varchar(255),
	token bytea,
	authentication_id varchar(255) PRIMARY KEY,
	user_name varchar(255),
	client_id varchar(255),
	authentication bytea,
	refresh_token varchar(255)
);
ALTER TABLE auth.access_token OWNER to tcm;

CREATE TABLE auth.refresh_token ( 
	token_id varchar(255),
	token bytea,
	authentication bytea 
);
ALTER TABLE auth.refresh_token OWNER to tcm;

-- *****************************************************************************

CREATE OR REPLACE FUNCTION calcAccessRight(userId int, elementId int)
RETURNS int AS $result$
DECLARE
	total integer;
	accessRightsCursor cursor(elementId int) FOR SELECT * FROM repo.access_right WHERE element_id = elementId ORDER BY order_no;
	accessRight record;
BEGIN
   	OPEN accessRightsCursor(elementId);
	LOOP 
		FETCH accessRightsCursor into accessRight;
	    EXIT WHEN NOT FOUND;
		IF userId IN (
			SELECT user_id 
				FROM repo.user2access_right 	
				WHERE access_right = accessRight.id
			UNION
			SELECT u2ug.user_id 
				FROM repo.user_group2access_right ug2ar 
				INNER JOIN adm.user2user_group u2ug ON ug2ar.user_group_id = u2ug.user_group_id 
				WHERE access_right = accessRight.id) 
		THEN
			RETURN accessRight.can_create::int + accessRight.can_read::int*2 + accessRight.can_update::int*4 + accessRight.can_delete::int*8;
		END IF;
   	END LOOP;
   	RETURN -1;
END;
$result$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hasAccessRight(userId int, elementId int, rightCode int)
RETURNS bool AS $result$
BEGIN
   	RETURN calcAccessRight(userId, elementId) & rightCode > 0;
END;
$result$ LANGUAGE plpgsql;

SELECT calcAccessRight(5, 5);
SELECT hasAccessRight(5, 5, 8);

-- *****************************************************************************

INSERT INTO adm.setting(name, value) VALUES ('root_ref',  uuid_generate_v1()); 
INSERT INTO adm.setting(name, value) VALUES ('trash_ref', uuid_generate_v1()); 

INSERT INTO adm.user(first_name, last_name, email, password, created_time, created_by_id)
    VALUES ('Krzysztof', 'Telech', 'tejlor@wp.pl','7c4a8d09ca3762af61e59520943dc26494f8941b', now(), 1);
    
INSERT INTO repo.element(ref, name, created_time, created_by_id) 
	VALUES ((SELECT value FROM adm.setting WHERE name = 'root_ref')::uuid, 'Root', now(), 1);
INSERT INTO repo.element(ref, name, created_time, created_by_id) 
	VALUES ((SELECT value FROM adm.setting WHERE name = 'trash_ref')::uuid, 'Trash', now(), 1);
	
INSERT INTO repo.folder(id) VALUES(1);
    

