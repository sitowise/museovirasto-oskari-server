INSERT INTO oskari_resource(resource_type, resource_mapping) VALUES ('attribute', 'ProjectItem+point');

INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'ProjectItem+point'), 'ROLE', 'VIEW_ATTRIBUTE', 1);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'ProjectItem+point'), 'ROLE', 'VIEW_ATTRIBUTE', 2);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'ProjectItem+point'), 'ROLE', 'VIEW_ATTRIBUTE', 3);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES ((SELECT id FROM oskari_resource WHERE resource_mapping = 'ProjectItem+point'), 'ROLE', 'VIEW_ATTRIBUTE', 4);