CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public.setting
(
    id serial NOT NULL PRIMARY KEY,
    name varchar(255) NOT NULL,
    value varchar(255)
);
ALTER TABLE public.setting OWNER to tcm;

INSERT INTO public.setting (name, value) VALUES ('root_ref', '00000000-0000-0000-0000-000000000000'); 
INSERT INTO public.setting (name, value) VALUES ('trash_ref', uuid_generate_v1()); 

CREATE TABLE public.user
(
    id serial NOT NULL PRIMARY KEY,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    email character varying NOT NULL UNIQUE,
    password character(40) NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES public.user (id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES public.user (id)
);
ALTER TABLE public.user OWNER to tcm;

INSERT INTO "user"(first_name, last_name, email, password, created_time, created_by_id)
    VALUES ('Krzysztof', 'Telech', 'tejlor@wp.pl','7c4a8d09ca3762af61e59520943dc26494f8941b', now(), 1);

CREATE TABLE public.user_group
(
    id serial NOT NULL PRIMARY KEY,
    name character varying NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES public.user (id),
    modified_time timestamp without time zone,
    modified_by_id integer REFERENCES public.user (id)
);
ALTER TABLE public.user_group OWNER to tcm;

CREATE TABLE public.user2user_group
(
    user_id integer REFERENCES public.user (id),
	user_group_id integer REFERENCES public.user_group (id)
);
ALTER TABLE public.user2user_group OWNER to tcm;
	
CREATE TABLE public.element
(
    id serial NOT NULL PRIMARY KEY,
    ref uuid NOT NULL UNIQUE,
    name character varying  NOT NULL,
    created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES public.user (id),
	modified_time timestamp without time zone,
    modified_by_id integer REFERENCES public.user (id)
);
ALTER TABLE public.element OWNER to tcm;

INSERT INTO public.element (ref, name, created_time, created_by_id) VALUES ((SELECT value FROM public.setting WHERE name = 'root_ref')::uuid, 'Root', current_date, 1);
INSERT INTO public.element (ref, name, created_time, created_by_id) VALUES ((SELECT value FROM public.setting WHERE name = 'trash_ref')::uuid, 'Trash', current_date, 1);

CREATE TABLE public.element_right
(
    id integer NOT NULL PRIMARY KEY,
	element_id integer NOT NULL REFERENCES public.element (id),
	user_id integer REFERENCES public.user (id),
	user_group_id integer REFERENCES public.user_group (id),
	read boolean,
	write boolean
);
ALTER TABLE public.element_right OWNER to tcm;

CREATE TABLE public.file
(
    id integer NOT NULL PRIMARY KEY,
	size integer NOT NULL,
	preview_size integer,
	mime_type character varying NOT NULL,
	preview_mime_type character varying NOT NULL
);
ALTER TABLE public.file OWNER to tcm;

CREATE TABLE public.folder
(
    id integer NOT NULL PRIMARY KEY,
	icon character varying
);
ALTER TABLE public.folder OWNER to tcm;

INSERT INTO public.folder(id, icon) VALUES(1, NULL);


CREATE TABLE public.association
(
    id serial NOT NULL PRIMARY KEY,
	parent_element_id integer REFERENCES public.element (id) NOT NULL,
	child_element_id integer REFERENCES public.element (id) NOT NULL,
	type char(1) NOT NULL,
	created_time timestamp without time zone NOT NULL,
    created_by_id integer NOT NULL REFERENCES public.user (id)
);
ALTER TABLE public.association OWNER to tcm;

-- OAuth2

CREATE SCHEMA auth;

CREATE TABLE auth.access_token 
( 
	token_id varchar(255),
	token bytea,
	authentication_id varchar(255) PRIMARY KEY,
	user_name varchar(255),
	client_id varchar(255),
	authentication bytea,
	refresh_token varchar(255)
);

CREATE TABLE auth.refresh_token
( 
	token_id varchar(255),
	token bytea,
	authentication bytea 
);
