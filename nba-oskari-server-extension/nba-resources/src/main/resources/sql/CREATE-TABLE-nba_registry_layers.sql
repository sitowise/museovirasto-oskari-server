-- Table: nba_registry_layers

-- DROP TABLE nba_registry_layers;

CREATE TABLE nba_registry_layers
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
