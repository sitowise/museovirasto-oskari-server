INSERT INTO oskari_resource(resource_type, resource_mapping) VALUES ('attribute', 'RKY2000_areas+featureName');

INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_areas+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 1);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_areas+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 2);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_areas+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 3);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_areas+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 4);

INSERT INTO oskari_resource(resource_type, resource_mapping) VALUES ('attribute', 'RKY2000_lines+featureName');

INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_lines+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 1);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_lines+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 2);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_lines+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 3);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_lines+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 4);

INSERT INTO oskari_resource(resource_type, resource_mapping) VALUES ('attribute', 'RKY2000_points+featureName');

INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_points+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 1);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_points+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 2);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_points+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 3);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY2000_points+featureName'), 'ROLE', 'VIEW_ATTRIBUTE', 4);

UPDATE nba_registry_layers SET gfi_attributes='["objectId", "featureName", "objectName", "nbaUrl", "createDate", "modifyDate", "author", "description"]' WHERE registry_id=5;