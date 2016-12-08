-- add item id attribute for some layers
UPDATE nba_registry_layers SET item_id_attribute='ALUE_ID' WHERE layer_id=86;
UPDATE nba_registry_layers SET item_id_attribute='ALUE_ID' WHERE layer_id=87;
UPDATE nba_registry_layers SET item_id_attribute='id' WHERE layer_id=96;
UPDATE nba_registry_layers SET item_id_attribute='id' WHERE layer_id=97;
UPDATE nba_registry_layers SET item_id_attribute='id' WHERE layer_id=98;
UPDATE nba_registry_layers SET item_id_attribute='KOHDEID' WHERE layer_id=153;
UPDATE nba_registry_layers SET item_id_attribute='KOHDE_ID' WHERE layer_id=154;
UPDATE nba_registry_layers SET item_id_attribute='KOHDEID' WHERE layer_id=155;
UPDATE nba_registry_layers SET item_id_attribute='KOHDE_ID' WHERE layer_id=156;

-- add configuration of gfi attributes for layers
UPDATE nba_registry_layers SET gfi_attributes='["objectName", "objectId", "nbaUrl", "createDate", "modifyDate", "author"]' WHERE registry_id=1;
UPDATE nba_registry_layers SET gfi_attributes='["objectId", "municipalityName", "objectName", "classification", "dating", "type", "subType", "nbaUrl", "createDate", "modifyDate", "author", "surveyingType", "surveyingAccuracy", "description", "areaChangeReason"]' WHERE registry_id=2;
UPDATE nba_registry_layers SET gfi_attributes='["objectId", "objectName", "conservationGroup", "conservationStatus", "nbaUrl", "createDate", "modifyDate", "author", "description"]' WHERE registry_id=3;
UPDATE nba_registry_layers SET gfi_attributes='["objectId", "objectName", "createDate", "modifyDate", "author"]' WHERE registry_id=4;
UPDATE nba_registry_layers SET gfi_attributes='["objectId", "objectName", "nbaUrl", "createDate", "modifyDate", "author", "description"]' WHERE registry_id=5;
UPDATE nba_registry_layers SET gfi_attributes='["objectName", "areaSelectionSource", "objectType", "createDate", "modifyDate", "author"]' WHERE registry_id=6;
UPDATE nba_registry_layers SET gfi_attributes='["objectName", "projectCategorySpecification", "year", "projectCategoryCode", "projectOrganization", "stageCode", "nbaUrl", "createDate", "modifyDate", "author", "type"]' WHERE registry_id=7;
UPDATE nba_registry_layers SET gfi_attributes='["objectName", "objectId", "archeologyPerson", "builtEnvironmentPerson", "underWaterArcheologyPerson", "restaurationPersons", "createDate", "modifyDate", "author"]' WHERE registry_id=8 AND layer_id=89;  -- KYS
UPDATE nba_registry_layers SET gfi_attributes='["objectName", "building", "archeology", "restauration", "www", "createDate", "modifyDate", "author"]' WHERE registry_id=8 AND layer_id=90;  -- Maakuntamuseo
UPDATE nba_registry_layers SET gfi_attributes='["objectId", "objectName", "municipalityNameSv", "ceasingDate", "municipalityCode", "municipalityName", "sapoUrl", "createDate", "modifyDate", "author"]' WHERE registry_id=8 AND layer_id=91;  -- Vanha kunta
UPDATE nba_registry_layers SET gfi_attributes='["objectId", "objectName", "createDate", "modifyDate", "author"]' WHERE registry_id=8 AND layer_id=92;  -- Kunta250
UPDATE nba_registry_layers SET gfi_attributes='' WHERE registry_id=8 AND layer_id=132; -- Maakunnat