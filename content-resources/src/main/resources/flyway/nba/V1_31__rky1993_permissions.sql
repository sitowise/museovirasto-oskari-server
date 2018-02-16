insert into oskari_resource (resource_type,resource_mapping) values ('attribute', 'RKY1993_points+id'); 
insert into oskari_resource (resource_type,resource_mapping) values ('attribute', 'RKY1993_lines+id'); 
insert into oskari_resource (resource_type,resource_mapping) values ('attribute', 'RKY1993_areas+id'); 

INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES
((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY1993_points+id'), 'ROLE', 'VIEW_ATTRIBUTE', 1);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES
((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY1993_lines+id'), 'ROLE', 'VIEW_ATTRIBUTE', 1);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES
((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY1993_areas+id'), 'ROLE', 'VIEW_ATTRIBUTE', 1);

INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES
((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY1993_points+id'), 'ROLE', 'VIEW_ATTRIBUTE', 2);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES
((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY1993_lines+id'), 'ROLE', 'VIEW_ATTRIBUTE', 2);
INSERT INTO oskari_permission(oskari_resource_id, external_type, permission, external_id) VALUES
((SELECT id FROM oskari_resource WHERE resource_mapping = 'RKY1993_areas+id'), 'ROLE', 'VIEW_ATTRIBUTE', 2);