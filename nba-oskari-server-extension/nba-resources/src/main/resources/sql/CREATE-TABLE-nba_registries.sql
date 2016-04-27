-- Table: nba_registries

-- DROP TABLE nba_registries;

CREATE TABLE nba_registries
(
  id bigserial NOT NULL,
  name character varying(256) NOT NULL,
  locale text,
  CONSTRAINT nba_registries_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE nba_registries
  OWNER TO oskari;
