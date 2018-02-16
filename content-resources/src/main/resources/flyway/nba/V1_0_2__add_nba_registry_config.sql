-- Table: nba_registries

-- DROP TABLE nba_registries;

CREATE TABLE IF NOT EXISTS nba_registries
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

  
-- Table: nba_registry_layers

-- DROP TABLE nba_registry_layers;

CREATE TABLE IF NOT EXISTS nba_registry_layers
(
  registry_id bigint NOT NULL,
  layer_id bigint NOT NULL,
  to_open boolean NOT NULL,
  to_highlight boolean NOT NULL,
  item_id_attribute character varying(256),
  CONSTRAINT nba_registry_layers_pkey PRIMARY KEY (registry_id, layer_id),
  CONSTRAINT nba_registry_layers_layer_id_fkey FOREIGN KEY (layer_id)
      REFERENCES oskari_maplayer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT nba_registry_layers_registry_id_fkey FOREIGN KEY (registry_id)
      REFERENCES nba_registries (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE nba_registry_layers
  OWNER TO oskari;

-- nba registries
/*
INSERT INTO nba_registries (id, name, locale) VALUES (1,'ancientMaintenance','{"fi":{"name":"Muinaisjaann. hoitorekisteri"},"en":{"name":"Ancient Monument Maintenance"}}');
INSERT INTO nba_registries (id, name, locale) VALUES (2,'ancientMonument','{"fi":{"name":"Muinaisjäännösrekisteri"},"en":{"name":"Ancient Monument"}}');
INSERT INTO nba_registries (id, name, locale) VALUES (3,'buildingHeritage','{"fi":{"name":"Rakennusperintorekisteri"},"en":{"name":"Building Heritage"}}');
INSERT INTO nba_registries (id, name, locale) VALUES (4,'rky1993','{"fi":{"name":"RKY1993"},"en":{"name":"RKY1993"}}');
INSERT INTO nba_registries (id, name, locale) VALUES (5,'rky2000','{"fi":{"name":"RKY2000"},"en":{"name":"RKY2000"}}');
*/
-- assignings of map layers to registries
/* TODO need to add map layers first to DB and check if layer ids are correct!

INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (1,86,TRUE,FALSE,null);
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (1,87,TRUE,FALSE,null);
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (1,88,TRUE,TRUE,'ALUE_ID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,123,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,124,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,125,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,126,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,127,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,128,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (2,129,TRUE,TRUE,'Tunnus');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (3,70,TRUE,TRUE,'KOHDEID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (4,93,TRUE,TRUE,'KOHDE_ID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (4,94,TRUE,TRUE,'KOHDE_ID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (4,95,TRUE,TRUE,'KOHDE_ID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (5,96,TRUE,TRUE,'KOHDE_ID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (5,97,TRUE,TRUE,'KOHDE_ID');
INSERT INTO nba_registry_layers (registry_id, layer_id, to_open, to_highlight, item_id_attribute) VALUES (5,98,TRUE,TRUE,'KOHDE_ID');

*/