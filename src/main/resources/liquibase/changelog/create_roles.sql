CREATE TABLE IF NOT EXISTS public.roles_multitenant
(
    id uuid NOT NULL,
    role_name character varying(255) NOT NULL,
    public_id uuid NOT NULL,
    description character varying(255),
    created_by character varying(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by character varying(255),
    last_modified_date TIMESTAMP,
    status character varying(30),
    version bigint DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT unique_role_name UNIQUE (role_name),
    CONSTRAINT unique_role_public_id UNIQUE (public_id)
);