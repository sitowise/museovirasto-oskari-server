DELETE FROM public.oskari_permission
	WHERE oskari_resource_id IN (SELECT id FROM public.oskari_resource WHERE resource_type = 'attribute' AND resource_mapping LIKE 'ProjectItem%')
	AND external_type = 'ROLE'
    AND external_id IN ('2', '3', '4');
	
INSERT INTO public.oskari_permission (oskari_resource_id, external_type, permission, external_id)
	SELECT oskari_resource_id, external_type, permission, '2' FROM public.oskari_permission
	WHERE oskari_resource_id IN (SELECT id FROM public.oskari_resource WHERE resource_type = 'attribute' AND resource_mapping LIKE 'ProjectItem%')
	AND external_type = 'ROLE'
    AND external_id = '1';
	
INSERT INTO public.oskari_permission (oskari_resource_id, external_type, permission, external_id)
	SELECT oskari_resource_id, external_type, permission, '3' FROM public.oskari_permission
	WHERE oskari_resource_id IN (SELECT id FROM public.oskari_resource WHERE resource_type = 'attribute' AND resource_mapping LIKE 'ProjectItem%')
	AND external_type = 'ROLE'
    AND external_id = '1';
	
INSERT INTO public.oskari_permission (oskari_resource_id, external_type, permission, external_id)
	SELECT oskari_resource_id, external_type, permission, '4' FROM public.oskari_permission
	WHERE oskari_resource_id IN (SELECT id FROM public.oskari_resource WHERE resource_type = 'attribute' AND resource_mapping LIKE 'ProjectItem%')
	AND external_type = 'ROLE'
    AND external_id = '1';