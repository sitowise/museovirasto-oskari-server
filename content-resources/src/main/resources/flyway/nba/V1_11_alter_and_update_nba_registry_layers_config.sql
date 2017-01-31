ALTER TABLE nba_registry_layers RENAME item_id_attribute TO main_item_id_attr;
ALTER TABLE nba_registry_layers ALTER COLUMN main_item_id_attr TYPE character varying;
ALTER TABLE nba_registry_layers ADD COLUMN sub_item_id_attr character varying;

--Ancient Monument Maintenance
UPDATE nba_registry_layers SET item_type='main' WHERE layer_id = 86;
UPDATE nba_registry_layers SET item_type='subAreas', sub_item_id_attr='OSA_ID' WHERE layer_id = 87;
UPDATE nba_registry_layers SET item_type='main' WHERE layer_id = 88;
--Ancient Monument
UPDATE nba_registry_layers SET item_type='subItems', sub_item_id_attr='Alakohdetunnus' WHERE registry_id = 2 AND item_type='sub';
UPDATE nba_registry_layers SET item_type='areas', sub_item_id_attr='OBJECTID' WHERE registry_id = 2 AND item_type='area';
--Building Heritage
UPDATE nba_registry_layers SET item_type='points', sub_item_id_attr='rakennusid' WHERE layer_id = 153;
UPDATE nba_registry_layers SET item_type='areas', sub_item_id_attr='OBJECTID' WHERE layer_id = 154;
UPDATE nba_registry_layers SET item_type='points', sub_item_id_attr='rakennusid' WHERE layer_id = 155;
UPDATE nba_registry_layers SET item_type='areas', sub_item_id_attr='OBJECTID' WHERE layer_id = 156;
--RKY1993
UPDATE nba_registry_layers SET item_type='areas', sub_item_id_attr='OBJECTID' WHERE layer_id = 93;
UPDATE nba_registry_layers SET item_type='points', sub_item_id_attr='OBJECTID' WHERE layer_id = 94;
UPDATE nba_registry_layers SET item_type='lines', sub_item_id_attr='OBJECTID' WHERE layer_id = 95;
--RKY2000
UPDATE nba_registry_layers SET item_type='areas', sub_item_id_attr='OBJECTID' WHERE layer_id = 96;
UPDATE nba_registry_layers SET item_type='points', sub_item_id_attr='OBJECTID' WHERE layer_id = 97;
UPDATE nba_registry_layers SET item_type='lines', sub_item_id_attr='OBJECTID' WHERE layer_id = 98;
--World Heritage
UPDATE nba_registry_layers SET item_type='main' WHERE layer_id = 99;
UPDATE nba_registry_layers SET item_type='main' WHERE layer_id = 100;
--Project
UPDATE nba_registry_layers SET item_type='areas', sub_item_id_attr='OBJECTID' WHERE layer_id = 151;
UPDATE nba_registry_layers SET item_type='points', sub_item_id_attr='OBJECTID' WHERE layer_id = 152;

